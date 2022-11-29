public class Task {
    private Integer ID;
    public String name;
    public String description;
    public String status;

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "ID:" + ID + ", название:<" + name + ">, описание:<" + description + ">, статус:<" + status + ">.";
    }
}