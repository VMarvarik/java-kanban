package HistoryFunctionalityRealization;
import TaskAppRealization.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> browsingHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            System.out.println("Задача не может быть пустой!");
        } else {
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