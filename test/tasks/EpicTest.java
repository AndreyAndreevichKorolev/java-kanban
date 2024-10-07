package tasks;

import managers.InMemoryTaskManager;
import managers.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

class EpicTest {
    Epic epic;

    @BeforeEach
    public void makeNewEpicForTest() {
        epic = new Epic("Эпик", "Эпик для проверки");
    }

    @Test
    public void ShouldBeTheSameEpicTwoEpicsWithTheSameId() {

        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.createNewEpic(epic);
        Epic epic2 = new Epic("Эпик2", "Эпик для проверки 2");
        epic2.setId(epic.getId());
        Assertions.assertEquals(epic.getId(), epic2.getId(), "id эпик не равны!");
    }

    @Test
    public void ShouldNotHaveDeletedSubtask() {

        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.createNewEpic(epic);
        // создали эпик
        Subtask subtask = new Subtask("Подзадача", "Подзадача для проверки", epic.getId());
        manager.createNewSubtask(subtask);
        // создали подзадачу и добавили в менеджер - теперь в списке подзадач эпика хранится эта подзадача
        ArrayList<Integer> forCheck = new ArrayList<>();
        forCheck.add(subtask.getId());
        // создали такой же список для проверки
        Assertions.assertEquals(epic.getSubtasksOfSpecificEpic(), forCheck, "Списки не одинаковые!");
        // проверили, что список подзадач эпика хранит данную подзадачу
        manager.deleteSubtask(subtask.getId());
        Assertions.assertNotEquals(epic.getSubtasksOfSpecificEpic(), forCheck, "Списки одинаковые!");
        // проверили, что список подзадач эпика теперь пустой и отличается от своей прошлой версии

    }

    @Test
    public void shouldBeWithStatusNewIfHaveOnlyNewSubtasks() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.createNewEpic(epic);
        Subtask subtask1 = new Subtask("подзадача1", "проверка", epic.getId());
        Subtask subtask2 = new Subtask("подзадача1", "проверка", epic.getId());
        Subtask subtask3 = new Subtask("подзадача1", "проверка", epic.getId());
        manager.createNewSubtask(subtask1);
        manager.createNewSubtask(subtask2);
        manager.createNewSubtask(subtask3);
        Assertions.assertEquals(Status.NEW, manager.receiveEpic(epic.getId()).getStatus(), "Статусы не совпадают");

    }

    @Test
    public void shouldBeWithStatusDoneIfHaveOnlyDoneSubtasks() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.createNewEpic(epic);
        Subtask subtask1 = new Subtask("подзадача1", "проверка", epic.getId());
        subtask1.setStatus(Status.DONE);
        Subtask subtask2 = new Subtask("подзадача1", "проверка", epic.getId());
        subtask2.setStatus(Status.DONE);
        Subtask subtask3 = new Subtask("подзадача1", "проверка", epic.getId());
        subtask3.setStatus(Status.DONE);
        manager.createNewSubtask(subtask1);
        manager.createNewSubtask(subtask2);
        manager.createNewSubtask(subtask3);
        Assertions.assertEquals(Status.DONE, manager.receiveEpic(epic.getId()).getStatus(), "Статусы не совпадают");

    }

    @Test
    public void shouldBeWithStatusInProgressIfHaveSubtasksWithDoneAndNewStatus() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.createNewEpic(epic);
        Subtask subtask1 = new Subtask("подзадача1", "проверка", epic.getId());
        subtask1.setStatus(Status.DONE);
        Subtask subtask2 = new Subtask("подзадача1", "проверка", epic.getId());
        subtask2.setStatus(Status.NEW);
        Subtask subtask3 = new Subtask("подзадача1", "проверка", epic.getId());
        subtask3.setStatus(Status.DONE);
        manager.createNewSubtask(subtask1);
        manager.createNewSubtask(subtask2);
        manager.createNewSubtask(subtask3);
        Assertions.assertEquals(Status.IN_PROGRESS, manager.receiveEpic(epic.getId()).getStatus(), "Статусы не совпадают");

    }

    @Test
    public void shouldBeWithStatusInProgressIfHaveOnlyInProgressSubtasks() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.createNewEpic(epic);
        Subtask subtask1 = new Subtask("подзадача1", "проверка", epic.getId());
        subtask1.setStatus(Status.IN_PROGRESS);
        Subtask subtask2 = new Subtask("подзадача1", "проверка", epic.getId());
        subtask2.setStatus(Status.IN_PROGRESS);
        Subtask subtask3 = new Subtask("подзадача1", "проверка", epic.getId());
        subtask3.setStatus(Status.IN_PROGRESS);
        manager.createNewSubtask(subtask1);
        manager.createNewSubtask(subtask2);
        manager.createNewSubtask(subtask3);
        Assertions.assertEquals(Status.IN_PROGRESS, manager.receiveEpic(epic.getId()).getStatus(), "Статусы не совпадают");

    }

    @Test
    public void shouldCountTheRightDuration() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.createNewEpic(epic);
        Subtask subtask1 = new Subtask("подзадача1", "проверка", epic.getId());
        subtask1.setDuration(Duration.ofMinutes(10));
        Subtask subtask2 = new Subtask("подзадача1", "проверка", epic.getId());
        subtask2.setDuration(Duration.ofMinutes(15));
        Subtask subtask3 = new Subtask("подзадача1", "проверка", epic.getId());
        subtask3.setDuration(Duration.ofMinutes(23));
        int rightDurationAfterAdd3Sub = 10 + 15 + 23;
        manager.createNewSubtask(subtask1);
        manager.createNewSubtask(subtask2);
        manager.createNewSubtask(subtask3);
        Assertions.assertEquals(rightDurationAfterAdd3Sub, epic.getDuration().toMinutes(), "Продолжительность вычеслена неправильно");
        int rightDurationAfterRemovingOneSub = 10 + 23;
        manager.deleteSubtask(subtask2.getId());
        Assertions.assertEquals(rightDurationAfterRemovingOneSub, epic.getDuration().toMinutes(), "после удаления подзадачи неправильная продолжительность эпика");
        Subtask subtask4 = new Subtask("подзадача1", "проверка", epic.getId());
        subtask4.setId(subtask3.getId());
        subtask4.setDuration(Duration.ofMinutes(12));
        int rightDurationAfterUpdatingOneSub = 10 + 12;
        manager.updateSubtask(subtask4);
        Assertions.assertEquals(rightDurationAfterUpdatingOneSub, epic.getDuration().toMinutes(), "после обновления подзадачи - не обновилась продолжительность эпика");
    }


    @Test
    public void shouldCountTheRightStartTimeAndEndTime() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.createNewEpic(epic);
        Subtask subtask1 = new Subtask("подзадача1", "проверка", epic.getId());
        subtask1.setDuration(Duration.ofMinutes(10));
        subtask1.setStartTime(LocalDateTime.of(2023, 9, 27, 19, 0));
        Subtask subtask2 = new Subtask("подзадача1", "проверка", epic.getId());
        subtask2.setStartTime(LocalDateTime.of(2023, 10, 27, 19, 0));
        subtask2.setDuration(Duration.ofMinutes(15));
        Subtask subtask3 = new Subtask("подзадача1", "проверка", epic.getId());
        subtask3.setDuration(Duration.ofMinutes(23));
        subtask3.setStartTime(LocalDateTime.of(2023, 11, 27, 19, 0));
        int rightDurationAfterAdd3Sub = 10 + 15 + 23;
        manager.createNewSubtask(subtask1);
        manager.createNewSubtask(subtask2);
        manager.createNewSubtask(subtask3);
        LocalDateTime startTime = LocalDateTime.of(2023, 9, 27, 19, 0);
        LocalDateTime endTime = subtask3.getEndTime();
        Assertions.assertEquals(startTime, epic.getStartTime(), "неправильно вычилили стар эпика");
        Assertions.assertEquals(endTime, epic.getEndTime(), "неправильно вычилили конец эпика");
        manager.deleteSubtask(subtask1.getId());
        LocalDateTime newStartTime = subtask2.getStartTime();
        Assertions.assertEquals(newStartTime, epic.getStartTime(), "после удаления первого по дате сабтаска из жпика не поменялось начало эпика");
        LocalDateTime newEndTime = subtask2.getEndTime();
        manager.deleteSubtask(subtask3.getId());
        Assertions.assertEquals(newEndTime, epic.getEndTime(), "После удаления последнего по дате сабтаска не поменялся конец эпика");

    }


}