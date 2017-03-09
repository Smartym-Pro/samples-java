package pro.smartum.reptracker.gateway.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import pro.smartum.reptracker.gateway.services.EmailAddressService;
import pro.smartum.reptracker.gateway.services.UserService;
import pro.smartum.reptracker.gateway.web.beans.EmailAddress;
import pro.smartum.reptracker.gateway.web.beans.StatusWrapper;
import pro.smartum.reptracker.gateway.web.beans.User;

import javax.validation.Valid;
import java.io.IOException;

/**
 * User: Sergey Valuy
 
 */
@Controller
@RequestMapping("/emailaddresses")
public class EmailAddressesController {

    private static final Logger log = LoggerFactory.getLogger(EmailAddressesController.class);

    @Autowired
    private EmailAddressService emailAddressService;
    @Autowired
    private UserService userService;
    @Autowired
    private ExceptionCodeResolver exceptionCodeResolver;

    @Secured("ROLE_SUPER_PARTNER")
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public User add(long userId, @Valid EmailAddress email) {
        log.info("Adding email [" + email + "] to User [" + userId + "]");
        User user = emailAddressService.addEmailAddress(email.getEmail(), userId);
        log.info("Added email [" + email + "] to User: " + user);
        return user;
    }

    @Secured("ROLE_PARTNER")
    @ResponseBody
    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    public StatusWrapper exists(@Valid EmailAddress email) {
        log.debug("Checking if email [" + email + "] exists");
        User user = userService.findByEmail(email.getEmail());
        if (user == null) {
            log.debug("Email [" + email + "] not found");
            return StatusWrapper.FAIL_STATUS;
        } else {
            log.debug("Email [" + email + "] exists");
            return StatusWrapper.SUCCESS_STATUS;
        }
    }

    // ********************** Exception handlers *********************
    @ResponseBody
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse onExceptionHandler(Exception e) throws IOException {
        return ControllerUtils.handleException(e);
    }
}
