package TaskKindObjects;

import Managers.InMemoryTaskManager;
import Managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    public void ShouldBeTheSameEpicTwoEpicsWithTheSameId(){
        Epic epic1 = new Epic("Эпик","Эпик для проверки ");
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.createNewEpic(epic1);
        Epic epic2 = new Epic("Эпик2","Эпик для проверки 2");
        epic2.setId(epic1.getId());
        Assertions.assertEquals(epic1.getId(), epic2.getId(), "id эпик не равны!");
    }

    @Test
    public void ShouldNotHaveDeletedSubtask(){
        Epic epic1 = new Epic("Эпик","Эпик для проверки");
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.createNewEpic(epic1);
        // создали эпик
        Subtask subtask = new Subtask("Подзадача", "Подзадача для проверки", epic1);
        manager.createNewSubtask(subtask);
        // создали подзадачу и добавили в менеджер - теперь в списке подзадач эпика хранится эта подзадача
        ArrayList<Subtask> forCheck = new ArrayList<>();
        forCheck.add(subtask);
        // создали такой же список для проверки
        Assertions.assertEquals(epic1.getSubtasksOfSpecificEpic(), forCheck, "Списки не одинаковые!");
        // проверили, что список подзадач эпика хранит данную подзадачу
        manager.deleteSubtask(subtask.getId());
        Assertions.assertNotEquals(epic1.getSubtasksOfSpecificEpic(), forCheck, "Списки одинаковые!");
        // проверили, что список подзадач эпика теперь пустой и отличается от своей прошлой версии

    }

}