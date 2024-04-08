package TaskKindObjects;

import Managers.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

}