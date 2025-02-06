package mirai.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;
import mirai.tasks.Deadline;
import mirai.tasks.Event;
import mirai.tasks.Task;
import mirai.tasks.ToDo;

/**
 * The Parser class encapsulates a parser to interpret the user's command lines.
 */
public class Parser {
    /** A map to map user commands to specific actions of the chatbot */
    private final Map<String, Command> commandMap;

    /**
     * Initialises a parser.
     */
    public Parser() {
        this.commandMap = new HashMap<>();
        commandMap.put("bye", this::endConversation);
        commandMap.put("deadline", this::addDeadline);
        commandMap.put("delete", this::deleteTask);
        commandMap.put("event", this::addEvent);
        commandMap.put("find", this::findTasks);
        commandMap.put("help", this::listAllSupportedCommands);
        commandMap.put("list", this::listAllTasks);
        commandMap.put("mark", this::markTask);
        commandMap.put("todo", this::addTodo);
        commandMap.put("unmark", this::unmarkTask);
        commandMap.put("UNKNOWN_COMMAND", this::handleUnknownCommand);
    }

    /**
     * Ends the conversation and closes the application after 2 seconds.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param storage The task storage
     * @return a goodbye message
     */
    private String endConversation(String[] args, TaskList tasks, Storage storage) {
        PauseTransition delayClosure = new PauseTransition(Duration.seconds(2));
        delayClosure.setOnFinished(event -> Platform.exit());
        delayClosure.play();

        return Message.GOODBYE;
    }

    /**
     * Adds a ToDo task to the list of tasks.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param storage The task storage
     * @return the message to tell the user the task addition is successful/failing
     */
    private String addTodo(String[] args, TaskList tasks, Storage storage) {
        if (args.length == 1) {
            return Message.ERROR + "Mirai does not understand a to-do task with no content...\n"
                    + "You can tell Mirai your to-do task by the syntax 'todo [task]'!\n";
        }

        String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        ToDo toDo = new ToDo(description);

        tasks.addTask(toDo);
        storage.logNewTask(toDo);

        return "Got it. I've added this task:\n"
                + "  " + toDo + "\n"
                + Message.getNumOfTasks(tasks.getSize());
    }

    /**
     * Parses a user's date-time string.
     * @param dateTime The user's date-time string
     * @return a LocalDateTime representation of the user's date-time
     */
    private static LocalDateTime parseUserDateTime(String dateTime) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm")) // 31/01/2025 1559
                .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) // 31/01/2025 15:59
                .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yy HHmm")) // 31/01/25 1559
                .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")) // 31/01/25 15:59
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")) // 2025-01-31 1559
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) // 2025-01-31 15:59
                .toFormatter(Locale.ENGLISH);

        return LocalDateTime.parse(dateTime, formatter);
    }

    /**
     * Adds a Deadline task to the list of tasks.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param storage The task storage
     * @return the message to tell the user the task addition is successful/failing
     */
    private String addDeadline(String[] args, TaskList tasks, Storage storage) {
        if (args.length == 1) {
            return Message.ERROR + "Mirai does not understand a deadline with no content...\n"
                + "You can tell Mirai your deadline by the syntax 'deadline [task] /by [deadline]'!\n"
                + Message.SUPPORTED_DATETIME_FORMATS;
        }

        int byIndex = 1;

        while (byIndex < args.length && !args[byIndex].equals("/by")) {
            byIndex++;
        }

        if (byIndex == args.length) {
            return Message.ERROR + "You forgot to add your deadline...\n"
                    + "You can tell Mirai your deadline by the syntax 'deadline [task] /by [deadline]'!\n"
                    + Message.SUPPORTED_DATETIME_FORMATS;
        }

        String description = String.join(" ", Arrays.copyOfRange(args, 1, byIndex));
        String deadline = String.join(" ", Arrays.copyOfRange(args, byIndex + 1, args.length));

        try {
            LocalDateTime deadlineTime = Parser.parseUserDateTime(deadline);
            Deadline task = new Deadline(description, deadlineTime);

            tasks.addTask(task);
            storage.logNewTask(task);

            return "Got it. I've added this task:\n"
                    + "  " + task + "\n"
                    + Message.getNumOfTasks(tasks.getSize());

        } catch (DateTimeParseException e) {
            return Message.ERROR + "Mirai does not understand your deadline...\n"
                    + Message.SUPPORTED_DATETIME_FORMATS;
        }
    }

    /**
     * Adds an Event task to the list of tasks.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param storage The task storage
     * @return the message to tell the user the task addition is successful/failing
     */
    private String addEvent(String[] args, TaskList tasks, Storage storage) {
        if (args.length == 1) {
            return Message.ERROR + "Mirai does not understand an event with no content...\n"
                    + "You can tell Mirai your event by the syntax 'event [task] /from [start time] /to [end time]'!\n"
                    + Message.SUPPORTED_DATETIME_FORMATS;
        }

        int fromIndex = 1;
        while (fromIndex < args.length && !args[fromIndex].equals("/from")) {
            fromIndex++;
        }

        if (fromIndex == args.length) {
            return Message.ERROR + "You forgot to specify your start time...\n"
                    + "You can tell Mirai your event by the syntax 'event [task] /from [start time] /to [end time]'!\n"
                    + Message.SUPPORTED_DATETIME_FORMATS;
        }

        String description = String.join(" ", Arrays.copyOfRange(args, 1, fromIndex));

        int toIndex = fromIndex + 1;
        while (toIndex < args.length && !args[toIndex].equals("/to")) {
            toIndex++;
        }

        if (toIndex == args.length) {
            return "You forgot to specify your end time...\n"
                    + "You can tell Mirai your event by the syntax 'event [task] /from [start time] /to [end time]'!\n"
                    + Message.SUPPORTED_DATETIME_FORMATS;
        }

        String startTimeString = String.join(" ", Arrays.copyOfRange(args, fromIndex + 1, toIndex));
        String endTimeString = String.join(" ", Arrays.copyOfRange(args, toIndex + 1, args.length));

        LocalDateTime startTime;

        try {
            startTime = Parser.parseUserDateTime(startTimeString);
        } catch (DateTimeParseException e) {
            return Message.ERROR + "Mirai does not understand your start time...\n"
                    + Message.SUPPORTED_DATETIME_FORMATS;
        }

        LocalDateTime endTime;

        try {
            endTime = Parser.parseUserDateTime(endTimeString);
        } catch (DateTimeParseException e) {
            return Message.ERROR + "Mirai does not understand your end time...\n"
                    + Message.SUPPORTED_DATETIME_FORMATS;
        }

        Event task = new Event(description, startTime, endTime);

        tasks.addTask(task);
        storage.logNewTask(task);

        return "Got it. I've added this task:\n"
                + "  " + task;
    }

    /**
     * Lists all tasks to the user.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param storage The task storage
     * @return the message string of all user's tasks
     */
    private String listAllTasks(String[] args, TaskList tasks, Storage storage) {
        StringBuilder result = new StringBuilder("Here are the tasks in your list:\n");

        for (int i = 0; i < tasks.getSize(); i++) {
            result.append(i + 1).append('.').append(tasks.getTask(i).toString()).append('\n');
        }

        return result.toString();
    }

    /**
     * Marks a task as done.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param storage The task storage
     * @return the message showing user the marked task, or an error message
     */
    private String markTask(String[] args, TaskList tasks, Storage storage) {
        int taskIndex = Integer.parseInt(args[1]);

        if (taskIndex < 1) {
            return Message.ERROR + "It looks like you have keyed in a non-positive index...\n"
                    + "Mirai stores your tasks with positive indexes. Please specify a positive index!";
        }

        if (taskIndex > tasks.getSize()) {
            return Message.ERROR + "It looks like you have keyed in a too large index...\n"
                    + "You are only having " + tasks.getSize()
                    + " task(s) in your list. Please specify an index smaller than this!";
        }

        tasks.markTask(taskIndex - 1);
        storage.relogAllTasks(tasks.getTaskList());

        return "Nice! I've marked this task as done:\n"
                + "  " + tasks.getTask(taskIndex - 1).toString();
    }

    /**
     * Marks a task as not done.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param storage The task storage
     * @return the message showing user the unmarked task, or an error message
     */
    private String unmarkTask(String[] args, TaskList tasks, Storage storage) {
        int taskIndex = Integer.parseInt(args[1]);

        if (taskIndex < 1) {
            return Message.ERROR + "It looks like you have keyed in a non-positive index...\n"
                    + "Mirai stores your tasks with positive indexes. Please specify a positive index!";
        }

        if (taskIndex > tasks.getSize()) {
            return Message.ERROR + "It looks like you have keyed in a too large index...\n"
                    + "You are only having " + tasks.getSize()
                    + " task(s) in your list. Please specify an index smaller than this!";
        }

        tasks.unmarkTask(taskIndex - 1);
        storage.relogAllTasks(tasks.getTaskList());

        return "OK, I've marked this task as not done yet:\n"
                + "  " + tasks.getTask(taskIndex - 1).toString();
    }

    /**
     * Informs the user that the command is undefined.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param storage The task storage
     * @return an error message telling the user that the command is undefined
     */
    private String handleUnknownCommand(String[] args, TaskList tasks, Storage storage) {
        return "Sorry, Mirai does not understand what you mean...\n"
                + "Please type 'help' to know what commands Mirai can understand!";
    }

    /**
     * Lists all commands supported by the chatbot.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param storage The task storage
     * @return a message string to list all supported commands
     */
    private String listAllSupportedCommands(String[] args, TaskList tasks, Storage storage) {
        StringBuilder message = new StringBuilder("Mirai currently supports the following commands:\n");

        for (String keyword : this.commandMap.keySet()) {
            if (!keyword.equals("UNKNOWN_COMMAND")) {
                message.append(keyword).append('\n');
            }
        }

        return message.toString();
    }

    /**
     * Removes a task at the specified index.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param storage The task storage
     * @return a message to tell the user the deleted task, or an error message
     */
    private String deleteTask(String[] args, TaskList tasks, Storage storage) {
        int taskIndex = Integer.parseInt(args[1]);

        if (taskIndex < 1) {
            return Message.ERROR + "It looks like you have keyed in a non-positive index...\n"
                    + "Mirai stores your tasks with positive indexes. Please specify a positive index!";
        }

        if (taskIndex > tasks.getSize()) {
            return Message.ERROR + "It looks like you have keyed in a too large index...\n"
                    + "You are only having " + tasks.getSize()
                    + " task(s) in your list. Please specify an index smaller than this!";
        }

        Task removedTask = tasks.getTask(taskIndex - 1);
        tasks.deleteTask(taskIndex - 1);
        storage.relogAllTasks(tasks.getTaskList());

        return "Noted. I've removed this task:\n"
                + "  " + removedTask.toString()
                + Message.getNumOfTasks(tasks.getSize());
    }

    /**
     * Displays to the user all tasks matching a given keyword.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param storage The task storage
     * @return a message showing the user all tasks with the matching keyword
     */
    public String findTasks(String[] args, TaskList tasks, Storage storage) {
        String keyword = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        TaskList filteredTaskList = tasks.filterBasedOnKeyword(keyword);

        StringBuilder message = new StringBuilder("Here are the matching tasks in your list:\n");

        for (int i = 0; i < filteredTaskList.getSize(); i++) {
            message.append(i + 1).append('.').append(filteredTaskList.getTask(i).toString()).append('\n');
        }

        return message.toString();
    }

    /**
     * Parses the user's command line and return the corresponding executable command.
     * @param command The user's command line
     * @return an executable Command
     */
    public Command parse(String command) {
        String[] args = command.split("\\s+");
        String keyword = args[0];

        if (this.commandMap.containsKey(keyword)) {
            return this.commandMap.get(keyword);
        } else {
            return this.commandMap.get("UNKNOWN_COMMAND");
        }
    }
}
