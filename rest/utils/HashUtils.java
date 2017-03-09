package pro.smartum.reptracker.gateway.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author Nadezhda Loginova
 * 
 */
public final class HashUtils {

    private HashUtils() {
    }

    public static String encodePassword(String password, String salt) {
        return DigestUtils.shaHex(password + "{" + salt + "}");
    }

    public static String encodeEmail(String email) {
        if (email == null) {
            return null;
        }
        return DigestUtils.md5Hex(email) + "|" + DigestUtils.shaHex(email);
    }
}
