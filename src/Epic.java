import java.util.ArrayList;

public class Epic extends Task {
 public ArrayList<Subtask> idOfSubtasks;

  Epic(String name, String description){
    super(name, description, Status.NEW);
    idOfSubtasks = new ArrayList<>();
  }


    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                '}';
    }
}
