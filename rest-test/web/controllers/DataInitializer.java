package pro.smartum.reptracker.gateway.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pro.smartum.reptracker.gateway.services.UserService;
import pro.smartum.reptracker.gateway.web.beans.User;
import pro.smartum.reptracker.gateway.services.StorageTestHelper;

import javax.annotation.PostConstruct;

/**
 * User: Sergey Valuy
 
 */
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserService userService;
    @Autowired
    private StorageTestHelper storageTestHelper;

    @PostConstruct
    public void initialize() {
        log.info("Initializing DB");
        User user = userService.createUser(storageTestHelper.buildUser("homer112", 1, 1), "a@b.com");
        log.info("Created user: " + user);
    }
}
