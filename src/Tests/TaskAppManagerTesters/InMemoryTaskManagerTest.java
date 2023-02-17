package Tests.TaskAppManagerTesters;

import TaskAppClasses.*;
import TaskAppEnums.Status;
import TaskAppEnums.Type;
import TaskAppManagers.InMemoryTaskManager;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    InMemoryTaskManager manager = new InMemoryTaskManager();
    public Task create(Type type) {
        if (type.equals(Type.TASK)) {
            return new Task ("01", "01", Status.NEW, 15, "2016-11-09 10:30");
        } else if (type.equals(Type.EPIC)) {
            return new Epic ("01", "01", Status.NEW);
        }
        return new Subtask("01", "01", Status.NEW, 0, 15, "2014-11-09 10:30");
    }

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
    }

    @Test
    public void addAnyTaskWithIntersectedTime() {
        Task task1 = new Task("#1", "#1", Status.NEW, 15,"2001-11-09 10:30");
        manager.saveTask(task1);
        Task task2 = new Task("#2", "#2", Status.NEW, 15,"2000-11-09 10:30");
        manager.saveTask(task2);
        //Добавляем задачу с пересекающим временем
        Task task3 = new Task("#3", "#3", Status.NEW, 15,"2000-11-09 10:40");
        assertEquals(0, manager.saveTask(task3));
    }

    @Test
    public void getPrioritizedTasks() {
        Task task1 = new Task("#1", "#1", Status.NEW, 15,"2001-11-09 10:30");
        manager.saveTask(task1);
        Task task2 = new Task("#2", "#2", Status.NEW, 15,"2000-11-09 10:30");
        manager.saveTask(task2);
        Task task3 = new Task("#3", "#3", Status.NEW, 15," ");
        manager.saveTask(task3);
        Task[] prioritizedTasks = new Task[3];
        prioritizedTasks[0] = task2;
        prioritizedTasks[1] = task1;
        prioritizedTasks[2] = task3;
        assertArrayEquals(prioritizedTasks, manager.getPrioritizedTasks().toArray());
    }

    @Test
    public void addNewTask() {
        Task task = create(Type.TASK);
        int taskId = manager.saveTask(task);
        Task savedTask = manager.getTaskByID(taskId);
        assertEquals(task, savedTask, "Задачи не совпадают");
        List<Task> tasks = manager.getAllTasks();
        assertEquals(1, tasks.size(), "Неверное количество задач");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void addNewEpic() {
        Epic epic = (Epic) create(Type.EPIC);
        int epicId = manager.saveEpic(epic);
        Epic savedEpic = manager.getEpicByID(epicId);
        assertEquals(epic, savedEpic, "Эпики не совпадают");
        List<Epic> epics = manager.getAllEpics();
        assertEquals(1, epics.size(), "Неверное количество эпиков");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
        assertEquals(Status.NEW, savedEpic.getStatus(), "Статусы не сопадают");

        Subtask subtask = new Subtask("1", "1", Status.IN_PROGRESS, epicId, 15, "2016-11-09 10:30");
        manager.saveSubtask(subtask);
        assertEquals(Status.IN_PROGRESS, savedEpic.getStatus(), "Статусы не сопадают");
    }

    @Test
    public void addNewSubtask() {
        Epic epic = (Epic) create(Type.EPIC);
        int epicId = manager.saveEpic(epic);
        Subtask subtask = new Subtask("1", "1", Status.NEW, epicId, 15, "2016-11-09 10:30");
        int subtaskId = manager.saveSubtask(subtask);
        Subtask savedSubtask = manager.getSubtaskByID(subtaskId);
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают");
        List<Subtask> subtasks =manager.getAllSubtasks();
        assertEquals(1, subtasks.size(), "Неверное количество подзадач");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");
        assertEquals(subtask.getEpicId(), epicId, "Эпик id не совпадает");
        assertNotNull(manager.getEpicByID(epicId));
        assertFalse(epic.getSubtaskList().isEmpty());
        assertTrue(epic.getSubtaskList().contains(savedSubtask));
    }

    @Test
    public void updateTask() {
        int taskId = manager.saveTask(create(Type.TASK));
        Task newTask = new Task("1", "1", Status.NEW, 15, "2017-11-09 10:30");
        newTask.setId(taskId);
        manager.updateTask(newTask);
        assertEquals(newTask, manager.getTaskByID(taskId), "Задачи не совпадают после обновления");
    }

    @Test
    public void updateEpic() {
        Epic epic = (Epic) create(Type.EPIC);
        int epicId = manager.saveEpic(epic);
        Epic newEpic = new Epic("2", "2", Status.NEW);
        newEpic.setId(epicId);
        manager.updateEpic(newEpic);
        assertNotNull(manager.updateEpic(newEpic));
        assertEquals(newEpic, manager.getEpicByID(epicId), "Эпики не совпадают после обновления");
    }

    @Test
    public void updateSubtask() {
        Epic epic = (Epic) create(Type.EPIC);
        int epicId = manager.saveEpic(epic);
        Subtask subtask = new Subtask("1", "1", Status.NEW, epicId, 15, "2017-11-09 10:30");
        int subtaskId = manager.saveSubtask(subtask);
        Subtask newSubtask = new Subtask("2", "2", Status.NEW, epicId, 15, "2017-11-09 10:30");
        newSubtask.setId(subtaskId);
        manager.updateSubtask(newSubtask);
        assertEquals(newSubtask, manager.getSubtaskByID(subtaskId), "Подзадачи не совпадают после обновления");
        assertTrue(epic.getSubtaskList().contains(newSubtask));
    }

    @Test
    public void removeTaskById() {
        Task task = create(Type.TASK);
        int taskId = manager.saveTask(task);
        manager.removeTaskByID(taskId);
        assertNull(manager.getTaskByID(taskId), "Задача не была удалена");
    }

    @Test
    public void removeEpicById() {
        Epic epic = (Epic) create(Type.EPIC);
        int epicId = manager.saveEpic(epic);
        manager.removeEpicByID(epicId);
        assertNull(manager.getEpicByID(epicId), "Эпик не был удален");
    }

    @Test
    public void removeSubtaskById() {
        Epic epic = (Epic) create(Type.EPIC);
        int epicId = manager.saveEpic(epic);
        Subtask subtask = new Subtask("1", "1", Status.NEW, epicId, 15, "2016-11-09 10:30");
        int subtaskId = manager.saveSubtask(subtask);
        assertFalse(epic.getSubtaskList().isEmpty());
        manager.removeSubtaskByID(subtaskId);
        assertNull(manager.getSubtaskByID(subtaskId), "Подзадача не была удалена");
        assertTrue(epic.getSubtaskList().isEmpty());
    }

    @Test
    public void removeAllTasks() {
        Task task1 = create(Type.TASK);
        Task task2 = new Task("2", "2", Status.NEW, 15, "2016-11-09 10:30");
        manager.saveTask(task1);
        manager.saveTask(task2);
        manager.deleteAllTasks();
        assertNull(manager.getAllTasks());
    }

    @Test
    public void removeAllEpics() {
        Epic epic1 = (Epic) create(Type.EPIC);
        Epic epic2 = new Epic("1", "1", Status.NEW);
        manager.saveEpic(epic1);
        manager.saveEpic(epic2);
        manager.deleteAllEpics();
        assertNull(manager.getAllEpics());
    }
    @Test
    public void removeAllSubtasks() {
        Epic epic = (Epic) create(Type.EPIC);
        int epicId = manager.saveEpic(epic);
        Subtask subtask1 = new Subtask("1", "1", Status.NEW, epicId, 15, "2016-11-09 10:30");
        Subtask subtask2 = new Subtask("2", "2", Status.DONE, epicId, 15, "2016-11-09 10:30");
        manager.saveSubtask(subtask1);
        manager.saveSubtask(subtask2);
        manager.deleteAllSubtasks();
        assertNull(manager.getAllSubtasks());
        assertTrue(manager.getEpicByID(epicId).getSubtaskList().isEmpty());
    }

    @Test
    public void getHistory() {
        Task task = create(Type.TASK);;
        int taskId = manager.saveTask(task);
        Epic epic = (Epic) create(Type.EPIC);
        int epicId = manager.saveEpic(epic);
        Subtask subtask = new Subtask("1", "1", Status.NEW, epicId, 15, "2017-11-09 10:30");
        int subtaskId = manager.saveSubtask(subtask);
        manager.getTaskByID(taskId);
        manager.getEpicByID(epicId);
        manager.getSubtaskByID(subtaskId);
        List<Task> history = new ArrayList<>();
        history.add(task);
        history.add(epic);
        history.add(subtask);
        assertFalse(manager.getHistory().isEmpty());
        assertEquals(history, manager.getHistory());
    }

    @Test
    public void getTaskWhenListIsEmpty() {
        assertNull(manager.getTaskByID(100));
    }

    @Test
    public void getEpicWhenListIsEmpty() {
        assertNull(manager.getEpicByID(100));
    }

    @Test
    public void getSubtaskWhenListIsEmpty() {
        assertNull(manager.getSubtaskByID(100));
    }

    @Test
    public void updateTaskWhenListIsEmpty() {
        Task task = create(Type.TASK);;
        assertNull(manager.updateTask(task));
    }

    @Test
    public void updateEpicWhenListIsEmpty() {
        Epic epic = (Epic) create(Type.EPIC);
        assertNull(manager.updateTask(epic));
    }

    @Test
    public void updateSubtaskWhenListIsEmpty() {
        Subtask subtask = new Subtask("1", "1", Status.NEW, 100, 15, "2016-11-09 10:30");
        assertNull(manager.updateTask(subtask));
    }

    @Test
    public void removeTaskWhenListIsEmpty() {
        assertEquals("Нельзя удалить задачу №" + 0 + ", так как ее нет.", manager.removeTaskByID(0));
        assertNull(manager.getAllTasks());
    }

    @Test
    public void removeEpicWhenListIsEmpty() {
        assertEquals("Нельзя удалить эпик №" + 0 + ", так как его нет.", manager.removeEpicByID(0));
        assertNull(manager.getAllEpics());
    }

    @Test
    public void removeSubtaskWhenListIsEmpty() {
        assertEquals("Нельзя удалить подзадачу №" + 0 + ", так как ее нет.", manager.removeSubtaskByID(0));
        assertNull(manager.getAllSubtasks());
    }

    @Test
    public void deleteAllTasksWhenListIsEmpty() {
        assertEquals("Список задач пуст, удалять нечего.", manager.deleteAllTasks());
    }

    @Test
    public void deleteAllEpicsWhenListIsEmpty() {
        assertEquals("Список эпиков пуст, нечего удалять.", manager.deleteAllEpics());
    }

    @Test
    public void deleteAllSubtasksWhenListIsEmpty() {
        assertEquals("Список подзадач пуст, нечего удалять.", manager.deleteAllSubtasks());
    }

    @Test
    public void getHistoryWhenListIsEmpty() {
        assertNull(manager.getHistory());
    }

    @Test
    public void getTaskByInvalidId() {
        int taskId = manager.saveTask(create(Type.TASK));
        assertNull(manager.getTaskByID(0));
    }

    @Test
    public void getEpicByInvalidId() {
        int epicId = manager.saveEpic((Epic) create(Type.EPIC));
        assertNull(manager.getEpicByID(0));
    }

    @Test
    public void getSubtaskByInvalidId() {
        int epicId = manager.saveEpic((Epic) create(Type.EPIC));
        int subtaskId = manager.saveSubtask(new Subtask("0", "0", Status.NEW, epicId, 15, "2016-11-09 10:30"));
        assertNull(manager.getSubtaskByID(0));
    }

    @Test
    public void removeTaskByInvalidId() {
        assertEquals("Нельзя удалить задачу №" + 0 + ", так как ее нет.", manager.removeTaskByID(0));
    }

    @Test
    public void removeEpicByInvalidId() {
        assertEquals("Нельзя удалить эпик №" + 0 + ", так как его нет.", manager.removeEpicByID(0));
    }

    @Test
    public void removeSubtaskByInvalidId() {
        assertEquals("Нельзя удалить подзадачу №" + 0 + ", так как ее нет.", manager.removeSubtaskByID(0));
    }
}