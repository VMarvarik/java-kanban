//Здравствуйте, Семен! Чувствую, что решение получилось тяжелым, готова ко всем разгромным комментариям :)

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task1 = new Task("Помыть полы", "Полы в гостиной и ванной", "NEW");
        int id1 = manager.saveTask(task1);
        Task task2 = new Task("Постирать одежду", "Майки", "NEW");
        int id2 = manager.saveTask(task2);
        Epic epic1 = new Epic("Покрасить стену", "Стена в спальней", "NEW");
        int idE1 = manager.saveEpic(epic1);
        Subtask subtask1 = new Subtask("Купить краску", "Цвет синий", "NEW");
        subtask1.setEpicId(idE1);
        manager.saveSubtask(subtask1);
        Subtask subtask2 = new Subtask("Купить валик", "30 см", "NEW");
        subtask2.setEpicId(idE1);
        manager.saveSubtask(subtask2);
        Epic epic2 = new Epic("Купить продукты", "Для ужина", "NEW");
        int idE2 = manager.saveEpic(epic2);
        Subtask subtask3 = new Subtask("Выбрать магазин", "Рядом с домом", "NEW");
        subtask3.setEpicId(idE2);
        int substackID = manager.saveSubtask(subtask3);

        System.out.println("Все задачи: \n" + manager.getAllTasks());
        System.out.println("Все эпики: \n" + manager.getAllEpics());
        System.out.println("Все подзадачи: \n" + manager.getAllSubtasks());

        Task newTask = new Task("Помыть полы", "Полы в гостиной и ванной", "DONE");
        newTask.setID(id1);
        manager.updateTask(newTask);
        System.out.println("Все задачи: \n" + manager.getAllTasks());

        Subtask newSubtask = new Subtask("Выбрать магазин", "Рядом с домом", "DONE");
        newSubtask.setEpicId(idE2);
        newSubtask.setID(substackID);
        manager.updateSubtask(newSubtask);
        System.out.println("Все эпики: \n" + manager.getAllEpics());
        System.out.println("Все подзадачи: \n" + manager.getAllSubtasks());

        manager.removeTaskByID(id2);
        manager.removeEpicByID(idE1);
        System.out.println("Все задачи: \n" + manager.getAllTasks());
        System.out.println("Все эпики: \n" + manager.getAllEpics());
        System.out.println("Все подзадачи: \n" + manager.getAllSubtasks());
    }
}