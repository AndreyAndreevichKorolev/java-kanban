package TaskKindObjects;

import java.util.ArrayList;
import Managers.Status;

public class Epic extends Task {
 public ArrayList<Subtask> idOfSubtasks;

  public Epic(String name, String description){
    super(name, description, Status.NEW);
    idOfSubtasks = new ArrayList<>();
  }


    @Override
    public String toString() {
        return "TaskKindObjects.Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                '}';
    }
}
