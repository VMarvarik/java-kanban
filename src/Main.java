//Добрый день, Семен! Изменила вывод, вроде бы красиво, но есть одна плавающая [...
// спасибо за ссылку, теперь окончательно разобралась!

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task1 = new Task("Помыть полы", "Полы в гостиной и ванной", Status.NEW);
        int id1 = manager.saveTask(task1);
        Task task2 = new Task("Постирать одежду", "Майки", Status.NEW);
        int id2 = manager.saveTask(task2);
        System.out.println("Вызов списка задач.");
        System.out.println("Новый список задач - " + manager.getAllTasks());
        Epic epic1 = new Epic("Покрасить стену", "Стена в спальней", Status.NEW);
        int idE1 = manager.saveEpic(epic1);
        System.out.println("Вызов списка эпиков.");
        System.out.println("Новый список эпиков - " + manager.getAllEpics());
        Subtask subtask1 = new Subtask("Купить краску", "Цвет синий", Status.NEW);
        subtask1.setEpicId(idE1);
        manager.saveSubtask(subtask1);
        Subtask subtask2 = new Subtask("Купить валик", "30 см", Status.NEW);
        subtask2.setEpicId(idE1);
        manager.saveSubtask(subtask2);
        System.out.println("Вызов списка подзадач.");
        System.out.println("Новый список подзадач - ");
        manager.getAllSubtasks();
        System.out.println("Новый список эпиков - " + manager.getAllEpics());
        Epic epic2 = new Epic("Купить продукты", "Для ужина", Status.NEW);
        int idE2 = manager.saveEpic(epic2);
        System.out.println("Вызов списка эпиков.");
        System.out.println("Новый список эпиков - " + manager.getAllEpics());
        Subtask subtask3 = new Subtask("Выбрать магазин", "Рядом с домом", Status.NEW);
        subtask3.setEpicId(idE2);
        int subtaskId = manager.saveSubtask(subtask3);
        System.out.println("Вызов списка подзадач.");
        System.out.println("Новый список подзадач - ");
        manager.getAllSubtasks();
        System.out.println("Новый список эпиков - " + manager.getAllEpics());

        System.out.println("Изменяем статус задачи 101 на DONE.");
        Task newTask = new Task("Помыть полы", "Полы в гостиной и ванной", Status.DONE);
        newTask.setId(id1);
        manager.updateTask(newTask);

        System.out.println("Изменяем статус подзадачи 303 на DONE.");
        Subtask newSubtask = new Subtask("Выбрать магазин", "Рядом с домом", Status.DONE);
        newSubtask.setEpicId(idE2);
        newSubtask.setId(subtaskId);
        manager.updateSubtask(newSubtask);
        System.out.println("Новый список эпиков - " + manager.getAllEpics());

        System.out.println("Убираем задачу 102.");
        manager.removeTaskByID(id2);
        System.out.println("Убираем эпик 201.");
        manager.removeEpicByID(idE1);

        System.out.println("Вызываем задачу 101 по ID:");
        manager.getTaskByID(id1);
        System.out.println("Вызываем эпик 201 по ID:");
        manager.getEpicByID(idE1);
        System.out.println("Вызываем подзадачу 303 по ID:");
        manager.getSubtaskByID(subtaskId);
        System.out.println("Меняем описание эпика 2 на 'для обеда':");
        Epic newEpic = new Epic("Купить продукты", "Для обеда", Status.NEW);
        newEpic.setId(idE2);
        manager.updateEpic(newEpic);
        System.out.println("Получаем подзадачи эпика 202:");
        manager.getEpicSubtasks(idE2);
        System.out.println("Удаляем подзадачу 303 по ID:");
        manager.removeSubtaskByID(subtaskId);
        System.out.println("Удаляем все задачи:");
        manager.deleteAllTasks();
        System.out.println("Удаляем все эпики:");
        manager.deleteAllEpics();
        System.out.println("Удаляем все подзадачи:");
        manager.deleteAllSubtasks();
        System.out.println("Пробуем вызвать список задач: ");
        manager.getAllTasks();
        System.out.println("Пробуем вызвать список эпиков: ");
        manager.getAllEpics();
        System.out.println("Пробуем вызвать список подзадач: ");
        manager.getAllSubtasks();
    }
}