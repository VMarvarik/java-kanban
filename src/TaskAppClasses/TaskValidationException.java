package TaskAppClasses;

public class TaskValidationException extends RuntimeException {

    public TaskValidationException() {
    }

    public TaskValidationException(String message) {
        super(message);
    }

    public TaskValidationException(String message, Throwable exception) {
        super(message, exception);
    }
}