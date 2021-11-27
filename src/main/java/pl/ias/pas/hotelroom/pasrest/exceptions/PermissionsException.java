package pl.ias.pas.hotelroom.pasrest.exceptions;

public class PermissionsException extends ApplicationException {


    public PermissionsException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
