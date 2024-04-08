package Managers;

import TaskKindObjects.Epic;
import TaskKindObjects.Subtask;
import TaskKindObjects.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    private int tracker = 1;
    HashMap<Integer,Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, Subtask> subtasks;

    public InMemoryTaskManager(){
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }


    @Override
    public void createNewTask(Task task){

            task.setId(tracker);
            tracker++;
            tasks.put(task.getId(), task);


    }

    @Override
    public ArrayList<Task> getAllTasks(){
        ArrayList<Task> allTasksOfManager = new ArrayList<>();
        for(Task task: tasks.values()){
            allTasksOfManager.add(task);
        }
        return allTasksOfManager;
    }

    @Override
    public void deleteAllTasks(){
        tasks.clear();
    }

    @Override
    public Task receiveTask(int id){

        return tasks.get(id);
    }

    @Override
    public void updateTask(Task task){
        if(!tasks.containsKey(task.getId())){
            System.out.println("Задача с заданным id не обнаружена, но Вы можете создать новую задачу.");
        } else{
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void deleteTask(int id){
        if(tasks.get(id) == null){
            System.out.println("Задача для удаления не найдена: не существует задачи с указанным id:" + id);

        } else{
            tasks.remove(id);
        }
    }

    @Override
    public ArrayList<Epic> getAllEpics(){
        ArrayList<Epic> allEpicsOfManager = new ArrayList<>();
        for(Epic epic: epics.values()){
            allEpicsOfManager.add(epic);
        }
        return allEpicsOfManager;
    }

    @Override
    public void deleteAllEpics(){
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Epic receiveEpic(int id){

      return epics.get(id);
    }

    @Override
    public void createNewEpic(Epic epic){

            epic.setId(tracker);
            tracker++;
            epics.put(epic.getId(), epic);

    }

    private void calculateStatusForEpic(Epic epic){
        if(epic.getSubtasksOfSpecificEpic().isEmpty()){
            epic.setStatus(Status.NEW);
        } else{
            int sumOfNew = 0;
            int sumOfInProgress = 0;
            int sumOfDone = 0;
            for (Subtask subtask : epic.getSubtasksOfSpecificEpic()) {
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

    @Override
    public void updateEpic(Epic epic){
       if(!epics.containsKey(epic.getId())){
           System.out.println("Эпик с данным id не обнаружен, но вы можете создать новый эпик.");
       } else{
           Epic oldEpic = epics.get(epic.getId());
           epic.setSubtasksOfSpecificEpic(oldEpic.getSubtasksOfSpecificEpic());
           epics.put(epic.getId(), epic);
       }
    }

    @Override
    public void deleteEpic(int id){
        if(epics.get(id) == null){
            System.out.println("Эпик для удаления не найден: не существует эпика с указанным id:" + id);
        } else{
            epics.remove(id);
        }
    }

    @Override
    public void createNewSubtask(Subtask subtask){
        Epic epic = subtask.getEpicOfSubtask();
        ArrayList<Subtask> subtasksOfEpic = epic.getSubtasksOfSpecificEpic();

            subtask.setId(tracker);
            tracker++;
            subtasks.put(subtask.getId(), subtask);
            subtasksOfEpic.add(subtask);
            calculateStatusForEpic(epic);

    }

    @Override
    public ArrayList<Subtask> getAllSubtasks(){
        ArrayList<Subtask> allSubtasksOfManager = new ArrayList<>();
        for(Subtask subtask: subtasks.values()){
            allSubtasksOfManager.add(subtask);
        }
        return allSubtasksOfManager;
    }



    @Override
    public void deleteAllSubtasks(){
        subtasks.clear();
        for(Epic epic: epics.values()){
            epic.getSubtasksOfSpecificEpic().clear();
            epic.setStatus(Status.NEW);
        }
    }









    @Override
    public Subtask receiveSubtask(int id){

        return subtasks.get(id);
    }

    @Override
    public void updateSubtask(Subtask subtask){
        if(!subtasks.containsKey(subtask.getId())){
            System.out.println("Подзадача с заданным id не найдена, но вы можете создать новую подзадачу!");
        } else{
            subtasks.put(subtask.getId(), subtask);
            Epic epic = subtask.getEpicOfSubtask();
            ArrayList<Subtask> subtasksOfEpic = epic.getSubtasksOfSpecificEpic();
            Subtask previousSubtask = null;
            for(Subtask subtask1: subtasksOfEpic){
                if(subtask1.getId() == subtask.getId()){
                    previousSubtask = subtask1;
                }
            }
            subtasksOfEpic.remove(previousSubtask);
            subtasksOfEpic.add(subtask);
            calculateStatusForEpic(epic);
        }
    }

    @Override
    public void deleteSubtask(int id){
        if(!subtasks.containsKey(id)){
            System.out.println("Подзадача для удаления не найдена: не существует подзадачи с указанным id.");
        } else{
            Subtask subtask = subtasks.get(id);
            Epic epic =subtask.getEpicOfSubtask();
            ArrayList<Subtask> subtasksOfEpic = epic.getSubtasksOfSpecificEpic();
            subtasksOfEpic.remove(subtask);
            subtasks.remove(id);
            calculateStatusForEpic(epic);
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic){
        return epic.getSubtasksOfSpecificEpic();
        
    }


}