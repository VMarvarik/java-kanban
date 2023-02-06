package TaskAppClasses;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Subtask> subtaskList;

    public Epic(String name, String description, Status status, long duration, String startTime) {
        super(name, description, status, duration, startTime);
        super.type = Type.EPIC;
        subtaskList = new ArrayList<>();
        this.endTime = super.getEndTime();
    }

    public void fillSubtaskList(Subtask subtask) {
        this.subtaskList.add(subtask);
        statusCheck();
        timeCheck();
    }

    public ArrayList<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void timeCheck() {
        for (Subtask subtask : this.subtaskList) {
            LocalDateTime subtaskStartTime = subtask.getStartTime();
            LocalDateTime subtaskEndTime = subtask.getEndTime();
            int duration = 0;
            duration += subtask.getDuration();
            this.duration = duration;
            if (subtaskStartTime != null && subtaskStartTime.isBefore(this.startTime)) {
                this.startTime = subtaskStartTime;
            }
            if (subtaskEndTime != null && subtaskEndTime.isAfter(this.endTime)) {
                this.endTime = subtaskEndTime;
            } else if (subtaskEndTime != null) {
                this.endTime = super.getEndTime();
            }
        }
    }

    public void statusCheck() {
        Status statusOfSubtask;
        int statusNew = 0;
        int statusDone = 0;
        int amountOfSubtasks = 0;
        for (int i = 0; i < this.subtaskList.size(); i++) {
            statusOfSubtask = subtaskList.get(i).status;
            amountOfSubtasks = this.subtaskList.size();
            switch (statusOfSubtask) {
                case NEW -> statusNew++;
                case DONE -> statusDone++;
            }
        }
        if (amountOfSubtasks == statusNew) {
            this.status = Status.NEW;
        } else if (amountOfSubtasks == statusDone) {
            this.status = Status.DONE;
        } else {
            this.status = Status.IN_PROGRESS;
        }
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        String result;
        result = "EpicID:" + super.getId() + ", название:<" + name + ">, описание:<" + description + ">, статус:<" + status
                + ">, длительность:<" + duration + ">, дата и время старта:<";

        if (startTime == null) {
            result += "отсутствует>." + ">, подзадачи:" + "\n";
        } else {
            result += startTime.format(formatter) + ">, подзадачи:" + "\n";
        }

        if (subtaskList.isEmpty()) {
            result += "<список подзадач пока пуст";
        } else {
            result += "<" + subtaskList;
        }
        return result + ">.";
    }
}