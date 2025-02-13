package mirai.utility;

/**
 * The Command interface encapsulates a user command.<br><br>
 *
 * <strong>Note:</strong> This is a functional interface whose functional method is
 * <code>execute(String[], TaskList, Storage)</code>
 */
@FunctionalInterface
public interface Command {
    /**
     * Executes the command. The execution can interact with the list of tasks and the storage.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param storage The task storage
     * @return Mirai's response, based on the user's command specified by args
     */
    public String execute(String[] args, TaskList tasks, Storage storage);
}
