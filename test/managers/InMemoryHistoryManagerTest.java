package managers;

import task_Kind_Objects.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class InMemoryHistoryManagerTest {

    @Test
    public void oldTaskVersionThatWasViewedShouldBeSavedInHistoryAfterAddingAnotherVersion() {
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

    @Test
    public void IfWeHaveCheckedTheSameTaskMoreThenOneTimeThatTaskShouldBeDeletedFromOldPlaceAndAddedToTheEndOfList() {
        InMemoryHistoryManager history = new InMemoryHistoryManager();
        Task task1 = new Task("Задача 1", "Первая задача", Status.NEW);
        Task task2 = new Task("Задача 2", "Вторая задача", Status.NEW);
        Task task3 = new Task("Задча 3", "Третья задача", Status.IN_PROGRESS);
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.createNewTask(task1);
        manager.createNewTask(task2);
        manager.createNewTask(task3);
        // создали задачи и задали им id при помощи менеджера

        history.add(task1);
        history.add(task2);
        history.add(task3);
        ArrayList<Task> oldList = history.getHistory();
        // добавили задачи в историю и получили список истории

        history.add(task1);
        ArrayList<Task> newList = history.getHistory();
        // просмотрели первую задачу еще раз - теперь она должна оказаться в конце списка - списки должны быть разные

        Assertions.assertNotEquals(oldList, newList, "Списки одинаковые!");
        System.out.println(oldList);
        System.out.println(newList);

        history.remove(task1.getId());
        ArrayList<Task> listAfterRemoving = history.getHistory();
        Assertions.assertNotEquals(listAfterRemoving, newList, "Списки одинаковые!");
        System.out.println(listAfterRemoving);
    }


}