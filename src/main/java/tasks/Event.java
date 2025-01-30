package tasks;

/**
 * Encapsulates a task that starts at a specific date/time and ends at a specific date/time.
 */
public class Event extends Task {
    protected String startTime;
    protected String endTime;

    public Event(String description, String startTime, String endTime) {
        super(description);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(), this.startTime, this.endTime);
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
