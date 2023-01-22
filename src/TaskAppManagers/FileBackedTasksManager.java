package TaskAppManagers;
import TaskAppClasses.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
public class FileBackedTasksManager extends InMemoryTaskManager {

    public static void main(String[] args) {
        final String PATH = "src/Backup.csv";
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(PATH);
        Task task1 = new Task("1", "1", Status.NEW);
        int id1 = fileBackedTasksManager.saveTask(task1);
        Task task2 = new Task("2", "2", Status.NEW);
        int id2 = fileBackedTasksManager.saveTask(task2);
        Task task3 = new Task("3", "3", Status.NEW);
        int id3 = fileBackedTasksManager.saveTask(task3);
        Epic epic1 = new Epic("7", "7", Status.NEW);
        int id7 = fileBackedTasksManager.saveEpic(epic1);
        Subtask subtask1 = new Subtask("8", "8", Status.NEW, id7);
        int id8 = fileBackedTasksManager.saveSubtask(subtask1);
        Subtask subtask2 = new Subtask("9", "9", Status.NEW, id7);
        int id9 = fileBackedTasksManager.saveSubtask(subtask2);
        Subtask subtask3 = new Subtask("10", "10", Status.NEW, id7);
        int id10 = fileBackedTasksManager.saveSubtask(subtask3);
        System.out.println("Вызываем таски 101, 102, 103, эпик 201, подзадачу 301.");
        fileBackedTasksManager.getTaskByID(id1);
        fileBackedTasksManager.getTaskByID(id2);
        fileBackedTasksManager.getTaskByID(id3);
        fileBackedTasksManager.getEpicByID(id7);
        fileBackedTasksManager.getSubtaskByID(id8);
        System.out.println("Файл CSV должен выглядить так\n" +
                "id,type,name,status,description,epic\n" +
                "101,TASK,1,NEW,1\n" +
                "102,TASK,2,NEW,2\n" +
                "103,TASK,3,NEW,3\n" +
                "201,EPIC,7,NEW,7\n" +
                "301,SUBTASK,8,NEW,8,201\n" +
                "302,SUBTASK,9,NEW,9,201\n" +
                "\n" +
                "101,102,103,201,301,");
        System.out.println("Создаем новый файл менеджер на основе файла из предыдущего файл менеджера.");
        FileBackedTasksManager newFileBackedTasksManager = loadFromFile(fileBackedTasksManager.getFile());
        System.out.println("Вызываем все таски, должны быть 101 102 103 ");
        System.out.println(newFileBackedTasksManager.getAllTasks());
        System.out.println("Вызываем все эпики, должен быть 201 с подзадачами 301 302 303");
        System.out.println(newFileBackedTasksManager.getAllEpics());
        System.out.println("Вызываем все сабтаски, должны быть 301 302 303");
        newFileBackedTasksManager.getAllSubtasks();
        System.out.println("Вызываем историю, она должна быть 101 102 103 201 301");
        System.out.println(newFileBackedTasksManager.getHistory());
    }

    private final File file;

    public FileBackedTasksManager(String filepath) {
        super();
        this.file = new File(filepath);
    }

    public File getFile() {
        return file;
    }

    public void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,epic");
            fileWriter.write("\n");
            for (Task task : getTaskHashMap().values()) {
                fileWriter.write(toString(task) + "\n");
            }
            for (Epic epic : getEpicHashMap().values()) {
                fileWriter.write(toString(epic) + "\n");
            }
            for (Subtask subtask : getSubtaskHashMap().values()) {
                fileWriter.write(toString(subtask) + "\n");
            }
            fileWriter.write("\n");
            fileWriter.write(historyToString(getInMemoryHistoryManager()));
        } catch (IOException exception) {
            throw new ManagerSaveException("Во время записи файла произошла ошибка.", exception);
        }
    }

    public String toString(Task task) {
        return String.join(",",
                Integer.toString(task.getId()),
        task.getType().toString(),
        task.getName(),
        task.getStatus().toString(),
        task.getDescription());
    }

    public static String historyToString(InMemoryHistoryManager manager) {
        if (manager.getHistory() != null) {
            StringBuilder historyString = new StringBuilder();
            for (Task task : manager.getHistory()) {
                historyString.append(task.getId());
                historyString.append(",");
            }
            return historyString.toString();
        }
        return "";
    }

    public static Task fromString(String value) {
        String[] split = value.split(",");
        if (split[1].equals("EPIC")) {
            Epic epic = new Epic(split[2],split[4],Status.valueOf(split[3]));
            epic.setId(Integer.parseInt(split[0]));
            return epic;
        } else if (split[1].equals("SUBTASK")) {
            Subtask subtask = new Subtask(split[2],split[4],Status.valueOf(split[3]),Integer.parseInt(split[5]));
            subtask.setId(Integer.parseInt(split[0]));
            return subtask;
        }
        Task task = new Task(split[2],split[4],Status.valueOf(split[3]));
        task.setId(Integer.parseInt(split[0]));
        return task;
    }

    public static ArrayList<Integer> historyFromString(String value) {
        String[] split = value.split(",");
        ArrayList<Integer> historyList = new ArrayList<>();
        for (String s : split) {
            historyList.add(Integer.valueOf(s));
        }
        return historyList;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fromFile = new FileBackedTasksManager(file.getPath());
        try  {
            String stringFile = Files.readString(Path.of(fromFile.getFile().getPath()));
            String[] lines = stringFile.split(System.lineSeparator());
            for (int i = 1; i < lines.length; i++) {
                if (!lines[i].isBlank() && i != lines.length - 1) {
                    switch (fromString(lines[i]).getType()) {
                        case TASK -> fromFile.saveTask(fromString(lines[i]));
                        case EPIC -> fromFile.saveEpic((Epic)fromString(lines[i]));
                        case SUBTASK -> fromFile.saveSubtask((Subtask)fromString(lines[i]));
                    }
                }
                if (i == lines.length - 1) {
                    ArrayList<Integer> history = historyFromString(lines[i]);
                    for (int taskId : history) {
                       if (fromFile.getTaskHashMap().containsKey(taskId)) {
                           fromFile.getTaskByID(taskId);
                       } else if (fromFile.getEpicHashMap().containsKey(taskId)) {
                           fromFile.getEpicByID(taskId);
                       } else if (fromFile.getSubtaskHashMap().containsKey(taskId)) {
                           fromFile.getSubtaskByID(taskId);
                       }
                    }
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Во время восстановления файла произошла ошибка.", exception);
        }
        return fromFile;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        try (BufferedWriter bf = Files.newBufferedWriter(Path.of("src/Backup.csv"),
                StandardOpenOption.TRUNCATE_EXISTING)) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Task getTaskByID(int id) {
        getInMemoryHistoryManager().add(getTaskHashMap().get(id));
        Task task = getTaskHashMap().get(id);
        save();
        return task;
    }

    @Override
    public int saveTask(Task task) {
        if (task.getId() == 0) {
            int id = super.saveTask(task);
            save();
            return id;
        }
        getTaskHashMap().put(task.getId(), task);
        save();
        return task.getId();
    }
    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void removeTaskByID(int id) {
        super.removeTaskByID(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public Task getEpicByID(int id) {
        if (getEpicHashMap().get(id) != null) {
            Task epic = getEpicHashMap().get(id);
            getInMemoryHistoryManager().add(getEpicHashMap().get(id));
            save();
            return epic;
        }
        return null;
    }

    @Override
    public int saveEpic(Epic epic) {
        if (epic.getId() == 0) {
            int id = super.saveEpic(epic);
            save();
            return id;
        }
        getEpicHashMap().put(epic.getId(), epic);
        save();
        return epic.getId();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void removeEpicByID(int epicId) {
        super.removeEpicByID(epicId);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
    }

    @Override
    public Task getSubtaskByID(int id) {
        if (getSubtaskHashMap().get(id) != null) {
            Task subtask = getSubtaskHashMap().get(id);
            getInMemoryHistoryManager().add(getSubtaskHashMap().get(id));
            save();
            return subtask;
        }
        return null;
    }

    @Override
    public int saveSubtask(Subtask subtask) {
        if (subtask.getId() == 0) {
            int id = super.saveSubtask(subtask);
            save();
            return id;
        }
        getSubtaskHashMap().put(subtask.getId(), subtask);
        getEpicHashMap().get(subtask.getEpicId()).fillSubtaskIList(subtask, getEpicHashMap().get(subtask.getEpicId()));
        epicStatusCheck();
        save();
        return subtask.getId();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        super.updateSubtask(newSubtask);
        save();
    }

    @Override
    public void removeSubtaskByID(int id) {
        super.removeSubtaskByID(id);
        save();
    }
}