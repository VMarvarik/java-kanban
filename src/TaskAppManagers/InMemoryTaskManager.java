package TaskAppManagers;
import TaskAppClasses.*;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int taskId = 100;
    private int epicId = 200;
    private int subtaskId = 300;
    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();
    private final InMemoryHistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    public InMemoryHistoryManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }

    public HashMap<Integer, Task> getTaskHashMap() {
        return taskHashMap;
    }

    public HashMap<Integer, Epic> getEpicHashMap() {
        return epicHashMap;
    }

    public HashMap<Integer, Subtask> getSubtaskHashMap() {
        return subtaskHashMap;
    }

    @Override
    public Collection<Task> getAllTasks() {
        if (!taskHashMap.isEmpty()) {
            return taskHashMap.values();
        }
        System.out.println("Список задач пуст.");
        return null;
    }

    @Override
    public void deleteAllTasks() {
        if (taskHashMap.isEmpty()) {
            System.out.println("Список задач пуст, удалять нечего.");
        } else {
            taskHashMap.clear();
            System.out.println("Все задачи удалены.");
        }
    }

    @Override
    public Task getTaskByID(int id) {
        if (taskHashMap.containsKey(id)) {
            System.out.println("Задача №" + id);
            System.out.println(taskHashMap.get(id));
            inMemoryHistoryManager.add(taskHashMap.get(id));
            return taskHashMap.get(id);
        }
        System.out.println("Задачи №" + id + " нет.");
        return null;
    }

    @Override
    public int saveTask(Task task) {
        task.setId(++taskId);
        taskHashMap.put(task.getId(), task);
        System.out.println("Задача №" + taskId + " сохранена.");
        return taskId;
    }

    @Override
    public void updateTask(Task newTask) {
        if (taskHashMap.containsKey(newTask.getId())) {
            taskHashMap.put(newTask.getId(), newTask);
            System.out.println("Задача №" + newTask.getId() + " обновлена.");
            System.out.println("Новый список задач - " + getAllTasks());
        } else {
            System.out.println("Нельзя обновить задачу №" + newTask.getId() + ", так как ее нет.");
        }
    }

    @Override
    public void removeTaskByID(int id) {
        if (taskHashMap.get(id) != null){
            taskHashMap.remove(id);
            inMemoryHistoryManager.remove(id);
            System.out.println("Задача №" + id + " удалена.");
            System.out.println("Новый список задач - " + getAllTasks());
        } else {
            System.out.println("Нельзя удалить задачу №" + id + ", так как ее нет.");
        }
    }

    @Override
    public Collection<Epic> getAllEpics() {
        if (!epicHashMap.isEmpty()) {
            return epicHashMap.values();
        }
        System.out.println("Список эпиков пуст.");
        return null;
    }

    @Override
    public void deleteAllEpics() {
        if (epicHashMap.isEmpty()) {
            System.out.println("Список эпиков пуст, нечего удалять.");
        } else {
            epicHashMap.clear();
            System.out.println("Все эпики удалены.");
        }
    }

    @Override
    public Task getEpicByID(int id) {
        if (epicHashMap.get(id) != null) {
            System.out.println("Эпик №" + id);
            System.out.println(epicHashMap.get(id));
            inMemoryHistoryManager.add(epicHashMap.get(id));
            return epicHashMap.get(id);
        }
        System.out.println("Эпика №" + id + " нет.");
        return null;
    }

    @Override
    public int saveEpic(Epic epic) {
        epic.setId(++epicId);
        epicHashMap.put(epic.getId(), epic);
        System.out.println("Эпик №" + epicId + " сохранен.");
        return epicId;
    }

    @Override
    public void updateEpic(Epic newEpic) {
        if (epicHashMap.containsKey(newEpic.getId())) {
            ArrayList<Subtask> newEpicSubtaskList = epicHashMap.get(newEpic.getId()).getSubtaskList();
            newEpic.getSubtaskList().addAll(newEpicSubtaskList);
            epicHashMap.put(newEpic.getId(), newEpic);
            System.out.println("Эпик №" + newEpic.getId() + " обновлен.");
            System.out.println("Новый список эпиков - " + getAllEpics());
        } else {
            System.out.println("Нельзя обновить эпик №" + newEpic.getId() + ", так как его нет.");
        }
    }

    @Override
    public void removeEpicByID(int epicId) {
        if (epicHashMap.get(epicId) != null) {
            for (int i = 0; i < epicHashMap.get(epicId).getSubtaskList().size(); i++) {
                subtaskHashMap.remove(epicHashMap.get(epicId).getSubtaskList().get(i).getId());
            }
            inMemoryHistoryManager.remove(epicId);
            epicHashMap.remove(epicId);
            System.out.println("Эпик №" + epicId + " удален.");
            if (getAllEpics() != null) {
                System.out.println("Новый список эпиков - " + getAllEpics());
            }
        } else {
            System.out.println("Нельзя удалить эпик №" + epicId + ", так как его нет.");
        }
    }

    @Override
    public String getEpicSubtasks(int epicId){
        if (!epicHashMap.get(epicId).getSubtaskList().isEmpty()) {
            System.out.println("Список подзадач эпика № " + epicId + " - "
                    + Arrays.toString(epicHashMap.get(epicId).getSubtaskList().toArray()));
            return Arrays.toString(epicHashMap.get(epicId).getSubtaskList().toArray());
        }
        System.out.println("Список подзадач эпика №" + epicId + "пуст.");
        return null;
    }

    @Override
    public void getAllSubtasks() {
        if (!subtaskHashMap.isEmpty() && !epicHashMap.isEmpty()) {
            for (int key : epicHashMap.keySet()) {
                System.out.println("TaskClasses.Epic ID: " + epicHashMap.get(key).getId());
                System.out.println(epicHashMap.get(key).getSubtaskList());
            }
        } else {
            System.out.println("Список подзадач пуст.");
        }
    }

    @Override
    public void deleteAllSubtasks() {
        if (!subtaskHashMap.isEmpty()) {
            subtaskHashMap.clear();
            System.out.println("Все подзадачи удалены.");
        } else {
            System.out.println("Список подзадач пуст, нечего удалять.");
        }
    }

    @Override
    public Task getSubtaskByID(int id) {
        if (subtaskHashMap.get(id) != null) {
            System.out.println("Подзадача №" + id);
            System.out.println(subtaskHashMap.get(id));
            inMemoryHistoryManager.add(subtaskHashMap.get(id));
            return subtaskHashMap.get(id);
        }
        System.out.println("Подзадачи №" + id + " нет.");
        return null;
    }

    @Override
    public int saveSubtask(Subtask subtask) {
        if (!epicHashMap.containsKey(subtask.getEpicId())) {
            System.out.println("Указанного epicId у подзадачи не существует.");
        } else {
            subtask.setId(++subtaskId);
            subtaskHashMap.put(subtask.getId(), subtask);
            epicHashMap.get(subtask.getEpicId()).fillSubtaskIList(subtask, epicHashMap.get(subtask.getEpicId()));
            epicStatusCheck();
            System.out.println("Подзадача №" + subtaskId + " сохранена.");
            return subtaskId;
        }
        return 0;
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        if (subtaskHashMap.containsKey(newSubtask.getId())) {
            subtaskHashMap.put(newSubtask.getId(), newSubtask);
            if (epicHashMap.containsKey(newSubtask.getEpicId())) {
                for (int i = 0; i < epicHashMap.get(newSubtask.getEpicId()).getSubtaskList().size(); i++) {
                    if (Objects.equals(epicHashMap.get(newSubtask.getEpicId()).getSubtaskList().get(i).getId(),
                            newSubtask.getId())) {
                        epicHashMap.get(newSubtask.getEpicId()).getSubtaskList().remove(i);
                        break;
                    }
                }
                epicHashMap.get(newSubtask.getEpicId()).fillSubtaskIList(newSubtask,
                        epicHashMap.get(newSubtask.getEpicId()));
            }
            epicStatusCheck();
            System.out.println("Подзадача №" + newSubtask.getId() + " обновлена.");
            System.out.println("Новый список подзадач - ");
            getAllSubtasks();
        } else {
            System.out.println("Нельзя обновить подзадачу №" + newSubtask.getId() + ", так как ее нет.");
        }
    }

    @Override
    public void removeSubtaskByID(int id) {
        inMemoryHistoryManager.remove(id);
        if (subtaskHashMap.containsKey(id)) {
            int epicID = subtaskHashMap.get(id).getEpicId();
            for (int i = 0; i < epicHashMap.get(epicID).getSubtaskList().size(); i++) {
                if (subtaskHashMap.get(id) == epicHashMap.get(epicID).getSubtaskList().get(i)) {
                    epicHashMap.get(epicID).getSubtaskList().remove(i);
                    break;
                }
            }
            subtaskHashMap.remove(id);
            System.out.println("Подзадача №" + id + " удалена.");
            if (subtaskHashMap.isEmpty()) {
                System.out.println("Список подзадач теперь пуст.");
            } else {
                System.out.println("Новый список подзадач - ");
                getAllSubtasks();
            }
        } else {
            System.out.println("Нельзя удалить подзадачу №" + id + ", так как ее нет.");
        }
    }

    @Override
    public void epicStatusCheck() {
        Status statusOfSubtask;
        int statusNew = 0;
        int statusDone = 0;
        int amountOfSubtasks = 0;
        for (int key : epicHashMap.keySet()) {
            if (epicHashMap.get(key).getSubtaskList().size() == 0) {
                epicHashMap.get(key).setStatus(Status.NEW);
                return;
            } else {
                for (int i = 0; i < epicHashMap.get(key).getSubtaskList().size(); i++) {
                    amountOfSubtasks = epicHashMap.get(key).getSubtaskList().size();
                    statusOfSubtask = epicHashMap.get(key).getSubtaskList().get(i).getStatus();
                    switch (statusOfSubtask) {
                        case NEW -> statusNew++;
                        case DONE -> statusDone++;
                    }
                }

                if (amountOfSubtasks == statusNew) {
                    epicHashMap.get(key).setStatus(Status.NEW);
                } else if (amountOfSubtasks == statusDone && epicHashMap.get(key).getStatus() != Status.DONE) {
                    epicHashMap.get(key).setStatus(Status.DONE);
                    System.out.println("Статус эпика " + key + " изменен на DONE.");
                } else if (epicHashMap.get(key).getStatus() != Status.IN_PROGRESS) {
                    epicHashMap.get(key).setStatus(Status.IN_PROGRESS);
                    System.out.println("Статус эпика " + key + " изменен на IN_PROGRESS.");
                }
                statusNew = 0;
                statusDone = 0;
            }
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }
}