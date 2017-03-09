package pro.smartum.reptracker.gateway.services;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pro.smartum.reptracker.gateway.services.EventParticipantService;
import pro.smartum.reptracker.gateway.services.ExternalUserService;
import pro.smartum.reptracker.gateway.services.PartnerService;
import pro.smartum.reptracker.gateway.services.TrackingAccountService;
import pro.smartum.reptracker.gateway.services.UserService;

/**
 * @author Sergey Valuy
 * 
 */
public class EventParticipantServiceImplTest extends AbstractServiceTest {

    @Autowired
    private EventParticipantService eventParticipantService;
    @Autowired
    private UserService userService;
    @Autowired
    private TrackingAccountService trackingAccountService;
    @Autowired
    private ExternalUserService externalUserService;
    @Autowired
    private StorageTestHelper storageTestHelper;
    @Autowired
    private PartnerService partnerService;

    @After
    public void tearDown() throws Exception {
        clearDB();
    }

    @Test
    public void test1() throws Exception {
    }
}
