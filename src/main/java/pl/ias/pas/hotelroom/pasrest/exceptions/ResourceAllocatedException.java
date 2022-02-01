package pl.ias.pas.hotelroom.pasrest.exceptions;

public class ResourceAllocatedException extends RuntimeException {
    public ResourceAllocatedException() {
    }

    public ResourceAllocatedException(String message) {
        super(message);
    }
}
