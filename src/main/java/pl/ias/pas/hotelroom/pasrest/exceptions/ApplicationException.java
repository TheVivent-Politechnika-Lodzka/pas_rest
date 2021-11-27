package pl.ias.pas.hotelroom.pasrest.exceptions;

public class ApplicationException extends Exception {

    private String errorCode;

    public ApplicationException() {
        super();
    }

    public ApplicationException(String message) {
        super(message);
        this.errorCode = null;
    }

    public ApplicationException(String message, String errorCode) {
        this(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
