/**
 * Encapsulates a task with a deadline.
 */
public class Deadline extends Task {
    protected String deadline;

    public Deadline(String description, String deadline) {
        super(description);
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), this.deadline);
    }

    @Override
    public String toNoteForm() {
        return String.format("D | %d | %s | %s",
                this.isDone ? 1 : 0,
                this.description,
                this.deadline);
    }
}
