package pro.smartum.reptracker.gateway.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import pro.smartum.reptracker.gateway.dao.entities.EventEntity;
import pro.smartum.reptracker.gateway.web.beans.FetchLimits;

import java.util.List;

/**
 * User: Sergey Valuy
 
 */
@Repository
public class EventHibernateDao extends BaseHibernateDao<EventEntity, Long> implements EventDao {

    @Override
    public long countByConsumer(long consumerId) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EventEntity.class)
                .createAlias("consumer", "c")
                .setProjection(Projections.rowCount())
                .add(Restrictions.eq("c.id", consumerId));
        return (Long) criteria.uniqueResult();
    }

    @NotNull
    @Override
    public List<EventEntity> findByConsumer(long consumerId, @NotNull FetchLimits fetchLimits) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EventEntity.class)
                .createAlias("consumer", "c")
                .add(Restrictions.eq("c.id", consumerId))
                .setFirstResult(fetchLimits.getOffset())
                .setMaxResults(fetchLimits.getPageSize());
//                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return (List<EventEntity>) criteria.list();
    }
}
