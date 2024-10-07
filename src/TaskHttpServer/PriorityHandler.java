package TaskHttpServer;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;


public class PriorityHandler extends BaseHttpHandler implements HttpHandler {
    InMemoryTaskManager manager;
    Gson gson;

    public PriorityHandler(TaskManager manager, Gson gson) {
        this.manager = (InMemoryTaskManager) manager;
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
            default:
                thereIsProblem(exchange);
        }
    }

    private void forGET(HttpExchange exchange, String[] partsOfPath) throws IOException {
        if (partsOfPath.length == 2 && partsOfPath[1].equals("prioritized")) {
            List<Task> priorityOfManager = manager.getPrioritizedTasks();
            String responseBody = gson.toJson(priorityOfManager);
            sendText200(exchange, responseBody);

        } else {
            thereIsProblem(exchange);
        }
    }
}
