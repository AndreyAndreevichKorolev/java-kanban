package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    private int tracker = 1;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;
    protected final HistoryManager history;
    private TreeSet<Task> sortedTasksByPriority = new TreeSet<>((task1, task2) -> {
        if (task1.getStartTime().isBefore(task2.getStartTime())) {
            return -1;
        } else if (task1.getStartTime().equals(task2.getStartTime())) {
            return 0;
        } else {
            return 1;
        }
    });

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        history = Managers.getDefaultHistory();

    }

    public static boolean isCross(Task task1, Task task2) {

        LocalDateTime startTime1 = task1.getStartTime();
        LocalDateTime startTime2 = task2.getStartTime();
        LocalDateTime endTime1 = task1.getEndTime();
        LocalDateTime endTime2 = task2.getEndTime();
        boolean isCrossed = false;
        if (task1.getId() != task2.getId()) {
            if (startTime1.equals(startTime2)) {
                isCrossed = true;
            } else if (startTime1.isBefore(startTime2)) {
                if (!startTime2.isAfter(endTime1)) {
                    isCrossed = true;
                }
            } else if (startTime1.isAfter(startTime2)) {
                if (!startTime1.isAfter(endTime2)) {
                    isCrossed = true;
                }
            } else {
                isCrossed = false;
            }

        } else {
            isCrossed = false;
        }
        return isCrossed;
    }

    public List<Task> getPrioritizedTasks() {
        return sortedTasksByPriority.stream().toList();
    }

    public void putTaskToTree(Task task) {
        if (task.getStartTime() != null) {
            sortedTasksByPriority.add(task);
        }

    }

    public void deleteTaskFromTree(Task task) {
        if (task.getStartTime() != null) {
            sortedTasksByPriority.remove(task);
        }
    }


    @Override
    public boolean createNewTask(Task task) {
        if (task.getStartTime() != null) {
            List<Task> crossedTasks = getPrioritizedTasks().stream().filter(taskFromTree -> InMemoryTaskManager.isCross(task, taskFromTree)).collect(Collectors.toList());
            if (!crossedTasks.isEmpty()) {
                System.out.println("Извините, но данная задача пересекается по времени с другими задачами, что недопустимо!");
                return false;
            }

        }
        task.setId(tracker);
        tracker++;
        tasks.put(task.getId(), task);
        // добавляем задачу в дерево
        putTaskToTree(task);
        return true;

    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasksOfManager = new ArrayList<>();
        for (Task task : tasks.values()) {
            allTasksOfManager.add(task);
        }
        return allTasksOfManager;
    }

    @Override
    public void deleteAllTasks() {
        for (Integer id : tasks.keySet()) {
            deleteTaskFromTree(tasks.get(id));
            //удаляем все задачи из дерева
            history.remove(id);
        }
        tasks.clear();
    }

    @Override
    public Task receiveTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            history.add(task);
        }
        return task;
    }

    @Override
    public int updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            System.out.println("Задача с заданным id не обнаружена, но Вы можете создать новую задачу.");
            return 0;
        } else {
            if (task.getStartTime() != null) {
                List<Task> crossedTasks = getPrioritizedTasks().stream().filter(taskFromTree -> InMemoryTaskManager.isCross(task, taskFromTree)).collect(Collectors.toList());
                if (!crossedTasks.isEmpty()) {
                    System.out.println("Извините, но данная задача пересекается по времени с другими задачами, что недопустимо!");
                    return -1;
                }
            }
            Task oldTask = tasks.get(task.getId());
            deleteTaskFromTree(oldTask);
            tasks.put(task.getId(), task);
            putTaskToTree(task); // добавили в дерево новую версию задачи, удалив старую
            return 1;

        }
    }

    @Override
    public boolean deleteTask(int id) {
        if (tasks.get(id) == null) {
            System.out.println("Задача для удаления не найдена: не существует задачи с указанным id:" + id);
            return false;

        } else {
            history.remove(id);
            deleteTaskFromTree(tasks.get(id));
            // удалили задачу из дерева
            tasks.remove(id);
            return true;
        }
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> allEpicsOfManager = new ArrayList<>();
        for (Epic epic : epics.values()) {
            allEpicsOfManager.add(epic);
        }
        return allEpicsOfManager;
    }

    @Override
    public void deleteAllEpics() {
        for (Integer id : epics.keySet()) {
            history.remove(id);
        }
        for (Integer id : subtasks.keySet()) {
            history.remove(id);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Epic receiveEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            history.add(epic);
        }

        return epic;
    }

    @Override
    public void createNewEpic(Epic epic) {

        epic.setId(tracker);
        tracker++;
        epics.put(epic.getId(), epic);

    }

    private void calculateStatusForEpic(Epic epic) {
        if (epic.getSubtasksOfSpecificEpic().isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            int sumOfNew = 0;
            int sumOfInProgress = 0;
            int sumOfDone = 0;
            for (Subtask subtask : getSubtasksOfEpic(epic)) {
                if (Status.NEW == subtask.getStatus()) {
                    sumOfNew++;
                } else if (Status.IN_PROGRESS == subtask.getStatus()) {
                    sumOfInProgress++;
                } else if (Status.DONE == subtask.getStatus()) {
                    sumOfDone++;
                }
            }

            if (sumOfInProgress > 0) {
                epic.setStatus(Status.IN_PROGRESS);
            } else if ((sumOfNew > 0) && (sumOfDone == 0)) {
                epic.setStatus(Status.NEW);
            } else if ((sumOfDone > 0) && (sumOfNew == 0)) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    @Override
    public boolean updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            System.out.println("Эпик с данным id не обнаружен, но вы можете создать новый эпик.");
            return false;
        } else {
            Epic oldEpic = epics.get(epic.getId());
            epic.setSubtasksOfSpecificEpic(oldEpic.getSubtasksOfSpecificEpic());
            epic.findStartTime(getSubtasksOfEpic(epic));
            epic.findDuration(getSubtasksOfEpic(epic));
            epic.findEndTime(getSubtasksOfEpic(epic));
            epics.put(epic.getId(), epic);
            return true;
        }
    }

    @Override
    public boolean deleteEpic(int id) {
        if (epics.get(id) == null) {
            System.out.println("Эпик для удаления не найден: не существует эпика с указанным id:" + id);
            return false;
        } else {
            Epic epic = epics.get(id);
            ArrayList<Integer> subtasksOfEpic = epic.getSubtasksOfSpecificEpic();
            for (Integer idOfSubtask : subtasksOfEpic) {
                history.remove(idOfSubtask);
                subtasks.remove(idOfSubtask);
            }
            history.remove(id);
            epics.remove(id);
            return true;
        }
    }

    @Override
    public boolean createNewSubtask(Subtask subtask) {
        if (subtask.getStartTime() != null) {
            List<Task> crossedTasks = getPrioritizedTasks().stream().filter(taskFromTree -> InMemoryTaskManager.isCross(subtask, taskFromTree)).collect(Collectors.toList());
            if (!crossedTasks.isEmpty()) {
                System.out.println("Извините, но данная задача пересекается по времени с другими задачами, что недопустимо!");
                return false;
            }
        }
        Epic epic = epics.get(subtask.getEpicOfSubtask());
        ArrayList<Integer> subtasksOfEpic = epic.getSubtasksOfSpecificEpic();

        subtask.setId(tracker);
        tracker++;
        subtasks.put(subtask.getId(), subtask);
        subtasksOfEpic.add(subtask.getId());
        epic.findStartTime(getSubtasksOfEpic(epic));
        epic.findDuration(getSubtasksOfEpic(epic));
        epic.findEndTime(getSubtasksOfEpic(epic));
        putTaskToTree(subtask); // добавляем новую подзадачу в дерево
        calculateStatusForEpic(epic);
        return true;

    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> allSubtasksOfManager = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            allSubtasksOfManager.add(subtask);
        }
        return allSubtasksOfManager;
    }


    @Override
    public void deleteAllSubtasks() {
        for (Integer id : subtasks.keySet()) {
            deleteTaskFromTree(subtasks.get(id));
            // удаляем из дерева все удаленные подзадачи
            history.remove(id);
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasksOfSpecificEpic().clear();
            epic.setStatus(Status.NEW);
        }
    }


    @Override
    public Subtask receiveSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            history.add(subtask);
        }

        return subtask;
    }

    @Override
    public int updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            System.out.println("Подзадача с заданным id не найдена, но вы можете создать новую подзадачу!");
            return 0;
        } else {
            if (subtask.getStartTime() != null) {
                List<Task> crossedTasks = getPrioritizedTasks().stream().filter(taskFromTree -> InMemoryTaskManager.isCross(subtask, taskFromTree)).collect(Collectors.toList());
                if (!crossedTasks.isEmpty()) {
                    System.out.println("Извините, но данная задача пересекается по времени с другими задачами, что недопустимо!");
                    return -1;
                }
            }
            deleteTaskFromTree(subtasks.get(subtask.getId()));
            subtasks.put(subtask.getId(), subtask);
            putTaskToTree(subtask); //удалили старую версию подзадачи из дерева и добавили новую
            Epic epic = epics.get(subtask.getEpicOfSubtask());

            epic.findStartTime(getSubtasksOfEpic(epic));
            epic.findDuration(getSubtasksOfEpic(epic));
            epic.findEndTime(getSubtasksOfEpic(epic));
            calculateStatusForEpic(epic);
            return 1;

        }
    }

    @Override
    public boolean deleteSubtask(int id) {
        if (!subtasks.containsKey(id)) {
            System.out.println("Подзадача для удаления не найдена: не существует подзадачи с указанным id.");
            return false;
        } else {
            history.remove(id);
            Subtask subtask = subtasks.get(id);
            Epic epic = epics.get(subtask.getEpicOfSubtask());
            ArrayList<Integer> subtasksOfEpic = epic.getSubtasksOfSpecificEpic();
            Integer idOfSubtask = subtask.getId();
            subtasksOfEpic.remove(idOfSubtask);
            epic.findDuration(getSubtasksOfEpic(epic));
            epic.findStartTime(getSubtasksOfEpic(epic));
            epic.findEndTime(getSubtasksOfEpic(epic));
            subtasks.remove(id);
            deleteTaskFromTree(subtask);
            // удалили подзадачу из дерева
            calculateStatusForEpic(epic);
            return true;
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        ArrayList<Integer> idOfSubtasksOfEpic = epic.getSubtasksOfSpecificEpic();
        ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
        for (Integer id : idOfSubtasksOfEpic) {
            subtasksOfEpic.add(subtasks.get(id));
        }

        return subtasksOfEpic;

    }

    @Override
    public ArrayList<Task> getHistory() {
        return history.getHistory();
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public void setTasks(HashMap<Integer, Task> tasks) {
        this.tasks = tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public void setEpics(HashMap<Integer, Epic> epics) {
        this.epics = epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(HashMap<Integer, Subtask> subtasks) {
        this.subtasks = subtasks;
    }
}
