package utility;

import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.ToDo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The Parser class encapsulates a parser to interpret the user's command lines.
 */
public class Parser {
    /** A map to map user commands to specific actions of the chatbot */
    private final Map<String, Command> commandMap;

    public Parser() {
        this.commandMap = new HashMap<>();
        commandMap.put("bye", this::endConversation);
        commandMap.put("deadline", this::addDeadline);
        commandMap.put("delete", this::deleteTask);
        commandMap.put("event", this::addEvent);
        commandMap.put("help", this::listAllSupportedCommands);
        commandMap.put("list", this::listAllTasks);
        commandMap.put("mark", this::markTask);
        commandMap.put("todo", this::addTodo);
        commandMap.put("unmark", this::unmarkTask);
        commandMap.put("UNKNOWN_COMMAND", this::handleUnknownCommand);
    }

    /**
     * Ends the conversation.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param ui The user interface
     * @param storage The task storage
     * @return a boolean signal to end the conversation
     */
    private boolean endConversation(String[] args, TaskList tasks, Ui ui, Storage storage) {
        ui.printGoodbye();
        return false;
    }

    /**
     * Adds a ToDo task to the list of tasks.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param ui The user interface
     * @param storage The task storage
     * @return a boolean signal to continue the conversation
     */
    private boolean addTodo(String[] args, TaskList tasks, Ui ui, Storage storage) {
        if (args.length == 1) {
            ui.printError("Mirai does not understand a to-do task with no content...",
                            "You can tell Mirai your to-do task by the syntax 'todo [task]'!");
        } else {
            String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            ToDo toDo = new ToDo(description);

            tasks.addTask(toDo);
            storage.logNewTask(toDo);

            ui.printResponse(
                    "Got it. I've added this task:",
                    "  " + toDo,
                    ui.getNumberOfTasksString(tasks.getSize())
            );
        }

        return true;
    }

    public static LocalDateTime parseUserDateTime(String dateTime) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm")) // 31/01/2025 1559
                .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) // 31/01/2025 15:59
                .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yy HHmm")) // 31/01/25 1559
                .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")) // 31/01/25 15:59
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")) // 2025-01-31 1559
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) // 2025-01-31 15:59
                .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy")) // 31/01/2025
                .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yy")) // 31/01/25
                .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd")) // 2025-01-31
                .toFormatter(Locale.ENGLISH);

        return LocalDateTime.parse(dateTime, formatter);
    }

    /**
     * Adds a Deadline task to the list of tasks.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param ui The user interface
     * @param storage The task storage
     * @return a boolean signal to continue the conversation
     */
    private boolean addDeadline(String[] args, TaskList tasks, Ui ui, Storage storage) {
        if (args.length == 1) {
            ui.printError(
                    "Mirai does not understand a deadline with no content...",
                    "You can tell Mirai your deadline by the syntax 'deadline [task] /by [deadline]'!"
            );
            ui.printSupportedDateTimeFormats();
        } else {
            int byIndex = 1;

            while (byIndex < args.length && !args[byIndex].equals("/by")) {
                byIndex++;
            }

            if (byIndex == args.length) {
                ui.printError(
                        "You forgot to add your deadline...",
                        "You can tell Mirai your deadline by the syntax 'deadline [task] /by [deadline]'!"
                );
                ui.printSupportedDateTimeFormats();
                return true;
            }

            String description = String.join(" ", Arrays.copyOfRange(args, 1, byIndex));
            String deadline = String.join(" ", Arrays.copyOfRange(args, byIndex + 1, args.length));

            try {
                LocalDateTime deadlineTime = Parser.parseUserDateTime(deadline);
                Deadline task = new Deadline(description, deadlineTime);

                tasks.addTask(task);
                storage.logNewTask(task);

                ui.printResponse(
                        "Got it. I've added this task:",
                        "  " + task,
                        ui.getNumberOfTasksString(tasks.getSize())
                );
            } catch (DateTimeParseException e) {
                ui.printError(
                        "Mirai does not understand your deadline...",
                        "Please specify your deadline in one of the following formats:",
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

        return true;
    }

    /**
     * Adds an Event task to the list of tasks.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param ui The user interface
     * @param storage The task storage
     * @return a boolean signal to continue the conversation
     */
    private boolean addEvent(String[] args, TaskList tasks, Ui ui, Storage storage) {
        if (args.length == 1) {
            ui.printError(
                    "Mirai does not understand an event with no content...",
                    "You can tell Mirai your event by the syntax 'event [task] /from [start time] /to [end time]'!"
            );
            ui.printSupportedDateTimeFormats();
        } else {
            int fromIndex = 1;
            while (fromIndex < args.length && !args[fromIndex].equals("/from")) {
                fromIndex++;
            }

            if (fromIndex == args.length) {
                ui.printError(
                        "You forgot to specify your start time...",
                        "You can tell Mirai your event by the syntax 'event [task] /from [start time] /to [end time]'!"
                );
                ui.printSupportedDateTimeFormats();
                return true;
            }

            String description = String.join(" ", Arrays.copyOfRange(args, 1, fromIndex));

            int toIndex = fromIndex + 1;
            while (toIndex < args.length && !args[toIndex].equals("/to")) {
                toIndex++;
            }

            if (toIndex == args.length) {
                ui.printError(
                        "You forgot to specify your end time...",
                        "You can tell Mirai your event by the syntax 'event [task] /from [start time] /to [end time]'!"
                );
                ui.printSupportedDateTimeFormats();
                return true;
            }

            String startTimeString = String.join(" ", Arrays.copyOfRange(args, fromIndex + 1, toIndex));
            String endTimeString = String.join(" ", Arrays.copyOfRange(args, toIndex + 1, args.length));

            LocalDateTime startTime;

            try {
                startTime = Parser.parseUserDateTime(startTimeString);
            } catch (DateTimeParseException e) {
                ui.printError(
                        "Mirai does not understand your start time...",
                        "Please specify your start time in one of the following formats:",
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

                return true;
            }

            LocalDateTime endTime;

            try {
                endTime = Parser.parseUserDateTime(endTimeString);
            } catch (DateTimeParseException e) {
                ui.printError(
                        "Mirai does not understand your end time...",
                        "Please specify your end time in one of the following formats:",
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

                return true;
            }

            Event task = new Event(description, startTime, endTime);

            tasks.addTask(task);
            storage.logNewTask(task);

            ui.printResponse(
                    "Got it. I've added this task:",
                    "  " + task,
                    ui.getNumberOfTasksString(tasks.getSize())
            );
        }

        return true;
    }

    /**
     * Lists all tasks to the user.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param ui The user interface
     * @param storage The task storage
     * @return a boolean signal to continue the conversation
     */
    private boolean listAllTasks(String[] args, TaskList tasks, Ui ui, Storage storage) {
        String[] taskStrings = new String[tasks.getSize() + 1];

        taskStrings[0] = "Here are the tasks in your list:";
        for (int i = 0; i < tasks.getSize(); i++) {
            taskStrings[i + 1] = (i + 1) + "." + tasks.getTask(i).toString();
        }

        ui.printResponse(taskStrings);
        return true;
    }

    /**
     * Marks a task as done.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param ui The user interface
     * @param storage The task storage
     * @return a boolean signal to continue the conversation
     */
    private boolean markTask(String[] args, TaskList tasks, Ui ui, Storage storage) {
        int taskIndex = Integer.parseInt(args[1]);

        if (taskIndex < 1) {
            ui.printError(
                    "It looks like you have keyed in a non-positive index...",
                    "Mirai stores your tasks with positive indexes. Please specify a positive index!"
            );
            return true;
        }

        if (taskIndex > tasks.getSize()) {
            ui.printError(
                    "It looks like you have keyed in a too large index...",
                    "You are only having " + tasks.getSize() +
                            " tasks in your list. Please specify an index smaller than this!"
            );
            return true;
        }

        tasks.markTask(taskIndex - 1);
        storage.relogAllTasks(tasks.getTaskList());

        ui.printResponse(
                "Nice! I've marked this task as done:",
                "  " + tasks.getTask(taskIndex - 1).toString()
        );

        return true;
    }

    /**
     * Marks a task as not done.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param ui The user interface
     * @param storage The task storage
     * @return a boolean signal to continue the conversation
     */
    private boolean unmarkTask(String[] args, TaskList tasks, Ui ui, Storage storage) {
        int taskIndex = Integer.parseInt(args[1]);

        if (taskIndex < 1) {
            ui.printError(
                    "It looks like you have keyed in a non-positive index...",
                    "Mirai stores your tasks with positive indexes. Please specify a positive index!"
            );
            return true;
        }

        if (taskIndex > tasks.getSize()) {
            ui.printError(
                    "It looks like you have keyed in a too large index...",
                    "You are only having " + tasks.getSize() +
                            " tasks in your list. Please specify an index smaller than this!"
            );
            return true;
        }

        tasks.unmarkTask(taskIndex - 1);
        storage.relogAllTasks(tasks.getTaskList());

        ui.printResponse(
                "OK, I've marked this task as not done yet:",
                "  " + tasks.getTask(taskIndex - 1).toString()
        );

        return true;
    }

    /**
     * Informs the user that the command is undefined.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param ui The user interface
     * @param storage The task storage
     * @return a boolean signal to continue the conversation
     */
    private boolean handleUnknownCommand(String[] args, TaskList tasks, Ui ui, Storage storage) {
        ui.printError(
                "Sorry, Mirai does not understand what you mean...",
                "Please type 'help' to know what commands Mirai can understand!"
        );

        return true;
    }

    /**
     * Lists all commands supported by the chatbot.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param ui The user interface
     * @param storage The task storage
     * @return a boolean signal to continue the conversation
     */
    private boolean listAllSupportedCommands(String[] args, TaskList tasks, Ui ui, Storage storage) {
        String[] commands = new String[this.commandMap.size() + 1];

        commands[0] = "Mirai currently supports the following commands:";
        int index = 1;
        for (String keyword : this.commandMap.keySet()) {
            commands[index++] = keyword;
        }

        ui.printResponse(commands);
        return true;
    }

    /**
     * Removes a task at the specified index.
     * @param args The user command, which is already split (by space) into an array
     * @param tasks The list of tasks
     * @param ui The user interface
     * @param storage The task storage
     * @return a boolean signal to continue the conversation
     */
    private boolean deleteTask(String[] args, TaskList tasks, Ui ui, Storage storage) {
        int taskIndex = Integer.parseInt(args[1]);

        if (taskIndex < 1) {
            ui.printError(
                    "It looks like you have keyed in a non-positive index...",
                    "Mirai stores your tasks with positive indexes. Please specify a positive index!"
            );
            return true;
        }

        if (taskIndex > tasks.getSize()) {
            ui.printError(
                    "It looks like you have keyed in a too large index...",
                    "You are only having " + tasks.getSize() +
                            " tasks in your list. Please specify an index smaller than this!"
            );
            return true;
        }

        Task removedTask = tasks.getTask(taskIndex - 1);
        tasks.deleteTask(taskIndex - 1);
        storage.relogAllTasks(tasks.getTaskList());

        ui.printResponse(
                "Noted. I've removed this task:",
                "  " + removedTask.toString(),
                ui.getNumberOfTasksString(tasks.getSize())
        );

        return true;
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
