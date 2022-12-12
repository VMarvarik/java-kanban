import TaskAppRealization.*;

//Семен, снова здравствуйте! :) Когда писала программу, не подумала о том, что кто-то может создать подзадачу и
// не привязать ее к эпику. Решила это через добавление параметра в конструктор и дополнительной проверки
// в методе saveSubtask на наличие такого epicId. Вопрос "Почему для реализации выбрала ArrayList<>()?" -
// немножко не поняла, но сделала просто лист как объект ArrayList. Мне кажется, что проверка на null не нужна,
// так как метод используется в методах saveTask, saveEpic, saveSubtask уже после встроенной проверки на null,
// но я все равно ее добавила, так как метод можно вызвать в main.
// Не могу понять как возвращать классы в Managers без их объявления, получается только каждый раз возвращать новый объект...
// Нашла ошибки вызовов методов в мейн, исправила.
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
        int id7 = Managers.getDefault().saveEpic(epic1);
        Subtask subtask1 = new Subtask("8", "8", Status.NEW, id7);
        int id8 = Managers.getDefault().saveSubtask(subtask1);
        System.out.println("Вызываем таски 1, 2, 3, 4, 5, 6. Вызываем эпик 7 и сабтаск 8.");
        Managers.getDefault().getTaskByID(id1);
        Managers.getDefault().getTaskByID(id2);
        Managers.getDefault().getTaskByID(id3);
        Managers.getDefault().getTaskByID(id4);
        Managers.getDefault().getTaskByID(id5);
        Managers.getDefault().getTaskByID(id6);
        Managers.getDefault().getEpicByID(id7);
        Managers.getDefault().getSubtaskByID(id8);
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