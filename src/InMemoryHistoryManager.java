import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> browsingHistory;

    public InMemoryHistoryManager() {
        browsingHistory = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        browsingHistory.add(task);
        final int MAX_HISTORY_SIZE = 10;
        if (browsingHistory.size() > MAX_HISTORY_SIZE) {
            browsingHistory.remove(0);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return browsingHistory;
    }
}