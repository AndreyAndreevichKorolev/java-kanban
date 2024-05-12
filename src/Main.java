import managers.InMemoryHistoryManager;
import managers.Status;
import managers.InMemoryTaskManager;
import TaskKindObjects.Epic;
import TaskKindObjects.Subtask;
import TaskKindObjects.Task;


public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        InMemoryHistoryManager history = new InMemoryHistoryManager();
        Task task1 = new Task("Задача1", "Помыть посуду", Status.NEW);
        Task task2 = new Task("Задача2", "Прочесть конспект", Status.IN_PROGRESS);
        Epic epic1 = new Epic("Эпик1", "Купить продукты");
        Subtask subtask1 = new Subtask("Подзадача1", "Молоко", epic1);
        Subtask subtask2 = new Subtask("Подзадача2", "Хлеб", epic1);
        Subtask subtask3 = new Subtask("Подзадача3", "Помидоры", epic1);
        Epic epic2 = new Epic("Эпик2", "Пустой эпик");
        manager.createNewTask(task1);
        manager.createNewTask(task2);
        manager.createNewEpic(epic1);
        manager.createNewSubtask(subtask1);
        manager.createNewSubtask(subtask2);
        manager.createNewSubtask(subtask3);
        manager.createNewEpic(epic2);
        manager.receiveTask(task1.getId());
        manager.receiveTask(task2.getId());
        manager.receiveEpic(epic1.getId());
        manager.receiveEpic(epic2.getId());
        manager.receiveSubtask(subtask1.getId());
        System.out.println(manager.getHistory());

        manager.receiveTask(task1.getId());
        System.out.println(manager.getHistory());

        manager.deleteTask(task2.getId());
        System.out.println(manager.getHistory());

        manager.deleteEpic(epic1.getId());
        System.out.println(manager.getHistory());


    }
}
