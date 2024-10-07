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
        manager.createNewSubtask(subtask1);
        manager.createNewSubtask(subtask2);
        manager.createNewSubtask(subtask3);
        manager.createNewSubtask(subtask4);
        Assertions.assertEquals(subtask1, manager.receiveSubtask(subtask1.getId()), "Задача не получена");
        manager.deleteSubtask(subtask1.getId());
        Assertions.assertEquals(3, manager.getAllSubtasks().size(), "задача не удалилась");
        manager.createNewSubtask(subtask1);
        Subtask receivedSubtask = manager.receiveSubtask(subtask1.getId());
        Assertions.assertEquals(subtask1, receivedSubtask, "задачу не удалось получить");
        Subtask updatedSubtask = new Subtask("задача", "задача для проверки", subtask1.getEpicOfSubtask());
        updatedSubtask.setId(subtask1.getId());
        manager.updateSubtask(updatedSubtask);
        Assertions.assertNotEquals(subtask1, manager.receiveSubtask(subtask1.getId()), "задача одна и та же - замена не произошла");
        manager.deleteAllSubtasks();
        Assertions.assertTrue(manager.getAllTasks().isEmpty(), "Список задач не пустой - все задачи не удалились");

    }


}