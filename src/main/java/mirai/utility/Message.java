package mirai.utility;

import java.util.HashMap;
import java.util.Map;

/**
 * An utility class containing message templates used throughout the Mirai chatbot.
 */
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

    public static final String STORAGE_FILE_CREATION_ERROR =
            "OOPS!!! Mirai cannot load data from your storage...\n"
            + "Mirai will create an empty task list.";

    public static final String ERROR = "OOPS!!! ";

    public static final Map<String, String> COMMAND_DESCRIPTION = new HashMap<>() {{
            put("bye", "Syntax: bye\nExits the Mirai chatbot application.");
            put("deadline", "Syntax: deadline [description] /by [date]\nStores a deadline task.");
            put("delete", "Syntax: delete [index]\nDeletes a task from the storage.");
            put("event", "Syntax: event [description] /from [date] to [date]\nStores an event task.");
            put("find", "Syntax: find [keyword]\nFinds a task based on a word/words. Note that everything after "
                    + "the 'find' keyword will be considered as one block for finding.");
            put("flexfind", "Syntax: flexfind [keyword]\nSort all tasks based on the relevance to the keyword."
                    + "Note that everything after the 'flexfind' keyword will be considered as one block for finding.");
            put("help", "Syntax: help\nLists all commands that Mirai supports.");
            put("list", "Syntax: list\nLists all tasks currently stored by Mirai.");
            put("mark", "Syntax: mark [index]\nMarks a task as done.");
            put("todo", "Syntax: todo [description]\nStores a to-do task.");
            put("unmark", "Syntax: unmark [index]\nMarks a task as uncompleted.");
        }
    };


    public static String getNumOfTasks(int numOfTasks) {
        return String.format("Now you have %d task%s in the list",
                numOfTasks,
                numOfTasks <= 1 ? "" : "s");
    }
}
