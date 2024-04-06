package Managers;

import TaskKindObjects.Epic;
import TaskKindObjects.Subtask;
import TaskKindObjects.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int tracker = 1;
    HashMap<Integer,Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, Subtask> subtasks;

    public TaskManager(){
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }


    public void createNewTask(Task task){

            task.setId(tracker);
            tracker++;
            tasks.put(task.getId(), task);


    }

    public ArrayList<Task> getAllTasks(){
        ArrayList<Task> allTasksOfManager = new ArrayList<>();
        for(Task task: tasks.values()){
            allTasksOfManager.add(task);
        }
        return allTasksOfManager;
    }

    public void deleteAllTasks(){
        tasks.clear();
    }

    public Task receiveTask(int id){
        return tasks.get(id);
    }

    public void updateTask(Task task){
        if(!tasks.containsKey(task.getId())){
            System.out.println("Задача с заданным id не обнаружена, но Вы можете создать новую задачу.");
        } else{
            tasks.put(task.getId(), task);
        }
    }

    public void deleteTask(int id){
        if(tasks.containsKey(id)){
            tasks.remove(id);
        } else{
            System.out.println("Задача для удаления не найдена: не существует задачи с указанным id.");
        }
    }

    public ArrayList<Epic> getAllEpics(){
        ArrayList<Epic> allEpicsOfManager = new ArrayList<>();
        for(Epic epic: epics.values()){
            allEpicsOfManager.add(epic);
        }
        return allEpicsOfManager;
    }

    public void deleteAllEpics(){
        epics.clear();
        subtasks.clear();
    }

    public Epic receiveEpic(int id){
      return epics.get(id);
    }

    public void createNewEpic(Epic epic){
        if(epics.containsValue(epic)){
            System.out.println("Такой эпик уже есть!");
        } else{
            epic.setId(tracker);
            tracker++;
            epics.put(epic.getId(), epic);
        }
    }

    private void countStatusForEpic(Epic epic){
        if(epic.idOfSubtasks.isEmpty()){
            epic.setStatus(Status.NEW);
        } else{
            int sumOfNew = 0;
            int sumOfInProgress = 0;
            int sumOfDone = 0;
            for (Subtask subtask : epic.idOfSubtasks) {
                if (Status.NEW == subtask.getStatus()) {
                    sumOfNew++;
                } else if (Status.IN_PROGRESS == subtask.getStatus()) {
                    sumOfInProgress++;
                } else if (Status.DONE == subtask.getStatus()) {
                    sumOfDone++;
                }
            }

            if (sumOfInProgress > 0) {
                epic.setStatus(Status.IN_PROGRESS);
            } else if ((sumOfNew > 0) && (sumOfDone == 0)) {
                epic.setStatus(Status.NEW);
            } else if ((sumOfDone > 0) && (sumOfNew == 0)) {
                epic.setStatus(Status.DONE);
            } else{
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    public void updateEpic(Epic epic){
       if(!epics.containsKey(epic.getId())){
           System.out.println("Эпик с данным id не обнаружен, но вы можете создать новый эпик.");
       } else{
           Epic oldEpic = epics.get(epic.getId());
           epic.idOfSubtasks = oldEpic.idOfSubtasks;
           epics.put(epic.getId(), epic);
       }
    }

    public void deleteEpic(int id){
        if(epics.containsKey(id)){
            epics.remove(id);
        } else{
            System.out.println("Эпик для удаления не найден: не существует эпика с указанным id.");
        }
    }

    public void createNewSubtask(Subtask subtask){
        Epic epic = subtask.getEpicOfSubtask();
        ArrayList<Subtask> subtasksOfEpic = epic.idOfSubtasks;
        if(subtasksOfEpic.contains(subtask)){
            System.out.println("Данный эпик уже содержит эту подзадачу!");
        } else{
            subtask.setId(tracker);
            tracker++;
            subtasks.put(subtask.getId(), subtask);
            subtasksOfEpic.add(subtask);
            countStatusForEpic(epic);
        }
    }

    public ArrayList<Subtask> getAllSubtasks(){
        ArrayList<Subtask> allSubtasksOfManager = new ArrayList<>();
        for(Subtask subtask: subtasks.values()){
            allSubtasksOfManager.add(subtask);
        }
        return allSubtasksOfManager;
    }



    public void deleteAllSubtasks(){
        for(Subtask subtask: subtasks.values()){
            Epic epic = subtask.getEpicOfSubtask();
            ArrayList<Subtask> subtasksOfEpic = epic.idOfSubtasks;
            subtasksOfEpic.remove(subtask);
            countStatusForEpic(epic);
            subtasks.remove(subtask.getId());
        }
    }

    public Subtask receiveSubtask(int id){return subtasks.get(id);}

    public void updateSubtask(Subtask subtask){
        if(!subtasks.containsKey(subtask.getId())){
            System.out.println("Подзадача с заданным id не найдена, но вы можете создать новую подзадачу!");
        } else{
            subtasks.put(subtask.getId(), subtask);
            Epic epic = subtask.getEpicOfSubtask();
            ArrayList<Subtask> subtasksOfEpic = epic.idOfSubtasks;
            Subtask previousSubtask = null;
            for(Subtask subtask1: subtasksOfEpic){
                if(subtask1.getId() == subtask.getId()){
                    previousSubtask = subtask1;
                }
            }
            subtasksOfEpic.remove(previousSubtask);
            subtasksOfEpic.add(subtask);
            countStatusForEpic(epic);
        }
    }

    public void deleteSubtask(int id){
        if(!subtasks.containsKey(id)){
            System.out.println("Подзадача для удаления не найдена: не существует подзадачи с указанным id.");
        } else{
            Subtask subtask = subtasks.get(id);
            Epic epic =subtask.getEpicOfSubtask();
            ArrayList<Subtask> subtasksOfEpic = epic.idOfSubtasks;
            subtasksOfEpic.remove(subtask);
            subtasks.remove(id);
            countStatusForEpic(epic);
        }
    }

    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic){
        return epic.idOfSubtasks;
        
    }

}
