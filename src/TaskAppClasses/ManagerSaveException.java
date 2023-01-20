package TaskAppClasses;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(final String message, Exception exception) {
        super(message);
        exception.printStackTrace();
    }
}