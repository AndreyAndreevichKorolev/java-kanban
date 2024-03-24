import java.util.Objects;

public class Task {
    public String name;
    public String description;
    private int id;
     private Status status;

    Task(String name, String description, Status status){
        this.name = name;
        this.description = description;
        this.status = status;


    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus(){
        return status;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
}
