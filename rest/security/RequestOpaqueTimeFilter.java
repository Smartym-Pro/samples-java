package pro.smartum.reptracker.gateway.security;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Sergey Valuy
 * 
 */
public class RequestOpaqueTimeFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestOpaqueTimeFilter.class);

    private static final String TIME_PARAMETER_FORMAT = "yyyyMMddHHmmss";
    private static final String TIME_PARAMETER_NAME = "opaque";
    private long maxValidDeltaMillis = 60 * 60 * 1000; //1 hour by default

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!validateRequestTime(request)) {
            String msg = "Opaque time parameter is invalid. Rejecting request";
            log.error(msg);
            throw new IllegalStateException(msg);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    private boolean validateRequestTime(ServletRequest request) {
        String timeParam = request.getParameter(TIME_PARAMETER_NAME);
        if (StringUtils.isBlank(timeParam)) {
            log.debug("Timestamp parameter [name=" + TIME_PARAMETER_NAME + "] is blank. Request is not authenticated");
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_PARAMETER_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date requestTimestamp;
        try {
            requestTimestamp = dateFormat.parse(timeParam);
        } catch (ParseException e) {
            log.debug("Timestamp parameter [name={},value={}] format is wrong. Request is not authenticated", TIME_PARAMETER_NAME, timeParam);
            return false;
        }

        Date currentDate = new Date();
        long delta = Math.abs(requestTimestamp.getTime() - currentDate.getTime());
        if (delta > maxValidDeltaMillis) {
            log.error("Request timestamp parameter [{}] is expired (today [{}]). Request is not authenticated", requestTimestamp, currentDate);
            return false;
        }
        return true;
    }

    public void setMaxValidDeltaMillis(long maxValidDeltaMillis) {
        if (maxValidDeltaMillis <= 0) {
            String msg = "Valid request time millis [" + maxValidDeltaMillis + "] must be positive";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        this.maxValidDeltaMillis = maxValidDeltaMillis;
    }
}
