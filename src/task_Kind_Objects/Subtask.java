package task_Kind_Objects;

import managers.Status;

public class Subtask extends Task {

    private Epic epicOfSubtask;

    public Subtask(String name, String description, Epic epicOfSubtask) {
        super(name, description, Status.NEW);
        this.epicOfSubtask = epicOfSubtask;
    }

    public Epic getEpicOfSubtask() {
        return epicOfSubtask;
    }

    @Override
    public String toString() {
        return "TaskKindObjects.Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                '}';
    }
}
