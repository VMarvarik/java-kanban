package TaskAppManagers;
import TaskAppClasses.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class FileBackedTasksManager extends InMemoryTaskManager {
    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager = Managers.getFileBacked("src/Backup.csv");
        System.out.println("Создаем задачи:");
        Task taskOne = new Task("01", "01", Status.NEW, 10, null);
        int taskIdOne = fileBackedTasksManager.saveTask(taskOne);
        Task taskTwo = new Task("02", "02", Status.IN_PROGRESS, 20, "2000-10-10 19:00");
        int taskIdTwo = fileBackedTasksManager.saveTask(taskTwo);

        Epic epicOne = new Epic("1", "1", Status.NEW);
        int epicIdOne = fileBackedTasksManager.saveEpic(epicOne);
        Subtask subtaskOne = new Subtask("111", "111", Status.IN_PROGRESS, epicIdOne, 60, "2023-02-07 10:00");
        int subtaskIdOne = fileBackedTasksManager.saveSubtask(subtaskOne);
        Subtask subtask3 = new Subtask("333", "333", Status.IN_PROGRESS, epicIdOne, 60, "2023-02-08 11:00" );
        fileBackedTasksManager.saveSubtask(subtask3);

        Epic epicTwo = new Epic("2", "2", Status.NEW);
        int epicIdTwo = fileBackedTasksManager.saveEpic(epicTwo);
        Subtask subtaskTwo = new Subtask("222", "222", Status.NEW, epicIdTwo, 100, "2000-10-10 20:00");
        int subtaskIdTwo = fileBackedTasksManager.saveSubtask(subtaskTwo);
        System.out.println("Выводим задачи в консоль:");
        System.out.println(fileBackedTasksManager.getTaskByID(taskIdOne));
        System.out.println(fileBackedTasksManager.getTaskByID(taskIdTwo));
        System.out.println("Выводим эпики в консоль:");
        System.out.println(fileBackedTasksManager.getEpicByID(epicIdOne));
        System.out.println(fileBackedTasksManager.getEpicByID(epicIdTwo));
        System.out.println("Выводим подзадачи в консоль:");
        System.out.println(fileBackedTasksManager.getSubtaskByID(subtaskIdOne));
        System.out.println(fileBackedTasksManager.getSubtaskByID(subtaskIdTwo));
        System.out.println("Выводим задачи по приоритету:");
        System.out.println(fileBackedTasksManager.getPrioritizedTasks());
        System.out.println("Загружаем новый менеджер из файла.");
        FileBackedTasksManager newFileBackedManager = loadFromFile(fileBackedTasksManager.getFile());
        System.out.println("Выводим задачи по приоритету:");
        System.out.println(newFileBackedManager.getPrioritizedTasks());
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
            fileWriter.write("id,type,name,status,description,duration minutes,start time,end time,epic");
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
        String toString = String.join(",",
                Integer.toString(task.getId()),
                task.getType().toString(),
                task.getName(),
                task.getStatus().toString(),
                task.getDescription(),
                Long.toString(task.getDuration()));
        if (task.getStartTime() != null) {
            toString += "," + task.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            toString += "," + task.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } else {
            toString += "," + null + "," + null;
        }
        if (task.getType().equals(Type.SUBTASK)) {
            Subtask subtask = (Subtask) task;
            toString += "," + subtask.getEpicId();
        }
        return toString;
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
        String timeFromString = null;
        if (!split[6].equals("null")) {
            timeFromString = split[6];
        }
        if (split[1].equals("EPIC")) {
            Epic epic = new Epic(split[2], split[4], Status.valueOf(split[3]));
            epic.setId(Integer.parseInt(split[0]));
            return epic;
        } else if (split[1].equals("SUBTASK")) {
            Subtask subtask = new Subtask(split[2], split[4], Status.valueOf(split[3]), Integer.parseInt(split[8]), Long.parseLong(split[5]), timeFromString);
            subtask.setId(Integer.parseInt(split[0]));
            return subtask;
        }
        Task task = new Task(split[2],split[4],Status.valueOf(split[3]), Long.parseLong(split[5]), timeFromString);
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
            String[] lines = stringFile.split("\n");
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
        if (getTaskHashMap().isEmpty()) {
            return "Список задач пуст, удалять нечего.";
        } else {
            getTaskHashMap().clear();
            save();
        }
        return "Все задачи удалены.";
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
            super.saveTask(task);
            save();
            return task.getId();
        }
        getTaskHashMap().put(task.getId(), task);
        addToPrioritized(task);
        save();
        return task.getId();
    }
    @Override
    public Task updateTask(Task newTask) {
        if (getTaskHashMap().containsKey(newTask.getId())) {
            Task oldTask = getTaskHashMap().get(newTask.getId());
            if (!(newTask.getStartTime() == oldTask.getStartTime() && newTask.getEndTime() == oldTask.getEndTime())) {
                timeValidation(newTask);
            }
            removeFromPrioritizedTree(oldTask);
            getTaskHashMap().put(newTask.getId(), newTask);
            System.out.println("Задача №" + newTask.getId() + " обновлена.");
            System.out.println("Новый список задач - " + getAllTasks());
            save();
            return newTask;
        } else {
            System.out.println("Нельзя обновить задачу №" + newTask.getId() + ", так как ее нет.");
        }
        return null;
    }

    @Override
    public String removeTaskByID(int id) {
        if (getTaskHashMap().get(id) != null){
            getTaskHashMap().remove(id);
            getInMemoryHistoryManager().remove(id);
            renewPrioritizedTree();
            save();
            return "Задача №" + id + " удалена.";
        }
        return "Нельзя удалить задачу №" + id + ", так как ее нет.";
    }

    @Override
    public String deleteAllEpics() {
        if (getEpicHashMap().isEmpty()) {
            return "Список эпиков пуст, нечего удалять.";
        } else {
            getEpicHashMap().clear();
            getSubtaskHashMap().clear();
            renewPrioritizedTree();
            save();
        }
        return "Все эпики удалены.";
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
            super.saveEpic(epic);
            save();
            return epic.getId();
        }
        getEpicHashMap().put(epic.getId(), epic);
        save();
        return epic.getId();
    }

    @Override
    public Epic updateEpic(Epic newEpic) {
        if (getEpicHashMap().containsKey(newEpic.getId())) {
            ArrayList<Subtask> newEpicSubtaskList = getEpicHashMap().get(newEpic.getId()).getSubtaskList();
            newEpic.getSubtaskList().addAll(newEpicSubtaskList);
            getEpicHashMap().put(newEpic.getId(), newEpic);
            System.out.println("Эпик №" + newEpic.getId() + " обновлен.");
            System.out.println("Новый список эпиков - " + getAllEpics());
            save();
            return newEpic;
        } else {
            System.out.println("Нельзя обновить эпик №" + newEpic.getId() + ", так как его нет.");
        }
        return null;
    }

    @Override
    public String removeEpicByID(int epicId) {
        if (getEpicHashMap().get(epicId) != null) {
            for (int i = 0; i < getEpicHashMap().get(epicId).getSubtaskList().size(); i++) {
                getSubtaskHashMap().remove(getEpicHashMap().get(epicId).getSubtaskList().get(i).getId());
            }
            getInMemoryHistoryManager().remove(epicId);
            getEpicHashMap().remove(epicId);
            renewPrioritizedTree();
            save();
            return "Эпик №" + epicId + " удален.";

        }
        return "Нельзя удалить эпик №" + epicId + ", так как его нет.";
    }

    @Override
    public String deleteAllSubtasks() {
        if (!getSubtaskHashMap().isEmpty()) {
            for (Subtask subtask : getSubtaskHashMap().values()) {
                int epicID = subtask.getEpicId();
                getEpicHashMap().get(epicID).getSubtaskList().clear();
                getEpicHashMap().get(epicID).timeCheck();
            }
            getSubtaskHashMap().clear();
            renewPrioritizedTree();
            save();
            return "Все подзадачи удалены.";
        }
        return "Список подзадач пуст, нечего удалять.";
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
            super.saveSubtask(subtask);
            save();
            return subtask.getId();
        }
        getSubtaskHashMap().put(subtask.getId(), subtask);
        getEpicHashMap().get(subtask.getEpicId()).fillSubtaskList(subtask);
        addToPrioritized(subtask);
        epicStatusCheck();
        save();
        return subtask.getId();
    }

    @Override
    public Subtask updateSubtask(Subtask newSubtask) {
        if (getSubtaskHashMap().containsKey(newSubtask.getId())) {
            Subtask oldSubtask = getSubtaskHashMap().get(newSubtask.getId());
            if (!(newSubtask.getStartTime() == oldSubtask.getStartTime() && newSubtask.getEndTime() == oldSubtask.getEndTime())) {
                timeValidation(newSubtask);
            }
            removeFromPrioritizedTree(oldSubtask);
            getSubtaskHashMap().put(newSubtask.getId(), newSubtask);
            if (getEpicHashMap().containsKey(newSubtask.getEpicId())) {
                for (int i = 0; i < getEpicHashMap().get(newSubtask.getEpicId()).getSubtaskList().size(); i++) {
                    if (Objects.equals(getEpicHashMap().get(newSubtask.getEpicId()).getSubtaskList().get(i).getId(),
                            newSubtask.getId())) {
                        getEpicHashMap().get(newSubtask.getEpicId()).getSubtaskList().remove(i);
                        break;
                    }
                }
                getEpicHashMap().get(newSubtask.getEpicId()).fillSubtaskList(newSubtask);
            }
            epicStatusCheck();
            System.out.println("Подзадача №" + newSubtask.getId() + " обновлена.");
            System.out.println("Новый список подзадач - ");
            getAllSubtasks();
            save();
            return newSubtask;
        } else {
            System.out.println("Нельзя обновить подзадачу №" + newSubtask.getId() + ", так как ее нет.");
        }
        return null;
    }

    @Override
    public String removeSubtaskByID(int id) {
        getInMemoryHistoryManager().remove(id);
        if (getSubtaskHashMap().containsKey(id)) {
            int epicID = getSubtaskHashMap().get(id).getEpicId();
            for (int i = 0; i < getEpicHashMap().get(epicID).getSubtaskList().size(); i++) {
                if (getSubtaskHashMap().get(id) == getEpicHashMap().get(epicID).getSubtaskList().get(i)) {
                    getEpicHashMap().get(epicID).getSubtaskList().remove(i);
                    getEpicHashMap().get(epicID).timeCheck();
                    break;
                }
            }
            getSubtaskHashMap().remove(id);
            renewPrioritizedTree();
            return "Подзадача №" + id + " удалена.";
        }
        return "Нельзя удалить подзадачу №" + id + ", так как ее нет.";
    }
}