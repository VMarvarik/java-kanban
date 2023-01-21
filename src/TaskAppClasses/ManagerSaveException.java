package TaskAppClasses;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException() {
    }

    public ManagerSaveException(String message, Exception exception) {
        super(message);
        exception.printStackTrace();
    }
}