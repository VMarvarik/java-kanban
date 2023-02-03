package TaskAppManagers;
import TaskAppClasses.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
public class FileBackedTasksManager extends InMemoryTaskManager {

    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("src/Backup.csv");
        Epic epic = new Epic ("01", "01", Status.NEW, 15, "2016-11-09 10:30");
        fileBackedTasksManager.saveEpic(epic);
        fileBackedTasksManager.saveTask(new Task ("01", "01", Status.NEW, 15, "2016-11-09 10:30"));
        FileBackedTasksManager newManager = loadFromFile(fileBackedTasksManager.getFile());
        System.out.println(newManager.getAllEpics());
        System.out.println(newManager.getAllTasks());
        System.out.println(newManager.getHistory());
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
            fileWriter.write("id,type,name,status,description,duration minutes,start time,epic");
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
        task.getDescription(),
        Long.toString(task.getDuration()),
        task.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
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
        return " ";
    }

    public static Task fromString(String value) {
        String[] split = value.split(",");
        if (split[1].equals("EPIC")) {
            Epic epic = new Epic(split[2],split[4], Status.valueOf(split[3]), Long.parseLong(split[5]), split[6]);
            epic.setId(Integer.parseInt(split[0]));
            return epic;
        } else if (split[1].equals("SUBTASK")) {
            Subtask subtask = new Subtask(split[2],split[4],Status.valueOf(split[3]),Integer.parseInt(split[5]), Long.parseLong(split[5]), split[6]);
            subtask.setId(Integer.parseInt(split[0]));
            return subtask;
        }
        Task task = new Task(split[2],split[4],Status.valueOf(split[3]), Long.parseLong(split[5]), split[6]);
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
        try {
            String stringFile = Files.readString(Path.of(file.getPath()));
            String[] lines = stringFile.split(System.lineSeparator());
            for (int i = 1; i < lines.length; i++) {
                if (!lines[i].isBlank() && i != lines.length - 1) {
                    switch (fromString(lines[i]).getType()) {
                        case TASK -> fromFile.saveTask(fromString(lines[i]));
                        case EPIC -> fromFile.saveEpic((Epic)fromString(lines[i]));
                        case SUBTASK -> fromFile.saveSubtask((Subtask)fromString(lines[i]));
                    }
                }
                if (!lines[i].isBlank() && i == lines.length - 1) {
                    ArrayList<Integer> history = historyFromString(lines[i]);
                    for (int taskId : history) {
                      fromFile.getInMemoryHistoryManager().add(fromFile.getTaskByID(taskId));
                      fromFile.getInMemoryHistoryManager().add(fromFile.getEpicByID(taskId));
                      fromFile.getInMemoryHistoryManager().add(fromFile.getSubtaskByID(taskId));
                    }
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Во время восстановления файла произошла ошибка.", exception);
        }
        return fromFile;
    }

    @Override
    public String deleteAllTasks() {
        super.deleteAllTasks();
        save();
        return super.deleteAllTasks();
    }

    @Override
    public Task getTaskByID(int id) {
        if (getTaskHashMap().get(id) != null) {
            Task task = getTaskHashMap().get(id);
            getInMemoryHistoryManager().add(getTaskHashMap().get(id));
            save();
            return task;
        }
        return null;
    }

    @Override
    public int saveTask(Task task) {
        if (task.getId() == 0) {
            int id = super.saveTask(task);
            save();
            return id;
        }
        super.saveTask(task);
        save();
        return task.getId();
    }
    @Override
    public Task updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
        return super.updateTask(newTask);
    }

    @Override
    public String removeTaskByID(int id) {
        super.removeTaskByID(id);
        save();
        return super.removeTaskByID(id);
    }

    @Override
    public String deleteAllEpics() {
        super.deleteAllEpics();
        save();
        return super.deleteAllEpics();
    }

    @Override
    public Epic getEpicByID(int id) {
        if (getEpicHashMap().get(id) != null) {
            Epic epic = getEpicHashMap().get(id);
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
    public Epic updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
        return super.updateEpic(newEpic);
    }

    @Override
    public String removeEpicByID(int epicId) {
        super.removeEpicByID(epicId);
        save();
        return super.removeEpicByID(epicId);
    }

    @Override
    public String deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
        return super.deleteAllSubtasks();
    }

    @Override
    public Subtask getSubtaskByID(int id) {
        if (getSubtaskHashMap().get(id) != null) {
            Subtask subtask = getSubtaskHashMap().get(id);
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
        getEpicHashMap().get(subtask.getEpicId()).fillSubtaskList(subtask);
        epicStatusCheck();
        save();
        return subtask.getId();
    }

    @Override
    public Subtask updateSubtask(Subtask newSubtask) {
        super.updateSubtask(newSubtask);
        save();
        return super.updateSubtask(newSubtask);
    }

    @Override
    public String removeSubtaskByID(int id) {
        super.removeSubtaskByID(id);
        save();
        return super.removeSubtaskByID(id);
    }
}