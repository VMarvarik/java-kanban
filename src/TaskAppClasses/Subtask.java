package TaskAppClasses;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubtaskId:" + super.getId() + ", название:<" + name + ">, описание:<"
                + description + ">, статус:<" + status + ">.";
    }
}