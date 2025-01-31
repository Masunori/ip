package mirai.utility;

/**
 * The Command interface encapsulates a user command.<br><br>
 *
 * <strong>Note:</strong> This is a functional interface whose functional method is
 * <code>execute(String[], TaskList, Ui, Storage)</code>
 */
@FunctionalInterface
public interface Command {
    /**
     * Executes the command. The execution can interact with the list of mirai.tasks, the UI, and the storage.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of mirai.tasks
     * @param ui The user interface
     * @param storage The task storage
     * @return a boolean signal, where <code>true</code> means to continue the conversation,
     *     and <code>false</code> means to end the conversation
     */
    public boolean execute(String[] args, TaskList tasks, Ui ui, Storage storage);
}
