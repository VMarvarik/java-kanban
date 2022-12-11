//Семен, здравствуйте! С наступающим!
public class Main {
    public static void main(String[] args) {
        Task task1 = new Task("1", "1", Status.NEW);
        int id1 = Managers.getDefault().saveTask(task1);
        Task task2 = new Task("2", "2", Status.NEW);
        int id2 = Managers.getDefault().saveTask(task2);
        Task task3 = new Task("3", "3", Status.NEW);
        int id3 = Managers.getDefault().saveTask(task3);
        Task task4 = new Task("4", "4", Status.NEW);
        int id4 = Managers.getDefault().saveTask(task4);
        Task task5 = new Task("5", "5", Status.NEW);
        int id5 = Managers.getDefault().saveTask(task5);
        Task task6 = new Task("6", "6", Status.NEW);
        int id6 = Managers.getDefault().saveTask(task6);
        Epic epic1 = new Epic("7", "7", Status.NEW);
        int id7 = Managers.getDefault().saveTask(epic1);
        Subtask subtask1 = new Subtask("8", "8", Status.NEW);
        int id8 = Managers.getDefault().saveTask(subtask1);
        System.out.println("Вызываем таски 1, 2, 3, 4, 5, 6. Вызываем эпик 7 и сабтаск 8.");
        Managers.getDefault().getTaskByID(id1);
        Managers.getDefault().getTaskByID(id2);
        Managers.getDefault().getTaskByID(id3);
        Managers.getDefault().getTaskByID(id4);
        Managers.getDefault().getTaskByID(id5);
        Managers.getDefault().getTaskByID(id6);
        Managers.getDefault().getTaskByID(id7);
        Managers.getDefault().getTaskByID(id8);
        System.out.println("Вызываем историю просмотров.");
        System.out.println(Managers.getDefaultHistory().getHistory());
        System.out.println("Вызываем таски 1, 2, 3. ");
        Managers.getDefault().getTaskByID(id1);
        Managers.getDefault().getTaskByID(id2);
        Managers.getDefault().getTaskByID(id3);
        System.out.println("Взываем историю просмотров. " +
                "История просмотров теперь должна быть - 2, 3, 4, 5, 6, 7, 8, 1, 2, 3");
        System.out.println(Managers.getDefaultHistory().getHistory());
    }
}