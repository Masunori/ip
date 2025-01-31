package mirai.ui;

import mirai.utility.Command;
import mirai.utility.Parser;
import mirai.utility.Storage;
import mirai.utility.TaskList;
import mirai.utility.Ui;

public class Mirai {
    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;
    private final Parser parser;

    public Mirai(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.parser = new Parser();

        try {
            this.tasks = new TaskList(storage.load());
        } catch (Exception e) {
            this.ui.printLoadingError();
            this.tasks = new TaskList();
        }
    }

    /**
     * Initiates a conversation with the chatbot.
     */
    public void startConversation() {
        this.ui.printGreeting();
        boolean isContinuing = true;

        while (isContinuing) {
            String commandLine = ui.readCommand();
            Command command = this.parser.parse(commandLine);
            isContinuing = command.execute(commandLine.split("\\s+"), this.tasks, this.ui, this.storage);
        }
    }

    public static void main(String[] args) {
        Mirai mirai = new Mirai("./data/mirai.txt");
        mirai.startConversation();
    }
}
