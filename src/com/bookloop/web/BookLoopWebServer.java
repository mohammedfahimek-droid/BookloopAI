package com.bookloop.web;

import com.bookloop.ai.ScoredBookMatch;
import com.bookloop.ai.SearchQueryIntent;
import com.bookloop.model.*;
import com.bookloop.repository.DataPersistenceManager;
import com.bookloop.service.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Built-in zero-dependency HTTP Web Server for BookLoop AI.
 * Serves modern Single-Page Application and interactive REST APIs on localhost:8080.
 */
public class BookLoopWebServer {

    private final int port;
    private final IUserService userService;
    private final IBookService bookService;
    private final IMatchingEngine matchingEngine;
    private final IBarterService barterService;
    private HttpServer server;

    public BookLoopWebServer(int port, IUserService userService, IBookService bookService,
                             IMatchingEngine matchingEngine, IBarterService barterService) {
        this.port = port;
        this.userService = userService;
        this.bookService = bookService;
        this.matchingEngine = matchingEngine;
        this.barterService = barterService;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());

        // 1. SPA Root Endpoint
        server.createContext("/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            if (!"/".equals(path) && !path.startsWith("/api/")) {
                // Route all UI paths to SPA
                sendHtml(exchange, BookLoopHtml.getHtml());
                return;
            }
            if ("/".equals(path)) {
                sendHtml(exchange, BookLoopHtml.getHtml());
            } else {
                sendNotFound(exchange);
            }
        });

        // 2. REST API: Statistics
        server.createContext("/api/stats", exchange -> {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendCors(exchange);
                return;
            }
            double savings = bookService.calculateTotalCampusSavings();
            int reused = bookService.getTotalBooksReused();
            int loops = barterService.findAllBarterLoops().size();
            int trees = (int) Math.max(1, Math.round(reused * 0.25));

            String json = String.format(Locale.US,
                    "{\"campusSavings\":%.2f,\"booksReused\":%d,\"activeLoops\":%d,\"treesSaved\":%d}",
                    savings, reused, loops, trees);
            sendJson(exchange, json);
        });

        // 3. REST API: Active User
        server.createContext("/api/active-user", exchange -> {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendCors(exchange);
                return;
            }
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String body = readBody(exchange);
                String userId = extractJsonField(body, "userId");
                if (userId != null && !userId.isBlank()) {
                    userService.findUserById(userId).ifPresent(userService::setActiveUser);
                }
            }
            User active = userService.getActiveUser();
            sendJson(exchange, JsonWriter.toJson(active));
        });

        // 4. REST API: All Students
        server.createContext("/api/users", exchange -> {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendCors(exchange);
                return;
            }
            List<Student> students = userService.getAllStudents();
            String json = JsonWriter.listToJson(students, JsonWriter::toJson);
            sendJson(exchange, json);
        });

        // 5. REST API: Books (Catalog & Creation)
        server.createContext("/api/books", exchange -> {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendCors(exchange);
                return;
            }

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String body = readBody(exchange);
                String title = extractJsonField(body, "title");
                String author = extractJsonField(body, "author");
                String edition = extractJsonField(body, "edition");
                String subject = extractJsonField(body, "subject");
                String condStr = extractJsonField(body, "condition");
                String modeStr = extractJsonField(body, "mode");
                String priceStr = extractJsonField(body, "price");
                String neededSub = extractJsonField(body, "neededSubject");
                String neededTitle = extractJsonField(body, "neededBookTitle");

                Condition condition = Condition.fromString(condStr);
                if (condition == null) condition = Condition.GOOD;

                ListingMode mode = ListingMode.fromString(modeStr);
                if (mode == null) mode = ListingMode.EXCHANGE;

                double price = 0.0;
                try {
                    if (priceStr != null) price = Double.parseDouble(priceStr);
                } catch (Exception ignored) {}

                String newId = "BK-" + (100 + bookService.getAllBooks().size() + 1);
                User owner = userService.getActiveUser();

                Book newBook = new Book(newId, title, author, edition, subject != null ? subject : "General",
                        condition, mode, price, price > 0 ? price * 1.6 : 60.0,
                        owner, neededSub, neededTitle);

                bookService.addBook(newBook);
                DataPersistenceManager.saveState(userService, bookService);
                sendJson(exchange, JsonWriter.toJson(newBook));
                return;
            }

            // GET Request - Catalog Listing
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
            String modeParam = queryParams.getOrDefault("mode", "ALL");
            String subjectParam = queryParams.getOrDefault("subject", "ALL");

            List<Book> books = bookService.getAvailableBooks();

            if (!"ALL".equalsIgnoreCase(modeParam)) {
                books = books.stream()
                        .filter(b -> b.getMode().name().equalsIgnoreCase(modeParam))
                        .toList();
            }

            if (!"ALL".equalsIgnoreCase(subjectParam)) {
                books = books.stream()
                        .filter(b -> b.getSubject().equalsIgnoreCase(subjectParam))
                        .toList();
            }

            String json = JsonWriter.listToJson(books, JsonWriter::toJson);
            sendJson(exchange, json);
        });

        // 6. REST API: AI Smart Search
        server.createContext("/api/search", exchange -> {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendCors(exchange);
                return;
            }
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
            String query = queryParams.getOrDefault("q", "affordable beginner book for learning Java");

            SearchQueryIntent intent = matchingEngine.analyzeQuery(query);
            List<ScoredBookMatch> matches = matchingEngine.findSmartRecommendations(query);

            String json = "{" +
                    "\"intent\":" + JsonWriter.toJson(intent) + "," +
                    "\"results\":" + JsonWriter.listToJson(matches, JsonWriter::toJson) +
                    "}";
            sendJson(exchange, json);
        });

        // 7. REST API: Reciprocal Barter Loops
        server.createContext("/api/barter-loops", exchange -> {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendCors(exchange);
                return;
            }
            List<BarterMatch> loops = barterService.findAllBarterLoops();
            String json = JsonWriter.listToJson(loops, JsonWriter::toJson);
            sendJson(exchange, json);
        });

        // 8. REST API: Barter Execution
        server.createContext("/api/barter-execute", exchange -> {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendCors(exchange);
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendMethodNotAllowed(exchange);
                return;
            }

            String body = readBody(exchange);
            String bookAId = extractJsonField(body, "bookAId");
            String bookBId = extractJsonField(body, "bookBId");

            if (bookAId == null || bookBId == null) {
                sendJson(exchange, "{\"success\":false,\"error\":\"Missing book IDs\"}");
                return;
            }

            try {
                Transaction tx = barterService.executeBarterExchange(bookAId, bookBId);
                DataPersistenceManager.saveState(userService, bookService);

                String json = "{" +
                        "\"success\":true," +
                        "\"receipt\":\"" + JsonWriter.escape(tx.generateReceipt()) + "\"," +
                        "\"transaction\":" + JsonWriter.toJson(tx) +
                        "}";
                sendJson(exchange, json);
            } catch (Exception e) {
                sendJson(exchange, "{\"success\":false,\"error\":\"" + JsonWriter.escape(e.getMessage()) + "\"}");
            }
        });

        // 9. REST API: Sustainability Leaderboard
        server.createContext("/api/leaderboard", exchange -> {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendCors(exchange);
                return;
            }
            List<User> topUsers = userService.getSustainabilityLeaderboard(10);
            String json = JsonWriter.listToJson(topUsers, JsonWriter::toJson);
            sendJson(exchange, json);
        });

        // 10. REST API: Transactions Log
        server.createContext("/api/transactions", exchange -> {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendCors(exchange);
                return;
            }
            List<Transaction> txs = bookService.getAllTransactions();
            String json = JsonWriter.listToJson(txs, JsonWriter::toJson);
            sendJson(exchange, json);
        });

        server.start();
        System.out.println("================================================================================");
        System.out.printf("  🚀 BookLoop AI Web App LIVE on: http://localhost:%d%n", port);
        System.out.println("  • Modern Campus Circular Economy Dashboard");
        System.out.println("  • Real-Time AI Semantic Intent & Barter Matching Engine");
        System.out.println("================================================================================");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private static void sendHtml(HttpExchange exchange, String html) throws IOException {
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void sendJson(HttpExchange exchange, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void sendCors(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        exchange.sendResponseHeaders(204, -1);
    }

    private static void sendNotFound(HttpExchange exchange) throws IOException {
        byte[] msg = "404 Not Found".getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(404, msg.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(msg);
        }
    }

    private static void sendMethodNotAllowed(HttpExchange exchange) throws IOException {
        byte[] msg = "405 Method Not Allowed".getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(405, msg.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(msg);
        }
    }

    private static String readBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static String extractJsonField(String json, String key) {
        if (json == null || key == null) return null;
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        Pattern pNum = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*([0-9.]+)");
        Matcher mNum = pNum.matcher(json);
        if (mNum.find()) {
            return mNum.group(1);
        }
        return null;
    }

    private static Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (query == null || query.isBlank()) return params;
        for (String pair : query.split("&")) {
            int idx = pair.indexOf('=');
            if (idx > 0) {
                String k = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                String v = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                params.put(k, v);
            }
        }
        return params;
    }
}
