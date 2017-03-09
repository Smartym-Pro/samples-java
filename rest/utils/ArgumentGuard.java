package pro.smartum.reptracker.gateway.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Sergey Valuy
 
 */
public final class ArgumentGuard {

    private static final Logger log = LoggerFactory.getLogger(ArgumentGuard.class);

    private ArgumentGuard() {
    }

    public static void checkNotNull(Object arg, String argName) {
        if (arg == null) {
            String msg = "Arg [" + argName + "] can't be NULL!";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
    }

    public static void checkNotBlank(String arg, String argName) {
        if (StringUtils.isBlank(arg)) {
            String msg = "Arg [" + argName + "] can't be BLANK!";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
    }
}
