package TaskAppManagers;

import TaskAppServerFunctionalityRealization.HttpTaskManager;

public final class Managers {
    public static HttpTaskManager getDefault() {
        return new HttpTaskManager("http://localhost:8078/");
    }

    public static FileBackedTasksManager getFileBacked(String filePath) {
        return new FileBackedTasksManager(filePath);
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}