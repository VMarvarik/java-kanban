package Tests;

import TaskAppClasses.Epic;
import TaskAppClasses.Subtask;
import TaskAppClasses.Task;
import TaskAppEnums.Status;
import TaskAppManagers.InMemoryTaskManager;
import TaskAppServerFunctionalityRealization.CustomJson.EpicSerializer;
import TaskAppServerFunctionalityRealization.CustomJson.SubtaskSerializer;
import TaskAppServerFunctionalityRealization.CustomJson.TaskSerializer;
import TaskAppServerFunctionalityRealization.HttpTaskServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.io.IOException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class HttpServerTest {

    private HttpTaskServer server;

    InMemoryTaskManager manager = new InMemoryTaskManager();

    private Gson gson;

    private Task task;

    private Task task2;

    private Epic epic;

    private Epic epic2;

    private Subtask subtask;

    private Subtask subtask2;

    private int taskId;

    private int epicId;

    private int subtaskId;


    @BeforeEach
    void beforeEach() throws IOException {
        gson = new GsonBuilder().
                registerTypeAdapter(Task.class, new TaskSerializer()).
                registerTypeAdapter(Epic.class, new EpicSerializer()).
                registerTypeAdapter(Subtask.class, new SubtaskSerializer()).
                create();

        manager = new InMemoryTaskManager();
        server = new HttpTaskServer(manager);
        task = new Task("1", "1", Status.NEW, 10, "2023-02-17 10:00");
        taskId = manager.saveTask(task);
        task2 = new Task("11", "11", Status.NEW, 10, "2023-02-17 14:00");
        epic = new Epic("2", "2", Status.NEW);
        epicId = manager.saveEpic(epic);
        epic2 = new Epic("22", "22", Status.NEW);
        subtask = new Subtask("3", "3", Status.NEW, epicId, 30, "2023-02-17 12:00");
        subtaskId = manager.saveSubtask(subtask);
        subtask2 = new Subtask("33", "33", Status.NEW, epicId, 40, "2023-02-17 20:00");

        manager.getTaskByID(taskId);
        manager.getEpicByID(epicId);
        manager.getSubtaskByID(subtaskId);

        server.start();
    }

    @AfterEach
    void afterEach() {
        server.stop();
    }

    @Test
    public void getTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/task/?id=101");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(task), response.body());
    }

    @Test
    public void getEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/epic/?id=201");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(epic), response.body());
    }

    @Test
    public void getSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/subtask/?id=301");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(subtask), response.body());
    }

    @Test
    public void getTasks() throws IOException, InterruptedException {
        manager.saveTask(task2); //Сохраняем дополнительную задачу
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals("\"" + manager.getAllTasks().toString() + "\"", response.body());
    }

    @Test
    public void getEpics() throws IOException, InterruptedException {
        manager.saveEpic(epic2); //Сохраняем дополнительный эпик
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals("\"" + manager.getAllEpics().toString() + "\"", response.body());
    }

    @Test
    public void getSubtasks() throws IOException, InterruptedException {
        manager.saveSubtask(subtask2); //Сохраняем дополнительную подзадачу
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals("\"" + manager.getAllSubtasks().toString() + "\"", response.body());
    }

    @Test
    public void getHistory() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals("\"" + manager.getHistory().toString() + "\"", response.body());
    }

    @Test
    public void getPrioritized() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals("\"" + manager.getPrioritizedTasks().toString() + "\"", response.body());
    }

    @Test
    public void getEpicSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/subtask/epic/?id=201");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals("\"" + manager.getEpicSubtasks(201) + "\"", response.body());
    }

    @Test
    public void postTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task2))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        System.out.println(response.body());
        assertEquals("Задача 102 сохранена", response.body());
        assertNotNull(manager.getTaskByID(102));
    }

    @Test
    public void postEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic2))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        System.out.println(response.body());
        assertEquals("Эпик 202 сохранен", response.body());
        assertNotNull(manager.getEpicByID(202));
    }

    @Test
    public void postSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask2))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        System.out.println(response.body());
        assertEquals("Подзадача 302 сохранена", response.body());
        assertNotNull(manager.getSubtaskByID(302));
    }

    @Test
    public void postUpdateTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/task/");
        task2.setId(taskId); //Ставим ID на тот, который есть, чтобы было обновление
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task2))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        System.out.println(response.body());
        assertEquals("Задача 101 обновлена", response.body());
        assertNotNull(manager.getTaskByID(taskId));
    }
    @Test
    public void postUpdateEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/epic/");
        epic2.setId(epicId); //Ставим ID на тот, который есть, чтобы было обновление
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic2))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        System.out.println(response.body());
        assertEquals("Эпик 201 обновлен", response.body());
        assertNotNull(manager.getEpicByID(201));
    }

    @Test
    public void postUpdateSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/subtask/");
        subtask2.setId(subtaskId); //Ставим ID на тот, который есть, чтобы было обновление
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask2))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        System.out.println(response.body());
        assertEquals("Подзадача 301 обновлена", response.body());
        assertNotNull(manager.getSubtaskByID(301));
    }

    @Test
    public void deleteTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/task/?id=101");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNull(manager.getTaskByID(101));
    }

    @Test
    public void deleteEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/epic/?id=201");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNull(manager.getEpicByID(201));
    }

    @Test
    public void deleteSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/subtask/?id=301");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNull(manager.getSubtaskByID(301));
    }

    @Test
    public void deleteTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNull(manager.getAllTasks());
    }
    @Test
    public void deleteEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNull(manager.getAllEpics());
    }

    @Test
    public void deleteSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNull(manager.getAllSubtasks());
    }

    @Test
    public void unknown() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:6080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(HttpRequest.BodyPublishers.ofString("test")).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
        assertEquals("Такого эндпоинта не существует", response.body());
    }
}
