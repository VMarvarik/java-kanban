package Tests.TaskAppManagerTesters;

import TaskAppClasses.*;
import TaskAppManagers.InMemoryHistoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class InMemoryHistoryManagerTest {

    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    public void beforeEach() {
        this.task = create(Type.TASK);
        this.task.setId(101);
        this.epic = (Epic) create(Type.EPIC);
        this.epic.setId(201);
        this.subtask = new Subtask("01", "01", Status.NEW, epic.getId(), 15, "2016-11-09 10:30");
        subtask.setId(301);
        this.inMemoryHistoryManager = new InMemoryHistoryManager();
    }

    public Task create(Type type) {
        if (type.equals(Type.TASK)) {
            return new Task ("01", "01", Status.NEW, 15, "2016-11-09 10:30");
        } else if (type.equals(Type.EPIC)) {
            return new Epic("01", "01", Status.NEW);
        }
        return new Subtask("01", "01", Status.NEW, 0, 15, "2016-11-09 10:30");
    }

    @Test
    void add() {
        inMemoryHistoryManager.add(task);
        Assertions.assertEquals(1, inMemoryHistoryManager.getHistory().size());
        inMemoryHistoryManager.add(epic);
        Assertions.assertEquals(2, inMemoryHistoryManager.getHistory().size());
        inMemoryHistoryManager.add(subtask);
        Assertions.assertEquals(3, inMemoryHistoryManager.getHistory().size());

        List<Task> history = new ArrayList<>();
        history.add(task);
        history.add(epic);
        history.add(subtask);
        Assertions.assertEquals(history, inMemoryHistoryManager.getHistory());
    }

    @Test
    void shallDeleteDuplicatesIfThereIsOne() {
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subtask);
        inMemoryHistoryManager.add(task);
        List<Task> history = new ArrayList<>();
        history.add(epic);
        history.add(subtask);
        history.add(task);
        Assertions.assertEquals(3, inMemoryHistoryManager.getHistory().size());
        Assertions.assertEquals(history, inMemoryHistoryManager.getHistory());
    }

    @Test
    void removeFromTheBeginning() {
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subtask);
        inMemoryHistoryManager.remove(task.getId());
        List<Task> history = new ArrayList<>();
        history.add(epic);
        history.add(subtask);
        Assertions.assertEquals(history, inMemoryHistoryManager.getHistory());
        inMemoryHistoryManager.remove(epic.getId());
        history.remove(epic);
        Assertions.assertEquals(history, inMemoryHistoryManager.getHistory());
    }

    @Test
    void removeFromTheMiddle() {
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subtask);
        inMemoryHistoryManager.remove(epic.getId());
        List<Task> history = new ArrayList<>();
        history.add(task);
        history.add(subtask);
        Assertions.assertEquals(history, inMemoryHistoryManager.getHistory());
    }

    @Test
    void removeFromTheEnd() {
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subtask);
        inMemoryHistoryManager.remove(subtask.getId());
        List<Task> history = new ArrayList<>();
        history.add(task);
        history.add(epic);
        Assertions.assertEquals(history, inMemoryHistoryManager.getHistory());
    }

    @Test
    void getHistoryWhenEmpty() {
        Assertions.assertNull(inMemoryHistoryManager.getHistory());
    }
}