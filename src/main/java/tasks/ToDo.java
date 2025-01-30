package tasks;

/**
 * Encapsulates a task without any date/time attached.
 */
public class ToDo extends Task {
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
