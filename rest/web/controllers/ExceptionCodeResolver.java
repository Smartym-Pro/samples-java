package pro.smartum.reptracker.gateway.web.controllers;

import org.springframework.stereotype.Component;

/**
 * User: Sergey Valuy
 
 */
@Component
public class ExceptionCodeResolver {

    public ExceptionCode getExceptionCode(Exception e) {
        for (ExceptionCode exceptionCode : ExceptionCode.values()) {
            if (exceptionCode.matches(e)) {
                return exceptionCode;
            }
        }
        return ExceptionCode.UNEXPECTED_ERROR;
    }
}
