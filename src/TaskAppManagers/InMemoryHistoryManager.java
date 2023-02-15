package TaskAppManagers;
import TaskAppCustomLinkedListRealization.CustomLinkedList;
import TaskAppClasses.Task;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList browsingHistory = new CustomLinkedList();

    @Override
    public void add(Task task) {
        final int MAX_HISTORY_SIZE = 10;
        if (task != null) {
            browsingHistory.linkLast(task);
        }
        if (browsingHistory.getSize() > MAX_HISTORY_SIZE) {
            browsingHistory.removeFirst();
        }
    }

    @Override
    public void remove(int id) {
        browsingHistory.removeNode(browsingHistory.getBrowsingHistoryMap().get(id));
    }

    @Override
    public ArrayList<Task> getHistory() {
        if (browsingHistory.getSize() == -1) {
            return null;
        }
        return browsingHistory.getTasks();
    }
}