package pro.smartum.reptracker.gateway.dao;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import pro.smartum.reptracker.gateway.dao.entities.EventTypeEntity;

/**
 * User: Sergey Valuy
 
 */
@Repository
public class EventTypeHibernateDao extends BaseHibernateDao<EventTypeEntity, Long> implements EventTypeDao {

    @Override
    public EventTypeEntity findByCode(int eventTypeCode) {
        return (EventTypeEntity) sessionFactory.getCurrentSession().createCriteria(EventTypeEntity.class)
                .add(Restrictions.eq("typeCode", eventTypeCode))
                .uniqueResult();
    }
}
