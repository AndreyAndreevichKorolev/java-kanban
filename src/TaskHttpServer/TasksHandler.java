package TaskHttpServer;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    TaskManager manager;
    Gson gson;

    public TasksHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] partsOfPath = path.split("/");


        switch (method) {
            case "GET":
                forGET(exchange, partsOfPath);
                break;
            case "POST":
                forPOST(exchange, partsOfPath);
                break;
            case "DELETE":
                forDELETE(exchange, partsOfPath);
                break;
            default:
                thereIsProblem(exchange);

        }


    }

    private void forGET(HttpExchange exchange, String[] partsOfPath) throws IOException {
        if (partsOfPath.length == 2 && partsOfPath[1].equals("tasks")) {
            ArrayList<Task> tasksFromManager = manager.getAllTasks();
            String responseBody = gson.toJson(tasksFromManager);
            sendText200(exchange, responseBody);

        } else if (partsOfPath.length == 3 && partsOfPath[1].equals("tasks")) {
            try {
                int idOfTask = Integer.parseInt(partsOfPath[2]);
                Task task = manager.receiveTask(idOfTask);
                if (task != null) {
                    String responseBody = gson.toJson(task);
                    sendText200(exchange, responseBody);
                } else {
                    sendNotFound(exchange);
                }
            } catch (NumberFormatException exp) {
                sendNotFound(exchange);
            }
        } else {
            thereIsProblem(exchange);
        }
    }

    private void forPOST(HttpExchange exchange, String[] partsOfPath) throws IOException {
        if (partsOfPath.length == 2 && partsOfPath[1].equals("tasks")) {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            if (!requestBody.isBlank()) {
                Task taskFromRequest = gson.fromJson(requestBody, Task.class);
                if (taskFromRequest.getId() != 0) {
                    int resultOfUpdating = manager.updateTask(taskFromRequest);
                    if (resultOfUpdating == -1) {
                        hasInteractions(exchange);
                    } else if (resultOfUpdating == 0) {
                        sendNotFound(exchange);
                    } else if (resultOfUpdating == 1) {
                        sendOnlyGoodCode201(exchange);
                    }
                } else {
                    boolean isCreatedTask = manager.createNewTask(taskFromRequest);
                    if (isCreatedTask) {
                        sendOnlyGoodCode201(exchange);
                    } else {
                        hasInteractions(exchange);
                    }
                }
            } else {
                sendNotFound(exchange);
            }

        } else {
            thereIsProblem(exchange);
        }
    }

    private void forDELETE(HttpExchange exchange, String[] partsOfPath) throws IOException {
        if (partsOfPath.length == 2 && partsOfPath[1].equals("tasks")) {
            manager.deleteAllTasks();
            sendOnlyGoodCode201(exchange);
        } else if (partsOfPath.length == 3 && partsOfPath[1].equals("tasks")) {
            try {
                int idOfTask = Integer.parseInt(partsOfPath[2]);
                boolean isDeletedTask = manager.deleteTask(idOfTask);
                if (isDeletedTask) {
                    sendOnlyGoodCode201(exchange);
                } else {
                    sendNotFound(exchange);
                }
            } catch (NumberFormatException exception) {
                sendNotFound(exchange);
            }
        } else {
            thereIsProblem(exchange);
        }
    }
}
