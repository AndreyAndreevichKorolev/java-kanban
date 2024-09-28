package tasks;

import managers.Status;

import java.time.LocalDateTime;

public class Subtask extends Task {

    private Epic epicOfSubtask;

    public Subtask(String name, String description, Epic epicOfSubtask) {
        super(name, description, Status.NEW);
        this.epicOfSubtask = epicOfSubtask;
        setType(Types.SUBTASK);
    }


    public Epic getEpicOfSubtask() {
        return epicOfSubtask;
    }

    @Override
    public String toString() {
        return "TaskKindObjects.Subtask{" + "name='" + name + '\'' + ", description='" + description + '\'' + ", id=" + getId() + ", status=" + getStatus() + '}';
    }
}
