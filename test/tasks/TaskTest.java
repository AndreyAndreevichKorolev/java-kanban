package tasks;

import managers.InMemoryTaskManager;
import managers.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    public void ShouldBeTheSameTaskTwoTasksWithTheSameId() {
        Task task1 = new Task("Задача1", "Задача первая", Status.NEW);
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.createNewTask(task1);
        Task task2 = new Task("Задача1.1", "Задача вторая", Status.IN_PROGRESS);
        task2.setId(task1.getId());
        Assertions.assertEquals(task1.getId(), task2.getId(), "id задач разные!");
    }

}