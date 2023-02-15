package TaskAppServerFunctionalityRealization;

import TaskAppClasses.Epic;
import TaskAppEnums.Endpoint;
import TaskAppEnums.Status;
import TaskAppClasses.Subtask;
import TaskAppClasses.Task;
import TaskAppManagers.FileBackedTasksManager;
import TaskAppManagers.Managers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class HttpTaskServer {
    private static final int PORT = 6080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new GsonBuilder().create();
    private static final FileBackedTasksManager fileBackedTasksManager
            = Managers.getFileBacked("src/Backup.csv");

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task/?id=101", new taskManagerHandler());
        httpServer.createContext("/tasks/task/", new taskManagerHandler());
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    static {
        Task task = new Task("Тест1", "Описание1", Status.NEW, 20, "2000-10-10 19:00");
        fileBackedTasksManager.saveTask(task);
        Task task1 = new Task("Тест2", "Описание2", Status.NEW, 10, "2001-11-11 19:00");
        fileBackedTasksManager.saveTask(task1);
        Epic epic = new Epic("Тест3", "Тест3", Status.NEW);
        int epicId = fileBackedTasksManager.saveEpic(epic);
        Epic epic1 = new Epic("Тест4", "Тест4", Status.NEW);
        int epic1Id = fileBackedTasksManager.saveEpic(epic1);
        Subtask subtask = new Subtask("Тест5", "Тест5", Status.NEW, epicId, 20, "2003-10-10 19:00");
        fileBackedTasksManager.saveSubtask(subtask);
        Subtask subtask1 = new Subtask("Тест6", "Тест6", Status.NEW, epic1Id, 30, "2004-10-10 19:00");
        fileBackedTasksManager.saveSubtask(subtask1);
    }

    static class taskManagerHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), exchange.getRequestURI().getQuery());
            switch (endpoint) {
                case GET_TASK -> handleGetTask(exchange);
                case GET_EPIC -> handleGetEpic(exchange);
                case GET_SUBTASK -> handleGetSubtask(exchange);
                case GET_TASKS -> handleGetTasks(exchange);
                case GET_EPICS -> handleGetEpics(exchange);
                case GET_SUBTASKS -> handleGetSubtasks(exchange);
                case GET_EPIC_SUBTASKS -> handleGetEpicSubtasks(exchange);
                case GET_HISTORY -> handleGetHistory(exchange);
                case GET_PRIORITIZED -> handleGetPrioritized(exchange);
                case POST_TASK -> handlePostTask(exchange);
                case POST_EPIC -> handlePostEpic(exchange);
                case POST_SUBTASK -> handlePostSubtask(exchange);
                case POST_UPDATE_TASK -> handlePostUpdateTask(exchange);
                case POST_UPDATE_EPIC -> handlePostUpdateEpic(exchange);
                case POST_UPDATE_SUBTASK -> handlePostUpdateSubtask(exchange);
                case DELETE_TASK -> handleDeleteTask(exchange);
                case DELETE_EPIC -> handleDeleteEpic(exchange);
                case DELETE_SUBTASK -> handleDeleteSubtask(exchange);
                case DELETE_TASKS -> handleDeleteTasks(exchange);
                case DELETE_EPICS -> handleDeleteEpics(exchange);
                case DELETE_SUBTASKS -> handleDeleteSubtasks(exchange);
                case UNKNOWN -> writeResponse(exchange, "Такого эндпоинта не существует", 404);
            }
        }

        private Endpoint getEndpoint(String requestPath, String requestMethod, String query) {
            String[] pathParts = requestPath.split("/");
            switch (requestMethod) {
                case "GET":
                    switch (pathParts[2]) {
                        case "task":
                            if (pathParts.length == 3 && query == null) {
                                return Endpoint.GET_TASKS;
                            }
                            return Endpoint.GET_TASK;
                        case "epic":
                            if (pathParts.length == 3 && query == null) {
                                return Endpoint.GET_EPICS;
                            }
                            return Endpoint.GET_EPIC;
                        case "subtask":
                            if (pathParts.length == 3 && query == null) {
                                return Endpoint.GET_SUBTASKS;
                            } else if (pathParts[3].equals("epic")) {
                                return Endpoint.GET_EPIC_SUBTASKS;
                            }
                            return Endpoint.GET_SUBTASK;
                        case "history":
                            return Endpoint.GET_HISTORY;
                        case "prioritized":
                            return Endpoint.GET_PRIORITIZED;
                    }
                case "POST":
                    switch (pathParts[2]) {
                        case "task" -> {
                            if (pathParts.length == 3 && query == null) {
                                return Endpoint.POST_TASK;
                            }
                            return Endpoint.POST_UPDATE_TASK;
                        }
                        case "epic" -> {
                            if (!query.isEmpty()) {
                                return Endpoint.POST_EPIC;
                            }
                            return Endpoint.POST_UPDATE_EPIC;
                        }
                        case "subtask" -> {
                            if (!query.isEmpty()) {
                                return Endpoint.POST_SUBTASK;
                            }
                            return Endpoint.POST_UPDATE_SUBTASK;
                        }
                    }
                case "DELETE":
                    switch (pathParts[2]) {
                        case "task" -> {
                            if (pathParts.length == 3 && query == null) {
                                return Endpoint.DELETE_TASKS;
                            }
                            return Endpoint.DELETE_TASK;
                        }
                        case "epic" -> {
                            if (pathParts.length == 3 && query == null) {
                                return Endpoint.DELETE_EPICS;
                            }
                            return Endpoint.DELETE_EPIC;
                        }
                        case "subtask" -> {
                            if (pathParts.length == 3 && query == null) {
                                return Endpoint.DELETE_SUBTASKS;
                            }
                            return Endpoint.DELETE_SUBTASK;
                        }
                    }
            }
            return Endpoint.UNKNOWN;
        }

        private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
            if (responseString.isBlank()) {
                exchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
                exchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
            exchange.close();
        }

        private Optional<Integer> getId(HttpExchange exchange) {
            String query = exchange.getRequestURI().getQuery();
            String id = query.substring(3);
            try {
                return Optional.of(Integer.parseInt(id));
            } catch (NumberFormatException exception) {
                return Optional.empty();
            }
        }

        private void handleGetTask(HttpExchange exchange) throws IOException {
            Optional<Integer> optionalInteger = getId(exchange);
            if (optionalInteger.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int id = optionalInteger.get();
            if (fileBackedTasksManager.getTaskByID(id) != null) {
                writeResponse(exchange, gson.toJson(fileBackedTasksManager.getTaskByID(id).toString()), 200);
                return;
            }
            writeResponse(exchange, "Задача с идентификатором " + id + " не найдена", 404);
        }

        private void handleGetEpic(HttpExchange exchange) throws IOException {
            Optional<Integer> optionalInteger = getId(exchange);
            if (optionalInteger.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор эпика", 400);
                return;
            }
            int id = optionalInteger.get();
            if (fileBackedTasksManager.getEpicByID(id) != null) {
                writeResponse(exchange, gson.toJson(fileBackedTasksManager.getEpicByID(id).toString()), 200);
                return;
            }
            writeResponse(exchange, "Эпик с идентификатором " + id + " не найден", 404);
        }

        private void handleGetSubtask(HttpExchange exchange) throws IOException {
            Optional<Integer> optionalInteger = getId(exchange);
            if (optionalInteger.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор подзадачи", 400);
                return;
            }
            int id = optionalInteger.get();
            if (fileBackedTasksManager.getSubtaskByID(id) != null) {
                writeResponse(exchange, gson.toJson(fileBackedTasksManager.getSubtaskByID(id).toString()), 200);
                return;
            }
            writeResponse(exchange, "Подзадача с идентификатором " + id + " не найдена", 404);
        }

        private void handleGetTasks(HttpExchange exchange) throws IOException {
            if (!fileBackedTasksManager.getTaskHashMap().isEmpty()) {
                writeResponse(exchange, gson.toJson(fileBackedTasksManager.getAllTasks().toString()), 200);
                return;
            }
            writeResponse(exchange, "Задачи отсутствуют.", 404);
        }

        private void handleGetEpics(HttpExchange exchange) throws IOException {
            if (!fileBackedTasksManager.getEpicHashMap().isEmpty()) {
                writeResponse(exchange, gson.toJson(fileBackedTasksManager.getAllEpics().toString()), 200);
                return;
            }
            writeResponse(exchange, "Эпики отсутствуют.", 404);
        }

        private void handleGetSubtasks(HttpExchange exchange) throws IOException {
            if (!fileBackedTasksManager.getSubtaskHashMap().isEmpty()) {
                writeResponse(exchange, gson.toJson(fileBackedTasksManager.getAllSubtasks().toString()), 200);
                return;
            }
            writeResponse(exchange, "Подзадачи отсутствуют.", 404);
        }

        private void handleGetEpicSubtasks(HttpExchange exchange) throws IOException {
            Optional<Integer> optionalInteger = getId(exchange);
            if (optionalInteger.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор эпика", 400);
                return;
            }
            int id = optionalInteger.get();
            if (fileBackedTasksManager.getEpicByID(id) != null) {
                if (fileBackedTasksManager.getEpicSubtasks(id) != null) {
                    writeResponse(exchange, gson.toJson(fileBackedTasksManager.getEpicSubtasks(id)), 200);
                    return;
                }
                writeResponse(exchange, "У эпика с идентификатором " + id + " нет подзадач.", 404);
                return;
            }
            writeResponse(exchange, "Эпик с идентификатором " + id + " не найден, нельзя вывести подзадачи", 404);
        }

        private void handleGetHistory(HttpExchange exchange) throws IOException {
            if (fileBackedTasksManager.getHistory() != null) {
                writeResponse(exchange, gson.toJson(fileBackedTasksManager.getHistory().toString()), 200);
                return;
            }
            writeResponse(exchange, "Истории нет", 404);
        }

        private void handleGetPrioritized(HttpExchange exchange) throws IOException {
            if (fileBackedTasksManager.getPrioritizedTasks() != null) {
                writeResponse(exchange, gson.toJson(fileBackedTasksManager.getPrioritizedTasks().toString()), 200);
                return;
            }
            writeResponse(exchange, "Нет задач, чтобы вывести по приоритету", 404);
        }

        private void handlePostTask(HttpExchange exchange) throws IOException {
            String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            Task newTask;
            try {
                newTask = gson.fromJson(body, Task.class);
            } catch (JsonSyntaxException ex) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            }
            int newTaskId = fileBackedTasksManager.saveTask(newTask);
            if (newTaskId != 0) {
                writeResponse(exchange, "Задача добавлена", 201);
                return;
            }
            writeResponse(exchange, "Задача не была добавлена, так как ее время пересекается с другой задачей", 400);
        }

        private void handlePostEpic(HttpExchange exchange) throws IOException {
            String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            Epic newEpic;
            try {
                newEpic = gson.fromJson(body, Epic.class);
            } catch (JsonSyntaxException ex) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            }
            int newEpicId = fileBackedTasksManager.saveTask(newEpic);
            if (newEpicId != 0) {
                writeResponse(exchange, "Эпик добавлен", 201);
                return;
            }
            writeResponse(exchange, "Эпик не был добавлен", 400);
        }

        private void handlePostSubtask(HttpExchange exchange) throws IOException {
            String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            Subtask newSubtask;
            try {
                newSubtask = gson.fromJson(body, Subtask.class);
            } catch (JsonSyntaxException ex) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            }
            int newSubtaskId = fileBackedTasksManager.saveSubtask(newSubtask);
            if (newSubtaskId != 0) {
                writeResponse(exchange, "Подзадача добавлена", 201);
                return;
            }
            writeResponse(exchange, "Подзадача не была добавлена, так как ее время пересекается с другой задачей", 400);
        }

        private void handlePostUpdateTask(HttpExchange exchange) throws IOException {
            String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            Task newTask;
            try {
                newTask = gson.fromJson(body, Task.class);
            } catch (JsonSyntaxException ex) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            }
            Optional<Integer> optionalInteger = getId(exchange);
            if (optionalInteger.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int id = optionalInteger.get();
            if (fileBackedTasksManager.getTaskByID(id) != null) {
                if (fileBackedTasksManager.updateTask(newTask) != null) {
                    writeResponse(exchange, "Задача " + id + " обновлена", 200);
                    return;
                }
                writeResponse(exchange, "Задачу " + id + " не получилось обновить", 400);
                return;
            }
            writeResponse(exchange, "Задача с идентификатором " + id + " не найдена", 404);
        }

        private void handlePostUpdateEpic(HttpExchange exchange) throws IOException {
            String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            Epic newEpic;
            try {
                newEpic = gson.fromJson(body, Epic.class);
            } catch (JsonSyntaxException ex) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            }
            Optional<Integer> optionalInteger = getId(exchange);
            if (optionalInteger.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор эпика", 400);
                return;
            }
            int id = optionalInteger.get();
            if (fileBackedTasksManager.getEpicByID(id) != null) {
                if (fileBackedTasksManager.updateEpic(newEpic) != null) {
                    writeResponse(exchange, "Эпик " + id + " обновлен", 200);
                    return;
                }
                writeResponse(exchange, "Эпик " + id + " не получилось обновить", 400);
                return;
            }
            writeResponse(exchange, "Эпик с идентификатором " + id + " не найден", 404);
        }

        private void handlePostUpdateSubtask(HttpExchange exchange) throws IOException {
            String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            Subtask newSubtask;
            try {
                newSubtask = gson.fromJson(body, Subtask.class);
            } catch (JsonSyntaxException ex) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
                return;
            }
            Optional<Integer> optionalInteger = getId(exchange);
            if (optionalInteger.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор подзадачи", 400);
                return;
            }
            int id = optionalInteger.get();
            if (fileBackedTasksManager.getSubtaskByID(id) != null) {
                if (fileBackedTasksManager.updateSubtask(newSubtask) != null) {
                    writeResponse(exchange, "Подзадача " + id + " обновлена", 200);
                    return;
                }
                writeResponse(exchange, "Подзадачу " + id + " не получилось обновить", 400);
                return;
            }
            writeResponse(exchange, "Подзадача с идентификатором " + id + " не найдена", 404);
        }

        private void handleDeleteTask(HttpExchange exchange) throws IOException {
            Optional<Integer> optionalInteger = getId(exchange);
            if (optionalInteger.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int id = optionalInteger.get();
            writeResponse(exchange, gson.toJson(fileBackedTasksManager.removeTaskByID(id)), 200);
        }

        private void handleDeleteEpic(HttpExchange exchange) throws IOException {
            Optional<Integer> optionalInteger = getId(exchange);
            if (optionalInteger.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int id = optionalInteger.get();
            writeResponse(exchange, gson.toJson(fileBackedTasksManager.removeEpicByID(id)), 200);
        }

        private void handleDeleteSubtask(HttpExchange exchange) throws IOException {
            Optional<Integer> optionalInteger = getId(exchange);
            if (optionalInteger.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int id = optionalInteger.get();
            writeResponse(exchange, gson.toJson(fileBackedTasksManager.removeSubtaskByID(id)), 200);
        }

        private void handleDeleteTasks(HttpExchange exchange) throws IOException {
            writeResponse(exchange, gson.toJson(fileBackedTasksManager.deleteAllTasks()), 200);
        }

        private void handleDeleteEpics(HttpExchange exchange) throws IOException {
            writeResponse(exchange, gson.toJson(fileBackedTasksManager.deleteAllEpics()), 200);
        }

        private void handleDeleteSubtasks(HttpExchange exchange) throws IOException {
            writeResponse(exchange, gson.toJson(fileBackedTasksManager.deleteAllSubtasks()), 200);
        }
    }
}