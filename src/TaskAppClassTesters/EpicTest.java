package TaskAppClassTesters;

import TaskAppClasses.Epic;
import TaskAppClasses.Status;
import TaskAppClasses.Subtask;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {
    Epic epic = new Epic("1", "1", Status.NEW, 15, "2016-11-09 10:30");
    public Subtask createSubtask(Status status) {
        return new Subtask("0", "0", status, epic.getId(), 15, "2016-11-09 10:30");
    }

    @BeforeEach
    public void createEpic() {
        this.epic = new Epic("1", "1", Status.NEW, 15, "2016-11-09 10:30");
    }

    @Test
    public void shouldReturnNewIfSubtaskListIsEmpty() {
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void shouldReturnNewIfAllSubtasksNew() {
        epic.fillSubtaskList(createSubtask(Status.NEW));
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void shouldReturnInProgressIfSubtasksNewAndDone() {
        epic.fillSubtaskList(createSubtask(Status.NEW));
        epic.fillSubtaskList(createSubtask(Status.DONE));
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void shouldReturnInProgressIfSubtasksInProgress() {
        epic.fillSubtaskList(createSubtask(Status.IN_PROGRESS));
        epic.fillSubtaskList(createSubtask(Status.IN_PROGRESS));
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}