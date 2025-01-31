package mirai.tasks;

/**
 * Encapsulates a task that can be done or not done.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Initialises a task.
     * @param description The task description
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon of the task.
     * @return <code>X</code> if done, blank if undone
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Marks the task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as undone
     */
    public void markAsUndone() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), this.description);
    }

    /**
     * Converts the task into a note form to store in a file.
     * @return A string representation of the task, store-able in a file
     */
    public abstract String toNoteForm();
}
