package TaskAppManagers;

public final class Managers {
    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static InMemoryTaskManager getFileBacked() {
        return new FileBackedTasksManager("src/BackupDocument/Backup.csv");
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}