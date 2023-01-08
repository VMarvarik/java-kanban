import TaskAppManagers.InMemoryTaskManager;
import TaskAppManagers.Managers;
import TaskAppClasses.*;
//Здравствуйте, Семен!
// 1) Не могу сказать, что выбор модификаторов доступа везде был осознанным :D Но сейчас вроде бы везде поставила правильные
// 2) Я считала, что надо указать все типы, как и у обычной реализации листа, чтобы при создании указывать принимаемый тип в <>,
// но теперь, поняла что это не нужно, если методы в классе принимают только определенный тип.
// Вопрос, а как сделать вложенный класс? Я подумала, что то, что я сделала и было вложенным.
//3) Я добавляю сначала первый элемент, а затем все последующие, так как я рекурсивно обращаюсь к linkNext,
// то есть "первый" элемент в цикле это linkNext у head.
//4) Я попробовала прописать сразу с хвостом, но что-то запуталась пока что, я на каникулах сама построила stack
// без использования библиотеки, но с linkedlist намучилась, долго соображала с тренажером, а потом здесь.
// Исправила ошибки с удалением, через тестирование нашла множество недочетов :)
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
        Subtask subtask2 = new Subtask("9", "9", Status.NEW, id7);
        int id9 = inMemoryTaskManager.saveSubtask(subtask2);
        Subtask subtask3 = new Subtask("10", "10", Status.NEW, id7);
        int id10 = inMemoryTaskManager.saveSubtask(subtask3);
        inMemoryTaskManager.getTaskByID(id1);
        inMemoryTaskManager.getTaskByID(id2);
        inMemoryTaskManager.getTaskByID(id3);
        inMemoryTaskManager.getTaskByID(id4);
        inMemoryTaskManager.getTaskByID(id5);
        inMemoryTaskManager.getTaskByID(id6);
        inMemoryTaskManager.getEpicByID(id7);
        inMemoryTaskManager.getSubtaskByID(id8);
        System.out.println("Вызываем историю. Она должна быть 1 2 3 4 5 6 7 8");
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println("Повторно вызываем таск 1, 6");
        inMemoryTaskManager.getTaskByID(id1);
        inMemoryTaskManager.getTaskByID(id6);
        System.out.println("Вызываем историю. Она должна быть 2 3 4 5 7 8 1 6");
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println("Удаляем задачу 3");
        inMemoryTaskManager.removeTaskByID(id3);
        System.out.println("Вызываем историю. Она должна быть 2 4 5 7 8 1 6");
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println("Удаляем сабтаск 8");
        inMemoryTaskManager.removeSubtaskByID(id8);
        System.out.println("Вызываем историю. Она должна быть 2 4 5 7 (без сабтаска 8 (301)) 1 6");
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println("Удаляем эпик 7");
        inMemoryTaskManager.removeEpicByID(id7);
        System.out.println("Вызываем историю. Она должна быть 2 4 5 1 6");
        System.out.println(inMemoryTaskManager.getHistory());
    }
}