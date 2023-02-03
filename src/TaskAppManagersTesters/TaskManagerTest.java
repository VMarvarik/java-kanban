package TaskAppManagersTesters;

import TaskAppManagers.TaskManager;
import org.junit.Test;

abstract class TaskManagerTest<T extends TaskManager> {

    @Test
    public abstract void addAnyTaskWithIntersectedTime();

    @Test
    public abstract void addNewTask();

    @Test
    public abstract void addNewEpic();

    @Test
    public abstract void addNewSubtask();

    @Test
    public abstract void updateTask();

    @Test
    public abstract void updateEpic();

    @Test
    public abstract void updateSubtask();

    @Test
    public abstract void removeTaskById();

    @Test
    public abstract void removeEpicById();
    @Test
    public abstract void removeSubtaskById();

    @Test
    public abstract void removeAllTasks();

    @Test
    public abstract void removeAllEpics();

    @Test
    public abstract void removeAllSubtasks();

    @Test
    public abstract void getHistory();

    @Test
    public abstract void getTaskWhenListIsEmpty();

    @Test
    public abstract void getEpicWhenListIsEmpty();

    @Test
    public abstract void getSubtaskWhenListIsEmpty();

    @Test
    public abstract void updateTaskWhenListIsEmpty();

    @Test
    public abstract void updateEpicWhenListIsEmpty();

    @Test
    public abstract void updateSubtaskWhenListIsEmpty();

    @Test
    public abstract void removeTaskWhenListIsEmpty();

    @Test
    public abstract void removeEpicWhenListIsEmpty();

    @Test
    public abstract void removeSubtaskWhenListIsEmpty();

    @Test
    public abstract void deleteAllTasksWhenListIsEmpty();

    @Test
    public abstract void deleteAllEpicsWhenListIsEmpty();

    @Test
    public abstract void deleteAllSubtasksWhenListIsEmpty();

    @Test
    public abstract void getHistoryWhenListIsEmpty();

    @Test
    public abstract void getTaskByInvalidId();

    @Test
    public abstract void getEpicByInvalidId();

    @Test
    public abstract void getSubtaskByInvalidId();

    @Test
    public abstract void removeTaskByInvalidId();

    @Test
    public abstract void removeEpicByInvalidId();

    @Test
    public abstract void removeSubtaskByInvalidId();

    @Test
    public abstract void getPrioritizedTasks();
}