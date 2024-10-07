package taskhttpserver;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {

    protected void sendText200(HttpExchange exchange, String text) throws IOException {
        byte[] textInBytes = text.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, textInBytes.length);
        exchange.getResponseBody().write(textInBytes);
        exchange.close();
    }

    protected void sendOnlyGoodCode201(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(201, 0);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, 0);
        exchange.close();
    }

    protected void hasInteractions(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(406, 0);
        exchange.close();
    }

    protected void thereIsProblem(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(500, 0);
        exchange.close();
    }


}
