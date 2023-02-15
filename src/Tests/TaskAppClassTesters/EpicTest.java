package Tests.TaskAppClassTesters;

import TaskAppClasses.Epic;
import TaskAppEnums.Status;
import TaskAppClasses.Subtask;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {
    Epic epic = new Epic("1", "1", Status.NEW);
    public Subtask createSubtask(Status status) {
        return new Subtask("0", "0", status, epic.getId(), 15, "2016-11-09 10:30");
    }

    @BeforeEach
    public void createEpic() {
        this.epic = new Epic("1", "1", Status.NEW);
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

    @Test

    public void epicTimeShouldChangeDependingOnSubtask() {
        //Создаем эпик с null временем проверяем вызов конечного, начального времен и длительности
        Epic epic = new Epic("#1", "#1", Status.NEW);
        assertNull(epic.getStartTime());
        assertNull(epic.getEndTime());
        assertEquals(0, epic.getDuration());
        //Создаем эпик с обычным временем
        Epic epic2 = new Epic("#2", "#2", Status.NEW);
        epic2.fillSubtaskList(new Subtask("1", "1", Status.NEW, epic2.getId(), 20, "2000-01-10 10:30"));
        assertEquals("2000-01-10 10:30", epic2.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        assertEquals("2000-01-10 10:50", epic2.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        //Добавляем в эпик подзадачу, должна быть смена времени на "2000-01-09 10:30"
        //Длительность должна быть 30
        Subtask subtask = new Subtask("#1", "#1", Status.NEW, epic2.getId(), 10, "2000-01-09 10:30");
        epic2.fillSubtaskList(subtask);
        assertEquals("2000-01-09 10:30", epic2.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        assertEquals(30, epic2.getDuration());
        //Проверяем смену времени окончания с null
        assertEquals("2000-01-10 10:50", epic2.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        //Создаем и добавляем еще одну подзадачу с более поздним временем начала, время старта эпика не должно измениться, также
        //проверяем продолжительность эпика, а его время окончания должно быть равно времени окончания самой поздней подзадачи
        Subtask subtask2 = new Subtask("#2", "#2", Status.NEW, epic2.getId(), 40, "2000-01-11 10:30");
        epic2.fillSubtaskList(subtask2);
        assertEquals("2000-01-09 10:30", epic2.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        assertEquals(70, epic2.getDuration());
        assertEquals(subtask2.getEndTime(), epic2.getEndTime());
    }
}