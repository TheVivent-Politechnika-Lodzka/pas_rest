package pl.ias.pas.hotelroom.pasrest.exceptions;

public class IDontKnowException extends RuntimeException {
    public IDontKnowException() {
    }

    public IDontKnowException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " ¯\\_(ツ)_/¯";
    }
}
