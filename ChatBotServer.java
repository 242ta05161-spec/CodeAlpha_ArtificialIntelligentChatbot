import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.*;

public class ChatBotServer {

    private static Map<String, String> responses = new HashMap<>();

    public static void main(String[] args) throws Exception {

        loadResponses();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Serve Website
        server.createContext("/", exchange -> {
            File file = new File("web/index.html");
            sendFile(exchange, file, "text/html");
        });

        server.createContext("/style.css", exchange -> {
            File file = new File("web/style.css");
            sendFile(exchange, file, "text/css");
        });

        server.createContext("/script.js", exchange -> {
            File file = new File("web/script.js");
            sendFile(exchange, file, "application/javascript");
        });

        // Chat API
        server.createContext("/chat", exchange -> {

            if ("POST".equals(exchange.getRequestMethod())) {

                InputStream is = exchange.getRequestBody();

                String userMessage = new String(is.readAllBytes()).toLowerCase();

                String response = getBotResponse(userMessage);

                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

                exchange.sendResponseHeaders(200, response.getBytes().length);

                OutputStream os = exchange.getResponseBody();

                os.write(response.getBytes());

                os.close();
            }
        });

        server.setExecutor(null);

        System.out.println("Server running at http://localhost:8080");

        server.start();
    }

    private static void loadResponses() {

        responses.put("hello", "Hello! Welcome to AI ChatBot.");
        responses.put("hi", "Hi there!");
        responses.put("how are you", "I am doing great!");
        responses.put("java", "Java is a powerful object-oriented language.");
        responses.put("ai", "Artificial Intelligence enables smart systems.");
        responses.put("bye", "Goodbye! Have a nice day.");
        responses.put("who created java", "Java was created by James Gosling.");
        responses.put("oop", "OOP means Object-Oriented Programming.");
    }

    private static String getBotResponse(String input) {

        for (String key : responses.keySet()) {

            if (input.contains(key)) {
                return responses.get(key);
            }
        }

        return "Sorry, I don't understand that yet.";
    }

    private static void sendFile(HttpExchange exchange, File file, String contentType)
            throws IOException {

        byte[] bytes = Files.readAllBytes(file.toPath());

        exchange.getResponseHeaders().set("Content-Type", contentType);

        exchange.sendResponseHeaders(200, bytes.length);

        OutputStream os = exchange.getResponseBody();

        os.write(bytes);

        os.close();
    }
}