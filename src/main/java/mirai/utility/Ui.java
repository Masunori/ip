package mirai.utility;

import java.util.Scanner;

/**
 * The Ui class encapsulates the user interface. The user interacts with the chatbot only at this point.
 */
public class Ui {
    private final Scanner scanner;

    /** The message the user will see upon initializing a conversation */
    private final String greeting = "     Hello! I'm Mirai, your beautiful and intelligent personal assistant!\n"
            + "     Now then, what can I do for you?";

    /** The message the user will see upon ending the conversation */
    private final String goodbye = "     Bye. Mirai hopes to see you again soon!";

    /**
     * Initialises a user interface.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays a line to the user.
     */
    public void printLine() {
        System.out.println("    ____________________________________________________________");
    }

    /**
     * Displays each message on a new line to the user.
     * @param messages The messages
     */
    public void printResponse(String... messages) {
        this.printLine();
        for (String message : messages) {
            System.out.println("     " + message);
        }
        this.printLine();
    }

    /**
     * Displays the greeting message to the user.
     */
    public void printGreeting() {
        this.printLine();
        System.out.println(greeting);
        this.printLine();
    }

    /**
     * Displays the goodbye message to the user.
     */
    public void printGoodbye() {
        this.printLine();
        System.out.println(goodbye);
        this.printLine();
        this.scanner.close();
    }

    /**
     * Returns a string to tell the user the number of mirai.tasks in the task list.
     * @param numberOfTasks The number of mirai.tasks
     * @return a string
     */
    public String getNumberOfTasksString(int numberOfTasks) {
        return String.format("Now you have %d task%s in the list.",
                numberOfTasks,
                numberOfTasks <= 1 ? "" : "s");
    }

    /**
     * Read the next line from the user.
     * @return The command the user enters
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Shows the loading error message to the user.
     */
    public void printLoadingError() {
        this.printLine();
        System.out.println("     OOPS!!! Mirai cannot load data from your storage...");
        System.out.println("     Mirai will create an empty task list.");
        this.printLine();
    }

    /**
     * Displays the error message to the user.
     */
    public void printError(String... message) {
        this.printLine();
        System.out.println("     OOPS!!! " + message[0]);
        for (int i = 1; i < message.length; i++) {
            System.out.println("     " + message[i]);
        }
        this.printLine();
    }

    /**
     * Displays to the user the list of supported date-time formats for date-related commands.
     */
    public void printSupportedDateTimeFormats() {
        this.printResponse(
                "Mirai supports the following date-time formats:",
                ">>> DD/MM/YYYY HHmm, such as 31/01/2025 1559",
                ">>> DD/MM/YYYY HH:mm, such as 31/01/2025 15:59",
                ">>> DD/MM/YY HHmm, such as 31/01/25 1559",
                ">>> DD/MM/YY HH:mm, such as 31/01/25 15:59",
                ">>> YYYY-MM-DD HHmm, such as 2025-01-31 1559",
                ">>> YYYY-MM-DD HH:mm, such as 2025-01-31 15:59",
                ">>> DD/MM/YY, such as 31/01/25",
                ">>> DD/MM/YY, such as 31/01/25",
                ">>> YYYY-MM-DD HHmm, such as 2025-01-31",
                "Note that if time is not provided, Mirai will assume 00:00!"
        );
    }
}
