package managers;

import task_Kind_Objects.Task;

public class TasksNode {
    public TasksNode previous;
    public TasksNode next;
    public Task data;

    TasksNode(Task data) {
        previous = null;
        next = null;
        this.data = data;
    }
}
