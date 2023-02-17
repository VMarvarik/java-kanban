package TaskAppClasses;

import TaskAppEnums.Status;
import TaskAppEnums.Type;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String name, String description, Status status, int epicId, long duration, String startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
        super.type = Type.SUBTASK;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        String toString = "SubtaskId:" + super.getId() + ", название:" + name + ", описание:"
                + description + ", статус:" + status +
                ", длительность:" + duration + ", дата и время старта:";
        if (startTime == null) {
            toString += "отсутствует,";
        } else {
            toString += startTime.format(formatter) + ",";
        }
         toString += " дата и время окончания:";
        if (endTime == null) {
            toString += "отсутствует.";
        } else {
            toString += endTime.format(formatter) + ".";
        }
        return toString;
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

}