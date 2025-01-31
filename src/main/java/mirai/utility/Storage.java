package mirai.utility;

import mirai.tasks.Deadline;
import mirai.tasks.Event;
import mirai.tasks.Task;
import mirai.tasks.ToDo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The Storage class encapsulates a storage of user's list.
 */
public class Storage {
    private final File file;

    public Storage(String filePath) {
        this.file = new File(filePath);
    }

    /**
     * Loads the mirai.tasks from the file whose path is specified during initialization.
     * @return a list containing the mirai.tasks
     * @throws IOException if an I/O error occurred during the file creation.
     */
    public List<Task> load() throws IOException {
        this.file.getParentFile().mkdirs();
        this.file.createNewFile();

        Scanner scanner = new Scanner(this.file);
        List<Task> taskList = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String[] taskContent = scanner.nextLine().split(" \\| ");

            switch (taskContent[0]) {
                case "T" -> {
                    Task toDoTask = new ToDo(taskContent[2]);
                    if (taskContent[1].equals("1")) {
                        toDoTask.markAsDone();
                    }
                    taskList.add(toDoTask);
                }
                case "D" -> {
                    Task deadlineTask = new Deadline(taskContent[2],
                            LocalDateTime.parse("2025-01-31T23:59"));
                    if (taskContent[1].equals("1")) {
                        deadlineTask.markAsDone();
                    }
                    taskList.add(deadlineTask);
                }
                case "E" -> {
                    Task eventTask = new Event(taskContent[2],
                            LocalDateTime.parse("2025-01-31T23:59"),
                            LocalDateTime.parse("2025-01-31T23:59"));
                    if (taskContent[1].equals("1")) {
                        eventTask.markAsDone();
                    }
                    taskList.add(eventTask);
                }
            }
        }

        scanner.close();
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
     * Overwrites the storage file using a new list of mirai.tasks.
     *
     * @param tasks the list of mirai.tasks to be used for overwriting
     */
    public void relogAllTasks(List<Task> tasks) {
        // delete all content from the old file
        try (FileWriter contentDeleter = new FileWriter(this.file)) {
            contentDeleter.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // add content from the new list of mirai.tasks
        try (FileWriter writer = new FileWriter(this.file, true)) {
            for (Task task : tasks) {
                writer.write(task.toNoteForm() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
