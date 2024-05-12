package managers;

import taskKindObjects.Epic;
import taskKindObjects.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import taskKindObjects.Task;

import java.util.ArrayList;

class ManagersTest {

    @Test
    public void ShouldReturnTheWorkingObjects() {
        Managers manager = new Managers();
        TaskManager taskManager = manager.getDefault();
        Task task1 = new Task("Первый таск", "Это должна быть первая в простмотренных задача", Status.NEW);
        Task task2 = new Task("Второй таск", "Это должна быть вторая в просмотренных задача", Status.IN_PROGRESS);
        Epic epic = new Epic("Эпик", "Это должна быть третья в просмотренных задача");
        Subtask subtask = new Subtask("Сабтаск", "Это должна быть четвертая в просмотренных задача", epic);
        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask);
        Assertions.assertNotEquals(0, task1.getId(), "менеджер на задал id задаче!");
        HistoryManager historyManager = manager.getDefaultHistory();
        ArrayList<Task> testHistory = new ArrayList<>();
        testHistory.add(task1);
        testHistory.add(task2);
        testHistory.add(epic);
        testHistory.add(subtask);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(epic);
        historyManager.add(subtask);
        Assertions.assertEquals(testHistory, historyManager.getHistory(), "списки разные, сохранение истории не работает!");


    }

}