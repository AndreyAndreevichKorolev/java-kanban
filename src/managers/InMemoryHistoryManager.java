package managers;

import task_Kind_Objects.Task;

import java.util.HashMap;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private TasksLinkedList lastViewedTasks;
    private HashMap<Integer, TasksNode> nodeFinder;

    public InMemoryHistoryManager() {
        lastViewedTasks = new TasksLinkedList();
        nodeFinder = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (!nodeFinder.containsKey(task.getId())) {
            TasksNode node = new TasksNode(task);
            nodeFinder.put(task.getId(), node);
            lastViewedTasks.linkLast(node);
        } else {
            removeNode(nodeFinder.get(task.getId()));
            TasksNode node = new TasksNode(task);
            nodeFinder.put(task.getId(), node);
            lastViewedTasks.linkLast(node);
        }
    }

    public void removeNode(TasksNode node) {
        if (lastViewedTasks.head == node) {
            TasksNode next = node.next;
            next.previous = null;
            lastViewedTasks.head = next;
        } else if (lastViewedTasks.tail == node) {
            TasksNode previous = node.previous;
            previous.next = null;
            lastViewedTasks.tail = previous;
        } else {
            TasksNode previous = node.previous;
            TasksNode next = node.next;
            previous.next = next;
            next.previous = previous;
        }
        lastViewedTasks.reduceSize();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return lastViewedTasks.getTasks();
    }

    @Override
    public void remove(int id) {
        if (nodeFinder.containsKey(id)) {
            TasksNode node = nodeFinder.get(id);
            removeNode(node);
            nodeFinder.remove(id);
        }
    }
}

class TasksLinkedList {
    private int size;
    TasksNode head;
    TasksNode tail;


    TasksLinkedList() {
        size = 0;
        head = null;
        tail = null;
    }

    public void reduceSize() {
        size--;
    }

    public int getSize() {
        return size;
    }

    public void linkLast(TasksNode node) {
        if (head == null) {
            head = node;
            tail = node;
        } else {
            TasksNode oldTail = tail;
            tail = node;
            oldTail.next = tail;
            tail.previous = oldTail;
        }
        size++;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> history = new ArrayList<>();
        TasksNode node = head;
        for (int i = 1; i <= getSize(); i++) {
            Task task = node.data;
            history.add(task);
            TasksNode nextNode = node.next;
            node = nextNode;
        }
        return history;
    }
}
