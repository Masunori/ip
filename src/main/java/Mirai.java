import java.util.Scanner;

public class Mirai {
    private static void echo(String text) {
        System.out.println("    ____________________________________________________________\n" +
                "     " + text + "\n" +
                "    ____________________________________________________________");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String greeting = "    ____________________________________________________________\n" +
                "     Hello! I'm Mirai, an artificial life form from the future, and your personal assistant!\n" +
                "     What can I do for you?\n" +
                "    ____________________________________________________________\n";

        String goodbye = "    ____________________________________________________________\n" +
                "     Bye. Hope to see you again soon!\n" +
                "    ____________________________________________________________";

        System.out.println(greeting);

        while (true) {
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                System.out.println(goodbye);
                break;
            }
            Mirai.echo(command);
        }
    }
}
