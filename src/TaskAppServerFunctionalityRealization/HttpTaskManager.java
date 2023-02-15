package TaskAppServerFunctionalityRealization;

import TaskAppClasses.Epic;
import TaskAppClasses.Subtask;
import TaskAppClasses.Task;
import TaskAppEnums.Status;
import TaskAppManagers.FileBackedTasksManager;
import TaskAppManagers.Managers;
import TaskAppServerFunctionalityRealization.CustomJson.HttpTaskManagerSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.format.DateTimeFormatter;
import static TaskAppEnums.Type.*;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvTaskClient;

    private String key;

    Gson gson;


    public HttpTaskManager(String urlServer) {
        super();
        kvTaskClient = new KVTaskClient(urlServer);
        gson = new GsonBuilder().setPrettyPrinting().
                registerTypeAdapter(HttpTaskManager.class, new HttpTaskManagerSerializer()).create();
    }

    @Override
    public void save() {
        kvTaskClient.put(key, gson.toJson(this));
    }

    public HttpTaskManager loadFromServer() {
        HttpTaskManager newHttpTaskManager = Managers.getDefault();
        String response = kvTaskClient.load(key);
        if (response != null) {
            String[] split = response.split("@");
            newHttpTaskManager.setKey(split[0]);
            newHttpTaskManager.setTaskId(Integer.parseInt(split[1]));
            newHttpTaskManager.setEpicId(Integer.parseInt(split[2]));
            newHttpTaskManager.setSubtaskId(Integer.parseInt(split[3]));
            if (!split[4].equals("null")) {
                saveTaskFromString(split[4], newHttpTaskManager);
            }
            if (!split[5].equals("null")) {
                saveTaskFromString(split[5], newHttpTaskManager);
            }
            if (!split[6].equals("null")) {
                saveTaskFromString(split[6], newHttpTaskManager);
            }
            if (!split[7].equals("null")) {
                saveHistoryFromString(split[7], newHttpTaskManager);
            }
        }
        return newHttpTaskManager;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString(Task task) {
        String toString = String.join("/",
                Integer.toString(task.getId()),
                task.getType().toString(),
                task.getName(),
                task.getStatus().toString(),
                task.getDescription(),
                Long.toString(task.getDuration()));
        if (task.getStartTime() != null) {
            toString += "/" + task.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            toString += "/" + task.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } else {
            toString += "/" + null + "/" + null;
        }
        if (task.getType().equals(SUBTASK)) {
            Subtask subtask = (Subtask) task;
            toString += "/" + subtask.getEpicId();
        }
        return toString;
    }

    public static Task fromString(String value) {
        String[] split = value.split("/");
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

    public void saveTaskFromString(String stringToSplit, HttpTaskManager manager) {
        String[] tasksSplit = stringToSplit.split(",");
        for (int i = 0; i < tasksSplit.length; i++) {
            String substring = new String();
            if (tasksSplit.length == 1) {
                substring = tasksSplit[i].substring(2, tasksSplit[i].length() - 2);
            } else if (i == 0) {
                substring = tasksSplit[i].substring(2, tasksSplit[i].length() - 1);
            } else if (i == tasksSplit.length - 1) {
                substring = tasksSplit[i].substring(1, tasksSplit[i].length() - 2);
            }  else {
                substring = tasksSplit[i].substring(1, tasksSplit[i].length() - 1);
            }
            switch (fromString(substring).getType()) {
                case TASK -> manager.saveTask(fromString(substring));
                case EPIC -> manager.saveEpic((Epic)fromString(substring));
                case SUBTASK -> manager.saveSubtask((Subtask)fromString(substring));
            }
        }
    }

    public void saveHistoryFromString(String stringToSplit, HttpTaskManager manager) {
        String[] historySplit = stringToSplit.split(",");
        for (String s : historySplit) {
            if (s != null) {
                manager.getInMemoryHistoryManager().add(manager.getTaskByID(Integer.parseInt(s)));
                manager.getInMemoryHistoryManager().add(manager.getEpicByID(Integer.parseInt(s)));
                manager.getInMemoryHistoryManager().add(manager.getSubtaskByID(Integer.parseInt(s)));
            }
        }
    }
}