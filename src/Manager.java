import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class Manager {
    Integer taskID;
    Integer epicID;
    Integer subtaskID;
    HashMap<Integer, Task> taskHashMap;
    HashMap<Integer, Epic> epicHashMap;
    HashMap<Integer, Subtask> subtaskHashMap;

    public Manager() {
        taskID = 100;
        epicID = 200;
        subtaskID = 300;
        taskHashMap = new HashMap<>();
        epicHashMap = new HashMap<>();
        subtaskHashMap = new HashMap<>();
    }

    public Collection<Task> getAllTasks() {
        return taskHashMap.values();
    }

    public void deleteAllTasks() {
        taskHashMap.clear();
    }

    public Task getTaskByID(Integer ID) {
        return taskHashMap.get(ID);
    }

    public int saveTask(Task task) {
        task.setID(++taskID);
        taskHashMap.put(task.getID(), task);
        return taskID;
    }

    public void updateTask(Task newTask) {
        if (taskHashMap.containsKey(newTask.getID())) {
            taskHashMap.put(newTask.getID(), newTask);
        }
    }

    public void removeTaskByID(int ID) {
        taskHashMap.remove(ID);
    }

    public Collection<Epic> getAllEpics() {
        return epicHashMap.values();
    }

    public void deleteAllEpics() {
        epicHashMap.clear();
    }

    public Task getEpicByID(Integer ID) {
        return epicHashMap.get(ID);
    }

    public int saveEpic(Epic epic) {
        epic.setID(++epicID);
        epicHashMap.put(epic.getID(), epic);
        return epicID;
    }

    public void updateEpic(Epic newEpic) {
        if (taskHashMap.containsKey(newEpic.getID())) {
            taskHashMap.put(newEpic.getID(), newEpic);
        }
    }

    public void removeEpicByID(int ID) {
        for (int i = 0; i < epicHashMap.get(ID).subtaskList.size(); i++) {
            subtaskHashMap.remove(epicHashMap.get(ID).subtaskList.get(i).getID());
        }
        epicHashMap.remove(ID);
    }

    public String getEpicSubtasks(int epicID){
        return Arrays.toString(epicHashMap.get(epicID).subtaskList.toArray());
    }

    public Collection<Subtask> getAllSubtasks() {
        return subtaskHashMap.values();
    }

    public void deleteAllSubtasks() {
        subtaskHashMap.clear();
    }

    public Task getSubtaskByID(Integer ID) {
        return subtaskHashMap.get(ID);
    }

    public int saveSubtask(Subtask subtask) {
        subtask.setID(++subtaskID);
        subtaskHashMap.put(subtask.getID(), subtask);
        epicHashMap.get(subtask.getEpicId()).fillSubtaskIList(subtask, epicHashMap.get(subtask.getEpicId()));
        epicStatusCheck();
        return subtaskID;
    }

    public void updateSubtask(Subtask newSubtask) {
        if (subtaskHashMap.containsKey(newSubtask.getID())) {
            subtaskHashMap.put(newSubtask.getID(), newSubtask);
            if (epicHashMap.containsKey(newSubtask.getEpicId())) {
                for (int i = 0; i < epicHashMap.get(newSubtask.getEpicId()).subtaskList.size(); i++) {
                    if (Objects.equals(epicHashMap.get(newSubtask.getEpicId()).subtaskList.get(i).getID(),
                            newSubtask.getID())) {
                        epicHashMap.get(newSubtask.getEpicId()).subtaskList.remove(i);
                        break;
                    }
                }
                epicHashMap.get(newSubtask.getEpicId()).fillSubtaskIList(newSubtask,
                        epicHashMap.get(newSubtask.getEpicId()));
            }
        }
        epicStatusCheck();
    }

    public void removeSubtaskByID(int ID) {
        int epicID = subtaskHashMap.get(ID).getEpicId();
        epicHashMap.get(epicID).subtaskList.remove(ID);
        subtaskHashMap.remove(ID);
    }

    public void epicStatusCheck() {
        Subtask subtask;
        String statusOfSubtask;
        int statusNew = 0;
        int statusDone = 0;
        int amountOfSubtasks = 0;
        for (int key : epicHashMap.keySet()) {
            for (int i = 0; i < epicHashMap.get(key).subtaskList.size(); i++) {
                if (epicHashMap.get(key).subtaskList.size() == 0) {
                    epicHashMap.get(key).status = "NEW";
                    return;
                } else {
                    amountOfSubtasks = epicHashMap.get(key).subtaskList.size();
                    subtask = epicHashMap.get(key).subtaskList.get(i);
                    statusOfSubtask = subtask.status;
                    if (statusOfSubtask.equals("NEW")) {
                        statusNew++;
                    } else if (statusOfSubtask.equals("DONE")) {
                        statusDone++;
                    }
                }
            }

            if (amountOfSubtasks == statusNew) {
                epicHashMap.get(key).status = "NEW";
            } else if (amountOfSubtasks == statusDone) {
                epicHashMap.get(key).status = "DONE";
            } else {
                epicHashMap.get(key).status = "IN_PROGRESS";
            }
            statusNew = 0;
            statusDone = 0;
        }
    }
}