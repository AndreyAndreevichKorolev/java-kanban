package taskhttpserver;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager manager;
    Gson gson;

    public SubtasksHandler(TaskManager manager, Gson gson) {
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
        if (partsOfPath.length == 2 && partsOfPath[1].equals("subtasks")) {
            ArrayList<Subtask> subtasksFromManager = manager.getAllSubtasks();
            String responseBody = gson.toJson(subtasksFromManager);
            sendText200(exchange, responseBody);

        } else if (partsOfPath.length == 3 && partsOfPath[1].equals("subtasks")) {
            try {
                int idOfSubtask = Integer.parseInt(partsOfPath[2]);
                Subtask subtask = manager.receiveSubtask(idOfSubtask);
                if (subtask != null) {
                    String responseBody = gson.toJson(subtask);
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
        if (partsOfPath.length == 2 && partsOfPath[1].equals("subtasks")) {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            if (!requestBody.isBlank()) {
                Subtask subtaskFromRequest = gson.fromJson(requestBody, Subtask.class);
                if (subtaskFromRequest.getId() != 0) {
                    int resultOfUpdating = manager.updateSubtask(subtaskFromRequest);
                    if (resultOfUpdating == -1) {
                        hasInteractions(exchange);
                    } else if (resultOfUpdating == 0) {
                        sendNotFound(exchange);
                    } else if (resultOfUpdating == 1) {
                        sendOnlyGoodCode201(exchange);
                    }
                } else {
                    boolean isCreatedSubtask = manager.createNewSubtask(subtaskFromRequest);
                    if (isCreatedSubtask) {
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
        if (partsOfPath.length == 2 && partsOfPath[1].equals("subtasks")) {
            manager.deleteAllSubtasks();
            sendOnlyGoodCode201(exchange);
        } else if (partsOfPath.length == 3 && partsOfPath[1].equals("subtasks")) {
            try {
                int idOfSubtask = Integer.parseInt(partsOfPath[2]);
                boolean isDeletedSubtask = manager.deleteSubtask(idOfSubtask);
                if (isDeletedSubtask) {
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
