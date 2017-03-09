package pro.smartum.reptracker.gateway.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import pro.smartum.reptracker.gateway.services.UserService;
import pro.smartum.reptracker.gateway.web.beans.EmailAddress;
import pro.smartum.reptracker.gateway.web.beans.User;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @author Sergey Valuy
 * 
 */
@Controller
@RequestMapping("/users")
public class UsersController {

    private static final Logger log = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private ExceptionCodeResolver exceptionCodeResolver;

    @Secured("ROLE_SUPER_PARTNER")
    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public User create(@Valid User user, @Valid EmailAddress email) {
        log.info("Creating User: " + user + "with email: [" + email + "]");
        User user1 = userService.createUser(user, email.getEmail());
        log.info("User created: " + user1);
        return user1;
    }

    @Secured("ROLE_SUPER_PARTNER")
    @ResponseBody
    @RequestMapping(value = "/getbyemail", method = RequestMethod.GET)
    public User getByEmail(@Valid EmailAddress email) {
        log.debug("Getting User by email [" + email + "]");
        User user = userService.getUserByEmail(email.getEmail());
        log.debug("Found User: " + user);
        return user;
    }

    @Secured("ROLE_SUPER_PARTNER")
    @ResponseBody
    @RequestMapping(value = "/getbyid", method = RequestMethod.GET)
    public User getById(long userId) {
        log.debug("Getting User by id [" + userId + "]");
        User user = userService.getUserById(userId);
        log.debug("Found User: " + user);
        return user;
    }

    @Secured("ROLE_SUPER_PARTNER")
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public User update(@Valid User user) {
        log.info("Updating User: " + user);
        User user1 = userService.updateUser(user);
        log.info("Update User: " + user1);
        return user1;
    }

    // ********************** Exception handlers *********************
    @ResponseBody
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse onExceptionHandler(Exception e) throws IOException {
        return ControllerUtils.handleException(e);
    }
}
