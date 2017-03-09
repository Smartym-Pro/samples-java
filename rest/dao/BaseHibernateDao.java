package pro.smartum.reptracker.gateway.dao;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import pro.smartum.reptracker.gateway.web.beans.FetchLimits;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @author Nadezhda Loginova
 * 
 */

abstract class BaseHibernateDao<T, PK extends Serializable> implements BaseDao<T, PK> {

    private final Class<T> type;

    @Autowired
    protected SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    protected BaseHibernateDao() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.type = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public T get(PK id) {
        return (T) sessionFactory.getCurrentSession().get(type, id);
    }

    @Override
    @Nullable
    public T getDetailed(PK id) {
        return get(id);
    }

    @Override
    public void persist(T obj) {
        sessionFactory.getCurrentSession().persist(obj);
    }

    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public PK save(T obj) {
        return (PK) sessionFactory.getCurrentSession().save(obj);
    }

    @Override
    public void delete(T obj) {
        sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    public void update(T obj) {
        sessionFactory.getCurrentSession().update(obj);
    }

    @Override
    public long countAll() {
        return (Long) sessionFactory.getCurrentSession().createCriteria(type)
                .setProjection(Projections.rowCount())
                .uniqueResult();
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public List<T> list(FetchLimits fetchLimits) {
        List list = sessionFactory.getCurrentSession().createCriteria(type)
                .setFirstResult((fetchLimits.getPageNumber() - 1) * fetchLimits.getPageSize())
                .setMaxResults(fetchLimits.getPageSize())
                .list();
        return (List<T>) list;
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public List<PK> listIds(FetchLimits fetchLimits) {
        List list = sessionFactory.getCurrentSession().createCriteria(type)
                .setFirstResult((fetchLimits.getPageNumber() - 1) * fetchLimits.getPageSize())
                .setMaxResults(fetchLimits.getPageSize())
                .setProjection(Projections.id())
                .list();
        return (List<PK>) list;
    }

    @Override
    public void flush() {
        sessionFactory.getCurrentSession().flush();
    }
}
