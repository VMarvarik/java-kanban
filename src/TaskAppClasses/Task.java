package TaskAppClasses;

public class Task {
    private int id;

    protected String name;

    protected String description;

    protected Status status;

    protected Type type;

    public Task(String name, String description, Status status) {

        this.name = name;

        this.description = description;

        this.status = status;

        this.type = Type.TASK;

        this.id = 0;
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

    @Override

    public String toString() {

        return "TaskId:" + id + ", название:<" + name + ">, описание:<" + description + ">, статус:<" + status + ">.";

    }
}