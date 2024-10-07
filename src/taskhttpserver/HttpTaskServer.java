package taskhttpserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 9090;
    static final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().registerTypeAdapter(LocalDateTime.class,
            new LocalDateTimeAdapter()).registerTypeAdapter(Duration.class, new DurationAdapter()).create();
    static TaskManager manager;
    private static HttpServer server = null;

    public static void main(String[] args) throws IOException {
        manager = Managers.getDefault();
        start();
    }

    public static void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TasksHandler(manager, gson));
        server.createContext("/epics", new EpicsHandler(manager, gson));
        server.createContext("/subtasks", new SubtasksHandler(manager, gson));
        server.createContext("/history", new HistoryHandler(manager, gson));
        server.createContext("/prioritized", new PriorityHandler(manager, gson));
        server.start();
        System.out.println("Работа сервера началась!");
    }

    public static void close() {
        if (server != null) {
            server.stop(1);
        }
    }
}
