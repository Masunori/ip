package mirai.utility;

import mirai.tasks.Task;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskListTest {
    private static TaskList taskList;
    private static int count = 1;

    /**
     * The DummyTask class encapsulates a dummy task used for testing
     */
    private static class DummyTask extends Task {
        /**
         * Initialises a dummy task.
         */
        public DummyTask() {
            super("Dummy task " + count);
            count++;
        }

        @Override
        public String toString() {
            return "[DUMMY]" + super.toString();
        }

        @Override
        public String toNoteForm() {
            return "[DUMMY]" + this.description;
        }
    }

    @BeforeAll
    public static void setUp() {
        taskList = new TaskList();
    }

    @Test
    @Order(1)
    public void addTask_addThreeTasks_success() {
        taskList.addTask(new DummyTask());
        taskList.addTask(new DummyTask());
        taskList.addTask(new DummyTask());
        assertEquals(3, taskList.getSize());
        assertEquals("[DUMMY][ ] Dummy task 1", taskList.getTask(0).toString());
        assertEquals("[DUMMY][ ] Dummy task 2", taskList.getTask(1).toString());
        assertEquals("[DUMMY][ ] Dummy task 3", taskList.getTask(2).toString());
    }

    @Test
    @Order(2)
    public void markTask_markTask1_success() {
        taskList.markTask(0);
        assertEquals(3, taskList.getSize());
        assertEquals("[DUMMY][X] Dummy task 1", taskList.getTask(0).toString());
    }

    @Test
    @Order(3)
    public void unmarkTask_unmarkTask1_success() {
        taskList.unmarkTask(0);
        assertEquals(3, taskList.getSize());
        assertEquals("[DUMMY][ ] Dummy task 1", taskList.getTask(0).toString());
    }

    @Test
    @Order(4)
    public void deleteTask_deleteTask3_success() {
        taskList.deleteTask(2);
        assertEquals(2, taskList.getSize());
        assertEquals("[DUMMY][ ] Dummy task 1", taskList.getTask(0).toString());
        assertEquals("[DUMMY][ ] Dummy task 2", taskList.getTask(1).toString());
    }
}
