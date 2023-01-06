package TaskAppClasses;

public class Node {
    Task task;
    private Node linkNext;
    private Node linkPrev;

    public Node(Node prev, Task task, Node next) {
        this.task = task;
        this.linkNext = next;
        this.linkPrev = prev;
    }

    public Node getLinkNext() {
        return linkNext;
    }

    public Node getLinkPrev() {
        return linkPrev;
    }

    public void setLinkNext(Node linkNext) {
        this.linkNext = linkNext;
    }

    public void setLinkPrev(Node linkPrev) {
        this.linkPrev = linkPrev;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}