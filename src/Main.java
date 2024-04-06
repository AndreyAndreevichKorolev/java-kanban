import Managers.Status;
import Managers.TaskManager;
import TaskKindObjects.Epic;
import TaskKindObjects.Subtask;
import TaskKindObjects.Task;


public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager manager = new TaskManager();
        Task task1 = new Task("name1", "description1", Status.NEW);
        Task task2 = new Task("name2", "description2", Status.IN_PROGRESS);
        manager.createNewTask(task1);
        manager.createNewTask(task2);
        Epic epic1 = new Epic("Name1", "Description1");
        Epic epic2 = new Epic("Name2", "Description2");
        manager.createNewEpic(epic1);
        manager.createNewEpic(epic2);
        Subtask subtask1 = new Subtask("__name1", "__description1", epic1);
        Subtask subtask2 = new Subtask("__name2", "__description2", epic2);
        Subtask subtask3 = new Subtask("__name3", "__description3", epic2);
        manager.createNewSubtask(subtask1);
        manager.createNewSubtask(subtask2);
        manager.createNewSubtask(subtask3);
        manager.getAllTasks();
        manager.getAllEpics();
        manager.getAllSubtasks();
        System.out.println();
        System.out.println();
        System.out.println();
        Task task3 = new Task("name1", "description1", Status.IN_PROGRESS);
        task3.setId(task1.getId());
        manager.updateTask(task3);
        Task task4 = new Task("name2", "description2", Status.DONE);
        task4.setId(task2.getId());
        manager.updateTask(task4);
        Subtask subtask4 = new Subtask("__name1", "__description1", epic1);
        subtask4.setStatus(Status.DONE);
        subtask4.setId(subtask1.getId());
        Subtask subtask5 = new Subtask("__name3", "__description3", epic2);
        subtask5.setStatus(Status.IN_PROGRESS);
        subtask5.setId(subtask3.getId());
        manager.updateSubtask(subtask4);
        manager.updateSubtask(subtask5);
        manager.getAllTasks();
        manager.getAllEpics();
        manager.getAllSubtasks();


    }
}
