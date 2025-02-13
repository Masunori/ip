package mirai.utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import mirai.tasks.Deadline;
import mirai.tasks.Event;
import mirai.tasks.Task;
import mirai.tasks.ToDo;

/**
 * The Storage class encapsulates a storage of user's list.
 */
public class Storage {
    private final File file;

    /**
     * Initialises a task storage. Note that if the file does not exist, this will silently create the file
     *     (and the folders).
     * @param filePath The path to the file to store the tasks.
     */
    public Storage(String filePath) {
        this.file = new File(filePath);
    }

    /**
     * Loads the tasks from the file whose path is specified during initialization.
     * @return a list containing the tasks
     * @throws IOException if an I/O error occurred during the file creation.
     */
    public List<Task> load() throws IOException {
        this.file.getParentFile().mkdirs();
        this.file.createNewFile();

        List<Task> taskList = new ArrayList<>();

        try (Scanner scanner = new Scanner(this.file)) {
            while (scanner.hasNextLine()) {
                String[] taskContent = scanner.nextLine().split(" \\| ");

                Task task = switch (taskContent[0]) {
                case "T" -> new ToDo(taskContent[2]);
                case "D" -> new Deadline(taskContent[2],
                        LocalDateTime.parse(taskContent[3]));
                case "E" -> new Event(taskContent[2],
                        LocalDateTime.parse(taskContent[3]),
                        LocalDateTime.parse(taskContent[4]));
                default -> null;
                };

                if (task == null) {
                    continue;
                }

                if (taskContent[1].equals("1")) {
                    task.markAsDone();
                }

                taskList.add(task);
            }
        }

        return taskList;
    }

    /**
     * Logs a new task to the file whose path is specified during initialization.
     * @param task The task to be logged
     */
    public void logNewTask(Task task) {
        try (FileWriter writer = new FileWriter(this.file, true)) {
            writer.write(task.toNoteForm() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Overwrites the storage file using a new list of tasks.
     *
     * @param tasks the list of tasks to be used for overwriting
     */
    public void relogAllTasks(List<Task> tasks) {
        // delete all content from the old file to ensure a clear file before overwriting
        try (FileWriter contentDeleter = new FileWriter(this.file)) {
            contentDeleter.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // add content from the new list of tasks
        try (FileWriter writer = new FileWriter(this.file, true)) {
            for (Task task : tasks) {
                writer.write(task.toNoteForm() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
