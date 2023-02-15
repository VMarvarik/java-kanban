import TaskAppClasses.Epic;
import TaskAppClasses.Subtask;
import TaskAppEnums.Status;
import TaskAppClasses.Task;
import TaskAppServerFunctionalityRealization.HttpTaskManager;
import TaskAppServerFunctionalityRealization.KVServer;
import TaskAppManagers.Managers;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new KVServer().start();
        HttpTaskManager httpTaskManager = Managers.getDefault();
        httpTaskManager.setKey("semyon");
        Task task1 = new Task("1", "1", Status.NEW, 10, "2016-11-09 10:30");
        int id1 = httpTaskManager.saveTask(task1);
        Task task2 = new Task("2", "2", Status.NEW, 15, "2017-11-09 10:30");
        int id2 = httpTaskManager.saveTask(task2);
        Epic epic1 = new Epic("3", "3", Status.NEW);
        int id3 = httpTaskManager.saveEpic(epic1);
        Epic epic2 = new Epic("4", "4", Status.IN_PROGRESS);
        int id4 = httpTaskManager.saveEpic(epic2);
        Subtask subtask1 = new Subtask("5", "5", Status.IN_PROGRESS, id3, 30, null);
        int id5 = httpTaskManager.saveSubtask(subtask1);
        httpTaskManager.getTaskByID(id1);
        httpTaskManager.getTaskByID(id2);
        httpTaskManager.getEpicByID(id3);
        httpTaskManager.getEpicByID(id4);
        httpTaskManager.getSubtaskByID(id5);
        HttpTaskManager newHttpTaskManager = httpTaskManager.loadFromServer();
        System.out.println("Ключ");
        System.out.println(newHttpTaskManager.getKey());
        System.out.println("История");
        System.out.println(newHttpTaskManager.getHistory());
        System.out.println("Все таски");
        System.out.println(newHttpTaskManager.getAllTasks());
        System.out.println("Все эпики");
        System.out.println(newHttpTaskManager.getAllEpics());
        System.out.println("Все подзадачи");
        System.out.println(newHttpTaskManager.getAllSubtasks());
    }
}