package pro.smartum.reptracker.gateway.services;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pro.smartum.reptracker.gateway.dao.*;
import pro.smartum.reptracker.gateway.dao.entities.*;
import pro.smartum.reptracker.gateway.utils.ArgumentGuard;
import pro.smartum.reptracker.gateway.utils.EntityUtils;
import pro.smartum.reptracker.gateway.web.beans.Event;
import pro.smartum.reptracker.gateway.web.beans.EventParticipant;
import pro.smartum.reptracker.gateway.web.beans.FetchLimits;
import pro.smartum.reptracker.gateway.web.beans.SearchResult;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Sergey Valuy
 
 */
@Service
public class EventServiceImpl implements EventService {

    private static final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    @Autowired
    private EventDao eventDao;
    @Autowired
    private ExternalUserDao externalUserDao;
    @Autowired
    private TrackingAccountDao trackingAccountDao;
    @Autowired
    private EventParticipantDao eventParticipantDao;
    @Autowired
    private EventTypeDao eventTypeDao;
    @Autowired
    private PartnerDao partnerDao;
    @Autowired
    private ReferralRelationsService referralRelationsService;

    @NotNull
    @Override
    public synchronized Event createEvent(@NotNull Event event, long parentPartnerId) {
        log.debug("Creating event " + event + " for partner " + parentPartnerId);
        ArgumentGuard.checkNotBlank(event.getBusiness().getExternalUserIdentifier(), "business.externalUserIdentifier");
        ArgumentGuard.checkNotBlank(event.getConsumer().getExternalUserIdentifier(), "consumer.externalUserIdentifier");
        if (event.getAffiliate() != null && StringUtils.isNotBlank(event.getAffiliate().getTrackingCode())) {
            ArgumentGuard.checkNotBlank(event.getAffiliate().getExternalUserIdentifier(), "affiliate.externalUserIdentifier");
        }

        PartnerEntity parentPartnerEntity = partnerDao.get(parentPartnerId);
        if (parentPartnerEntity == null) {
            String msg = "Parent partner with ID [" + parentPartnerId + "] not found";
            log.error(msg);
            throw new ObjectNotFoundException(msg);
        }
        log.debug("Parent partner found: " + parentPartnerEntity);

        Integer eventTypeCode = event.getEventTypeCode();
        EventTypeEntity eventTypeEntity = eventTypeDao.findByCode(eventTypeCode);
        if (eventTypeEntity == null) {
            String msg = "EventType with code [" + eventTypeCode + "] not found";
            log.error(msg);
            throw new ObjectNotFoundException(msg);
        }

        EventParticipantEntity businessEventParticipantEntity = processBusiness(event, parentPartnerEntity);
        EventParticipantEntity consumerEventParticipantEntity = processConsumer(event, parentPartnerEntity);
        EventParticipantEntity affiliateEventParticipantEntity = processAffiliate(event, parentPartnerEntity);

//        5) create event and bind it to these found/created participants
        EventEntity eventEntity = BeanConverter.convert(event, eventTypeEntity, businessEventParticipantEntity,
                consumerEventParticipantEntity, affiliateEventParticipantEntity);
        eventEntity.setCreateDate(new Date());
        eventDao.persist(eventEntity);

        if (eventEntity.getAffiliate() != null) {
            ExternalUserEntity referrer = eventEntity.getAffiliate().getExternalUser();
            ExternalUserEntity referral = eventEntity.getConsumer().getExternalUser();
            log.info("Registering referral relation: " + referrer.getId() + "(" + referrer.getUserIdentifier() + ") -> " +
                    referral.getId() + "(" + referral.getUserIdentifier() + ")");
            referralRelationsService.registerEvent(referrer, referral, eventTypeCode, parentPartnerEntity);
        }

        return BeanConverter.convert(eventEntity);
    }

    private EventParticipantEntity processBusiness(Event event, PartnerEntity parentPartnerEntity) {
        EventParticipant business = event.getBusiness();
        log.debug("Finding external user for business: " + business);
//        "1) search for external user by user_identifier + parent_partner (request author) for both business and customer
//        2) if not found - creating ones
        ExternalUserEntity businessExternalUserEntity = getOrCreateExternalUser(business.getExternalUserIdentifier(), parentPartnerEntity);

//        If create event includes business.trackingAccountId, it is only valid if it matches the Partner trackingAccountid.
// If so, bind the participant’s business to that trackingAccountId.
        log.debug("Finding tracking account for business");
        TrackingAccountEntity businessTrackingAccountEntity = null;
        String businessTrackingCode = business.getTrackingCode();
        if (StringUtils.isNotBlank(businessTrackingCode)) {
            log.debug("Validating business tracking code [" + businessTrackingCode + "]");
            String parentPartnerTrackingCode = parentPartnerEntity.getTrackingAccount().getTrackingCode();
            if (!businessTrackingCode.equals(parentPartnerTrackingCode)) {
                String msg = "Business tracking code [" + businessTrackingCode + " must match parent partner tracking code [" + parentPartnerTrackingCode + "]!";
                log.error(msg);
                throw new IllegalArgumentException(msg);
            }
            log.debug("Business tracking code validated");
            businessTrackingAccountEntity = trackingAccountDao.findByTrackingCode(businessTrackingCode);
            EntityUtils.ensureTrackingAccountExists(businessTrackingAccountEntity, businessTrackingCode);
        }

//        3) search for event participants with these external users
//        4) if not found - create them
        EventParticipantEntity businessEventParticipantEntity = getOrCreateEventParticipant(businessExternalUserEntity, businessTrackingAccountEntity);
        return businessEventParticipantEntity;
    }

    private EventParticipantEntity processConsumer(Event event, PartnerEntity parentPartnerEntity) {
        EventParticipant consumer = event.getConsumer();
        log.debug("Finding external user for consumer: " + consumer);
//        "1) search for external user by user_identifier + parent_partner (request author) for both business and customer
//        2) if not found - creating ones
        ExternalUserEntity consumerExternalUserEntity = getOrCreateExternalUser(consumer.getExternalUserIdentifier(), parentPartnerEntity);

//        If an consumer.trackingAccountId is passed, check if it is valid/exists.
        TrackingAccountEntity consumerTrackingAccountEntity = null;
        String consumerTrackingCode = consumer.getTrackingCode();
        if (StringUtils.isNotBlank(consumerTrackingCode)) {
            log.debug("Finding tracking account for consumer tracking code [" + consumerTrackingCode + "]");
            consumerTrackingAccountEntity = trackingAccountDao.findByTrackingCode(consumerTrackingCode);
            EntityUtils.ensureTrackingAccountExists(consumerTrackingAccountEntity, consumerTrackingCode);
            log.debug("Tracking account for consumer found: " + consumerTrackingAccountEntity);
        }

//        3) search for event participants with these external users
//        4) if not found - create them
        EventParticipantEntity consumerEventParticipantEntity = getOrCreateEventParticipant(consumerExternalUserEntity, consumerTrackingAccountEntity);
        return consumerEventParticipantEntity;
    }

    private EventParticipantEntity processAffiliate(Event event, PartnerEntity parentPartnerEntity) {
        EventParticipant affiliate = event.getAffiliate();
        if (affiliate == null ||
                (StringUtils.isBlank(affiliate.getExternalUserIdentifier()) && StringUtils.isBlank(affiliate.getTrackingCode()))) {
            log.debug("No affiliate party for event " + event);
            return null;
        }
        ExternalUserEntity affiliateExternalUserEntity = null;
        String affiliateExternalUserIdentifier = affiliate.getExternalUserIdentifier();
        if (StringUtils.isNotBlank(affiliateExternalUserIdentifier)) {
            log.debug("Finding external user for affiliate: " + affiliate);
//        "1) search for external user by user_identifier + parent_partner (request author) for both business and customer
//        2) if not found - creating ones
            affiliateExternalUserEntity = getOrCreateExternalUser(affiliateExternalUserIdentifier, parentPartnerEntity);
        }

//        If an affiliate.trackingAccountId is passed, check if it is valid/exists. If it is valid, bind the participant’s affiliate to that trackingAccountId.
        TrackingAccountEntity affiliateTrackingAccountEntity = null;
        String affiliateTrackingCode = affiliate.getTrackingCode();
        if (affiliateExternalUserEntity != null && StringUtils.isNotBlank(affiliateTrackingCode)) {
            log.debug("Finding tracking account for affiliate tracking code [" + affiliateTrackingCode + "]");
            affiliateTrackingAccountEntity = trackingAccountDao.findByTrackingCode(affiliateTrackingCode);
            EntityUtils.ensureTrackingAccountExists(affiliateTrackingAccountEntity, affiliateTrackingCode);
            log.debug("Tracking account for affiliate found: " + affiliateTrackingAccountEntity);
        }

//        3) search for event participants with these external users
//        4) if not found - create them
        EventParticipantEntity affiliateEventParticipantEntity = null;
        if (affiliateExternalUserEntity != null) {
            affiliateEventParticipantEntity = getOrCreateEventParticipant(affiliateExternalUserEntity, affiliateTrackingAccountEntity);
        }
        return affiliateEventParticipantEntity;
    }

    private ExternalUserEntity getOrCreateExternalUser(String externalUserIdentifier, PartnerEntity parentPartnerEntity) {
        ExternalUserEntity externalUserEntity = externalUserDao.findByUserIdentifier(externalUserIdentifier, parentPartnerEntity.getId());
        if (externalUserEntity == null) {
            log.debug("External user for identifier [" + externalUserIdentifier + "] and partner " + parentPartnerEntity.getId() + " not found. Creating new one");
            externalUserEntity = new ExternalUserEntity(externalUserIdentifier, null, 0);
            externalUserEntity.setParentPartner(parentPartnerEntity);
            externalUserEntity.setCreateDate(new Date());
            externalUserDao.persist(externalUserEntity);
        }
        return externalUserEntity;
    }

    private EventParticipantEntity getOrCreateEventParticipant(ExternalUserEntity externalUserEntity,
                                                               TrackingAccountEntity trackingAccountEntity) {
        long externalUserId = externalUserEntity.getId();
        if (trackingAccountEntity != null) {
            log.debug("Tracking account for event participant is defined");
            Long trackingAccountId = trackingAccountEntity.getId();
            log.debug("Searching for bound event participant for pair TrackingAccountID: " + trackingAccountId + ", ExternalUserId: " + externalUserId);
            //check for bound pair
            EventParticipantEntity boundEventParticipantEntity = eventParticipantDao.findEventParticipant(trackingAccountId, externalUserId);
            if (boundEventParticipantEntity != null) {
                log.debug("Found bound event participant (" + boundEventParticipantEntity.getId() + ") for pair TrackingAccountID: " +
                        trackingAccountId + ", ExternalUserId: " + externalUserId);
                return boundEventParticipantEntity;
            } else {
                log.debug("Bound event participant not found. Searching for unbound for ExternalUserId: " + externalUserId);
                //if not found - check for unbound pair
                EventParticipantEntity unboundEventParticipant = eventParticipantDao.findEventParticipant(null, externalUserId);
                if (unboundEventParticipant != null) {
                    log.debug("Found unbound event participant (" + unboundEventParticipant.getId() + ") for ExternalUserId: " + externalUserId);
                    unboundEventParticipant.setTrackingAccount(trackingAccountEntity);
                    log.debug("Updating unbound event participant to bound with TrackingAccountID: " + trackingAccountId);
                    return unboundEventParticipant;
                } else {
                    log.debug("Unbound event participant not found. Creating new one for pair TrackingAccountID: " +
                            trackingAccountId + ", ExternalUserId: " + externalUserId);
                    EventParticipantEntity newEventParticipantEntity = new EventParticipantEntity(trackingAccountEntity, externalUserEntity);
                    eventParticipantDao.persist(newEventParticipantEntity);
                    log.debug("Created new event participant (" + newEventParticipantEntity.getId() + ") for pair TrackingAccountID: " + trackingAccountId + ", ExternalUserId: " + externalUserId);
                    return newEventParticipantEntity;
                }
            }
        } else {
            log.debug("Tracking account for event participant is not defined");
            EventParticipantEntity existingEventParticipant = eventParticipantDao.findEventParticipantByExternalUser(externalUserId);
            if (existingEventParticipant != null) {
                log.debug("Existing event participant found (" + existingEventParticipant.getId() + ") for ExternalUserId: " + externalUserId);
                return existingEventParticipant;
            } else {
                log.debug("Existing event participant for ExternalUserId: " + externalUserId + " not found. Creating new unbound one");
                EventParticipantEntity newEventParticipantEntity = new EventParticipantEntity(null, externalUserEntity);
                eventParticipantDao.persist(newEventParticipantEntity);
                log.debug("Created new event participant (" + newEventParticipantEntity.getId() + ") for ExternalUserId: " + externalUserId);
                return newEventParticipantEntity;
            }
        }
    }

    @NotNull
    @Override
    public SearchResult<Event> listEventsForExternalUser(@NotNull String externalUserIdentifier, long partnerId, @NotNull FetchLimits fetchLimits) {
//        1) external user identifier + parent partner id from request auth -> external user
        ExternalUserEntity externalUserEntity = externalUserDao.findByUserIdentifier(externalUserIdentifier, partnerId);
        EntityUtils.ensureExternalUserExists(externalUserEntity, externalUserIdentifier, partnerId);

//        2) find participant for this external user (must be 1)
        Long externalUserId = externalUserEntity.getId();
        EventParticipantEntity eventParticipantEntity = eventParticipantDao.findEventParticipantByExternalUser(externalUserId);
        EntityUtils.ensureEventParticipantExists(eventParticipantEntity, externalUserId);

//        3) find events where this participant is consumer"
        long consumerId = eventParticipantEntity.getId();
        long totalCount = eventDao.countByConsumer(consumerId);
        List<EventEntity> eventEntities = eventDao.findByConsumer(consumerId, fetchLimits);
        List<Event> events = new LinkedList<Event>();
        for (EventEntity eventEntity : eventEntities) {
            events.add(BeanConverter.convert(eventEntity));
        }
        return new SearchResult<Event>(events, totalCount);
    }

    @NotNull
    @Override
    public Event getEventById(long id) {
        EventEntity eventEntity = eventDao.get(id);
        return BeanConverter.convert(eventEntity);
    }

    @NotNull
    @Override
    public SearchResult<Event> listEventsForTrackingAccount(long partnerId, @NotNull String trackingCode, FetchLimits fetchLimits) {
        log.debug("Listing events for partner " + partnerId + " and user tracking code [" + trackingCode + "]");

//        1) user tracking code -> user tracking account
        TrackingAccountEntity trackingAccountEntity = trackingAccountDao.findByTrackingCode(trackingCode);
        EntityUtils.ensureTrackingAccountExists(trackingAccountEntity, trackingCode);
        log.debug("Tracking account found. ID: " + trackingAccountEntity.getId());

//        2) partner id -> list of ext users for this partner
        log.debug("Finding external users for partner " + partnerId);
        PartnerEntity partnerEntity = partnerDao.get(partnerId);
        EntityUtils.ensurePartnerExists(partnerEntity, partnerId);
        List<ExternalUserEntity> externalUserEntities = externalUserDao.findByParentPartnerId(partnerId);
        if (externalUserEntities.isEmpty()) {
            log.debug("External users for partner id " + partnerId + " not found");
            return SearchResult.emptySearchResult();
        }
        List<Long> externalUserIds = new LinkedList<Long>();
        for (ExternalUserEntity externalUserEntity : externalUserEntities) {
            externalUserIds.add(externalUserEntity.getId());
        }
        log.debug("Found " + externalUserIds.size() + " external users for partner " + partnerId + " : " + externalUserIds);

//        3) find participant for this tracking account + ext user from list from #2 (must be 1)
        EventParticipantEntity consumerEventParticipantEntity = null;
        List<EventParticipantEntity> eventParticipantEntities = eventParticipantDao.findEventParticipantByTrackingAccount(trackingAccountEntity.getId());
        for (EventParticipantEntity eventParticipantEntity : eventParticipantEntities) {
            long externalUserId = eventParticipantEntity.getExternalUser().getId();
            if (externalUserIds.contains(externalUserId)) {
                consumerEventParticipantEntity = eventParticipantEntity;
                log.debug("Found consumer event participant entity. ID: " + consumerEventParticipantEntity.getId());
                break;
            }
        }
        if (consumerEventParticipantEntity == null) {
            String msg = "Consumer event participant entity not found";
            log.error(msg);
            throw new ObjectNotFoundException(msg);
        }

//        4) find events for this participant where he is consumer
        long consumerId = consumerEventParticipantEntity.getId();
        log.debug("Finding events for consumer id " + consumerId);
        long totalCount = eventDao.countByConsumer(consumerId);
        List<EventEntity> eventEntities = eventDao.findByConsumer(consumerId, fetchLimits);
        List<Event> events = new LinkedList<Event>();
        for (EventEntity eventEntity : eventEntities) {
            events.add(BeanConverter.convert(eventEntity));
        }
        return new SearchResult<Event>(events, totalCount);
    }

}
