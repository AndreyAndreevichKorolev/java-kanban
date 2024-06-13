import managers.InMemoryHistoryManager;
import managers.Status;
import managers.InMemoryTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import managers.FileBackedTaskManager;

import java.io.File;


public class Main {

    public static void main(String[] args) {
        File file = new File("/Users/andreykorolev/Desktop/popochka.csv");
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());


    }
}
