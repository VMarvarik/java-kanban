package Tests.TaskAppManagerTesters;

import TaskAppClasses.*;
import TaskAppManagers.FileBackedTasksManager;
import TaskAppManagers.Managers;
import org.junit.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import static org.junit.Assert.*;

//Здравствуй, Семен! Дописала тест на время здесь, а также добавила в начало тест на getPrioritizedTasks в InMemoryTaskManagerTest
//Заменила lineSeparator на "\n", поэтому теперь все должно рабоать корректно 
public class FileBackedTaskManagerTest extends InMemoryTaskManagerTest {

    FileBackedTasksManager manager = Managers.getFileBacked("src/Backup.csv");

    @Override
    public void beforeEach() {
        this.manager = Managers.getFileBacked("src/Backup.csv");
    }

    @Test
    public void saveTasksIfTheirTimeIntersects() {
        Task taskCorrectTime = new Task ("01", "01", Status.NEW, 15, "2016-11-09 10:30");
        manager.saveTask(taskCorrectTime);
        //Создаем и пробуем сохранить задачу, эпик с таким же временем.
        Task taskWrongTime = new Task ("01", "01", Status.NEW, 15, "2016-11-09 10:30");
        assertEquals(0, manager.saveTask(taskWrongTime));
        Epic epicWrongTime = new Epic ("01", "01", Status.NEW, 15, "2016-11-09 10:30");
        assertEquals(0, manager.saveEpic(epicWrongTime));
        //Создаем правильный эпик и пытаемся добавить подзадачу с пересекающимся временем
        Epic epicCorrectTime = new Epic ("01", "01", Status.NEW, 15, "2016-10-09 10:30");
        int epicId = manager.saveEpic(epicCorrectTime);
        Subtask subtaskWrongTime = new Subtask ("01", "01", Status.NEW, epicId, 30, "2016-11-09 10:30");
        assertEquals(0, manager.saveSubtask(subtaskWrongTime));
        //Проверяем, что при вызове всех задач, эпиков и подзадач не выводятся некорректные
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.add(taskCorrectTime);
        ArrayList<Task> allEpics = new ArrayList<>();
        allEpics.add(epicCorrectTime);
        assertEquals(allTasks, manager.getAllTasks());
        assertEquals(allEpics, manager.getAllEpics());
        assertNull(manager.getAllSubtasks());
    }

    @Test
    public void saveAndLoadWhenTaskListIsEmpty() {
        //Сохраняю данные пустого менеджера в файл, затем читаю файл, чтобы убедиться, что в нем только одна строка
        //Загружаю из этого файла новый менеджер и проверяю пустой список задач.
        manager.save();
        try {
            String stringFile = Files.readString(Path.of(manager.getFile().getPath()));
            String[] lines = stringFile.split(System.lineSeparator());
            assertEquals(3, lines.length); //размер 3, потому что в файле две пустые строки
            assertEquals(" ", lines[lines.length - 1]); // проверяем пустую строку истории
            assertEquals("id,type,name,status,description,duration minutes,start time,epic", lines[0]);
        } catch (IOException ignored) {}
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(manager.getFile());
        assertNull(newManager.getAllTasks());
        assertNull(newManager.getAllEpics());
        assertNull(newManager.getAllSubtasks());
    }

    @Test
    public void saveAndLoadWhenEpicSubtaskListIsEmpty() {
        //Создаю эпик без подзадач, он пишется автоматически в файл, проверяю файл на правильный размер и наличие нужной строки
        //Создаю новый менеджер проверяю наличие эпиков и отсутствие сабтасков
        int epicId = manager.saveEpic((Epic) create(Type.EPIC));
        try {
            String stringFile = Files.readString(Path.of(manager.getFile().getPath()));
            String[] lines = stringFile.split(System.lineSeparator());
            assertEquals(4, lines.length);
            assertEquals(" ", lines[lines.length - 1]); // проверяем пустую строку истории
            assertEquals("201,EPIC,01,NEW,01,15,2015-11-09 10:30", lines[1]);
        } catch (IOException ignored) {}
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(manager.getFile());
        assertEquals(manager.getEpicByID(epicId).toString(), newManager.getEpicByID(epicId).toString());
        assertNull(newManager.getAllSubtasks());
    }

    @Test
    public void saveAndLoadWhenHistoryIsEmpty() {
        //Сохраняем в файл менеджер с одной задачей и эпиком без истории, проверяем пустую строку истории, проверяем пустую
        // историю в новом менеджере, потом сравниваем эпик и задачу, проверяем отсутствие подзадач.
        int epicId = manager.saveEpic((Epic) create(Type.EPIC));
        int taskId = manager.saveTask(create(Type.TASK));
        try {
            String stringFile = Files.readString(Path.of(manager.getFile().getPath()));
            String[] lines = stringFile.split(System.lineSeparator());
            assertEquals(5, lines.length);
            assertEquals("101,TASK,01,NEW,01,15,2016-11-09 10:30", lines[1]);
            assertEquals("201,EPIC,01,NEW,01,15,2015-11-09 10:30", lines[2]);
            assertEquals(" ", lines[lines.length - 1]); // проверяем пустую строку истории
        } catch (IOException ignored) {}
        FileBackedTasksManager newManager = FileBackedTasksManager.loadFromFile(manager.getFile());
        assertNull(newManager.getHistory());
        assertEquals(manager.getEpicByID(epicId).toString(), newManager.getEpicByID(epicId).toString());
        assertEquals(manager.getTaskByID(taskId).toString(), newManager.getTaskByID(taskId).toString());
        assertNull(newManager.getAllSubtasks());
    }
}