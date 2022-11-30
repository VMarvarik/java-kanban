public class Task {
    private Integer id;
    public String name;
    public String description;
    public Status status;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ID:" + id + ", название:<" + name + ">, описание:<" + description + ">, статус:<" + status + ">.";
    }
}