package tasks;

import managers.Status;

public class Subtask extends Task {

    private Integer epicOfSubtask;

    public Subtask(String name, String description, Integer epicOfSubtask) {
        super(name, description, Status.NEW);
        this.epicOfSubtask = epicOfSubtask;
        setType(Types.SUBTASK);
    }


    public Integer getEpicOfSubtask() {
        return epicOfSubtask;
    }

    @Override
    public String toString() {
        return "TaskKindObjects.Subtask{" + "name='" + name + '\'' + ", description='" + description + '\'' + ", id=" + getId() + ", status=" + getStatus() + '}';
    }
}
