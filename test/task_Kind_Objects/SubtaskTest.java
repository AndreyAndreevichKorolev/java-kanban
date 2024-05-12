package task_Kind_Objects;

import managers.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubtaskTest {

    @Test
    public void ShouldBeTheSameSubtaskTwoSubtasksWithTheSameId() {
        Epic epic = new Epic("Эпик", "Эпик для проверки сабтасков");
        Subtask subtask1 = new Subtask("Сабтаск1", "Первый сабтаск", epic);
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.createNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Сабтаск2", "Второй сабтаск", epic);
        subtask2.setId(subtask1.getId());
        Assertions.assertEquals(subtask1.getId(), subtask2.getId(), "id сабтасков не равны!");
    }

}