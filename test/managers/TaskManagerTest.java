package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

class TaskManagerTest {

    public InMemoryTaskManager manager;
    public Epic epic;
    public Task task;
    public Subtask subtask1;
    public Subtask subtask2;
    public Subtask subtask3;
    public Subtask subtask4;

    @BeforeEach
    public void makeTestReady() {
        manager = new InMemoryTaskManager();
        epic = new Epic("epic", "epic for test");
        task = new Task("task", "task for test", Status.DONE);
        task.setDuration(Duration.ofMinutes(10));
        task.setStartTime(LocalDateTime.of(2024, 1, 1, 0, 0));
        subtask1 = new Subtask("subtask1", "subtask for test", epic.getId());
        subtask1.setDuration(Duration.ofMinutes(10));
        subtask2 = new Subtask("subtask2", "subtask for test", epic.getId());
        subtask3 = new Subtask("subtask3", "subtask for test", epic.getId());
        subtask3.setDuration(Duration.ofMinutes(10));
        subtask3.setStartTime(LocalDateTime.of(2025, 1, 1, 0, 0));
        subtask4 = new Subtask("subtask4", "subtask for test", epic.getId());
        subtask4.setDuration(Duration.ofMinutes(10));
        subtask4.setStartTime(LocalDateTime.of(2025, 1, 1, 5, 0));
    }

    @Test
    public void TestsForTask() {
        manager.createNewTask(task);
        Assertions.assertEquals(task, manager.receiveTask(task.getId()), "Задача одна и та же - создание успешно");
        manager.deleteTask(task.getId());
        Assertions.assertTrue(manager.getAllTasks().isEmpty(), "задача не удалилась");
        manager.createNewTask(task);
        Task receivedTask = manager.receiveTask(task.getId());
        Assertions.assertEquals(task, receivedTask, "задачу не удалось получить");
        Task updatedTask = new Task("задача", "задача для проверки", Status.NEW);
        updatedTask.setId(task.getId());
        manager.updateTask(updatedTask);
        Assertions.assertNotEquals(task, manager.receiveTask(task.getId()), "задача одна и та же - замена не произошла");
        manager.deleteAllTasks();
        Assertions.assertTrue(manager.getAllTasks().isEmpty(), "Список задач не пустой - все задачи не удалились");

    }

    @Test
    public void TestsForEpics() {
        manager.createNewEpic(epic);
        Assertions.assertEquals(epic, manager.receiveEpic(epic.getId()), "Задача одна и та же - создание успешно");
        manager.deleteEpic(epic.getId());
        Assertions.assertTrue(manager.getAllEpics().isEmpty(), "задача не удалилась");
        manager.createNewEpic(epic);
        Epic receivedEpic = manager.receiveEpic(epic.getId());
        Assertions.assertEquals(epic, receivedEpic, "задачу не удалось получить");
        Epic updatedEpic = new Epic("задача", "задача для проверки");
        updatedEpic.setId(epic.getId());
        manager.updateEpic(updatedEpic);
        Assertions.assertNotEquals(epic, manager.receiveEpic(epic.getId()), "задача одна и та же - замена не произошла");
        manager.deleteAllEpics();
        Assertions.assertTrue(manager.getAllEpics().isEmpty(), "Список задач не пустой - все задачи не удалились");

    }

    @Test
    public void TestsForSubtasks() {
        Epic epic1 = new Epic("epic", "epic for testing");
        InMemoryTaskManager manager1 = new InMemoryTaskManager();
        manager1.createNewEpic(epic1);
        Subtask subtask5 = new Subtask("subtask5", "subtask5", epic1.getId());
        Subtask subtask6 = new Subtask("subtask6", "subtask6", epic1.getId());
        Subtask subtask7 = new Subtask("subtask7", "subtask7", epic1.getId());
        Subtask subtask8 = new Subtask("subtask8", "subtask8", epic1.getId());
        manager1.createNewSubtask(subtask5);
        manager1.createNewSubtask(subtask6);
        manager1.createNewSubtask(subtask7);
        manager1.createNewSubtask(subtask8);
        Assertions.assertEquals(subtask5, manager1.receiveSubtask(subtask5.getId()), "Задача не получена");
        manager1.deleteSubtask(subtask5.getId());
        Assertions.assertEquals(3, manager1.getAllSubtasks().size(), "задача не удалилась");
        manager1.createNewSubtask(subtask5);
        Subtask receivedSubtask = manager1.receiveSubtask(subtask5.getId());
        Assertions.assertEquals(subtask5, receivedSubtask, "задачу не удалось получить");
        Subtask updatedSubtask = new Subtask("задача", "задача для проверки", subtask5.getEpicOfSubtask());
        updatedSubtask.setId(subtask5.getId());
        manager1.updateSubtask(updatedSubtask);
        Assertions.assertNotEquals(subtask5, manager1.receiveSubtask(subtask5.getId()), "задача одна и та же - замена не произошла");
        manager1.deleteAllSubtasks();
        Assertions.assertTrue(manager1.getAllTasks().isEmpty(), "Список задач не пустой - все задачи не удалились");

    }


}