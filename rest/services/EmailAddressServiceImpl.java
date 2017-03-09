package pro.smartum.reptracker.gateway.services;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pro.smartum.reptracker.gateway.dao.EmailAddressDao;
import pro.smartum.reptracker.gateway.dao.UserDao;
import pro.smartum.reptracker.gateway.dao.entities.EmailAddressEntity;
import pro.smartum.reptracker.gateway.dao.entities.UserEntity;
import pro.smartum.reptracker.gateway.utils.EntityUtils;
import pro.smartum.reptracker.gateway.utils.HashUtils;
import pro.smartum.reptracker.gateway.web.beans.User;

/**
 * @author Sergey Valuy
 * 
 */
@Service
public class EmailAddressServiceImpl implements EmailAddressService {

    private static final Logger log = LoggerFactory.getLogger(EmailAddressServiceImpl.class);

    @Autowired
    private EmailAddressDao emailAddressDao;

    @Autowired
    private UserDao userDao;

    @NotNull
    @Override
    public User addEmailAddress(@NotNull String email, long userId) {
        UserEntity userEntity = userDao.get(userId);
        EntityUtils.ensureUserExists(userEntity, userId);
        EmailAddressEntity duplicate = emailAddressDao.findByEmailHash(HashUtils.encodeEmail(email));
        if (duplicate != null) {
            String msg = "Email [" + email + "] already exists";
            log.error(msg);
            throw new DuplicateObjectException(msg);
        }

        emailAddressDao.persist(new EmailAddressEntity(email, HashUtils.encodeEmail(email), userEntity));

        return BeanConverter.convert(userEntity);
    }
}
