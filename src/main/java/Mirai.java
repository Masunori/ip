import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.ToDo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Arrays;

public class Mirai {
    /** A scanner to continuously parse user input */
    private final Scanner scanner;

    /** The message the user will see upon initializing a conversation */
    private static final String greeting = "    ____________________________________________________________\n" +
            "     Hello! I'm Mirai, your beautiful and intelligent personal assistant!\n" +
            "     As a small token of appreciation, I have created a file to store your current tasks!\n" +
            "     You can access it at 'ip/data/mirai.txt'.\n" +
            "     Now then, what can I do for you?\n" +
            "    ____________________________________________________________";

    /** The message the user will see upon ending the conversation */
    private static final String goodbye = "    ____________________________________________________________\n" +
            "     Bye. Mirai hopes to see you again soon!\n" +
            "    ____________________________________________________________";

    /** The list of tasks of the user. */
    private final List<Task> taskList;

    /** A map to map user commands to specific actions of the chatbot */
    private final Map<String, Command> commandMap;

    /** The file to store all user's tasks, which is at the address 'ip/data/mirai.txt'.<br>
     * When the user initiates a conversation, this file (and the folder) will be silently generated. */
    private File taskFile;

    public Mirai() {
        this.scanner = new Scanner(System.in);
        this.taskList = new ArrayList<>();

        this.commandMap = new HashMap<>();
        commandMap.put("bye", args -> endConversation());
        commandMap.put("deadline", this::addDeadline);
        commandMap.put("delete", args -> deleteTask(Integer.parseInt(args[1]) - 1));
        commandMap.put("help", args -> listAllSupportedCommands());
        commandMap.put("event", this::addEvent);
        commandMap.put("list", args -> printStorage());
        commandMap.put("mark", args -> markTask(Integer.parseInt(args[1]) - 1));
        commandMap.put("todo", this::addToDo);
        commandMap.put("unmark", args -> unmarkTask(Integer.parseInt(args[1]) - 1));
    }

    /**
     * Adds a task to the task list and logs it into a file with the address './data/mirai.txt'.<br>
     * This method will silently create the required directory and file if they do not exist.
     *
     * @param task The task to be added and logged.
     */
    private void addAndLogTask(Task task) {
        this.taskList.add(task);

        try (FileWriter writer = new FileWriter(this.taskFile, true)) {
            writer.write(task.toNoteForm() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Re-logs all tasks into the file at the address './data/mirai.txt'.<br>
     * This method will silently create the required directory and file if they do not exist.
     */
    private void relogTasks() {
        // delete all file content
        try (FileWriter contentDeleter = new FileWriter(this.taskFile)) {
            contentDeleter.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // rewrite all tasks into the file
        try (FileWriter writer = new FileWriter(this.taskFile)) {
            for (Task task : this.taskList) {
                writer.write(task.toNoteForm() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a user-readable string that informs the user the number of tasks in the list.
     *
     * @return a string informing the user about the number of tasks in the list
     */
    private String printNumberOfTasks() {
        return String.format("Now you have %d task%s in the list.\n",
                this.taskList.size(),
                this.taskList.size() <= 1 ? "" : "s");
    }

    /**
     * Ends the conversation, return a signal to exit the conversation loop.
     *
     * @return the signal to keep/exit the conversation loop
     */
    private boolean endConversation() {
        System.out.println(goodbye);
        return true;
    }

    /**
     * Adds a to-do type task to the list of tasks, return a signal to continue the conversation loop.
     *
     * @param command The user's command
     * @return the signal to keep/exit the conversation loop
     */
    private boolean addToDo(String... command) {
        if (command.length == 1) {
            System.out.println("    ____________________________________________________________\n" +
                    "     OOPS!!! Mirai does not understand a to-do task with no content...\n" +
                    "     You can tell Mirai your to-do task by the syntax 'todo [task]'!\n" +
                    "    ____________________________________________________________");
        } else {
            String[] descriptionAsArray = Arrays.copyOfRange(command, 1, command.length);
            String description = String.join(" ", descriptionAsArray);
            ToDo task = new ToDo(description);

            this.addAndLogTask(task);
            System.out.println("    ____________________________________________________________\n" +
                    "     Got it. I've added this task:\n" +
                    "       " + task + "\n" +
                    "     " + this.printNumberOfTasks() +
                    "    ____________________________________________________________");
        }

        return false;
    }

    /**
     * Adds a deadline type task to the list of tasks, return a signal to continue the conversation loop.
     * @param command The user's command
     * @return the signal to keep/exit the conversation loop
     */
    private boolean addDeadline(String... command) {
        if (command.length == 1) {
            System.out.println("    ____________________________________________________________\n" +
                    "     OOPS!!! Mirai does not understand a deadline with no content...\n" +
                    "     You can tell Mirai your deadline by the syntax 'deadline [task] /by [deadline]'!\n" +
                    "    ____________________________________________________________");
        } else {
            int byIndex = 1;

            while (byIndex < command.length && !command[byIndex].equals("/by")) {
                byIndex++;
            }

            if (byIndex == command.length) {
                System.out.println("    ____________________________________________________________\n" +
                        "     OOPS!!! You forgot to add your deadline...\n" +
                        "     You can tell Mirai your deadline by the syntax 'deadline [task] /by [deadline]'!\n" +
                        "    ____________________________________________________________");

                return false;
            }

            String description = String.join(" ", Arrays.copyOfRange(command, 1, byIndex));
            String deadline = String.join(" ", Arrays.copyOfRange(command, byIndex + 1, command.length));

            Deadline task = new Deadline(description, deadline);

            this.addAndLogTask(task);
            System.out.println("    ____________________________________________________________\n" +
                    "     Got it. I've added this task:\n" +
                    "       " + task + "\n" +
                    "     " + this.printNumberOfTasks() +
                    "    ____________________________________________________________");
        }

        return false;
    }

    /**
     * Adds an event type task to the list of tasks, return a signal to continue the conversation loop.
     * @param command The user's command
     * @return the signal to keep/exit the conversation loop
     */
    private boolean addEvent(String... command) {
        if (command.length == 1) {
            System.out.println("    ____________________________________________________________\n" +
                    "     OOPS!!! Mirai does not understand an event with no content...\n" +
                    "     You can tell Mirai your event by the syntax 'event [task] /from [start time] /to end time'!\n" +
                    "    ____________________________________________________________");
        } else {
            int fromIndex = 1;
            while (fromIndex < command.length && !command[fromIndex].equals("/from")) {
                fromIndex++;
            }

            if (fromIndex == command.length) {
                System.out.println("    ____________________________________________________________\n" +
                        "     OOPS!!! You forgot to specify your start time...\n" +
                        "     You can tell Mirai your event by the syntax 'event [task] /from [start time] /to end time'!\n" +
                        "    ____________________________________________________________");

                return false;
            }

            String description = String.join(" ", Arrays.copyOfRange(command, 1, fromIndex));

            int toIndex = fromIndex + 1;
            while (toIndex < command.length && !command[toIndex].equals("/to")) {
                toIndex++;
            }

            if (toIndex == command.length) {
                System.out.println("    ____________________________________________________________\n" +
                        "     OOPS!!! You forgot to specify your end time...\n" +
                        "     You can tell Mirai your event by the syntax 'event [task] /from [start time] /to end time'!\n" +
                        "    ____________________________________________________________");

                return false;
            }

            String startTime = String.join(" ", Arrays.copyOfRange(command, fromIndex + 1, toIndex));
            String endTime = String.join(" ", Arrays.copyOfRange(command, toIndex + 1, command.length));

            Event task = new Event(description, startTime, endTime);

            this.addAndLogTask(task);
            System.out.println("    ____________________________________________________________\n" +
                    "     Got it. I've added this task:\n" +
                    "       " + task + "\n" +
                    "     " + this.printNumberOfTasks() +
                    "    ____________________________________________________________");
        }

        return false;
    }

    /**
     * Lists all tasks stored, return a signal to continue the conversation loop.
     * @return the signal to keep/exit the conversation loop
     */
    private boolean printStorage() {
        System.out.println("    ____________________________________________________________");
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < this.taskList.size(); i++) {
            System.out.println(String.format("     %d.%s", i + 1, this.taskList.get(i)));
        }
        System.out.println("    ____________________________________________________________");
        return false;
    }

    /**
     * Marks a task with a specified index as done, return a signal to continue the conversation loop.
     * @param taskIndex The index of the task
     * @return the signal to keep/exit the conversation loop
     */
    private boolean markTask(int taskIndex) {
        if (taskIndex < 0) {
            System.out.println("    ____________________________________________________________\n" +
                    "     OOPS!!! It looks like you have keyed in a non-positive index...\n" +
                    "     Mirai stores your tasks with positive indexes. Please specify a positive index!\n" +
                    "    ____________________________________________________________");

            return false;
        }

        if (taskIndex >= this.taskList.size()) {
            System.out.println("    ____________________________________________________________\n" +
                    "     OOPS!!! It looks like you have keyed in a too large index...\n" +
                    "     You are only having " + this.taskList.size() +
                    " tasks in your list. Please specify an index smaller than this!\n" +
                    "    ____________________________________________________________");

            return false;
        }

        Task task = this.taskList.get(taskIndex);
        task.markAsDone();

        this.relogTasks();

        System.out.println("    ____________________________________________________________\n" +
                "     Nice! I've marked this task as done:\n" +
                "       " + task + "\n" +
                "    ____________________________________________________________");

        return false;
    }

    /**
     * Marks a task with a specified index as undone, return a signal to continue the conversation loop.
     * @param taskIndex The index of the task
     * @return the signal to keep/exit the conversation loop
     */
    private boolean unmarkTask(int taskIndex) {
        if (taskIndex < 0) {
            System.out.println("    ____________________________________________________________\n" +
                    "     OOPS!!! It looks like you have keyed in a non-positive index...\n" +
                    "     Mirai stores your tasks with positive indexes. Please specify a positive index!\n" +
                    "    ____________________________________________________________");

            return false;
        }

        if (taskIndex >= this.taskList.size()) {
            System.out.println("    ____________________________________________________________\n" +
                    "     OOPS!!! It looks like you have keyed in a too large index...\n" +
                    "     You are only having " + this.taskList.size() +
                    " tasks in your list. Please specify an index smaller than this!\n" +
                    "    ____________________________________________________________");

            return false;
        }

        Task task = this.taskList.get(taskIndex);
        task.markAsUndone();

        this.relogTasks();

        System.out.println("    ____________________________________________________________\n" +
                "     OK, I've marked this task as not done yet:\n" +
                "     " + task + "\n" +
                "    ____________________________________________________________");

        return false;
    }

    /**
     * Warns the user that their command is not supported, return a signal to continue the conversation loop.
     * @return the signal to keep/exit the conversation loop
     */
    private boolean handleUnknownCommand() {
        System.out.println("    ____________________________________________________________\n" +
                "     OOPS!!! Sorry, Mirai does not understand what you mean...\n" +
                "     Please type 'help' to know what commands Mirai can understand!\n" +
                "    ____________________________________________________________");

        return false;
    }

    /**
     * Lists all commands supported by the chatbot, return a signal to continue the conversation loop.
     * @return the signal to keep/exit the conversation loop
     */
    private boolean listAllSupportedCommands() {
        System.out.println("    ____________________________________________________________\n" +
                "     Mirai currently supports the following command");

        for (String command : this.commandMap.keySet()) {
            System.out.println("     >> " + command);
        }

        System.out.println("    ____________________________________________________________");
        return false;
    }

    /**
     * Removes a task at the specified index from the list, return a signal to continue the conversation loop.
     * @param taskIndex the index of the task
     * @return the signal to keep/exit the conversation loop
     */
    private boolean deleteTask(int taskIndex) {
        if (taskIndex < 0) {
            System.out.println("    ____________________________________________________________\n" +
                    "     OOPS!!! It looks like you have keyed in a non-positive index...\n" +
                    "     Mirai stores your tasks with positive indexes. Please specify a positive index!\n" +
                    "    ____________________________________________________________");

            return false;
        }

        if (taskIndex >= this.taskList.size()) {
            System.out.println("    ____________________________________________________________\n" +
                    "     OOPS!!! It looks like you have keyed in a too large index...\n" +
                    "     You are only having " + this.taskList.size() +
                    " tasks in your list. Please specify an index smaller than this!\n" +
                    "    ____________________________________________________________");

            return false;
        }

        Task task = this.taskList.get(taskIndex);
        this.taskList.remove(taskIndex);

        this.relogTasks();

        System.out.println("    ____________________________________________________________\n" +
                "     Noted. I've removed this task:\n" +
                "       " + task + "\n" +
                "     " + this.printNumberOfTasks() +
                "    ____________________________________________________________");

        return false;
    }

    /**
     * Initiates a conversation with the chatbot.
     */
    public void startConversation() {
        this.taskFile = new File("./data/mirai.txt");
        this.taskFile.getParentFile().mkdirs();
        try {
            this.taskFile.createNewFile();
        } catch (IOException e) {
            System.out.println("    ____________________________________________________________\n" +
                    "     OOPS!!! Mirai cannot create a file to store your tasks...\n" +
                    "    ____________________________________________________________");
            e.printStackTrace();
        }

        System.out.println(greeting);
        boolean isEnd = false;

        while (!isEnd) {
            String command = scanner.nextLine();
            String[] commandAsArray = command.split("\\s+");

            if (this.commandMap.containsKey(commandAsArray[0])) {
                isEnd = this.commandMap.get(commandAsArray[0]).execute(commandAsArray);
            } else {
                isEnd = this.handleUnknownCommand();
            }
        }
    }

    public static void main(String[] args) {
        Mirai mirai = new Mirai();
        mirai.startConversation();
    }
}
