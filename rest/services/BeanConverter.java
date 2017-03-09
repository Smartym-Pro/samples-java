package pro.smartum.reptracker.gateway.services;


import java.util.HashSet;
import java.util.Set;

import pro.smartum.reptracker.gateway.dao.entities.*;
import pro.smartum.reptracker.gateway.utils.HashUtils;
import pro.smartum.reptracker.gateway.web.beans.*;

/**
 * @author Sergey Valuy
 * 
 */
public final class BeanConverter {

    private BeanConverter() {
    }

    public static UserEntity convert(User bean, CountryEntity countryEntity, LanguageEntity languageEntity) {
        UserEntity entity = new UserEntity();
        entity.setName(bean.getName());
        entity.setCountry(countryEntity);
        entity.setPostalCode(bean.getPostalCode());
        entity.setMaxTrackingAccountCount(bean.getMaxTrackingAccountCount());
        entity.setMaxAuthLevel(bean.getMaxAuthLevel());
        entity.setGender(bean.getGender());
        entity.setBirthYear(bean.getBirthYear());
        entity.setPrimaryLanguage(languageEntity);
        return entity;
    }

    public static User convert(UserEntity entity) {
        User bean = new User();
        bean.setId(entity.getId());
        bean.setName(entity.getName());
        bean.setCreateDate(entity.getCreateDate());
        CountryEntity country = entity.getCountry();
        if (country != null) {
            bean.setCountry(country.getIsoCode());
        }
        bean.setPostalCode(entity.getPostalCode());
        bean.setMaxTrackingAccountCount(entity.getMaxTrackingAccountCount());
        bean.setMaxAuthLevel(entity.getMaxAuthLevel());
        bean.setGender(entity.getGender());
        bean.setBirthYear(entity.getBirthYear());
        LanguageEntity primaryLanguage = entity.getPrimaryLanguage();
        if (primaryLanguage != null) {
            bean.setPrimaryLanguage(primaryLanguage.getIsoCode());
        }
        Set<EmailAddressEntity> emailAddressEntities = entity.getEmails();
        if (emailAddressEntities != null) {
            Set<String> emails = new HashSet<String>();
            for (EmailAddressEntity emailAddressEntity : emailAddressEntities) {
                emails.add(emailAddressEntity.getEmail());
            }
            bean.setEmailAddresses(emails);
        }
        Set<TrackingAccountEntity> trackingAccountEntities = entity.getTrackingAccounts();
        if (trackingAccountEntities != null) {
            Set<String> trackingCodes = new HashSet<String>();
            for (TrackingAccountEntity trackingAccountEntity : trackingAccountEntities) {
                trackingCodes.add(trackingAccountEntity.getTrackingCode());
            }
            bean.setTrackingCodes(trackingCodes);
        }
        return bean;
    }

    public static Partner convert(PartnerEntity entity) {
        Partner bean = new Partner(entity.getName(), entity.getApiSecretCode(), entity.getUrl(), entity.isSuperPartner());
        bean.setId(entity.getId());
        return bean;
    }

    public static TrackingAccount convert(TrackingAccountEntity entity) {
        return new TrackingAccount(entity.getId(), entity.getTrackingCode());
    }

    public static EventParticipant convert(EventParticipantEntity entity) {
        EventParticipant bean = new EventParticipant();
        if (entity.getTrackingAccount() != null) {
            bean.setTrackingCode(entity.getTrackingAccount().getTrackingCode());
        }
        if (entity.getExternalUser() != null) {
            bean.setExternalUserIdentifier(entity.getExternalUser().getUserIdentifier());
        }
        bean.setId(entity.getId());
        bean.setApproved(entity.isApproved());
        return bean;
    }

    public static ExternalUserEntity convert(ExternalUser bean, PartnerEntity parentPartnerEntity) {
        ExternalUserEntity entity = new ExternalUserEntity();
        entity.setId(bean.getId());
        entity.setCreateDate(bean.getCreateDate());
        entity.setUserIdentifier(bean.getUserIdentifier());
        entity.setUserEmail(bean.getUserEmail());
        entity.setEmailHash(HashUtils.encodeEmail(bean.getUserEmail()));
        entity.setEmailValidationLevel(bean.getEmailValidationLevel());
        entity.setParentPartner(parentPartnerEntity);
        return entity;
    }

    public static ExternalUser convert(ExternalUserEntity entity) {
        ExternalUser bean = new ExternalUser(entity.getUserIdentifier(), entity.getUserEmail(),
                entity.getEmailValidationLevel(), entity.getParentPartner().getId());
        bean.setId(entity.getId());
        bean.setCreateDate(entity.getCreateDate());
        return bean;
    }

    public static Event convert(EventEntity entity) {
        Event bean = new Event();
        bean.setId(entity.getId());
        bean.setCreateDate(entity.getCreateDate());
        bean.setEventTypeCode(entity.getEventType().getTypeCode());
        bean.setSiteEventIdentifier(entity.getSiteEventIdentifier());
        bean.setSiteEventDate(entity.getSiteEventDate());
        bean.setEventDescription(entity.getEventDescription());
        bean.setAffiliateRelationStrength(entity.getAffiliateRelationStrength());
        if (entity.getBusiness() != null) {
            bean.setBusiness(convert(entity.getBusiness()));
        }
        if (entity.getConsumer() != null) {
            bean.setConsumer(convert(entity.getConsumer()));
        }
        if (entity.getAffiliate() != null) {
            bean.setAffiliate(convert(entity.getAffiliate()));
        }
        return bean;
    }

    public static EventEntity convert(Event bean, EventTypeEntity eventTypeEntity,
                                      EventParticipantEntity businessEventParticipantEntity,
                                      EventParticipantEntity consumerEventParticipantEntity,
                                      EventParticipantEntity affiliateEventParticipantEntity) {
        EventEntity entity = new EventEntity();
        entity.setEventType(eventTypeEntity);
        entity.setSiteEventIdentifier(bean.getSiteEventIdentifier());
        entity.setSiteEventDate(bean.getSiteEventDate());
        entity.setEventDescription(bean.getEventDescription());
        entity.setAffiliateRelationStrength(bean.getAffiliateRelationStrength());
        entity.setBusiness(businessEventParticipantEntity);
        entity.setConsumer(consumerEventParticipantEntity);
        entity.setAffiliate(affiliateEventParticipantEntity);
        return entity;
    }

    public static PartnerEntity convert(Partner bean, TrackingAccountEntity trackingAccountEntity) {
        PartnerEntity entity = new PartnerEntity(bean.getName(), bean.getApiSecretCode(), bean.getUrl(), bean.isSuperPartner());
        entity.setId(bean.getId());
        entity.setTrackingAccount(trackingAccountEntity);
        return entity;
    }

    public static ReferralRelation convert(ReferralRelationEntity entity) {
        ExternalUser referrer = convert(entity.getReferrer());
        ExternalUser referral = convert(entity.getReferral());
        Partner partner = convert(entity.getPartner());
        ReferralRelation bean = new ReferralRelation(referrer, referral, partner, entity.getDepth(), entity.getEventTypeCode());
        return bean;
    }
}
