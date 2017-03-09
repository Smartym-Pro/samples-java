package pro.smartum.reptracker.gateway.dao;

import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import pro.smartum.reptracker.gateway.dao.entities.UserEntity;

/**
 * @author Sergey Valuy
 * 
 */
@Repository
public class UserHibernateDao extends BaseHibernateDao<UserEntity, Long> implements UserDao {

    @Override
    public UserEntity findByTrackingCode(@NotNull String trackingCode) {
        return (UserEntity) sessionFactory.getCurrentSession().createCriteria(UserEntity.class)
                .createAlias("trackingAccounts", "r")
                .add(Restrictions.eq("r.trackingCode", trackingCode))
                .uniqueResult();
    }

    @Override
    public UserEntity findByEmailHash(String emailHash) {
        return (UserEntity) sessionFactory.getCurrentSession().createCriteria(UserEntity.class)
                .createAlias("emails", "e")
                .add(Restrictions.eq("e.emailHash", emailHash))
                .uniqueResult();
    }
}
