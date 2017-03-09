package pro.smartum.reptracker.gateway.dao;

import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Repository;

import pro.smartum.reptracker.gateway.dao.entities.ExternalUserEntity;

import java.util.List;

/**
 * @author Sergey Valuy
 * 
 */
@Repository
public class ExternalUserHibernateDao extends BaseHibernateDao<ExternalUserEntity, Long> implements ExternalUserDao {

    @Override
    public ExternalUserEntity findByUserIdentifier(String externalUserIdentifier, long parentPartnerId) {
        return (ExternalUserEntity) sessionFactory.getCurrentSession().createCriteria(ExternalUserEntity.class)
                .createAlias("parentPartner", "p")
                .add(Restrictions.eq("p.id", parentPartnerId))
                .add(Restrictions.eq("userIdentifier", externalUserIdentifier))
                .uniqueResult();
    }

    @NotNull
    @Override
    public List<ExternalUserEntity> findByParentPartnerId(long partnerId) {
        List list = sessionFactory.getCurrentSession().createCriteria(ExternalUserEntity.class)
                .createAlias("parentPartner", "p")
                .add(Restrictions.eq("p.id", partnerId))
                .list();
        return (List<ExternalUserEntity>) list;
    }

    @Override
    public ExternalUserEntity findByPartnerUserEmailHash(String emailHash) {
        return (ExternalUserEntity) sessionFactory.getCurrentSession().createCriteria(ExternalUserEntity.class)
                .add(Restrictions.eq("emailHash", emailHash))
                .uniqueResult();
    }

    @Nullable
    @Override
    public ExternalUserEntity findByUserEmailHash(@NotNull String emailHash) {
        return (ExternalUserEntity) sessionFactory.getCurrentSession().createCriteria(ExternalUserEntity.class)
                .add(Restrictions.eq("emailHash", emailHash))
                .uniqueResult();
    }
}
