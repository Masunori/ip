import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Mirai {
    private final Scanner scanner;
    private final String greeting;
    private final String goodbye;
    private final List<String> storage;

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
    }

    private void addToList(String text) {
        this.storage.add(text);
        System.out.println("    ____________________________________________________________\n" +
                "     added: " + text + "\n" +
                "    ____________________________________________________________");
    }

    private void printStorage() {
        System.out.println("    ____________________________________________________________");
        for (int i = 0; i < this.storage.size(); i++) {
            System.out.println(String.format("     %d. %s", i + 1, this.storage.get(i)));
        }
        System.out.println("    ____________________________________________________________");
    }

    public void startConversation() {
        System.out.println(greeting);

        while (true) {
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                System.out.println(goodbye);
                break;
            }

            if (command.equals("list")) {
                this.printStorage();
            } else {
                this.addToList(command);
            }
        }
    }

    public static void main(String[] args) {
        Mirai mirai = new Mirai();
        mirai.startConversation();
    }
}
