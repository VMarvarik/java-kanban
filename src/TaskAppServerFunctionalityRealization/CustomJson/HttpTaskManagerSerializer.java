package TaskAppServerFunctionalityRealization.CustomJson;

import TaskAppClasses.Epic;
import TaskAppClasses.Subtask;
import TaskAppClasses.Task;
import TaskAppServerFunctionalityRealization.HttpTaskManager;
import com.google.gson.*;
import java.lang.reflect.Type;

public class HttpTaskManagerSerializer implements JsonSerializer<HttpTaskManager> {
    @Override
    public JsonElement serialize(HttpTaskManager manager, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        JsonArray tasks = new JsonArray();
        result.addProperty("User key", manager.getKey());
        result.addProperty("TaskId", manager.getTaskId());
        result.addProperty("EpicId", manager.getEpicId());
        result.addProperty("SubtaskId", manager.getSubtaskId());

        if (!manager.getTaskHashMap().isEmpty()) {
            for (Task task: manager.getTaskHashMap().values()) {
                tasks.add(manager.toString(task));
            }
            result.addProperty("Tasks", tasks.toString());
        } else {
            result.addProperty("Tasks", "null");
        }

        JsonArray epics = new JsonArray();
        if (!manager.getEpicHashMap().isEmpty()) {
            for (Epic epic : manager.getEpicHashMap().values()) {
                epics.add(manager.toString(epic));
            }
            result.addProperty("Epics", epics.toString());
        } else {
            result.addProperty("Epics", "null");
        }

        JsonArray subtasks = new JsonArray();
        if (!manager.getSubtaskHashMap().isEmpty()) {
            for (Subtask subtask : manager.getSubtaskHashMap().values()) {
                subtasks.add(manager.toString(subtask));
            }
            result.addProperty("Subtasks", subtasks.toString());
        } else {
            result.addProperty("Subtasks", "null");
        }

        if (manager.getHistory() != null) {
            StringBuilder historyString = new StringBuilder();
            for (Task task : manager.getHistory()) {
                historyString.append(task.getId());
                historyString.append(",");
            }
            result.addProperty("History", historyString.toString());
        } else {
            result.addProperty("History", "null");
        }
        return result;
    }
}