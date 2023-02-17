package TaskAppServerFunctionalityRealization.CustomJson;

import TaskAppClasses.Epic;
import com.google.gson.*;
import java.lang.reflect.Type;

public class EpicSerializer implements JsonSerializer<Epic> {
    @Override
    public JsonElement serialize(Epic epic, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();

        result.addProperty("Type", "EPIC");
        result.addProperty("Id", epic.getId());
        result.addProperty("Name", epic.getName());
        result.addProperty("Description", epic.getDescription());
        switch (epic.getStatus()) {
            case NEW -> result.addProperty("Status", "NEW");
            case IN_PROGRESS -> result.addProperty("Status", "IN_PROGRESS");
            case DONE -> result.addProperty("Status", "DONE");
        }
        result.addProperty("Duration", epic.getDuration());
        return result;
    }
}