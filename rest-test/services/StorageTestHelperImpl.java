package pro.smartum.reptracker.gateway.services;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.persister.collection.AbstractCollectionPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pro.smartum.reptracker.gateway.dao.CountryDao;
import pro.smartum.reptracker.gateway.dao.EventTypeDao;
import pro.smartum.reptracker.gateway.dao.LanguageDao;
import pro.smartum.reptracker.gateway.dao.entities.CountryEntity;
import pro.smartum.reptracker.gateway.dao.entities.EventTypeEntity;
import pro.smartum.reptracker.gateway.dao.entities.LanguageEntity;
import pro.smartum.reptracker.gateway.web.beans.User;

import java.util.Map;

/**
 * @author Sergey Valuy
 * 
 */
@Component
public class StorageTestHelperImpl implements StorageTestHelper {

    private final static Logger log = LoggerFactory.getLogger(StorageTestHelperImpl.class);

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private CountryDao countryDao;
    @Autowired
    private LanguageDao languageDao;
    @Autowired
    private EventTypeDao eventTypeDao;

    @Override
    public void clearTables(Class... entityClasses) {
        clearRelationTables();
        for (Class entityClass : entityClasses) {
            String entityName = sessionFactory.getClassMetadata(entityClass).getEntityName();
            log.info("Deleting from table " + entityName);
            String hql = "delete from " + entityName;
            sessionFactory.getCurrentSession().createQuery(hql).executeUpdate();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clearRelationTables() {
        Map<String, CollectionMetadata> metadata = sessionFactory.getAllCollectionMetadata();
        for (CollectionMetadata collectionMetadata : metadata.values()) {
            String relationEntityName = collectionMetadata.getRole();
            if (collectionMetadata instanceof AbstractCollectionPersister) {
                log.info("Deleting from relation table " + relationEntityName);
                AbstractCollectionPersister persister = (AbstractCollectionPersister) collectionMetadata;
                if (persister.isManyToMany()) {
                    SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery("delete from " + persister.getTableName());
                    sqlQuery.executeUpdate();
                    continue;
                }
            }
            log.debug("Skipping relation " + relationEntityName);
        }
    }

    @Override
    public void createCountry(String isoCode, String fullName) {
        countryDao.persist(new CountryEntity(isoCode, fullName));
    }

    @Override
    public void createLanguage(String isoCode, String fullName) {
        languageDao.persist(new LanguageEntity(isoCode, fullName));
    }

    @Override
    public User buildUser(String name, int maxTrackingAccountCount, int maxAuthLevel) {
        User user = new User();
        user.setName(name);
        user.setMaxTrackingAccountCount(maxTrackingAccountCount);
        user.setMaxAuthLevel(maxAuthLevel);
        return user;
    }

    @Override
    public void createEventType(int typeCode, String typeName) {
        eventTypeDao.persist(new EventTypeEntity(typeName, typeCode));
    }

}
