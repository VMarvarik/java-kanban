import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Subtask> subtaskList;
    private Integer ID;
    public Epic(String name, String description, String status) {
        super(name, description, status);
        subtaskList = new ArrayList<>();
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void fillSubtaskIList(Subtask subtask, Epic epic) {
        epic.subtaskList.add(subtask);
    }

    @Override
    public String toString() {
        return "EpicID:" + ID + ", название:<" + name + ">, описание:<" + description + ">, статус:<" + status
                + ">, подзадачи:<" + subtaskList + ">.";
    }
}