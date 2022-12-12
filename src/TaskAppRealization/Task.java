package TaskAppRealization;

public class Task {

    private int id;

    public String name;

    public String description;

    public Status status;

    public Task(String name, String description, Status status) {

        this.name = name;

        this.description = description;

        this.status = status;

    }

    public int getId() {

        return id;

    }

    public void setId(int id) {

        this.id = id;

    }

    @Override

    public String toString() {

        return "TaskId:" + id + ", название:<" + name + ">, описание:<" + description + ">, статус:<" + status + ">.";

    }
}