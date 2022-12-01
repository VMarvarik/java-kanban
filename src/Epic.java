import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Subtask> subtaskList;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        subtaskList = new ArrayList<>();
    }

    public void fillSubtaskIList(Subtask subtask, Epic epic) {
        epic.subtaskList.add(subtask);
    }

    @Override
    public String toString() {
        String result;
        result = "\nEpicID:" + super.getId() + ", название:<" + name + ">, описание:<" + description + ">, статус:<" + status
                + ">, подзадачи:" + "\n";
        if (subtaskList.isEmpty()) {
            result += "<список подзадач пока пуст";
        } else {
            result += "<" + subtaskList;
        }
        return result + ">.";
    }
}