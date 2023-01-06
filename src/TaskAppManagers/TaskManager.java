package TaskAppManagers;
import TaskAppClasses.*;
import java.util.Collection;

public interface TaskManager {
    Collection<Task> getAllTasks();

    void deleteAllTasks();

    Task getTaskByID(int id);

    int saveTask(Task task);

    void updateTask(Task newTask);

    void removeTaskByID(int id);

    Collection<Epic> getAllEpics();

    void deleteAllEpics();

    Task getEpicByID(int id);

    int saveEpic(Epic epic);

    void updateEpic(Epic newEpic);

    void removeEpicByID(int id);

    String getEpicSubtasks(int epicId);

    void getAllSubtasks();

    void deleteAllSubtasks();

    Task getSubtaskByID(int id);

    int saveSubtask(Subtask subtask);

    void updateSubtask(Subtask newSubtask);

    void removeSubtaskByID(int id);

    void epicStatusCheck();
}