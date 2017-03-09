package pro.smartum.reptracker.gateway.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * @author Sergey Valuy
 * 
 */
public final class ControllerUtils {

    private static final Logger log = LoggerFactory.getLogger(ControllerUtils.class);

    private static final ExceptionCodeResolver exceptionCodeResolver = new ExceptionCodeResolver();

    private ControllerUtils() {
    }

    public static ErrorResponse handleException(Exception e) {
        log.error("Exception: " + e, e);
        String message = e.getMessage();
        if (e instanceof BindException) {
            List<FieldError> fieldErrors = ((BindException) e).getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                message += "Field [" + fieldError.getField() + "] value [" + fieldError.getRejectedValue() + "] invalid. " + fieldError.getDefaultMessage() + "\n";
            }
        }
        ExceptionCode exceptionCode = exceptionCodeResolver.getExceptionCode(e);
        return new ErrorResponse(exceptionCode, message);
    }
}
