package TaskAppManagers;
import TaskAppClasses.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> browsingHistory = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            browsingHistory.add(task);
            final int MAX_HISTORY_SIZE = 10;
            if (browsingHistory.size() > MAX_HISTORY_SIZE) {
                browsingHistory.remove(0);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return browsingHistory;
    }
}