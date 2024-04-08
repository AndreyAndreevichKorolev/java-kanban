package Managers;
import TaskKindObjects.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int MAX_SIZE_OF_LAST_TASKS_LIST = 10;
    private static List<Task> lastViewedTasks;

    public InMemoryHistoryManager(){
        lastViewedTasks = new ArrayList<>();
    }

    @Override
    public void add(Task task){
        if(lastViewedTasks.size() == MAX_SIZE_OF_LAST_TASKS_LIST){
            lastViewedTasks.remove(0);
            lastViewedTasks.add(task);
        }else{
            lastViewedTasks.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory(){
        return (ArrayList) lastViewedTasks;
    }
}
