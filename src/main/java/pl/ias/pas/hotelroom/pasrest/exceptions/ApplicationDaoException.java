package pl.ias.pas.hotelroom.pasrest.exceptions;

public class ApplicationDaoException extends ApplicationException {

    public ApplicationDaoException(String errorCode, String message) {
        super(errorCode, message);
    }
}
