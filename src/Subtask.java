public class Subtask extends Task {
    private Integer epicId;
    private  Integer ID;
    public Subtask(String name, String description, String status) {
        super(name, description, status);
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "EpicID:" + epicId + ", ID:" + ID + ", название:<" + name + ">, описание:<"
                + description + ">, статус:<" + status + ">.";
    }
}