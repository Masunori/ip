package mirai.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Encapsulates a task that starts at a specific date/time and ends at a specific date/time.
 */
public class Event extends Task {
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public Event(String description, LocalDateTime startTime, LocalDateTime endTime) {
        super(description);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)",
                super.toString(),
                this.startTime.format(DateTimeFormatter.ofPattern("MMM dd YYYY, HHmm")),
                this.endTime.format(DateTimeFormatter.ofPattern("MMM dd YYYY, HHmm")));
    }

    @Override
    public String toNoteForm() {
        return String.format("E | %d | %s | %s | %s",
                this.isDone ? 1 : 0,
                this.description,
                this.startTime,
                this.endTime);
    }

}
