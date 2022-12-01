import java.util.*;

public class Manager {
    private Integer taskId;
    private Integer epicId;
    private Integer subtaskId;
    private final HashMap<Integer, Task> taskHashMap;
    private final HashMap<Integer, Epic> epicHashMap;
    private final HashMap<Integer, Subtask> subtaskHashMap;

    public Manager() {
        taskId = 100;
        epicId = 200;
        subtaskId = 300;
        taskHashMap = new HashMap<>();
        epicHashMap = new HashMap<>();
        subtaskHashMap = new HashMap<>();
    }

    public Collection<Task> getAllTasks() {
        if (!taskHashMap.isEmpty()) {
            return taskHashMap.values();
        }
        System.out.println("Список задач пуст.");
        return null;
    }

    public void deleteAllTasks() {
        if (taskHashMap.isEmpty()) {
            System.out.println("Список задач пуст, удалять нечего.");
        } else {
            taskHashMap.clear();
            System.out.println("Все задачи удалены.");
        }
    }

    public Task getTaskByID(Integer id) {
        if (taskHashMap.containsKey(id)) {
            System.out.println("Задача №" + id);
            System.out.println(taskHashMap.get(id));
            return taskHashMap.get(id);
        }
        System.out.println("Задачи №" + id + " нет.");
        return null;
    }

    public int saveTask(Task task) {
        task.setId(++taskId);
        taskHashMap.put(task.getId(), task);
        System.out.println("Задача №" + taskId + " сохранена.");
        return taskId;
    }

    public void updateTask(Task newTask) {
        if (taskHashMap.containsKey(newTask.getId())) {
            taskHashMap.put(newTask.getId(), newTask);
            System.out.println("Задача №" + newTask.getId() + " обновлена.");
            System.out.println("Новый список задач - " + getAllTasks());
        } else {
            System.out.println("Нельзя обновить задачу №" + newTask.getId() + ", так как ее нет.");
        }
    }

    public void removeTaskByID(int id) {
        if (taskHashMap.get(id) != null){
            taskHashMap.remove(id);
            System.out.println("Задача №" + id + " удалена.");
            System.out.println("Новый список задач - " + getAllTasks());
        } else {
            System.out.println("Нельзя удалить задачу №" + id + ", так как ее нет.");
        }
    }

    public Collection<Epic> getAllEpics() {
        if (!epicHashMap.isEmpty()) {
            return epicHashMap.values();
        }
        System.out.println("Список эпиков пуст.");
        return null;
    }

    public void deleteAllEpics() {
        if (epicHashMap.isEmpty()) {
            System.out.println("Список эпиков пуст, нечего удалять.");
        } else {
            epicHashMap.clear();
            System.out.println("Все эпики удалены.");
        }
    }

    public Task getEpicByID(Integer id) {
        if (epicHashMap.get(id) != null) {
            System.out.println("Эпик №" + id);
            System.out.println(epicHashMap.get(id));
            return epicHashMap.get(id);
        }
        System.out.println("Эпика №" + id + " нет.");
        return null;
    }

    public int saveEpic(Epic epic) {
        epic.setId(++epicId);
        epicHashMap.put(epic.getId(), epic);
        System.out.println("Эпик №" + epicId + " сохранен.");
        return epicId;
    }

    public void updateEpic(Epic newEpic) {
        if (epicHashMap.containsKey(newEpic.getId())) {
            ArrayList<Subtask> newSubtask = epicHashMap.get(newEpic.getId()).subtaskList;
            newEpic.subtaskList.addAll(newSubtask);
            epicHashMap.put(newEpic.getId(), newEpic);
            System.out.println("Эпик №" + newEpic.getId() + " обновлен.");
            System.out.println("Новый список эпиков - " + getAllEpics());
        } else {
            System.out.println("Нельзя обновить эпик №" + newEpic.getId() + ", так как его нет.");
        }
    }

    public void removeEpicByID(int id) {
        if (epicHashMap.get(id) != null) {
            for (int i = 0; i < epicHashMap.get(id).subtaskList.size(); i++) {
                subtaskHashMap.remove(epicHashMap.get(id).subtaskList.get(i).getId());
            }
            epicHashMap.remove(id);
            System.out.println("Эпик №" + id + " удален.");
            System.out.println("Новый список эпиков - " + getAllEpics());
        } else {
            System.out.println("Нельзя удалить эпик №" + id + ", так как его нет.");
        }
    }

    public String getEpicSubtasks(int epicId){
        if (!epicHashMap.get(epicId).subtaskList.isEmpty()) {
            System.out.println("Список подзадач эпика № " + epicId + " - "
                    + Arrays.toString(epicHashMap.get(epicId).subtaskList.toArray()));
            return Arrays.toString(epicHashMap.get(epicId).subtaskList.toArray());
        }
        System.out.println("Список подзадач эпика №" + epicId + "пуст.");
        return null;
    }

    public void getAllSubtasks() {
        if (!subtaskHashMap.isEmpty() && !epicHashMap.isEmpty()) {
            for (int key : epicHashMap.keySet()) {
                System.out.println("Epic ID: " + epicHashMap.get(key).getId());
                System.out.println(epicHashMap.get(key).subtaskList);
            }
        } else {
            System.out.println("Список подзадач пуст.");
        }
    }

    public void deleteAllSubtasks() {
        if (!subtaskHashMap.isEmpty()) {
            subtaskHashMap.clear();
            System.out.println("Все подзадачи удалены.");
        } else {
            System.out.println("Список подзадач пуст, нечего удалять.");
        }
    }

    public Task getSubtaskByID(Integer id) {
        if (subtaskHashMap.get(id) != null) {
            System.out.println("Подзадача №" + id);
            System.out.println(subtaskHashMap.get(id));
            return subtaskHashMap.get(id);
        }
        System.out.println("Подзадачи №" + id + " нет.");
        return null;
    }

    public int saveSubtask(Subtask subtask) {
        subtask.setId(++subtaskId);
        subtaskHashMap.put(subtask.getId(), subtask);
        epicHashMap.get(subtask.getEpicId()).fillSubtaskIList(subtask, epicHashMap.get(subtask.getEpicId()));
        epicStatusCheck();
        System.out.println("Подзадача №" + subtaskId + " сохранена.");
        return subtaskId;
    }

    public void updateSubtask(Subtask newSubtask) {
        if (subtaskHashMap.containsKey(newSubtask.getId())) {
            subtaskHashMap.put(newSubtask.getId(), newSubtask);
            if (epicHashMap.containsKey(newSubtask.getEpicId())) {
                for (int i = 0; i < epicHashMap.get(newSubtask.getEpicId()).subtaskList.size(); i++) {
                    if (Objects.equals(epicHashMap.get(newSubtask.getEpicId()).subtaskList.get(i).getId(),
                            newSubtask.getId())) {
                        epicHashMap.get(newSubtask.getEpicId()).subtaskList.remove(i);
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

    public void removeSubtaskByID(int id) {
        if (subtaskHashMap.containsKey(id)) {
            int epicID = subtaskHashMap.get(id).getEpicId();
            for (int i = 0; i < epicHashMap.get(epicID).subtaskList.size(); i++) {
                if (subtaskHashMap.get(id) == epicHashMap.get(epicID).subtaskList.get(i)) {
                    epicHashMap.get(epicID).subtaskList.remove(i);
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

    public void epicStatusCheck() {
        Status statusOfSubtask;
        int statusNew = 0;
        int statusDone = 0;
        int amountOfSubtasks = 0;
        for (int key : epicHashMap.keySet()) {
            if (epicHashMap.get(key).subtaskList.size() == 0) {
                epicHashMap.get(key).status = Status.NEW;
                return;
            } else {
                for (int i = 0; i < epicHashMap.get(key).subtaskList.size(); i++) {
                    amountOfSubtasks = epicHashMap.get(key).subtaskList.size();
                    statusOfSubtask = epicHashMap.get(key).subtaskList.get(i).status;
                    switch (statusOfSubtask) {
                        case NEW -> statusNew++;
                        case DONE -> statusDone++;
                    }
                }

                if (amountOfSubtasks == statusNew) {
                    epicHashMap.get(key).status = Status.NEW;
                } else if (amountOfSubtasks == statusDone && epicHashMap.get(key).status != Status.DONE) {
                    epicHashMap.get(key).status = Status.DONE;
                    System.out.println("Статус эпика " + key + " изменен на DONE.");
                } else if (epicHashMap.get(key).status != Status.IN_PROGRESS) {
                    epicHashMap.get(key).status = Status.IN_PROGRESS;
                    System.out.println("Статус эпика " + key + " изменен на IN_PROGRESS.");
                }
                statusNew = 0;
                statusDone = 0;
            }
        }
    }
}