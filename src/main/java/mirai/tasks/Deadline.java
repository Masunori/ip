package mirai.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Encapsulates a task with a deadline.
 */
public class Deadline extends Task {
    protected LocalDateTime deadline;

    public Deadline(String description, LocalDateTime deadline) {
        super(description);
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)",
                super.toString(),
                this.deadline.format(DateTimeFormatter.ofPattern("MMM dd YYYY, HHmm")));
    }

    @Override
    public String toNoteForm() {
        return String.format("D | %d | %s | %s",
                this.isDone ? 1 : 0,
                this.description,
                this.deadline);
    }
}
