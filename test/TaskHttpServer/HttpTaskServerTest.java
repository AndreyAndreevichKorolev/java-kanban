package TaskHttpServer;

import managers.Managers;
import managers.Status;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

class HttpTaskServerTest {

    static HttpClient client = null;
    static Task task1 = null;
    static Epic epic1 = null;

    @BeforeEach
    public void PreparingForTasting() throws IOException {
        HttpTaskServer.manager = Managers.getDefault();
        client = HttpClient.newHttpClient();
        task1 = new Task("Task1", "First task", Status.NEW);
        epic1 = new Epic("Epic1", "first epic");
        HttpTaskServer.start();
    }

    @AfterEach
    public void ClosingServer() {
        HttpTaskServer.close();
    }

    @Test
    public void ShouldSaveCorrectlyAllKindsOfTasks() throws IOException, InterruptedException {
        URI uriForPOSTTask = URI.create("http://localhost:9090/tasks");
        URI uriForPOSTEpic = URI.create("http://localhost:9090/epics");
        URI uriForPOSTSubtask = URI.create("http://localhost:9090/subtasks");
        Subtask subtask1 = new Subtask("subtask", "subtask of epic", 2);
        String task = HttpTaskServer.gson.toJson(task1);
        String epic = HttpTaskServer.gson.toJson(epic1);
        String subtask = HttpTaskServer.gson.toJson(subtask1);
        HttpRequest requestForTask = HttpRequest.newBuilder().uri(uriForPOSTTask).header("Accept", "application/json").POST(HttpRequest.BodyPublishers.ofString(task)).build();
        HttpRequest requestForEpic = HttpRequest.newBuilder().uri(uriForPOSTEpic).header("Accept", "application/json").POST(HttpRequest.BodyPublishers.ofString(epic)).build();
        HttpResponse responseFromMakingTask = client.send(requestForTask, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        HttpResponse responseFromMakingEpic = client.send(requestForEpic, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        Assertions.assertEquals(201, responseFromMakingTask.statusCode(), "Статус другой - не добавилась задача");
        Assertions.assertEquals(201, responseFromMakingEpic.statusCode(), "Статус другой - не добавился эпик");
        HttpRequest requestForSubtask = HttpRequest.newBuilder().uri(uriForPOSTSubtask).header("Accept", "application/json").POST(HttpRequest.BodyPublishers.ofString(subtask)).build();
        HttpResponse responseFromMakingSubtask = client.send(requestForSubtask, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        Assertions.assertEquals(201, responseFromMakingSubtask.statusCode(), "Код не совпадает - подзадача не создана");

    }

    @Test
    public void ShouldUpdateCorrectlyAllKindsOfTasks() throws IOException, InterruptedException {
        URI uriForPOSTTask = URI.create("http://localhost:9090/tasks");
        URI uriForPOSTEpic = URI.create("http://localhost:9090/epics");
        URI uriForPOSTSubtask = URI.create("http://localhost:9090/subtasks");

        TaskManager manager = HttpTaskServer.manager;
        manager.createNewTask(task1);
        manager.createNewEpic(epic1);
        Subtask subtask = new Subtask("subtask", "subtask for tasting", epic1.getId());
        manager.createNewSubtask(subtask);

        Task anotherTask = new Task(task1.name, task1.description + " changed", task1.getStatus());
        anotherTask.setId(task1.getId());
        String updatedTaskRequest = HttpTaskServer.gson.toJson(anotherTask);
        HttpRequest requestForUpdatingTask = HttpRequest.newBuilder().uri(uriForPOSTTask).POST(HttpRequest.BodyPublishers.ofString(updatedTaskRequest)).header("Accept", "application/json").build();
        HttpResponse responseOfUpdatingTask = client.send(requestForUpdatingTask, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        Assertions.assertEquals(201, responseOfUpdatingTask.statusCode(), "не получилось изменить задачу");

        Epic anotherEpic = new Epic(epic1.name, epic1.description + " changed");
        anotherEpic.setId(epic1.getId());
        String updatedEpicRequest = HttpTaskServer.gson.toJson(anotherEpic);
        HttpRequest requestForUpdatingEpic = HttpRequest.newBuilder().uri(uriForPOSTEpic).POST(HttpRequest.BodyPublishers.ofString(updatedEpicRequest)).header("Accept", "application/json").build();
        HttpResponse responseOfUpdatingEpic = client.send(requestForUpdatingEpic, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        Assertions.assertEquals(201, responseOfUpdatingEpic.statusCode(), "не получилось изменить задачу");
        String updatedEpicRequestWithAllSubtasks = HttpTaskServer.gson.toJson(anotherEpic);

        Subtask anotherSubtask = new Subtask(subtask.name, subtask.description + " changed", subtask.getEpicOfSubtask());
        anotherSubtask.setId(subtask.getId());
        String updatedSubtaskRequest = HttpTaskServer.gson.toJson(anotherSubtask);
        HttpRequest requestForUpdatingSubtask = HttpRequest.newBuilder().uri(uriForPOSTSubtask).POST(HttpRequest.BodyPublishers.ofString(updatedSubtaskRequest)).header("Accept", "application/json").build();
        HttpResponse responseOfUpdatingSubtask = client.send(requestForUpdatingSubtask, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        Assertions.assertEquals(201, responseOfUpdatingSubtask.statusCode(), "не получилось изменить задачу");

        URI uriForGETTask = URI.create("http://localhost:9090/tasks/" + anotherTask.getId());
        URI uriForGETEpic = URI.create("http://localhost:9090/epics/" + anotherEpic.getId());
        URI uriForGETSubtask = URI.create("http://localhost:9090/subtasks/" + anotherSubtask.getId());

        HttpRequest requestForGettingTask = HttpRequest.newBuilder().uri(uriForGETTask).GET().header("Accept", "application/json").build();
        HttpRequest requestForGettingEpic = HttpRequest.newBuilder().uri(uriForGETEpic).GET().header("Accept", "application/json").build();
        HttpRequest requestForGettingSubtask = HttpRequest.newBuilder().uri(uriForGETSubtask).GET().header("Accept", "application/json").build();

        HttpResponse responseOfGettingTask = client.send(requestForGettingTask, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        HttpResponse responseOfGettingEpic = client.send(requestForGettingEpic, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        HttpResponse responseOfGettingSubtask = client.send(requestForGettingSubtask, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        Assertions.assertEquals(200, responseOfGettingTask.statusCode(), "вы не получили необходимую задачу по id");
        Assertions.assertEquals(200, responseOfGettingEpic.statusCode(), "вы не получили необходимый эпик по id");
        Assertions.assertEquals(200, responseOfGettingSubtask.statusCode(), "вы не получили необходимую подзадачу по id");

        HttpRequest requestForDeletingSubtask = HttpRequest.newBuilder().uri(uriForPOSTSubtask).DELETE().header("Accept", "application/json").build();
        HttpRequest requestForDeletingTask = HttpRequest.newBuilder().uri(uriForPOSTSubtask).DELETE().header("Accept", "application/json").build();
        HttpRequest requestForDeletingEpic = HttpRequest.newBuilder().uri(uriForPOSTSubtask).DELETE().header("Accept", "application/json").build();

        HttpResponse responseOfDeletingTask = client.send(requestForDeletingSubtask, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        HttpResponse responseOfDeletingEpic = client.send(requestForDeletingTask, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        HttpResponse responseOfDeletingSubtask = client.send(requestForDeletingEpic, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        Assertions.assertEquals(201, responseOfDeletingTask.statusCode(), "вы не удалили необходимую задачу");
        Assertions.assertEquals(201, responseOfDeletingEpic.statusCode(), "вы не удалили необходимый эпик");
        Assertions.assertEquals(201, responseOfDeletingSubtask.statusCode(), "вы не удалили необходимую подзадачу");


    }


}