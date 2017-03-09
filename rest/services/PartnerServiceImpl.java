package pro.smartum.reptracker.gateway.services;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pro.smartum.reptracker.gateway.dao.PartnerDao;
import pro.smartum.reptracker.gateway.dao.TrackingAccountDao;
import pro.smartum.reptracker.gateway.dao.entities.PartnerEntity;
import pro.smartum.reptracker.gateway.dao.entities.TrackingAccountEntity;
import pro.smartum.reptracker.gateway.utils.EntityUtils;
import pro.smartum.reptracker.gateway.web.beans.Partner;

import java.util.UUID;

/**
 * @author Sergey Valuy
 * 
 */
@Service
public class PartnerServiceImpl implements PartnerService {

    private static final Logger log = LoggerFactory.getLogger(PartnerServiceImpl.class);

    @Autowired
    private PartnerDao partnerDao;
    @Autowired
    private TrackingAccountDao trackingAccountDao;

    @Override
    public Partner findById(long id) {
        PartnerEntity entity = partnerDao.get(id);
        if (entity == null) {
            log.debug("Partner for id [" + id + "] not found");
            return null;
        }
        return BeanConverter.convert(entity);
    }

    @NotNull
    @Override
    public Partner createPartner(@NotNull String name, String url, long trackingAccountId) {
        TrackingAccountEntity trackingAccountEntity = trackingAccountDao.get(trackingAccountId);
        EntityUtils.ensureTrackingAccountExists(trackingAccountEntity, trackingAccountId);
        String apiSecretCode = UUID.randomUUID().toString();
        Partner partner = new Partner(name, apiSecretCode, url, false);
        PartnerEntity entity = BeanConverter.convert(partner, trackingAccountEntity);
        partnerDao.persist(entity);
        return BeanConverter.convert(entity);
    }
}
