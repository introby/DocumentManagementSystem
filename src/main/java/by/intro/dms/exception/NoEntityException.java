package by.intro.dms.exception;

public class NoEntityException extends Exception {

    public NoEntityException() {
    }

    public NoEntityException(String message) {
        super(message);
    }

    public NoEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoEntityException(Throwable cause) {
        super(cause);
    }

    public NoEntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
