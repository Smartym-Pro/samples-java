package pro.smartum.reptracker.gateway.dao;

import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import pro.smartum.reptracker.gateway.dao.entities.TrackingAccountEntity;

/**
 * @author Sergey Valuy
 * 
 */
@Repository
public class TrackingAccountHibernateDao extends BaseHibernateDao<TrackingAccountEntity, Long> implements TrackingAccountDao {

    @Override
    public TrackingAccountEntity findByTrackingCode(@NotNull String trackingCode) {
        return (TrackingAccountEntity) sessionFactory.getCurrentSession()
                .createCriteria(TrackingAccountEntity.class)
                .add(Restrictions.eq("trackingCode", trackingCode))
                .uniqueResult();
    }
}
