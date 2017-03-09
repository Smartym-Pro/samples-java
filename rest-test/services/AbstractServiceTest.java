package pro.smartum.reptracker.gateway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import pro.smartum.reptracker.gateway.dao.entities.CountryEntity;
import pro.smartum.reptracker.gateway.dao.entities.EmailAddressEntity;
import pro.smartum.reptracker.gateway.dao.entities.EventEntity;
import pro.smartum.reptracker.gateway.dao.entities.EventParticipantEntity;
import pro.smartum.reptracker.gateway.dao.entities.EventTypeEntity;
import pro.smartum.reptracker.gateway.dao.entities.ExternalUserEntity;
import pro.smartum.reptracker.gateway.dao.entities.LanguageEntity;
import pro.smartum.reptracker.gateway.dao.entities.PartnerEntity;
import pro.smartum.reptracker.gateway.dao.entities.ReferralRelationEntity;
import pro.smartum.reptracker.gateway.dao.entities.TrackingAccountEntity;
import pro.smartum.reptracker.gateway.dao.entities.UserEntity;

@ContextConfiguration(locations = {"classpath:spring/test-app-config.xml"})
public abstract class AbstractServiceTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    protected EventService eventService;
    @Autowired
    protected EventParticipantService eventParticipantService;
    @Autowired
    protected ExternalUserService externalUserService;
    @Autowired
    protected UserService userService;
    @Autowired
    protected StorageTestHelper storageTestHelper;
    @Autowired
    protected PartnerService partnerService;
    @Autowired
    protected TrackingAccountService trackingAccountService;
    @Autowired
    protected ReferralRelationsService referralRelationsService;
    @Autowired
    protected ReportsService reportsService;

    @Autowired
    protected StorageTestHelper storageHelper;

    protected void clearDB() {
        storageHelper.clearTables(
                ReferralRelationEntity.class,
                EventEntity.class,
                EventTypeEntity.class,
                EventParticipantEntity.class,
                EmailAddressEntity.class,
                ExternalUserEntity.class,
                PartnerEntity.class,
                TrackingAccountEntity.class,
                UserEntity.class,
                CountryEntity.class,
                LanguageEntity.class,
                EventTypeEntity.class
        );
    }

}
