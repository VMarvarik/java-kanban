package TaskAppManagers;
import TaskAppClasses.Node;
import TaskAppClasses.Task;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList<Task> browsingHistory = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        final int MAX_HISTORY_SIZE = 10;
        if (task != null) {
            browsingHistory.linkLast(task);
        }
        if (browsingHistory.size > MAX_HISTORY_SIZE) {
            browsingHistory.removeFirst();
        }
    }

    @Override
    public void remove(int id) {
        browsingHistory.removeNode(browsingHistory.getBrowsingHistoryMap().get(id));
    }

    @Override
    public ArrayList<Task> getHistory() {
        return browsingHistory.getTasks();
    }
}

class CustomLinkedList <T> {
    public Node head;
    public Node tail;
    private final HashMap<Integer, Node> browsingHistoryMap = new HashMap<>();
    int size = -1;

    public HashMap<Integer, Node> getBrowsingHistoryMap() {
        return browsingHistoryMap;
    }

    public void linkLast(Task task) {
        if (browsingHistoryMap.containsKey(task.getId())) {
            removeNode(browsingHistoryMap.get(task.getId()));
        }
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
            browsingHistoryMap.put(task.getId(), head);
        } else {
            oldTail.setLinkNext(newNode);
            browsingHistoryMap.put(task.getId(), tail);
        }
        size++;
    }
    public ArrayList<Task> getTasks() {
        ArrayList<Task> history = new ArrayList<>();
        Node x = head;
        history.add(x.getTask());
        for (int i = 0; i < size; i++) {
            if (x.getLinkNext() != null) {
                x = x.getLinkNext();
                history.add(x.getTask());
            } else {
                break;
            }
        }
        return history;
    }

    public void removeNode(Node node) {
        if (node.getLinkPrev() == null) {
            removeFirst();
            return;
        }
        if (node.getLinkNext() != null) {
            node.getLinkPrev().setLinkNext(node.getLinkNext());
        } else {
            tail = node.getLinkPrev();
        }
        size--;
    }

    public void removeFirst() {
        Node first = head;
        final Node next = first.getLinkNext();
        first.setTask(null);
        first.setLinkNext(null);
        head = next;
        if (next == null) {
            tail = null;
        } else {
            next.setLinkPrev(null);
        }
        size--;
    }
}