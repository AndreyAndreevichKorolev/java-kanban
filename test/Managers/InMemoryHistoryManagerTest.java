package Managers;

import TaskKindObjects.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    public void oldTaskVersionThatWasViewedShouldBeSavedInHistoryAfterAddingAnotherVersion(){
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Задача1", "Первая версия задачи 1", Status.NEW);
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task2 = new Task("Задача1", "Вторая версия задачи 1", Status.IN_PROGRESS);
        taskManager.createNewTask(task1);
        historyManager.add(task1);
        task2.setId(task1.getId());
        taskManager.updateTask(task2);
        Assertions.assertNotEquals(taskManager.receiveTask(task2.getId()), task1, "Задача в истории изменила " +
                "свои данные после обновления");
    }

}