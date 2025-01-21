import java.util.Scanner;

public class Mirai {
    private final Scanner scanner;
    private final String greeting;
    private final String goodbye;

    public Mirai() {
        this.scanner = new Scanner(System.in);
        this.greeting = "    ____________________________________________________________\n" +
                "     Hello! I'm Mirai, your beautiful and intelligent personal assistant!\n" +
                "     What can I do for you?\n" +
                "    ____________________________________________________________";
        this.goodbye = "    ____________________________________________________________\n" +
                "     Bye. Mirai hopes to see you again soon!\n" +
                "    ____________________________________________________________";
    }

    private void echo(String text) {
        System.out.println("    ____________________________________________________________\n" +
                "     " + text + "\n" +
                "    ____________________________________________________________");
    }

    public void startConversation() {

        System.out.println(greeting);

        while (true) {
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                System.out.println(goodbye);
                break;
            }
            this.echo(command);
        }
    }

    public static void main(String[] args) {
        Mirai mirai = new Mirai();
        mirai.startConversation();
    }
}
