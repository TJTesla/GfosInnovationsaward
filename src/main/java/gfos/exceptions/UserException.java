package gfos.exceptions;

// Nicht mehr benutzt

public class UserException extends Exception {
    private final String message;

    public UserException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
