package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.Types;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File pathToFile;

    public FileBackedTaskManager(File pathToFile) {
        this.pathToFile = pathToFile;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(pathToFile)) {
            writer.write("id, type, name, status, description, startTime, duration, endTime, epic");
            for (Task task : getTasks().values()) {
                writer.write("\n" + toString(task));
            }
            for (Epic epic : getEpics().values()) {
                writer.write("\n" + toString(epic) + "," + epic.getEndTime());
            }
            for (Subtask subtask : getSubtasks().values()) {
                writer.write("\n" + toString(subtask) + "," + "" + "," + subtask.getEpicOfSubtask());
            }


        } catch (IOException exp) {
            throw new ManagerSaveException();
        }


    }

    public String toString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.name + "," + task.getStatus() + "," + task.description + "," + task.getStartTime() + "," + task.getDuration().toMinutes();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        List<String> stringOfTasks = null;
        try {
            stringOfTasks = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        if (stringOfTasks.isEmpty()) {
            System.out.println("Файл пустой!");
            return manager;
        } else if (!"id, type, name, status, description, startTime, duration, endTime, epic".equals(stringOfTasks.get(0))) {
            System.out.println("Файл не содержит задач: он содержит иную информацию!");
            return manager;
        }

        for (int i = 1; i < stringOfTasks.size(); i++) {
            Task task = manager.fromString(stringOfTasks.get(i));
            if (Types.TASK.equals(task.getType())) {
                manager.getTasks().put(task.getId(), task);
                manager.putTaskToTree(task); // заполняем также дерево приоритетности
            } else if (Types.EPIC.equals(task.getType())) {
                Epic epic = (Epic) task;
                manager.getEpics().put(epic.getId(), epic);
            } else if (Types.SUBTASK.equals(task.getType())) {
                Subtask subtask = (Subtask) task;
                manager.getSubtasks().put(subtask.getId(), subtask);
                manager.putTaskToTree(task);
            }
        }
        for (Epic epic : manager.getEpics().values()) {   // добавил заполнение списка сабтасков эпика

            ArrayList<Integer> idOfSubtasksOfEpic = new ArrayList<>();
            for (Subtask subtask : manager.getSubtasks().values()) {
                if (epic.getId() == subtask.getEpicOfSubtask()) {
                    idOfSubtasksOfEpic.add(subtask.getId());
                }
            }
            epic.setSubtasksOfSpecificEpic(idOfSubtasksOfEpic);
        }
        return manager;
    }

    public Task fromString(String value) {
        String[] attributes = value.split(",");
        Task taskFromString = null;
        int id = 0;
        String name = null;
        Status status = null;
        String description = null;
        LocalDateTime startTime = null;
        Duration duration = null;

        for (int i = 0; i < attributes.length; i++) {
            if (i == 0) {
                id = Integer.parseInt(attributes[i]);
            } else if (i == 2) {
                name = attributes[2];
            } else if (i == 3) {
                if (attributes[3].equals("NEW")) {
                    status = Status.NEW;
                } else if (attributes[3].equals("IN_PROGRESS")) {
                    status = Status.IN_PROGRESS;
                } else {
                    status = Status.DONE;
                }
            } else if (i == 4) {
                description = attributes[4];
            } else if (i == 5) {
                if (!attributes[5].equals("null")) {
                    startTime = LocalDateTime.parse(attributes[5]);
                } else {
                    startTime = null;
                }

            } else if (i == 6) {
                duration = Duration.ofMinutes(Integer.parseInt(String.valueOf(attributes[6])));
            }
        }
        switch (attributes[1]) {
            case "TASK":
                taskFromString = new Task(name, description, status);
                taskFromString.setId(id);
                taskFromString.setDuration(duration);
                taskFromString.setStartTime(startTime);
                break;
            case "EPIC":
                Epic epicFromString = new Epic(name, description);

                epicFromString.setId(id);
                epicFromString.setStatus(status);
                epicFromString.setDuration(duration);
                epicFromString.setStartTime(startTime);
                LocalDateTime endTime = null;
                if (attributes[7].equals("null")) {
                    epicFromString.setEndTime(null);
                } else {
                    endTime = LocalDateTime.parse(attributes[7]);
                }
                epicFromString.setEndTime(endTime);
                taskFromString = epicFromString;
                break;
            case "SUBTASK":
                Integer epicOfSubtask = getEpics().get(Integer.parseInt(attributes[8])).getId();
                taskFromString = new Subtask(name, description, epicOfSubtask);
                taskFromString.setId(id);
                taskFromString.setStatus(status);
                taskFromString.setStartTime(startTime);
                taskFromString.setDuration(duration);
                break;
        }
        return taskFromString;
    }

    @Override
    public boolean createNewTask(Task task) {
        boolean isCreated = super.createNewTask(task);
        save();
        return isCreated;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public int updateTask(Task task) {
        int statusOfUpdating = super.updateTask(task);
        save();
        return statusOfUpdating;
    }

    @Override
    public boolean deleteTask(int id) {
        boolean isDeleted = super.deleteTask(id);
        save();
        return isDeleted;
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void createNewEpic(Epic epic) {
        super.createNewEpic(epic);
        save();
    }

    @Override
    public boolean updateEpic(Epic epic) {
        boolean isUpdated = super.updateEpic(epic);
        save();
        return isUpdated;
    }

    @Override
    public boolean deleteEpic(int id) {
        boolean isDeleted = super.deleteEpic(id);
        save();
        return isDeleted;
    }

    @Override
    public boolean createNewSubtask(Subtask subtask) {
        boolean isCreated = super.createNewSubtask(subtask);
        save();
        return isCreated;
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public int updateSubtask(Subtask subtask) {
        int statusOfUpdating = super.updateSubtask(subtask);
        save();
        return statusOfUpdating;
    }

    @Override
    public boolean deleteSubtask(int id) {
        boolean isDeleted = super.deleteSubtask(id);
        return isDeleted;
    }
}
