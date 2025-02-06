package mirai.utility;

public class Message {
    public static final String GREETING =
            "Hello! I'm Mirai, your beautiful and intelligent personal assistant!\n"
            + "Now then, what can I do for you?";

    public static final String GOODBYE =
            "Bye. Mirai hopes to see you again soon!\n"
            + "Application closing...";

    public static final String SUPPORTED_DATETIME_FORMATS =
            "Mirai supports the following date-time formats:\n"
            + ">>> DD/MM/YYYY HHmm, such as 31/01/2025 1559\n"
            + ">>> DD/MM/YYYY HH:mm, such as 31/01/2025 15:59\n"
            + ">>> DD/MM/YY HHmm, such as 31/01/25 1559\n"
            + ">>> DD/MM/YY HH:mm, such as 31/01/25 15:59\n"
            + ">>> YYYY-MM-DD HHmm, such as 2025-01-31 1559\n"
            + ">>> YYYY-MM-DD HH:mm, such as 2025-01-31 15:59";

    public static String getNumOfTasks(int numOfTasks) {
        return String.format("Now you have %d task%s in the list",
                numOfTasks,
                numOfTasks <= 1 ? "" : "s");
    }

    public static final String STORAGE_FILE_CREATION_ERROR =
            "OOPS!!! Mirai cannot load data from your storage...\n"
            + "Mirai will create an empty task list.";

    public static final String ERROR = "OOPS!!! ";
}
