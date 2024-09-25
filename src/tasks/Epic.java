package tasks;

import java.util.ArrayList;

import managers.Status;

public class Epic extends Task {
    private ArrayList<Subtask> subtasksOfSpecificEpic;

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        subtasksOfSpecificEpic = new ArrayList<>();
        setType(Types.EPIC);
    }

    public ArrayList<Subtask> getSubtasksOfSpecificEpic() {
        return subtasksOfSpecificEpic;
    }

    public void setSubtasksOfSpecificEpic(ArrayList<Subtask> subtasksOfSpecificEpic) {
        this.subtasksOfSpecificEpic = subtasksOfSpecificEpic;
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
