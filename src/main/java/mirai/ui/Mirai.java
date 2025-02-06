package mirai.ui;

import mirai.utility.Command;
import mirai.utility.Parser;
import mirai.utility.Storage;
import mirai.utility.TaskList;
import mirai.utility.Ui;

/**
 * The Mirai class encapsulates a chatbot named Mirai.
 */
public class Mirai {
    private final Storage storage;
    private TaskList tasks;
    private final Parser parser;

    /**
     * Initialises the chatbot.
     * @param filePath The path of the file to store the list of tasks. If the file does not exist, silently create one.
     */
    public Mirai(String filePath) {
        this.storage = new Storage(filePath);
        this.parser = new Parser();

        try {
            this.tasks = new TaskList(storage.load());
        } catch (Exception e) {
            this.tasks = new TaskList();
        }
    }

    /**
     * Get a response from Mirai based on the user's text message.
     * @param commandLine The user's command
     * @return Mirai's response
     */
    public String getResponse(String commandLine) {
        Command command = this.parser.parse(commandLine);
        return command.execute(commandLine.split("\\s+"), this.tasks, this.storage);
    }
}
