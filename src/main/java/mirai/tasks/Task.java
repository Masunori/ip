package mirai.tasks;

import mirai.utility.Utility;

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
     * Checks if the task description contains a certain string.
     * @param keyword The string to check for containment
     * @return <code>true</code> if the task description contains the keyword, <code>false</code> otherwise
     */
    public boolean contains(String keyword) {
        return this.description.contains(keyword);
    }

    /**
     * Converts the task into a note form to store in a file.
     * @return A string representation of the task, store-able in a file
     */
    public abstract String toNoteForm();

    /**
     * Computes the closeness of the task's description to the user's keyword.<br>
     * This returns 0 if the keyword is contained within the task's description. Else, split the keywords and
     * description into two String arrays and perform matching for each keyword word.
     *
     * @param keyword The user's keyword
     * @return the closeness of the task's description to the user's keyword
     */
    public double computeCloseness(String keyword) {
        String[] keywordWords = keyword.split("\\s+");
        String[] descriptionWords = this.description.split("\\s+");

        double distance = 0;

        // for each keyword word, compute the smallest edit distance to any of the description word
        for (String kw : keywordWords) {
            double minDistanceToKw = Double.MAX_VALUE;

            for (String dw : descriptionWords) {
                if (dw.contains(kw)) {
                    minDistanceToKw = 0;
                    break;
                }

                minDistanceToKw = Math.min(minDistanceToKw, Utility.getEditDistance(dw, kw));
            }

            // sum the smallest edit distances for each keyword word up
            distance += minDistanceToKw;
        }

        return distance;
    }
}
