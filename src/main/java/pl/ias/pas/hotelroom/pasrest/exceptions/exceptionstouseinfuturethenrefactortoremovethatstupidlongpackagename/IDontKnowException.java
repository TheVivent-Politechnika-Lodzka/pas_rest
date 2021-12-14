package pl.ias.pas.hotelroom.pasrest.exceptions.exceptionstouseinfuturethenrefactortoremovethatstupidlongpackagename;

public class IDontKnowException extends Exception {
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
