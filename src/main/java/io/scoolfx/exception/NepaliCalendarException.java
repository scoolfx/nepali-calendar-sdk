package io.scoolfx.exception;

public class NepaliCalendarException extends RuntimeException {

    private final ErrorCode errorCode;

    public enum ErrorCode {
        OUT_OF_RANGE,
        INVALID_BS_DATE,
        DATA_LOAD_ERROR
    }

    public NepaliCalendarException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public NepaliCalendarException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}