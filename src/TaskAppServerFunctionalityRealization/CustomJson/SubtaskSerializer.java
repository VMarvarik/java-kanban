package TaskAppServerFunctionalityRealization.CustomJson;

import TaskAppClasses.Subtask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

public class SubtaskSerializer implements JsonSerializer<Subtask> {
    @Override
    public JsonElement serialize(Subtask subtask, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();

        result.addProperty("Type", "SUBTASK");
        result.addProperty("Id", subtask.getId());
        result.addProperty("Name", subtask.getName());
        result.addProperty("Description", subtask.getDescription());

        switch (subtask.getStatus()) {
            case NEW -> result.addProperty("Status", "NEW");
            case IN_PROGRESS -> result.addProperty("Status", "IN_PROGRESS");
            case DONE -> result.addProperty("Status", "DONE");
        }
        result.addProperty("EpicId", subtask.getEpicId());
        result.addProperty("Duration", subtask.getDuration());
        result.addProperty("StartTime", subtask.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        return result;
    }
}