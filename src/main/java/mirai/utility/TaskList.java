package mirai.utility;

import mirai.tasks.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * The TaskList class encapsulates a list of mirai.tasks.
 */
public class TaskList {
    private final List<Task> taskList;

    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    public TaskList(List<Task> taskList) {
        this.taskList = new ArrayList<>(taskList);
    }

    /**
     * Adds a task to the list of mirai.tasks.
     * @param task The task to be added
     */
    public void addTask(Task task) {
        this.taskList.add(task);
    }

    /**
     * Removes a task from the list according to the specified index.
     * @param taskIndex The index of the task to be removed
     */
    public void deleteTask(int taskIndex) {
        this.taskList.remove(taskIndex);
    }

    /**
     * Marks a task from the task list as done, according to the specified index.
     * @param taskIndex The index of the task to be marked as done
     */
    public void markTask(int taskIndex) {
        this.taskList.get(taskIndex).markAsDone();
    }

    /**
     * Marks a task from the task list as undone, according to the specified index.
     * @param taskIndex The index of the task to be marked as undone
     */
    public void unmarkTask(int taskIndex) {
        this.taskList.get(taskIndex).markAsUndone();
    }

    /**
     * Returns the number of mirai.tasks currently stored in this task list.
     * @return the number of mirai.tasks
     */
    public int getSize() {
        return this.taskList.size();
    }

    /**
     * Returns the list of mirai.tasks using Java's List.
     * @return the list of mirai.tasks
     */
    public List<Task> getTaskList() {
        return new ArrayList<>(this.taskList);
    }

    /**
     * Returns the task at the specified index. Tasks are numbered based on the order of insertion.
     * @param index The index
     * @return The task at the index
     */
    public Task getTask(int index) {
        return this.taskList.get(index);
    }
}
