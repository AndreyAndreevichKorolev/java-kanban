package tasks;

import managers.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubtaskTest {

    @Test
    public void ShouldBeTheSameSubtaskTwoSubtasksWithTheSameId() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик", "Эпик для проверки сабтасков");
        taskManager.createNewEpic(epic);
        Subtask subtask1 = new Subtask("Сабтаск1", "Первый сабтаск", epic.getId());
        taskManager.createNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Сабтаск2", "Второй сабтаск", epic.getId());
        subtask2.setId(subtask1.getId());
        Assertions.assertEquals(subtask1.getId(), subtask2.getId(), "id сабтасков не равны!");
    }

}