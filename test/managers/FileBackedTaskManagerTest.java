package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

class FileBackedTaskManagerTest {
    File pathToFile;
    FileBackedTaskManager manager;
    Task task;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    Subtask subtask4;
    Epic epic;

    @BeforeEach
    public void makeTestReady() throws IOException {
        pathToFile = new File("Test.CSV");

        pathToFile.createNewFile();

        manager = new FileBackedTaskManager(pathToFile);
        task = new Task("Задача!", "Задача для тестирования", Status.NEW);
        task.setDuration(Duration.ofMinutes(10));
        task.setStartTime(LocalDateTime.of(2000, 10, 17, 12, 0));
        epic = new Epic("Эпик!", "Единственный эпик");
        subtask1 = new Subtask("подзадача 1", "подзадача эпика первая", epic);
        subtask1.setStartTime(LocalDateTime.of(2000, 10, 17, 13, 0));
        subtask1.setDuration(Duration.ofMinutes(15));
        subtask2 = new Subtask("подзадача 2", "подзадача эпика вторая", epic);
        subtask2.setStartTime(LocalDateTime.of(2000, 10, 17, 14, 0));
        subtask2.setDuration(Duration.ofMinutes(20));
        subtask3 = new Subtask("подзадача 3", "подзадача эпика третья", epic);
        manager.createNewTask(task);
        manager.createNewEpic(epic);
        manager.createNewSubtask(subtask1);
        manager.createNewSubtask(subtask2);
        manager.createNewSubtask(subtask3);
    }

    @Test
    public void shouldSaveTasksCorrectlyAndThenReadThemCorrectly() {
        manager.save();
        FileBackedTaskManager manager2 = FileBackedTaskManager.loadFromFile(pathToFile);
        Assertions.assertEquals(task, manager2.receiveTask(task.getId()), "У двух менеджеров разные задачи");
        Assertions.assertEquals(subtask1, manager2.receiveSubtask(subtask1.getId()), "у двух менеджеров разные подзадачи");
        Assertions.assertEquals(subtask2, manager2.receiveSubtask(subtask2.getId()), "у двух менеджеров разные подзадачи");
        Assertions.assertEquals(subtask3, manager2.receiveSubtask(subtask3.getId()), "у двух менеджеров разные подзадачи");
        Assertions.assertEquals(epic, manager2.receiveEpic(epic.getId()), "у двух менеджеров разные эпики");
        Assertions.assertEquals(epic.getSubtasksOfSpecificEpic(), manager2.receiveEpic(epic.getId()).getSubtasksOfSpecificEpic(), "списки эпиков разные!");
        Assertions.assertEquals(subtask1.getEpicOfSubtask(), manager2.receiveSubtask(subtask1.getId()).getEpicOfSubtask(), " у одних и тех же подзадач разные эпики");
    }

}