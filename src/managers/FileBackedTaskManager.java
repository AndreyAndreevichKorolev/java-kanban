package managers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import tasks.Task;
import tasks.Epic;
import tasks.Subtask;
import tasks.Types;

import java.util.List;

import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File pathToFile;

    public FileBackedTaskManager(File pathToFile) {
        this.pathToFile = pathToFile;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(pathToFile)) {
            writer.write("id,type,name,status,description,epic");
            for (Task task : getTasks().values()) {
                writer.write("\n" + toString(task));
            }
            for (Epic epic : getEpics().values()) {
                writer.write("\n" + toString(epic));
            }
            for (Subtask subtask : getSubtasks().values()) {
                writer.write("\n" + toString(subtask) + "," + subtask.getEpicOfSubtask().getId());
            }


        } catch (IOException exp) {
            throw new ManagerSaveException();
        }


    }

    public String toString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.name + "," + task.getStatus() + "," + task.description;
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
        } else if (!"id,type,name,status,description,epic".equals(stringOfTasks.get(0))) {
            System.out.println("Файл не содержит задач: он содержит иную информацию!");
            return manager;
        }

        for (int i = 1; i < stringOfTasks.size(); i++) {
            Task task = manager.fromString(stringOfTasks.get(i));
            if (Types.TASK.equals(task.getType())) {
                manager.getTasks().put(task.getId(), task);
            } else if (Types.EPIC.equals(task.getType())) {
                Epic epic = (Epic) task;
                manager.getEpics().put(epic.getId(), epic);
            } else if (Types.SUBTASK.equals(task.getType())) {
                Subtask subtask = (Subtask) task;
                manager.getSubtasks().put(subtask.getId(), subtask);
            }
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
            }
        }
        switch (attributes[1]) {
            case "TASK":
                taskFromString = new Task(name, description, status);
                taskFromString.setId(id);
                break;
            case "EPIC":
                taskFromString = new Epic(name, description);
                taskFromString.setId(id);
                taskFromString.setStatus(status);
                break;
            case "SUBTASK":
                Epic epicOfSubtask = getEpics().get(Integer.parseInt(attributes[5]));
                taskFromString = new Subtask(name, description, epicOfSubtask);
                taskFromString.setId(id);
                taskFromString.setStatus(status);
                break;
        }
        return taskFromString;
    }

    @Override
    public void createNewTask(Task task) {
        super.createNewTask(task);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
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
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void createNewSubtask(Subtask subtask) {
        super.createNewSubtask(subtask);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }
}
