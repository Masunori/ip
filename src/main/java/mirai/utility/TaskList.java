package mirai.utility;

import java.util.ArrayList;
import java.util.List;

import mirai.tasks.Task;

/**
 * The TaskList class encapsulates a list of tasks.
 */
public class TaskList {
    private final List<Task> taskList;

    /**
     * Initialises a new list of tasks.
     */
    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    /**
     * Initialises a list of tasks from another list.
     * @param taskList the list of tasks to reference from
     */
    public TaskList(List<Task> taskList) {
        this.taskList = new ArrayList<>(taskList);
    }

    /**
     * Adds a task to the list of tasks.
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
     * Returns the number of tasks currently stored in this task list.
     * @return the number of tasks
     */
    public int getSize() {
        return this.taskList.size();
    }

    /**
     * Returns the list of tasks using Java's List.
     * @return the list of tasks
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
