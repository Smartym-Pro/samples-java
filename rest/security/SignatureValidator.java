package pro.smartum.reptracker.gateway.security;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import pro.smartum.reptracker.gateway.utils.HashUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Sergey Valuy
 * 
 */
@Component
class SignatureValidator implements RequestValidator {

    private static final Logger log = LoggerFactory.getLogger(SignatureValidator.class);

    @Override
    public boolean isValid(Map<String, String> httpRequestParams, String apiSignature, String passwordHash) {
        log.debug("Received api signature: " + apiSignature + ". Validating");
        String expectedSignature = calculateExpectedSignature(httpRequestParams, passwordHash);
        if (expectedSignature.equalsIgnoreCase(apiSignature)) {
            log.info("Api signature valid");
            return true;
        }
        log.error("Api signature invalid. Expected: " + expectedSignature + ", but received: " + apiSignature);
        return false;
    }

    String calculateExpectedSignature(Map<String, String> httpRequestParams, String apiSecretCode) {
        String line = "";
        List<String> paramNames = new LinkedList<String>(httpRequestParams.keySet());
        Collections.sort(paramNames);
        for (String paramName : paramNames) {
            if (!RequestSignatureAuthenticationFilter.SIGNATURE_PARAMETER_NAME.equalsIgnoreCase(paramName)) {
                String paramValue = httpRequestParams.get(paramName);
                if (paramValue == null) {
                    paramValue = "";
                }
                line += paramName + paramValue;
            }
        }
        line += apiSecretCode;
        return DigestUtils.shaHex(line);
    }

    public static void main(String[] args) {
        String admin123 = HashUtils.encodePassword("admin123", "admin@email.com");
        System.out.println(admin123);
        String user123 = HashUtils.encodePassword("user123", "user@email.com");
        System.out.println(user123);
    }

}
