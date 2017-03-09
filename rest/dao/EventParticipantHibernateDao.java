package pro.smartum.reptracker.gateway.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Repository;

import pro.smartum.reptracker.gateway.dao.entities.EventParticipantEntity;

import java.util.List;

/**
 * @author Sergey Valuy
 * 
 */
@Repository
public class EventParticipantHibernateDao extends BaseHibernateDao<EventParticipantEntity, Long> implements EventParticipantDao {

    @Nullable
    @Override
    public EventParticipantEntity findEventParticipant(Long trackingAccountId, Long externalUserId) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EventParticipantEntity.class)
                .createAlias("trackingAccount", "r", JoinType.LEFT_OUTER_JOIN)
                .createAlias("externalUser", "u", JoinType.LEFT_OUTER_JOIN);
        criteria.add(trackingAccountId != null ? Restrictions.eq("r.id", trackingAccountId) : Restrictions.isNull("r.id"));
        criteria.add(externalUserId != null ? Restrictions.eq("u.id", externalUserId) : Restrictions.isNull("u.id"));
        return (EventParticipantEntity) criteria.uniqueResult();
    }

    @Nullable
    @Override
    public EventParticipantEntity findEventParticipantByExternalUser(long externalUserId) {
//        List list = sessionFactory.getCurrentSession().createCriteria(EventParticipantEntity.class)
//                .createAlias("externalUser", "u")
//                .add(Restrictions.eq("u.id", externalUserId))
//                .list();
//        return null;
        return (EventParticipantEntity) sessionFactory.getCurrentSession().createCriteria(EventParticipantEntity.class)
                .createAlias("externalUser", "u")
                .add(Restrictions.eq("u.id", externalUserId))
                .uniqueResult();
    }

    @NotNull
    @Override
    public List<EventParticipantEntity> findEventParticipantByTrackingAccount(long trackingAccountId) {
        List list = sessionFactory.getCurrentSession().createCriteria(EventParticipantEntity.class)
                .createAlias("trackingAccount", "t")
                .add(Restrictions.eq("t.id", trackingAccountId))
                .list();
        return (List<EventParticipantEntity>) list;
    }
}
