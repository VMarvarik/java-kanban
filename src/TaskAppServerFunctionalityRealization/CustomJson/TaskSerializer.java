package TaskAppServerFunctionalityRealization.CustomJson;

import TaskAppClasses.Task;
import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

public class TaskSerializer implements JsonSerializer<Task> {

    @Override
    public JsonElement serialize(Task task, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();

        result.addProperty("Type", "TASK");
        result.addProperty("Id", task.getId());
        result.addProperty("Name", task.getName());
        result.addProperty("Description", task.getDescription());
        switch (task.getStatus()) {
            case NEW -> result.addProperty("Status", "NEW");
            case IN_PROGRESS -> result.addProperty("Status", "IN_PROGRESS");
            case DONE -> result.addProperty("Status", "DONE");
        }
        result.addProperty("Duration", task.getDuration());
        result.addProperty("StartTime", task.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        return result;
    }
}