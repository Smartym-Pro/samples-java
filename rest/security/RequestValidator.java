package pro.smartum.reptracker.gateway.security;

import java.util.Map;

/**
 * @author Sergey Valuy
 * 
 */
public interface RequestValidator {

    boolean isValid(Map<String, String> httpRequestParams, String apiSignature, String passwordHash);
}
