package TaskAppRealization;
import HistoryFunctionalityRealization.InMemoryHistoryManager;

public final class Managers {
    static InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    static InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    public static InMemoryTaskManager getDefault() {
        return inMemoryTaskManager;
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return inMemoryHistoryManager;
    }
}