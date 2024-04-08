package Managers;

import TaskKindObjects.Epic;
import TaskKindObjects.Subtask;
import TaskKindObjects.Task;

import java.util.ArrayList;

public interface TaskManager {
    void createNewTask(Task task);

    ArrayList<Task> getAllTasks();

    void deleteAllTasks();

    Task receiveTask(int id);

    void updateTask(Task task);

    void deleteTask(int id);

    ArrayList<Epic> getAllEpics();

    void deleteAllEpics();

    Epic receiveEpic(int id);

    void createNewEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpic(int id);

    void createNewSubtask(Subtask subtask);

    ArrayList<Subtask> getAllSubtasks();

    void deleteAllSubtasks();

    Subtask receiveSubtask(int id);

    void updateSubtask(Subtask subtask);

    void deleteSubtask(int id);

    ArrayList<Subtask> getSubtasksOfEpic(Epic epic);

    ArrayList<Task> getHistory();
}
