package pro.smartum.reptracker.gateway.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import pro.smartum.reptracker.gateway.security.SecurityUser;
import pro.smartum.reptracker.gateway.security.SecurityUtils;
import pro.smartum.reptracker.gateway.services.EventService;
import pro.smartum.reptracker.gateway.utils.ArgumentGuard;
import pro.smartum.reptracker.gateway.web.beans.Event;
import pro.smartum.reptracker.gateway.web.beans.FetchLimits;
import pro.smartum.reptracker.gateway.web.beans.SearchResult;

import javax.validation.Valid;
import java.io.IOException;

/**
 * User: Sergey Valuy
 
 */
@Controller
@RequestMapping("/events")
public class EventsController {

    private static final Logger log = LoggerFactory.getLogger(EventsController.class);

    @Autowired
    private EventService eventService;
    @Autowired
    private ExceptionCodeResolver exceptionCodeResolver;

    @Secured("ROLE_PARTNER")
    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Event create(@Valid Event event) {
        log.info("Creating site event: " + event);
        SecurityUser currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            String msg = "No security user in context!";
            log.error(msg);
            throw new IllegalStateException(msg);
        }
        Event event1 = eventService.createEvent(event, currentUser.getId());
        log.info("Created site event: " + event1);
        return event1;
    }

    @Secured("ROLE_PARTNER")
    @ResponseBody
    @RequestMapping(value = "/listForExternalUser", method = RequestMethod.GET)
    public SearchResult<Event> listForExternalUser(String externalUserIdentifier, @Valid FetchLimits fetchLimits) {
        ArgumentGuard.checkNotBlank(externalUserIdentifier, "externalUserIdentifier");
        ArgumentGuard.checkNotNull(fetchLimits, "fetchLimits");
        log.debug("Listing events for externalUserIdentifier: " + externalUserIdentifier + ", fetchLimits: " + fetchLimits);
        SecurityUser currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            String msg = "No security user in context!";
            log.error(msg);
            throw new IllegalStateException(msg);
        }
        SearchResult<Event> events = eventService.listEventsForExternalUser(externalUserIdentifier, currentUser.getId(), fetchLimits);
        log.info("Returning " + events.getResult().size() + " of " + events.getTotalCount() + " events");
        return events;
    }

    @Secured("ROLE_SUPER_PARTNER")
    @ResponseBody
    @RequestMapping(value = "/listForTrackingAccount", method = RequestMethod.GET)
    public SearchResult<Event> listSuperPartner(long partnerId, String trackingCode, @Valid FetchLimits fetchLimits) {
        ArgumentGuard.checkNotNull(fetchLimits, "fetchLimits");
        ArgumentGuard.checkNotBlank(trackingCode, "trackingCode");
        log.debug("Listing site events for partnerId: " + partnerId + ", trackingCode: " + trackingCode + ", fetchLimits: " + fetchLimits);
        SearchResult<Event> events = eventService.listEventsForTrackingAccount(partnerId, trackingCode, fetchLimits);
//        SearchResult<Event> events = null;
        log.info("Returning " + events.getResult().size() + " of " + events.getTotalCount() + " events");
        return events;
    }

    // ********************** Exception handlers *********************
    @ResponseBody
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse onExceptionHandler(Exception e) throws IOException {
        return ControllerUtils.handleException(e);
    }
}
