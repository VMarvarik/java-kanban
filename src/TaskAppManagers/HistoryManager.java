package TaskAppManagers;
import TaskAppClasses.Task;
import java.util.List;

public interface HistoryManager {
    void add(Task task);
    List<Task> getHistory();
}