// Здравствуйте, Семен!
// Нашла ошибки через тестирование - постралась все исправить.
// Override это переопределение метода, а сама фраза пишется для компилятора, я подумала,
// что мне надо заново определять поле ID для наследников и вызывать сеттеры и геттеры. Я перепрочла конспект и вспомнила,
// что эти методы наследуются, как и переменная, которую я могу вызвать через super.
// Получается, я зачем-то создавала переменную идентичную родительской и переписывала методы
// (А я стала активно пользоваться alt + insert, быть может, если я писала это от руки, то бы задумалась xD).
// Спасибо, что не рассказали, в чем проблема, теперь я точно понимаю принцип наследования.

// Возник вопрос, если мы обновим эпик и его статус будет new, нужно ли менять статусы подзадач,
// которые он унаследует от прошлой версии эпика? Или он не будет их наследовать,
// а прошлые подзадачи нужно удалять при обновлении эпика?
// Надеюсь, что проблемы, о которых вы писали в начале, у вас благополучно разрешились!

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task1 = new Task("Помыть полы", "Полы в гостиной и ванной", Status.NEW);
        int id1 = manager.saveTask(task1);
        Task task2 = new Task("Постирать одежду", "Майки", Status.NEW);
        int id2 = manager.saveTask(task2);
        Epic epic1 = new Epic("Покрасить стену", "Стена в спальней", Status.NEW);
        int idE1 = manager.saveEpic(epic1);
        Subtask subtask1 = new Subtask("Купить краску", "Цвет синий", Status.NEW);
        subtask1.setEpicId(idE1);
        manager.saveSubtask(subtask1);
        Subtask subtask2 = new Subtask("Купить валик", "30 см", Status.NEW);
        subtask2.setEpicId(idE1);
        manager.saveSubtask(subtask2);
        Epic epic2 = new Epic("Купить продукты", "Для ужина", Status.NEW);
        int idE2 = manager.saveEpic(epic2);
        Subtask subtask3 = new Subtask("Выбрать магазин", "Рядом с домом", Status.NEW);
        subtask3.setEpicId(idE2);
        int subtaskId = manager.saveSubtask(subtask3);

        System.out.println("Изменяем статус задачи 101 на DONE");
        Task newTask = new Task("Помыть полы", "Полы в гостиной и ванной", Status.DONE);
        newTask.setId(id1);
        manager.updateTask(newTask);

        System.out.println("Изменяем статус подзадачи 303 на DONE");
        Subtask newSubtask = new Subtask("Выбрать магазин", "Рядом с домом", Status.DONE);
        newSubtask.setEpicId(idE2);
        newSubtask.setId(subtaskId);
        manager.updateSubtask(newSubtask);
        
        manager.removeTaskByID(id2);
        manager.removeEpicByID(idE1);

        manager.getTaskByID(id1);
        manager.getEpicByID(idE1);
        manager.getSubtaskByID(subtaskId);
        System.out.println("Меняем описание эпика 2 на 'для обеда'");
        Epic newEpic = new Epic("Купить продукты", "Для обеда", Status.NEW);
        newEpic.setId(idE2);
        manager.updateEpic(newEpic);
        manager.getEpicSubtasks(idE2);
        manager.removeSubtaskByID(subtaskId);
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();
    }
}