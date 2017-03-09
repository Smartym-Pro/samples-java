package pro.smartum.reptracker.gateway.services;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Test;

import pro.smartum.reptracker.gateway.services.ObjectNotFoundException;
import pro.smartum.reptracker.gateway.web.beans.*;

import java.util.*;

/**
 * User: Sergey Valuy
 
 */
public class EventServiceImplListEventsForTrackingAccountTest extends AbstractServiceTest {

    @After
    public void tearDown() throws Exception {
        clearDB();
    }

    @Test(expected = ObjectNotFoundException.class)
    public void testListNonExistingEventByExternalUserIdentifier() throws Exception {
        long partnerId = createPartner("a@b.com");

        Date siteEventDate = new Date(System.currentTimeMillis() - 60 * 1000L);
        String siteEventIdentifier = "site-purchase-1";
        String businessExternalUserIdentifier = "business-id";
        String consumerExternalUserIdentifier = "consumer-id";

        int eventTypeCode = 1;
        storageTestHelper.createEventType(eventTypeCode, "PURCHASE");
        Event event1 = createEvent(partnerId, eventTypeCode, siteEventDate, siteEventIdentifier,
                businessExternalUserIdentifier, consumerExternalUserIdentifier, null);

        eventService.listEventsForExternalUser("consumer-id", -1, FetchLimits.ALL_RECORDS);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void testListNonExistingPartnerByExternalUserIdentifier() throws Exception {
        long partnerId = createPartner("a@b.com");

        Date siteEventDate = new Date(System.currentTimeMillis() - 60 * 1000L);
        String siteEventIdentifier = "site-purchase-1";
        String businessExternalUserIdentifier = "business-id";
        String consumerExternalUserIdentifier = "consumer-id";

        int eventTypeCode = 1;
        storageTestHelper.createEventType(eventTypeCode, "PURCHASE");
        Event event1 = createEvent(partnerId, eventTypeCode, siteEventDate, siteEventIdentifier,
                businessExternalUserIdentifier, consumerExternalUserIdentifier, null);

        eventService.listEventsForExternalUser("bad-consumer-id", partnerId, FetchLimits.ALL_RECORDS);
    }

    @Test
    public void testListByTrackingCode() throws Exception {
        long partnerId1 = createPartner("a@b.com");

        User consumerUser = userService.createUser(storageTestHelper.buildUser("bart", 1, 1), "a1@b.com");
        TrackingAccount consumerTrackingAccount = trackingAccountService.createTrackingAccount(consumerUser.getId(), "tc1");
        String consumerTrackingCode = consumerTrackingAccount.getTrackingCode();

        Date siteEventDate = new Date(System.currentTimeMillis() - 60 * 1000L);
        String siteEventIdentifier = "site-purchase-1";
        String businessExternalUserIdentifier = "business-id";
        String consumerExternalUserIdentifier = "consumer-id";

        int eventTypeCode = 1;
        storageTestHelper.createEventType(eventTypeCode, "PURCHASE");

        //+correct consumer tracking code and partner
        Event event11 = createEvent(partnerId1, eventTypeCode, siteEventDate, siteEventIdentifier,
                businessExternalUserIdentifier, null,
                consumerExternalUserIdentifier, consumerTrackingCode,
                null, null);

        //+no consumer tracking code, but will be associated with event participant from even11 by ext user
        Event event22 = createEvent(partnerId1, eventTypeCode, siteEventDate, siteEventIdentifier,
                businessExternalUserIdentifier,
                consumerExternalUserIdentifier,
                null);

        //+correct consumer tracking code and partner, another ext user id
        //todo: this is illegal for our system now
//        Event event33 = createEvent(partnerId1, eventTypeCode, siteEventDate, siteEventIdentifier,
//                businessExternalUserIdentifier, null,
//                "another-consumer-id", consumerTrackingCode,
//                null, null);

        long partnerId2 = createPartner("a2@b.com");
        //-incorrect partner, correct tracking code
        Event event44 = createEvent(partnerId2, eventTypeCode, siteEventDate, siteEventIdentifier,
                businessExternalUserIdentifier, null,
                consumerExternalUserIdentifier, consumerTrackingCode,
                null, null);

        //-correct consumer tracking code, but used for affiliate
        Event event55 = createEvent(partnerId1, eventTypeCode, siteEventDate, siteEventIdentifier,
                businessExternalUserIdentifier, null,
                "another-consumer-id2", null,
                "affiliate-id", consumerTrackingCode);

        //+correct consumer tracking code and partner, another consumer ext user (ok)
        //todo: this is illegal for our system now
//        Event event66 = createEvent(partnerId1, eventTypeCode, siteEventDate, siteEventIdentifier,
//                consumerExternalUserIdentifier, null,
//                businessExternalUserIdentifier, consumerTrackingCode,
//                consumerExternalUserIdentifier, null);

        SearchResult<Event> searchResult = eventService.listEventsForTrackingAccount(partnerId1, consumerTrackingCode, FetchLimits.ALL_RECORDS);

        Assert.assertEquals(2, searchResult.getTotalCount());
        List<Event> events = searchResult.getResult();
        Assert.assertEquals(2, events.size());
        Set<Long> eventIds = new HashSet<Long>();
        for (Event event : events) {
            eventIds.add(event.getId());
        }
        HashSet<Long> expected = new HashSet<Long>(Arrays.asList(event11.getId(), event22.getId()));
        Assert.assertEquals(expected, eventIds);

        searchResult = eventService.listEventsForExternalUser(consumerExternalUserIdentifier, partnerId1, new FetchLimits(1, 1));
        Assert.assertEquals(2, searchResult.getTotalCount());
        Assert.assertEquals(1, searchResult.getResult().size());
    }

    private Event createEvent(long partnerId, int eventTypeCode, Date siteEventDate, String siteEventIdentifier,
                              String businessExternalUserIdentifier, String consumerExternalUserIdentifier, String affiliateExternalUserIdentifier) {
        return createEvent(partnerId, eventTypeCode, siteEventDate, siteEventIdentifier,
                businessExternalUserIdentifier, null,
                consumerExternalUserIdentifier, null,
                affiliateExternalUserIdentifier, null);
    }

    private Event createEvent(long partnerId, int eventTypeCode, Date siteEventDate, String siteEventIdentifier,
                              String businessExternalUserIdentifier, String businessTrackingCode,
                              String consumerExternalUserIdentifier, String consumerTrackingCode,
                              String affiliateExternalUserIdentifier, String affiliateTrackingCode) {
        Event event = new Event(eventTypeCode);
        event.setSiteEventDate(siteEventDate);
        event.setSiteEventIdentifier(siteEventIdentifier);
        event.setBusiness(new EventParticipant(businessTrackingCode, businessExternalUserIdentifier));
        event.setConsumer(new EventParticipant(consumerTrackingCode, consumerExternalUserIdentifier));
        if (affiliateExternalUserIdentifier != null) {
            event.setAffiliate(new EventParticipant(affiliateTrackingCode, affiliateExternalUserIdentifier));
        }
        return eventService.createEvent(event, partnerId);
    }

    private Long createPartner(String email) {
        User user = userService.createUser(storageTestHelper.buildUser("homer", 1, 1), email);
        TrackingAccount trackingAccount = trackingAccountService.createTrackingAccount(user.getId(), "code{" + email + "}");
        Partner partner = partnerService.createPartner("p1", null, trackingAccount.getId());
        return partner.getId();
    }
}
