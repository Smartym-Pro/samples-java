package pro.smartum.reptracker.gateway.services;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pro.smartum.reptracker.gateway.dao.entities.Gender;
import pro.smartum.reptracker.gateway.services.DuplicateObjectException;
import pro.smartum.reptracker.gateway.services.EmailAddressService;
import pro.smartum.reptracker.gateway.services.ObjectNotFoundException;
import pro.smartum.reptracker.gateway.services.TrackingAccountService;
import pro.smartum.reptracker.gateway.services.UserService;
import pro.smartum.reptracker.gateway.web.beans.TrackingAccount;
import pro.smartum.reptracker.gateway.web.beans.User;

import java.util.Date;

/**
 * @author Sergey Valuy
 * 
 */
public class UserServiceImplTest extends AbstractServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailAddressService emailAddressService;

    @Autowired
    private TrackingAccountService trackingAccountService;

    @After
    public void tearDown() throws Exception {
        clearDB();
    }

    @Test
    public void testCreateMinimalUser() throws Exception {
        String name = "homer";
        Integer maxTrackingAccountCount = 4;
        Integer maxAuthLevel = 42;
        String email = "a@b.com";

        User user = storageHelper.buildUser(name, maxTrackingAccountCount, maxAuthLevel);
        Long id;
        Date createDate;
        {
            User user1 = userService.createUser(user, email);
            id = user1.getId();
            Assert.assertNotNull(id);
            createDate = user1.getCreateDate();
            Assert.assertNotNull(createDate);
            Assert.assertEquals(name, user1.getName());
            Assert.assertEquals(maxTrackingAccountCount, user1.getMaxTrackingAccountCount());
            Assert.assertEquals(maxAuthLevel, user1.getMaxAuthLevel());
        }
        {
            User user2 = userService.getUserById(id);
            Assert.assertEquals(id, user2.getId());
            Assert.assertEquals(createDate, user2.getCreateDate());
            Assert.assertEquals(name, user2.getName());
            Assert.assertEquals(maxTrackingAccountCount, user2.getMaxTrackingAccountCount());
            Assert.assertEquals(maxAuthLevel, user2.getMaxAuthLevel());
        }
    }

    @Test
    public void testCreateFullUser() throws Exception {
        String name = "homer";
        Integer maxTrackingAccountCount = 4;
        Integer maxAuthLevel = 42;
        String country = "USA";
        String language = "ENG";
        String postalCode = "68560";
        Gender gender = Gender.MALE;
        Integer birthYear = 1970;
        String email = "a@b.com";

        User user = storageHelper.buildUser(name, maxTrackingAccountCount, maxAuthLevel);
        user.setCountry(country);
        user.setPrimaryLanguage(language);
        user.setPostalCode(postalCode);
        user.setGender(gender);
        user.setBirthYear(birthYear);

        storageHelper.createCountry(country, "United States of America");
        storageHelper.createLanguage(language, "English");

        User user1 = userService.createUser(user, email);

        User user2 = userService.getUserById(user1.getId());
        Assert.assertEquals(country, user2.getCountry());
        Assert.assertEquals(language, user2.getPrimaryLanguage());
        Assert.assertEquals(name, user2.getName());
        Assert.assertEquals(maxTrackingAccountCount, user2.getMaxTrackingAccountCount());
        Assert.assertEquals(maxAuthLevel, user2.getMaxAuthLevel());
        Assert.assertEquals(postalCode, user2.getPostalCode());
        Assert.assertEquals(gender, user2.getGender());
        Assert.assertEquals(birthYear, user2.getBirthYear());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void testCountryNotFound() throws Exception {
        User user = storageHelper.buildUser("homer", 4, 42);
        user.setCountry("non-existing-country");

        userService.createUser(user, "a@b.com");
    }

    @Test(expected = ObjectNotFoundException.class)
    public void testLanguageNotFound() throws Exception {
        User user = storageHelper.buildUser("homer", 4, 42);
        user.setPrimaryLanguage("bad-language");

        userService.createUser(user, "a@b.com");
    }

    @Test(expected = ObjectNotFoundException.class)
    public void testGetNonExistingUser() throws Exception {
        userService.getUserById(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateUserWithoutId() throws Exception {
        User user = storageHelper.buildUser("homer", 1, 1);
        userService.updateUser(user);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void testUpdateNonExistingUser() throws Exception {
        User user = storageHelper.buildUser("homer", 1, 1);
        user.setId(1L);
        userService.updateUser(user);
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = storageHelper.buildUser("homer", 1, 1);
        user.setCountry("USA");
        user.setPrimaryLanguage("ENG");
        user.setPostalCode("68560");
        user.setGender(Gender.MALE);
        user.setBirthYear(1970);

        storageHelper.createCountry("USA", "United States of America");
        storageHelper.createLanguage("ENG", "English");

        User user1 = userService.createUser(user, "a@b.com");

        User user2 = storageHelper.buildUser("homer1", 2, 2);
        user2.setCountry("UKR");
        user2.setPrimaryLanguage("UA");
        user2.setPostalCode("56890");
        user2.setGender(Gender.FEMALE);
        user2.setBirthYear(1980);
        user2.setId(user1.getId());
        Date updatedCreateDate = new Date(1, 1, 1);
        user2.setCreateDate(updatedCreateDate);

        storageHelper.createCountry("UKR", "Ukraine");
        storageHelper.createLanguage("UA", "Ukrainian");

        userService.updateUser(user2);

        User user3 = userService.getUserById(user1.getId());

        Assert.assertEquals(user2.getName(), user3.getName());
        Assert.assertEquals(user2.getMaxAuthLevel(), user3.getMaxAuthLevel());
        Assert.assertEquals(user2.getMaxTrackingAccountCount(), user3.getMaxTrackingAccountCount());
        Assert.assertEquals(user2.getCountry(), user3.getCountry());
        Assert.assertEquals(user2.getPrimaryLanguage(), user3.getPrimaryLanguage());
        Assert.assertEquals(user2.getPostalCode(), user3.getPostalCode());
        Assert.assertEquals(user2.getGender(), user3.getGender());
        Assert.assertEquals(user2.getBirthYear(), user3.getBirthYear());

        Assert.assertEquals(user1.getId(), user3.getId());
        Assert.assertNotNull(user3.getCreateDate());
        Assert.assertFalse(user3.getCreateDate().equals(updatedCreateDate));
        Assert.assertEquals(user1.getCreateDate().getTime(), user3.getCreateDate().getTime());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void testGetByNonExistingEmail() throws Exception {
        userService.getUserByEmail("bad-email");
    }

    @Test
    public void testGetByEmail() throws Exception {
        String email = "a@b.com";
        User user = userService.createUser(storageHelper.buildUser("homer", 1, 42), email);

        User user2 = userService.getUserByEmail(email);
        Assert.assertEquals(user.getId(), user2.getId());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void testGetByNonExistingTrackingAccountId() throws Exception {
        userService.getByTrackingCode("non-existing-code");
    }

    @Test
    public void testGetByTrackingAccountId() throws Exception {
        User user = userService.createUser(storageHelper.buildUser("homer", 1, 42), "a@b.com");
        TrackingAccount trackingAccount = trackingAccountService.createTrackingAccount(user.getId(), "tc1");

        User user1 = userService.getByTrackingCode(trackingAccount.getTrackingCode());
        Assert.assertEquals(user.getId(), user1.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserWithNullEmail() throws Exception {
        userService.createUser(storageHelper.buildUser("homer", 1, 42), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserWithBlankEmail() throws Exception {
        userService.createUser(storageHelper.buildUser("homer", 1, 42), " ");
    }

    @Test(expected = DuplicateObjectException.class)
    public void testCreateUserWithDuplicateEmail() throws Exception {
        userService.createUser(storageHelper.buildUser("homer", 1, 42), "a@b.com");
        userService.createUser(storageHelper.buildUser("homer1", 1, 42), "a@b.com");
    }

    @Test
    public void testUpdateMaxTrackingAccountCount() throws Exception {
        User user = userService.createUser(storageHelper.buildUser("homer", 2, 0), "a@b.com");
        trackingAccountService.createTrackingAccount(user.getId(), "tc1");
        trackingAccountService.createTrackingAccount(user.getId(), "tc2");

        user.setMaxTrackingAccountCount(3);

        User user1 = userService.updateUser(user);

        Assert.assertEquals(3, (int) user1.getMaxTrackingAccountCount());
        Assert.assertEquals(2, user1.getTrackingCodes().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateMaxTrackingAccountCountLessThanCurrent() throws Exception {
        User user = userService.createUser(storageHelper.buildUser("homer", 2, 0), "a@b.com");
        trackingAccountService.createTrackingAccount(user.getId(), "tc1");
        trackingAccountService.createTrackingAccount(user.getId(), "tc2");

        user.setMaxTrackingAccountCount(1);
        userService.updateUser(user);
    }
}
