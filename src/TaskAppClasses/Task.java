package TaskAppClasses;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task implements Comparable<Task> {

    private int id;

    protected String name;

    protected String description;

    protected Status status;

    protected Type type;

    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    protected long duration;

    protected LocalDateTime startTime;

    protected LocalDateTime endTime;

    public Task(String name, String description, Status status, long duration, String startTime) {

        this.name = name;

        this.description = description;

        this.status = status;

        this.type = Type.TASK;

        this.id = 0;

        this.duration = duration;

        if (startTime == null || startTime.isBlank() || startTime.isEmpty()) {
            this.startTime = null;
        } else {
            this.startTime = LocalDateTime.parse(startTime, formatter);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            endTime = startTime.plusMinutes(duration);
            return endTime;
        }
        return null;
    }

    @Override
    public String toString() {
        String toString = "TaskId:" + id + ", название:<" + name + ">, описание:<" + description + ">, статус:<" + status
                + ">, длительность:<" + duration + ">, дата и время старта:<";
        if (startTime == null) {
            return toString + "отсутствует>.";
        }
        return toString + startTime.format(formatter) + ">.";
    }

    public int compareTo(Task task){
        if (this.startTime == null) {
            return 1;
        } else if (task.getStartTime() == null) {
            return -1;
        } else if (this.startTime == null && task.getStartTime() == null) {
            return 1;
        } else if (this.startTime.isAfter(task.getStartTime())) {
            return 1;
        }
        return -1;
    }
}