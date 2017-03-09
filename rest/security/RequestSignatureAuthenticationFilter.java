package pro.smartum.reptracker.gateway.security;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import pro.smartum.reptracker.gateway.dao.entities.UserRole;
import pro.smartum.reptracker.gateway.services.PartnerService;
import pro.smartum.reptracker.gateway.web.beans.Partner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Valuy
 * 
 */
public class RequestSignatureAuthenticationFilter extends GenericFilterBean {

    private final static Logger log = LoggerFactory.getLogger(RequestSignatureAuthenticationFilter.class);

    private static final String USER_PARAMETER_NAME = "auth_id";
    static final String SIGNATURE_PARAMETER_NAME = "signature";

    private final SignatureValidator signatureValidator = new SignatureValidator();

    @Autowired
    private PartnerService partnerService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        if (!validateRequest(request)) {
            log.error("Request is invalid");
        }
        chain.doFilter(request, response);
    }

    private boolean validateRequest(ServletRequest request) {
        String userId = request.getParameter(USER_PARAMETER_NAME);
        String signature = request.getParameter(SIGNATURE_PARAMETER_NAME);
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(signature)) {
            log.debug("User Id [" + userId + "] or signature [" + signature + "] is blank. Request is not authenticated");
            return false;
        }
        long partnerId;
        try {
            partnerId = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            log.error("User ID has invalid format. Error: " + e, e);
            return false;
        }
        Partner partner = partnerService.findById(partnerId);
        if (partner == null) {
            log.debug("User [" + userId + "] not found");
            return false;
        }

        String apiSecretCode = partner.getApiSecretCode();
        if (isSignatureValid(request, signature, apiSecretCode)) {
            log.debug("User is authenticated");
            SecurityUser securityUser;
            if (partner.isSuperPartner()) {
                securityUser = new SecurityUser(partner.getId(), UserRole.ROLE_SUPER_PARTNER, UserRole.ROLE_PARTNER);
            } else {
                securityUser = new SecurityUser(partner.getId(), UserRole.ROLE_PARTNER);
            }
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(securityUser,
                    partner.getApiSecretCode(), securityUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(token);
            log.debug("Request authenticated");
            return true;
        } else {
            log.debug("Request signature is invalid");
            return false;
        }
    }

    private boolean isSignatureValid(ServletRequest request, String signature, String apiSecretCode) {
        Map<String, String[]> map = request.getParameterMap();
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            String paramValue = null;
            String[] value = entry.getValue();
            if (value != null && value.length > 0) {
                paramValue = value[0];
            }
            parametersMap.put(entry.getKey(), paramValue);
        }
        return signatureValidator.isValid(parametersMap, signature, apiSecretCode);
    }
}
