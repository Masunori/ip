import java.util.*;

public class Mirai {
    private final Scanner scanner;
    private final String greeting;
    private final String goodbye;
    private final List<Task> storage;

    private final Map<String, Command> commandMap;

    public Mirai() {
        this.scanner = new Scanner(System.in);
        this.greeting = "    ____________________________________________________________\n" +
                "     Hello! I'm Mirai, your beautiful and intelligent personal assistant!\n" +
                "     What can I do for you?\n" +
                "    ____________________________________________________________";
        this.goodbye = "    ____________________________________________________________\n" +
                "     Bye. Mirai hopes to see you again soon!\n" +
                "    ____________________________________________________________";
        this.storage = new ArrayList<>();

        this.commandMap = new HashMap<>();
        commandMap.put("bye", args -> endConversation());
        commandMap.put("deadline", this::addDeadline);
        commandMap.put("event", this::addEvent);
        commandMap.put("list", args -> printStorage());
        commandMap.put("mark", args -> markTask(Integer.parseInt(args[1]) - 1));
        commandMap.put("todo", this::addToDo);
        commandMap.put("unmark", args -> unmarkTask(Integer.parseInt(args[1]) - 1));
    }

    private String printNumberOfTasks() {
        return String.format("Now you have %d task%s in the list.\n",
                this.storage.size(),
                this.storage.isEmpty() ? "" : "s");
    }

    /**
     * Ends the conversation, return a signal to exit the conversation loop.
     * @return the signal to keep/exit the conversation loop
     */
    private boolean endConversation() {
        System.out.println(goodbye);
        return true;
    }

    /**
     * Add a to-do type task to the list of tasks, return a signal to continue the conversation loop.
     * @param command The user's command
     * @return the signal to keep/exit the conversation loop
     */
    private boolean addToDo(String... command) {
        String[] descriptionAsArray = Arrays.copyOfRange(command, 1, command.length);
        String description = String.join(" ", descriptionAsArray);
        ToDo task = new ToDo(description);

        this.storage.add(task);
        System.out.println("    ____________________________________________________________\n" +
                "     Got it. I've added this task:\n" +
                "       " + task + "\n" +
                "     " + this.printNumberOfTasks() +
                "    ____________________________________________________________");
        return false;
    }

    /**
     * Add a deadline type task to the list of tasks, return a signal to continue the conversation loop.
     * @param command The user's command
     * @return the signal to keep/exit the conversation loop
     */
    private boolean addDeadline(String... command) {
        int byIndex = 1;

        while (!command[byIndex].equals("/by")) {
            byIndex++;
        }

        String description = String.join(" ", Arrays.copyOfRange(command, 1, byIndex));
        String deadline = String.join(" ", Arrays.copyOfRange(command, byIndex + 1, command.length));

        Deadline task = new Deadline(description, deadline);

        this.storage.add(task);
        System.out.println("    ____________________________________________________________\n" +
                "     Got it. I've added this task:\n" +
                "       " + task + "\n" +
                "     " + this.printNumberOfTasks() +
                "    ____________________________________________________________");
        return false;
    }

    /**
     * Add an event type task to the list of tasks, return a signal to continue the conversation loop.
     * @param command The user's command
     * @return the signal to keep/exit the conversation loop
     */
    private boolean addEvent(String... command) {
        int fromIndex = 1;
        while (!command[fromIndex].equals("/from")) {
            fromIndex++;
        }

        String description = String.join(" ", Arrays.copyOfRange(command, 1, fromIndex));

        int toIndex = fromIndex + 1;
        while (!command[toIndex].equals("/to")) {
            toIndex++;
        }

        String startTime = String.join(" ", Arrays.copyOfRange(command, fromIndex + 1, toIndex));
        String endTime = String.join(" ", Arrays.copyOfRange(command, toIndex + 1, command.length));

        Event task = new Event(description, startTime, endTime);

        this.storage.add(task);
        System.out.println("    ____________________________________________________________\n" +
                "     Got it. I've added this task:\n" +
                "       " + task + "\n" +
                "     " + this.printNumberOfTasks() +
                "    ____________________________________________________________");
        return false;
    }

    /**
     * List all tasks stored, return a signal to continue the conversation loop.
     * @return the signal to keep/exit the conversation loop
     */
    private boolean printStorage() {
        System.out.println("    ____________________________________________________________");
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < this.storage.size(); i++) {
            System.out.println(String.format("     %d.%s", i + 1, this.storage.get(i)));
        }
        System.out.println("    ____________________________________________________________");
        return false;
    }

    /**
     * Mark a task with a specified index as done, return a signal to continue the conversation loop.
     * @param taskIndex The index of the task
     * @return the signal to keep/exit the conversation loop
     */
    private boolean markTask(int taskIndex) {
        Task task = this.storage.get(taskIndex);
        task.markAsDone();

        System.out.println("    ____________________________________________________________\n" +
                "     Nice! I've marked this task as done:\n" +
                "     " + task + "\n" +
                "    ____________________________________________________________");

        return false;
    }

    /**
     * Mark a task with a specified index as undone, return a signal to continue the conversation loop.
     * @param taskIndex The index of the task
     * @return the signal to keep/exit the conversation loop
     */
    private boolean unmarkTask(int taskIndex) {
        Task task = this.storage.get(taskIndex);
        task.markAsUndone();

        System.out.println("    ____________________________________________________________\n" +
                "     OK, I've marked this task as not done yet:\n" +
                "     " + task + "\n" +
                "    ____________________________________________________________");

        return false;
    }

    /**
     * Initiate a conversation with the chatbot.
     */
    public void startConversation() {
        System.out.println(greeting);
        boolean isEnd = false;

        while (!isEnd) {
            String command = scanner.nextLine();
            String[] commandAsArray = command.split("\\s+");

            if (this.commandMap.containsKey(commandAsArray[0])) {
                isEnd = this.commandMap.get(commandAsArray[0]).execute(commandAsArray);
            } else {
//                isEnd = this.addToList(command);
            }
        }
    }

    public static void main(String[] args) {
        Mirai mirai = new Mirai();
        mirai.startConversation();
    }
}
