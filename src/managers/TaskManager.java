package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public interface TaskManager {
    boolean createNewTask(Task task);

    ArrayList<Task> getAllTasks();

    void deleteAllTasks();

    Task receiveTask(int id);

    int updateTask(Task task);

    boolean deleteTask(int id);

    ArrayList<Epic> getAllEpics();

    void deleteAllEpics();

    Epic receiveEpic(int id);

    void createNewEpic(Epic epic);

    boolean updateEpic(Epic epic);

    boolean deleteEpic(int id);

    boolean createNewSubtask(Subtask subtask);

    ArrayList<Subtask> getAllSubtasks();

    void deleteAllSubtasks();

    Subtask receiveSubtask(int id);

    int updateSubtask(Subtask subtask);

    boolean deleteSubtask(int id);

    ArrayList<Subtask> getSubtasksOfEpic(Epic epic);

    ArrayList<Task> getHistory();
}
