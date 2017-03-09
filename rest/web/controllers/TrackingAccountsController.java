package pro.smartum.reptracker.gateway.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import pro.smartum.reptracker.gateway.services.TrackingAccountService;
import pro.smartum.reptracker.gateway.services.UserService;
import pro.smartum.reptracker.gateway.utils.ArgumentGuard;
import pro.smartum.reptracker.gateway.web.beans.StatusWrapper;
import pro.smartum.reptracker.gateway.web.beans.TrackingAccount;
import pro.smartum.reptracker.gateway.web.beans.User;

import java.io.IOException;

/**
 * User: Sergey Valuy
 
 */
@Controller
@RequestMapping("/trackingaccounts")
public class TrackingAccountsController {

    private static final Logger log = LoggerFactory.getLogger(TrackingAccountsController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private TrackingAccountService trackingAccountService;

    @Secured("ROLE_SUPER_PARTNER")
    @ResponseBody
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public User get(String trackingCode) {
        log.debug("Getting User for Tracking code [" + trackingCode + "]");
        ArgumentGuard.checkNotBlank(trackingCode, "trackingCode");
        User user = userService.getByTrackingCode(trackingCode);
        log.debug("Found User: " + user);
        return user;
    }

    @Secured("ROLE_SUPER_PARTNER")
    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public TrackingAccount create(long userId, String trackingCode) {
        log.info("Creating TrackingAccount with code [" + trackingCode + "] for user with ID: " + userId);
        ArgumentGuard.checkNotBlank(trackingCode, "trackingCode");
//        SecurityUser currentUser = SecurityUtils.getCurrentUser();
//        if (currentUser == null) {
//            String msg = "No security user in context!";
//            log.error(msg);
//            throw new IllegalStateException(msg);
//        }
        TrackingAccount trackingAccount = trackingAccountService.createTrackingAccount(userId, trackingCode);
        log.info("Created tracking account: " + trackingAccount);
        return trackingAccount;
    }

    @Secured("ROLE_PARTNER")
    @ResponseBody
    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    public StatusWrapper exists(String trackingCode) {
        log.debug("Checking if tracking code [" + trackingCode + "] exists");
        ArgumentGuard.checkNotBlank(trackingCode, "trackingCode");
        TrackingAccount trackingAccount = trackingAccountService.findTrackingAccountByTrackingCode(trackingCode);
        if (trackingAccount == null) {
            log.debug("Tracking code [" + trackingCode + "] not found");
            return StatusWrapper.FAIL_STATUS;
        } else {
            log.debug("Tracking code [" + trackingCode + "] exists");
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
