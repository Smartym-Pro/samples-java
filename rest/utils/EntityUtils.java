package pro.smartum.reptracker.gateway.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.smartum.reptracker.gateway.dao.entities.*;
import pro.smartum.reptracker.gateway.services.ObjectNotFoundException;

/**
 * @author Sergey Valuy
 * 
 */
public final class EntityUtils {

    private final static Logger log = LoggerFactory.getLogger(EntityUtils.class);

    private EntityUtils() {
    }

    public static void ensureExternalUserExists(ExternalUserEntity externalUserEntity, String externalUserIdentifier, long partnerId) {
        if (externalUserEntity == null) {
            String msg = "ExternalUser with identifier [" + externalUserIdentifier + "] for partner " + partnerId + " not found";
            log.error(msg);
            throw new ObjectNotFoundException(msg);
        }
    }

    public static void ensureTrackingAccountExists(TrackingAccountEntity trackingAccountEntity, String trackingCode) {
        if (trackingAccountEntity == null) {
            String msg = "TrackingAccount with code [" + trackingCode + "] not found";
            log.error(msg);
            throw new ObjectNotFoundException(msg);
        }
    }

    public static void ensureUserExists(UserEntity userEntity, long userId) {
        if (userEntity == null) {
            String msg = "User for id [" + userId + "] not found";
            log.error(msg);
            throw new ObjectNotFoundException(msg);
        }
    }

    public static void ensureUserExists(UserEntity userEntity, String email) {
        if (userEntity == null) {
            String msg = "User for email [" + email + "] not found";
            log.error(msg);
            throw new ObjectNotFoundException(msg);
        }
    }

    public static void ensureLanguageExists(LanguageEntity languageEntity, String language) {
        if (languageEntity == null) {
            String msg = "Language not found: [" + language + "]";
            log.error(msg);
            throw new ObjectNotFoundException(msg);
        }
    }

    public static void ensureCountryExists(CountryEntity countryEntity, String country) {
        if (countryEntity == null) {
            String msg = "Country not found: [" + country + "]";
            log.error(msg);
            throw new ObjectNotFoundException(msg);
        }
    }

    public static void ensureTrackingAccountExists(TrackingAccountEntity trackingAccountEntity, long trackingAccountId) {
        if (trackingAccountEntity == null) {
            String msg = "TrackingAccount for ID [" + trackingAccountId + "] not found";
            log.error(msg);
            throw new ObjectNotFoundException(msg);
        }
    }

    public static void ensurePartnerExists(PartnerEntity partnerEntity, long partnerId) {
        if (partnerEntity == null) {
            String msg = "Partner for ID [" + partnerId + "] not found";
            log.error(msg);
            throw new ObjectNotFoundException(msg);
        }
    }

    public static void ensureEventParticipantExists(EventParticipantEntity eventParticipantEntity, long externalUserId) {
        if (eventParticipantEntity == null) {
            String msg = "EventParticipant for external user ID [" + externalUserId + "] not found";
            log.error(msg);
            throw new ObjectNotFoundException(msg);
        }
    }

    public static void ensurePartnerExists(PartnerEntity partnerEntity, String partnerTrackingCode) {
        if (partnerEntity == null) {
            String msg = "Partner for tracking code [" + partnerTrackingCode + "] not found";
            log.error(msg);
            throw new ObjectNotFoundException(msg);
        }
    }

    public static void ensureExternalUserByTrackingCodeExists(ExternalUserEntity externalUserEntity, String trackingCode, long partnerId) {
        if (externalUserEntity == null) {
            String msg = "ExternalUser with tracking code [" + trackingCode + "] for partner " + partnerId + " not found";
            log.error(msg);
            throw new ObjectNotFoundException(msg);
        }
    }
}
