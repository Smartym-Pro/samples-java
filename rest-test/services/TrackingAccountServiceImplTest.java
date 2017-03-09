package pro.smartum.reptracker.gateway.services;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pro.smartum.reptracker.gateway.services.DuplicateObjectException;
import pro.smartum.reptracker.gateway.services.LimitExceededException;
import pro.smartum.reptracker.gateway.services.TrackingAccountService;
import pro.smartum.reptracker.gateway.services.UserService;
import pro.smartum.reptracker.gateway.web.beans.TrackingAccount;
import pro.smartum.reptracker.gateway.web.beans.User;

/**
 * @author Sergey Valuy
 * 
 */
public class TrackingAccountServiceImplTest extends AbstractServiceTest {

    @Autowired
    private TrackingAccountService trackingAccountService;
    @Autowired
    private UserService userService;
    @Autowired
    private StorageTestHelper storageTestHelper;

    @After
    public void tearDown() throws Exception {
        clearDB();
    }

    @Test
    public void testFindByTrackingCode() throws Exception {
        User user = userService.createUser(storageTestHelper.buildUser("homer", 1, 1), "a@b.com");
        TrackingAccount trackingAccount = trackingAccountService.createTrackingAccount(user.getId(), "tc1");

        TrackingAccount trackingAccount1 = trackingAccountService.findTrackingAccountByTrackingCode(trackingAccount.getTrackingCode());

        Assert.assertNotNull(trackingAccount1);
        Assert.assertEquals(trackingAccount.getTrackingCode(), trackingAccount1.getTrackingCode());
    }

    @Test
    public void testFindNonExistingTrackingCode() throws Exception {
        TrackingAccount trackingAccount = trackingAccountService.findTrackingAccountByTrackingCode("non-existing-code");
        Assert.assertNull(trackingAccount);
    }

    @Test
    public void testCreateTrackingAccount() throws Exception {
        User user = userService.createUser(storageTestHelper.buildUser("homer", 2, 1), "a@b.com");
        trackingAccountService.createTrackingAccount(user.getId(), "tc1");
        trackingAccountService.createTrackingAccount(user.getId(), "tc2");

        try {
            trackingAccountService.createTrackingAccount(user.getId(), "tc3");
            Assert.fail();
        } catch (LimitExceededException e) {
        }
    }

    @Test(expected = DuplicateObjectException.class)
    public void testCreateNonUniqeCode() throws Exception {
        User user1 = userService.createUser(storageTestHelper.buildUser("homer1", 2, 1), "a1@b.com");
        trackingAccountService.createTrackingAccount(user1.getId(), "tc1");
        User user2 = userService.createUser(storageTestHelper.buildUser("homer2", 2, 1), "a2@b.com");
        trackingAccountService.createTrackingAccount(user2.getId(), "tc1");
    }
}
