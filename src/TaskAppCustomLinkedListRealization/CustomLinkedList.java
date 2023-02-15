package TaskAppCustomLinkedListRealization;
import TaskAppClasses.Task;
import TaskAppCustomLinkedListRealization.Node;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomLinkedList {
    private Node head;
    private Node tail;
    private final HashMap<Integer, Node> browsingHistoryMap = new HashMap<>();
    private int size = -1;

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
            browsingHistoryMap.put(task.getId(), newNode);
        }
        size++;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> history = new ArrayList<>();
        Node node = head;
        history.add(node.getTask());
        for (int i = 0; i < size; i++) {
            if (node.getLinkNext() != null) {
                node = node.getLinkNext();
                history.add(node.getTask());
            } else {
                break;
            }
        }
        return history;
    }

    public void removeNode(Node node) {
        if (node == null) {
            return;
        }
        final Node next = node.getLinkNext();
        final Node prev = node.getLinkPrev();
        if (prev == null) {
            removeFirst();
            return;
        }

        if (next == null) {
            removeLast();
            return;
        }
        prev.setLinkNext(next);
        node.setLinkPrev(null);
        next.setLinkPrev(prev);
        node.setLinkNext(null);
        node.setTask(null);
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

    private void removeLast() {
        Node last = tail;
        final Node prev = last.getLinkPrev();
        last.setTask(null);
        last.setLinkPrev(null);
        tail = prev;
        if (prev == null) {
            head = null;
        } else {
            prev.setLinkNext(null);
        }
        size--;
    }

    public int getSize() {
        return size;
    }
}