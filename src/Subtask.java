public class Subtask extends Task {
    private Integer epicId;

    public Subtask(String name, String description, Status status) {
        super(name, description, status);
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "EpicID:" + epicId + ", ID:" + super.getId() + ", название:<" + name + ">, описание:<"
                + description + ">, статус:<" + status + ">.";
    }
}