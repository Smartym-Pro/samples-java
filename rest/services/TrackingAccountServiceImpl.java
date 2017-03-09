package pro.smartum.reptracker.gateway.services;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pro.smartum.reptracker.gateway.dao.TrackingAccountDao;
import pro.smartum.reptracker.gateway.dao.UserDao;
import pro.smartum.reptracker.gateway.dao.entities.TrackingAccountEntity;
import pro.smartum.reptracker.gateway.dao.entities.UserEntity;
import pro.smartum.reptracker.gateway.utils.EntityUtils;
import pro.smartum.reptracker.gateway.web.beans.TrackingAccount;

/**
 * User: Sergey Valuy
 
 */
@Service
public class TrackingAccountServiceImpl implements TrackingAccountService {

    private static final Logger log = LoggerFactory.getLogger(TrackingAccountServiceImpl.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private TrackingAccountDao trackingAccountDao;
    @Autowired
    private EventService eventService;

    @NotNull
    @Override
    public TrackingAccount createTrackingAccount(long userId, @NotNull String trackingCode) {
        UserEntity userEntity = userDao.get(userId);
        EntityUtils.ensureUserExists(userEntity, userId);
        TrackingAccountEntity duplicate = trackingAccountDao.findByTrackingCode(trackingCode);
        if (duplicate != null) {
            String msg = "TrackingAccount with code [" + trackingCode + "] already exists";
            log.error(msg);
            throw new DuplicateObjectException(msg);
        }

        int currentTrackingAccountsCount = userEntity.getTrackingAccounts().size();
        int maxTrackingAccountCount = userEntity.getMaxTrackingAccountCount();
        if (currentTrackingAccountsCount >= maxTrackingAccountCount) {
            String msg = "Can't create any more tracking accounts for user [" + userId + "]. Max: " + maxTrackingAccountCount +
                    ", current: " + currentTrackingAccountsCount;
            log.error(msg);
            throw new LimitExceededException(msg);
        }
        TrackingAccountEntity trackingAccountEntity = new TrackingAccountEntity(trackingCode, userEntity);
        trackingAccountDao.persist(trackingAccountEntity);

//        log.debug("Creating tracking account creation event");
//        Event event = new Event();
//        eventService.createEvent(event, parentPartnerId);

        return BeanConverter.convert(trackingAccountEntity);
    }

    @Nullable
    @Override
    public TrackingAccount findTrackingAccountByTrackingCode(@NotNull String trackingCode) {
        TrackingAccountEntity trackingAccountEntity = trackingAccountDao.findByTrackingCode(trackingCode);
        if (trackingAccountEntity == null) {
            return null;
        }
        return BeanConverter.convert(trackingAccountEntity);
    }
}
