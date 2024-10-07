package taskhttpserver;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.ArrayList;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager manager;
    Gson gson;

    public HistoryHandler(TaskManager manager, Gson gson) {
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
            default:
                thereIsProblem(exchange);
        }
    }

    private void forGET(HttpExchange exchange, String[] partsOfPath) throws IOException {
        if (partsOfPath.length == 2 && partsOfPath[1].equals("history")) {
            ArrayList<Task> historyOfManager = manager.getHistory();
            String responseBody = gson.toJson(historyOfManager);
            sendText200(exchange, responseBody);

        } else {
            thereIsProblem(exchange);
        }
    }
}
