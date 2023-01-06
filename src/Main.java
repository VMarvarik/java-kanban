import TaskAppManagers.InMemoryTaskManager;
import TaskAppManagers.Managers;
import TaskAppClasses.*;
//Здравствуйте, Семен! С наступившим 2023 годом и рождеством! Желаю, чтобы этот год был полон счастья и хороших учеников :)
public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = Managers.getDefault();
        Task task1 = new Task("1", "1", Status.NEW);
        int id1 = inMemoryTaskManager.saveTask(task1);
        Task task2 = new Task("2", "2", Status.NEW);
        int id2 = inMemoryTaskManager.saveTask(task2);
        Task task3 = new Task("3", "3", Status.NEW);
        int id3 = inMemoryTaskManager.saveTask(task3);
        Task task4 = new Task("4", "4", Status.NEW);
        int id4 = inMemoryTaskManager.saveTask(task4);
        Task task5 = new Task("5", "5", Status.NEW);
        int id5 = inMemoryTaskManager.saveTask(task5);
        Task task6 = new Task("6", "6", Status.NEW);
        int id6 = inMemoryTaskManager.saveTask(task6);
        Epic epic1 = new Epic("7", "7", Status.NEW);
        int id7 = inMemoryTaskManager.saveEpic(epic1);
        Subtask subtask1 = new Subtask("8", "8", Status.NEW, id7);
        int id8 = inMemoryTaskManager.saveSubtask(subtask1);
        inMemoryTaskManager.getTaskByID(id1);
        inMemoryTaskManager.getTaskByID(id1);
        inMemoryTaskManager.getTaskByID(id2);
        inMemoryTaskManager.getTaskByID(id3);
        inMemoryTaskManager.getTaskByID(id4);
        inMemoryTaskManager.getTaskByID(id5);
        inMemoryTaskManager.getTaskByID(id6);
        inMemoryTaskManager.getEpicByID(id7);
        inMemoryTaskManager.getSubtaskByID(id8);
        inMemoryTaskManager.getTaskByID(id6);
        inMemoryTaskManager.getTaskByID(id6);
        System.out.println("Вызываем историю. Она должна быть 1 2 3 4 5 7 8 6");
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println("Удаляем таск 1, 2 и сабтаск 8");
        inMemoryTaskManager.removeTaskByID(id1);
        inMemoryTaskManager.removeTaskByID(id2);
        inMemoryTaskManager.removeSubtaskByID(id8);
        System.out.println("Вызываем историю. Она должна быть 3 4 5 7 6");
        System.out.println(inMemoryTaskManager.getHistory());
    }
}