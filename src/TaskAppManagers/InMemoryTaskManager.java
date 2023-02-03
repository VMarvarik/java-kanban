package TaskAppManagers;
import TaskAppClasses.*;

import java.time.LocalDateTime;
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
    public List<Task> getAllTasks() {
        if (!taskHashMap.isEmpty()) {
            return new ArrayList<>(taskHashMap.values());
        }
        System.out.println("Список задач пуст.");
        return null;
    }

    @Override
    public String deleteAllTasks() {
        if (taskHashMap.isEmpty()) {
            return "Список задач пуст, удалять нечего.";
        } else {
            taskHashMap.clear();
        }
        return "Все задачи удалены.";
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
        if (!taskHashMap.isEmpty() && !timeValidation(task)) {
            return 0;
        }
        task.setId(++taskId);
        taskHashMap.put(task.getId(), task);
        System.out.println("Задача №" + taskId + " сохранена.");
        return taskId;
    }

    @Override
    public Task updateTask(Task newTask) {
        if (taskHashMap.containsKey(newTask.getId())) {
            taskHashMap.put(newTask.getId(), newTask);
            System.out.println("Задача №" + newTask.getId() + " обновлена.");
            System.out.println("Новый список задач - " + getAllTasks());
            return newTask;
        } else {
            System.out.println("Нельзя обновить задачу №" + newTask.getId() + ", так как ее нет.");
        }
        return null;
    }

    @Override
    public String removeTaskByID(int id) {
        if (taskHashMap.get(id) != null){
            taskHashMap.remove(id);
            inMemoryHistoryManager.remove(id);
            return "Задача №" + id + " удалена.";
        }
        return "Нельзя удалить задачу №" + id + ", так как ее нет.";
    }

    @Override
    public List<Epic> getAllEpics() {
        if (!epicHashMap.isEmpty()) {
            return new ArrayList<>(epicHashMap.values());
        }
        System.out.println("Список эпиков пуст.");
        return null;
    }

    @Override
    public String deleteAllEpics() {
        if (epicHashMap.isEmpty()) {
            return "Список эпиков пуст, нечего удалять.";
        } else {
            epicHashMap.clear();
        }
        return "Все эпики удалены.";
    }

    @Override
    public Epic getEpicByID(int id) {
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
        if (!epicHashMap.isEmpty() && !timeValidation(epic)) {
            return 0;
        }
        epic.setId(++epicId);
        epicHashMap.put(epic.getId(), epic);

        System.out.println("Эпик №" + epicId + " сохранен.");
        return epicId;
    }

    @Override
    public Epic updateEpic(Epic newEpic) {
        if (epicHashMap.containsKey(newEpic.getId())) {
            ArrayList<Subtask> newEpicSubtaskList = epicHashMap.get(newEpic.getId()).getSubtaskList();
            newEpic.getSubtaskList().addAll(newEpicSubtaskList);
            epicHashMap.put(newEpic.getId(), newEpic);
            System.out.println("Эпик №" + newEpic.getId() + " обновлен.");
            System.out.println("Новый список эпиков - " + getAllEpics());
            return newEpic;
        } else {
            System.out.println("Нельзя обновить эпик №" + newEpic.getId() + ", так как его нет.");
        }
        return null;
    }

    @Override
    public String removeEpicByID(int epicId) {
        if (epicHashMap.get(epicId) != null) {
            for (int i = 0; i < epicHashMap.get(epicId).getSubtaskList().size(); i++) {
                subtaskHashMap.remove(epicHashMap.get(epicId).getSubtaskList().get(i).getId());
            }
            inMemoryHistoryManager.remove(epicId);
            epicHashMap.remove(epicId);
            return "Эпик №" + epicId + " удален.";

        }
        return "Нельзя удалить эпик №" + epicId + ", так как его нет.";
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
    public List<Subtask> getAllSubtasks() {
        if (!subtaskHashMap.isEmpty()) {
            return new ArrayList<>(subtaskHashMap.values());
        }
        return null;
    }

    @Override
    public String deleteAllSubtasks() {
        if (!subtaskHashMap.isEmpty()) {
            for (Subtask subtask : subtaskHashMap.values()) {
                int epicID = subtask.getEpicId();
                epicHashMap.get(epicID).getSubtaskList().clear();
                epicHashMap.get(epicID).timeCheck();
            }
            subtaskHashMap.clear();
            return "Все подзадачи удалены.";
        }
        return "Список подзадач пуст, нечего удалять.";
    }

    @Override
    public Subtask getSubtaskByID(int id) {
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
            if (!subtaskHashMap.isEmpty() && !timeValidation(subtask)) {
                return 0;
            }
            subtask.setId(++subtaskId);
            subtaskHashMap.put(subtask.getId(), subtask);
            epicHashMap.get(subtask.getEpicId()).fillSubtaskList(subtask);
            epicStatusCheck();
            System.out.println("Подзадача №" + subtaskId + " сохранена.");
            return subtaskId;
        }
        return 0;
    }

    @Override
    public Subtask updateSubtask(Subtask newSubtask) {
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
                epicHashMap.get(newSubtask.getEpicId()).fillSubtaskList(newSubtask);
            }
            epicStatusCheck();
            System.out.println("Подзадача №" + newSubtask.getId() + " обновлена.");
            System.out.println("Новый список подзадач - ");
            getAllSubtasks();
            return newSubtask;
        } else {
            System.out.println("Нельзя обновить подзадачу №" + newSubtask.getId() + ", так как ее нет.");
        }
        return null;
    }

    @Override
    public String removeSubtaskByID(int id) {
        inMemoryHistoryManager.remove(id);
        if (subtaskHashMap.containsKey(id)) {
            int epicID = subtaskHashMap.get(id).getEpicId();
            for (int i = 0; i < epicHashMap.get(epicID).getSubtaskList().size(); i++) {
                if (subtaskHashMap.get(id) == epicHashMap.get(epicID).getSubtaskList().get(i)) {
                    epicHashMap.get(epicID).getSubtaskList().remove(i);
                    epicHashMap.get(epicID).timeCheck();
                    break;
                }
            }
            subtaskHashMap.remove(id);
            return "Подзадача №" + id + " удалена.";
        }
        return "Нельзя удалить подзадачу №" + id + ", так как ее нет.";
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
    public ArrayList<Task> getPrioritizedTasks() {
        TreeSet<Task> prioritizedTree = new TreeSet<>();
        if (!taskHashMap.isEmpty()) {
            prioritizedTree.addAll(taskHashMap.values());
        }
        if (!epicHashMap.isEmpty()){
            prioritizedTree.addAll(epicHashMap.values());
        }
        if (!subtaskHashMap.isEmpty()) {
            prioritizedTree.addAll(subtaskHashMap.values());
        }
        return new ArrayList<>(prioritizedTree);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    public boolean timeValidation(Task task) {
        if (task.getStartTime() != null) {
            boolean isValid = false;
            for (int i = 0; i < getPrioritizedTasks().size(); i++) {
                LocalDateTime startTime = getPrioritizedTasks().get(i).getStartTime();
                LocalDateTime endTime = getPrioritizedTasks().get(i).getEndTime();
                if (getPrioritizedTasks().get(i).getStartTime().getYear() == task.getStartTime().getYear()
                        && getPrioritizedTasks().get(i).getStartTime().getDayOfYear() == task.getStartTime().getDayOfYear()) {
                    if (endTime.isBefore(task.getStartTime())) {
                        isValid = true;
                    }
                    if (task.getEndTime().isBefore(startTime)) {
                        isValid = true;
                    }
                    return isValid;
                }
            }
            return true;
        }
        return true;
    }
}