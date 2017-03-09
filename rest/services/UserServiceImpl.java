package pro.smartum.reptracker.gateway.services;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pro.smartum.reptracker.gateway.dao.CountryDao;
import pro.smartum.reptracker.gateway.dao.EmailAddressDao;
import pro.smartum.reptracker.gateway.dao.LanguageDao;
import pro.smartum.reptracker.gateway.dao.UserDao;
import pro.smartum.reptracker.gateway.dao.entities.CountryEntity;
import pro.smartum.reptracker.gateway.dao.entities.EmailAddressEntity;
import pro.smartum.reptracker.gateway.dao.entities.LanguageEntity;
import pro.smartum.reptracker.gateway.dao.entities.UserEntity;
import pro.smartum.reptracker.gateway.utils.ArgumentGuard;
import pro.smartum.reptracker.gateway.utils.EntityUtils;
import pro.smartum.reptracker.gateway.utils.HashUtils;
import pro.smartum.reptracker.gateway.web.beans.User;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Sergey Valuy
 * 
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    public static final int DEFAULT_MAX_AUTH_LEVEL = 0;

    @Autowired
    private UserDao userDao;
    @Autowired
    private CountryDao countryDao;
    @Autowired
    private LanguageDao languageDao;
    @Autowired
    private EmailAddressDao emailAddressDao;

    @NotNull
    @Override
    public User createUser(User user, String email) {
        ArgumentGuard.checkNotBlank(email, "email");
        CountryEntity countryEntity = getCountryEntity(user.getCountry());
        LanguageEntity languageEntity = getLanguageEntity(user.getPrimaryLanguage());
        if (user.getMaxAuthLevel() == null) {
            user.setMaxAuthLevel(DEFAULT_MAX_AUTH_LEVEL);
        }
        UserEntity userEntity = BeanConverter.convert(user, countryEntity, languageEntity);
        userEntity.setCreateDate(new Date());
        userDao.persist(userEntity);

        EmailAddressEntity duplicate = emailAddressDao.findByEmailHash(HashUtils.encodeEmail(email));
        if (duplicate != null) {
            String msg = "Email [" + email + "] already exists (" + duplicate.getUser().getName() + ")";
            log.error(msg);
            throw new DuplicateObjectException(msg);
        }
        EmailAddressEntity emailAddressEntity = new EmailAddressEntity(email, HashUtils.encodeEmail(email), userEntity);
        emailAddressDao.persist(emailAddressEntity);

        Set<EmailAddressEntity> emails = new HashSet<EmailAddressEntity>();
        emails.add(emailAddressEntity);
        userEntity.setEmails(emails);

        return BeanConverter.convert(userEntity);
    }

    @NotNull
    @Override
    public User getUserById(long userId) {
        UserEntity userEntity = userDao.get(userId);
        EntityUtils.ensureUserExists(userEntity, userId);
        return BeanConverter.convert(userEntity);
    }

    @NotNull
    @Override
    public User updateUser(User user) {
        Long id = user.getId();
        ArgumentGuard.checkNotNull(id, "id");
        UserEntity entity = userDao.get(id);
        EntityUtils.ensureUserExists(entity, id);
        if (user.getMaxAuthLevel() == null) {
            user.setMaxAuthLevel(DEFAULT_MAX_AUTH_LEVEL);
        }

        entity.setName(user.getName());
        entity.setCountry(getCountryEntity(user.getCountry()));
        entity.setPostalCode(user.getPostalCode());
        int currentTrackingAccountsCount = entity.getTrackingAccounts().size();
        Integer maxTrackingAccountCount = user.getMaxTrackingAccountCount();
        if (currentTrackingAccountsCount > maxTrackingAccountCount) {
            String msg = "Can't set max tracking account count (" + maxTrackingAccountCount + ") greater than current (" +
                    currentTrackingAccountsCount + ")";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        entity.setMaxTrackingAccountCount(maxTrackingAccountCount);
        entity.setMaxAuthLevel(user.getMaxAuthLevel());
        entity.setGender(user.getGender());
        entity.setBirthYear(user.getBirthYear());
        entity.setPrimaryLanguage(getLanguageEntity(user.getPrimaryLanguage()));
        return BeanConverter.convert(entity);
    }

    @NotNull
    @Override
    public User getUserByEmail(String email) {
        UserEntity entity = userDao.findByEmailHash(HashUtils.encodeEmail(email));
        EntityUtils.ensureUserExists(entity, email);
        return BeanConverter.convert(entity);
    }

    @Nullable
    @Override
    public User findByEmail(String email) {
        UserEntity entity = userDao.findByEmailHash(HashUtils.encodeEmail(email));
        if (entity == null) {
            return null;
        }
        return BeanConverter.convert(entity);
    }

    @NotNull
    @Override
    public User getByTrackingCode(@NotNull String trackingCode) {
        UserEntity userEntity = userDao.findByTrackingCode(trackingCode);
        if (userEntity == null) {
            String msg = "User for Tracking code [" + trackingCode + "] not found";
            log.error(msg);
            throw new ObjectNotFoundException(msg);
        }
        return BeanConverter.convert(userEntity);
    }

    private LanguageEntity getLanguageEntity(String primaryLanguage) {
        LanguageEntity languageEntity = null;
        if (StringUtils.isNotBlank(primaryLanguage)) {
            languageEntity = languageDao.findByIsoCode(primaryLanguage);
            EntityUtils.ensureLanguageExists(languageEntity, primaryLanguage);
        }
        return languageEntity;
    }

    private CountryEntity getCountryEntity(String country) {
        CountryEntity countryEntity = null;
        if (StringUtils.isNotBlank(country)) {
            countryEntity = countryDao.findByIsoCode(country);
            EntityUtils.ensureCountryExists(countryEntity, country);
        }
        return countryEntity;
    }
}
