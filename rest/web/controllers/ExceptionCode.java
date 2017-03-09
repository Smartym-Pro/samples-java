package pro.smartum.reptracker.gateway.web.controllers;

import org.springframework.beans.TypeMismatchException;
import org.springframework.validation.BindException;

import pro.smartum.reptracker.gateway.services.DuplicateObjectException;
import pro.smartum.reptracker.gateway.services.LimitExceededException;
import pro.smartum.reptracker.gateway.services.ObjectNotFoundException;

/**
 * User: Sergey Valuy
 
 */
public enum ExceptionCode {

    INVALID_ARGUMENTS_ERROR(101, IllegalArgumentException.class, BindException.class, TypeMismatchException.class),
    ILLEGAL_STATE_ERROR(102, IllegalStateException.class),

    OBJECT_NOT_FOUND_ERROR(201, ObjectNotFoundException.class),
    DUPLICATE_OBJECT_ERROR(202, DuplicateObjectException.class),

    LIMIT_EXCEEDED_ERROR(301, LimitExceededException.class),

    UNEXPECTED_ERROR(901, Exception.class);

    private final int code;
    private final Class<? extends Exception>[] classes;

    private ExceptionCode(int code, Class<? extends Exception>... classes) {
        this.code = code;
        this.classes = classes;
    }

    public int getCode() {
        return code;
    }

    public boolean matches(Exception e) {
        for (Class<? extends Exception> aClass : classes) {
            if (aClass.isInstance(e)) {
                return true;
            }
        }
        return false;
    }
}
