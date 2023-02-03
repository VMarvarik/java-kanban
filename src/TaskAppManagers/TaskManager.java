package TaskAppManagers;
import TaskAppClasses.*;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();

    String deleteAllTasks();

    Task getTaskByID(int id);

    int saveTask(Task task);

    Task updateTask(Task newTask);

    String removeTaskByID(int id);

    List<Epic> getAllEpics();

    String deleteAllEpics();

    Epic getEpicByID(int id);

    int saveEpic(Epic epic);

    Epic updateEpic(Epic newEpic);

    String removeEpicByID(int id);

    String getEpicSubtasks(int epicId);

    List<Subtask> getAllSubtasks();

    String deleteAllSubtasks();

    Subtask getSubtaskByID(int id);

    int saveSubtask(Subtask subtask);

    Subtask updateSubtask(Subtask newSubtask);

    String removeSubtaskByID(int id);

    void epicStatusCheck();

    ArrayList<Task> getHistory();

    ArrayList<Task> getPrioritizedTasks();
}