package TaskAppClasses;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TreeSet;

public class Epic extends Task {
    private final ArrayList<Subtask> subtaskList;

    public Epic(String name, String description, Status status) {
        super(name, description, status, 0, null);
        super.type = Type.EPIC;
        subtaskList = new ArrayList<>();
        this.endTime = null;
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
        this.duration = 0;
        TreeSet<Subtask> prioritizedTree = new TreeSet<>(subtaskList);
        LocalDateTime latestTime;
        if (!prioritizedTree.isEmpty() && prioritizedTree.first().getStartTime() != null) {
            this.startTime = prioritizedTree.first().getStartTime();
            latestTime = prioritizedTree.first().getEndTime();
            for (Subtask subtask : prioritizedTree) {
                if (subtask.getEndTime() != null && subtask.getEndTime().isAfter(latestTime)) {
                    latestTime = subtask.getEndTime();
                }
                this.duration += subtask.getDuration();
            }
            this.endTime = latestTime;
        } else {
            this.startTime = null;
            this.endTime = null;
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
            result += "отсутствует>,";
        } else {
            result += startTime.format(formatter);
        }
        result += ">, дата и время окончания:<";
        if (endTime == null) {
            result += "отсутствует>,";
        } else {
            result += endTime.format(formatter) + ">,";
        }
        result += " подзадачи:";
        if (subtaskList.isEmpty()) {
            result += "<список подзадач пока пуст>";
        } else {
            result += "<" + subtaskList;
        }
        return result + ">.";
    }
}