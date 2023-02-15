package TaskAppExceptions;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException() {
    }

    public ManagerSaveException(String message) {
        super(message);
    }

    public ManagerSaveException(String message, Throwable exception) {
        super(message, exception);
    }
}