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
        commandMap.put("list", args -> printStorage());
        commandMap.put("mark", args -> markTask(Integer.parseInt(args[1]) - 1));
        commandMap.put("unmark", args -> unmarkTask(Integer.parseInt(args[1]) - 1));
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
     * Add the task to the list of tasks, return a signal to continue the conversation loop.
     * @param text The description of the task
     * @return the signal to keep/exit the conversation loop
     */
    private boolean addToList(String text) {
        Task task = new Task(text);
        this.storage.add(task);
        System.out.println("    ____________________________________________________________\n" +
                "     added: " + task + "\n" +
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
                isEnd = this.addToList(command);
            }
        }
    }

    public static void main(String[] args) {
        Mirai mirai = new Mirai();
        mirai.startConversation();
    }
}
