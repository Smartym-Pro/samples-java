package pro.smartum.reptracker.gateway.services;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pro.smartum.reptracker.gateway.services.DuplicateObjectException;
import pro.smartum.reptracker.gateway.services.EmailAddressService;
import pro.smartum.reptracker.gateway.services.ObjectNotFoundException;
import pro.smartum.reptracker.gateway.services.UserService;
import pro.smartum.reptracker.gateway.web.beans.User;

import java.util.Arrays;
import java.util.HashSet;

/**
 * User: Sergey Valuy
 
 */
public class EmailAddressServiceImplTest extends AbstractServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailAddressService emailAddressService;

    @After
    public void tearDown() throws Exception {
        clearDB();
    }

    @Test(expected = ObjectNotFoundException.class)
    public void testAddEmailAddressToNonExistingUser() throws Exception {
        emailAddressService.addEmailAddress("a@b.com", -1);
    }

    @Test(expected = DuplicateObjectException.class)
    public void testAddDuplicateEmailAddress() throws Exception {
        String email = "a@b.com";
        User user = storageHelper.buildUser("homer", 1, 42);
        User user1 = userService.createUser(user, email);
        long user1Id = user1.getId();

        emailAddressService.addEmailAddress(email, user1Id);
    }

    @Test
    public void testAddMultipleEmails() throws Exception {
        String email1 = "a1@b.com";
        User user = storageHelper.buildUser("homer", 1, 42);
        User user1 = userService.createUser(user, email1);
        long user1Id = user1.getId();

        String email2 = "a2@b.com";
        User user11 = emailAddressService.addEmailAddress(email2, user1Id);
        Assert.assertEquals(new HashSet<String>(Arrays.asList(email1, email2)), user11.getEmailAddresses());
    }
}
