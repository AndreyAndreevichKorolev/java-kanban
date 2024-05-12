package managers;

import taskKindObjects.Epic;
import taskKindObjects.Subtask;
import taskKindObjects.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void shouldFindTaskById(){
        Task task = new Task("Таск1", "Первая задача", Status.NEW);
        Epic epic = new Epic("Эпик1", "Вторая задача");
        Subtask subtask = new Subtask("Сабтаск", "Третья задача", epic);
        taskManager.createNewTask(task);
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask);
        Assertions.assertEquals(task,taskManager.receiveTask(task.getId()), "менеджер не нашел задачу по id");
        Assertions.assertEquals(epic, taskManager.receiveEpic(epic.getId()), "менеджер не нашел задачу по id");
        Assertions.assertEquals(subtask, taskManager.receiveSubtask(subtask.getId()), "менеджер не нашел задачу по id");
    }

    @Test
    public void twoTasksOneWithHandCreatedIdAndOneWithManagerCreatedIdShouldntConfront(){
        Task handTask = new Task("Задача вручную", "Задача с id, сгенерированным вручную", Status.NEW);
        Task managerTask = new Task("Задача автоматом", "Задача с id, сгенерированным менеджером", Status.NEW);
        taskManager.createNewTask(managerTask);
        handTask.setId(managerTask.getId());
        taskManager.createNewTask(handTask);
        Assertions.assertEquals(2, taskManager.tasks.size(), "В списоке нет 2-х задач!");
    }

    @Test
    public void taskMustntChangeAfterAddingToManager(){
        Task task1 = new Task("Задaча", "Задача", Status.NEW);
        Task task2 = new Task("Задaча", "Задача", Status.NEW);
        taskManager.createNewTask(task1);
        task2.setId(task1.getId());
        Assertions.assertEquals(task2, task1, "Задача изменилась после добавления в менеджер!");
    }

}