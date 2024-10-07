package taskHttpServer;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager manager;
    Gson gson;

    public EpicsHandler(TaskManager manager, Gson gson) {
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
        if (partsOfPath.length == 2 && partsOfPath[1].equals("epics")) {
            ArrayList<Epic> epicsFromManager = manager.getAllEpics();
            String responseBody = gson.toJson(epicsFromManager);
            sendText200(exchange, responseBody);

        } else if (partsOfPath.length == 3 && partsOfPath[1].equals("epics")) {
            try {
                int idOfEpic = Integer.parseInt(partsOfPath[2]);
                Epic epic = manager.receiveEpic(idOfEpic);
                if (epic != null) {
                    String responseBody = gson.toJson(epic);
                    sendText200(exchange, responseBody);
                } else {
                    sendNotFound(exchange);
                }
            } catch (NumberFormatException exp) {
                sendNotFound(exchange);
            }
        } else if (partsOfPath.length == 4 && partsOfPath[1].equals("epics") && partsOfPath[3].equals("subtasks")) {
            try {
                int idOfEpic = Integer.parseInt(partsOfPath[2]);
                Epic epic = manager.receiveEpic(idOfEpic);
                if (epic != null) {
                    ArrayList<Subtask> subtasksOfEpic = manager.getSubtasksOfEpic(epic);
                    String responseBody = gson.toJson(subtasksOfEpic);
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
        if (partsOfPath.length == 2 && partsOfPath[1].equals("epics")) {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            if (!requestBody.isBlank()) {
                Epic epicFromRequest = gson.fromJson(requestBody, Epic.class);
                if (epicFromRequest.getId() != 0) {
                    boolean resultOfUpdating = manager.updateEpic(epicFromRequest);
                    if (resultOfUpdating) {
                        sendOnlyGoodCode201(exchange);
                    } else {
                        sendNotFound(exchange);
                    }

                } else {
                    manager.createNewEpic(epicFromRequest);
                    sendOnlyGoodCode201(exchange);
                }
            } else {
                sendNotFound(exchange);
            }

        } else {
            thereIsProblem(exchange);
        }
    }

    private void forDELETE(HttpExchange exchange, String[] partsOfPath) throws IOException {
        if (partsOfPath.length == 2 && partsOfPath[1].equals("epics")) {
            manager.deleteAllEpics();
            sendOnlyGoodCode201(exchange);
        } else if (partsOfPath.length == 3 && partsOfPath[1].equals("epics")) {
            try {
                int idOfEpic = Integer.parseInt(partsOfPath[2]);
                boolean isDeletedEpic = manager.deleteEpic(idOfEpic);
                if (isDeletedEpic) {
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
