package pro.smartum.reptracker.gateway.web.controllers;

/**
 * @author Sergey Valuy
 * 
 */
public class ErrorResponse {

    private final int exceptionCode;
    private final String exceptionCodeName;
    private final String message;

    public ErrorResponse(ExceptionCode exceptionCode, String message) {
        this.exceptionCode = exceptionCode.getCode();
        this.exceptionCodeName = exceptionCode.name();
        this.message = message;
    }

    public int getExceptionCode() {
        return exceptionCode;
    }

    public String getExceptionCodeName() {
        return exceptionCodeName;
    }

    public String getMessage() {
        return message;
    }
}
