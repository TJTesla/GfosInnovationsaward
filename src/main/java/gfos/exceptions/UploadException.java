package gfos.exceptions;

// Fehlermeldung bei Problemen beim Hochladen von Dateien

public class UploadException extends Exception {
    private final String msg;

    public UploadException(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }
}
