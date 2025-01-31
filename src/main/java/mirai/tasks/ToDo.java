package mirai.tasks;

/**
 * Encapsulates a task without any date/time attached.
 */
public class ToDo extends Task {
    /**
     * Initialises a task to be done.
     * @param description The to-do task description
     */
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return String.format("[T]%s", super.toString());
    }

    @Override
    public String toNoteForm() {
        return String.format("T | %d | %s",
                this.isDone ? 1 : 0,
                this.description);
    }
}
