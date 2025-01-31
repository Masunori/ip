package mirai.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class TaskTest {
    @Test
    public void toDoToString_readBookUnmarked_success() {
        Task toDo = new ToDo("read book");
        assertEquals("[T][ ] read book", toDo.toString());
    }

    @Test
    public void toDoToString_readBookMarked_success() {
        Task toDo = new ToDo("read book");
        toDo.markAsDone();
        assertEquals("[T][X] read book", toDo.toString());
    }

    @Test
    public void toDoToNoteForm_readBookUnmarked_success() {
        Task toDo = new ToDo("read book");
        assertEquals("T | 0 | read book", toDo.toNoteForm());
    }

    @Test
    public void deadlineToString_returnBookUnmarked_success() {
        Task deadline = new Deadline("return book", LocalDateTime.parse("2025-01-31T15:59"));
        assertEquals("[D][ ] return book (by: Jan 31 2025, 1559)", deadline.toString());
    }

    @Test
    public void deadlineToString_returnBookMarked_success() {
        Task deadline = new Deadline("return book", LocalDateTime.parse("2025-01-31T15:59"));
        deadline.markAsDone();
        assertEquals("[D][X] return book (by: Jan 31 2025, 1559)", deadline.toString());
    }

    @Test
    public void deadlineToNoteForm_returnBookUnmarked_success() {
        Task deadline = new Deadline("return book", LocalDateTime.parse("2025-01-31T15:59"));
        assertEquals("D | 0 | return book | 2025-01-31T15:59", deadline.toNoteForm());
    }

    @Test
    public void eventToString_meetingUnmarked_success() {
        Task event = new Event("meeting",
                LocalDateTime.parse("2025-01-31T16:00"),
                LocalDateTime.parse("2025-01-31T18:00"));
        assertEquals("[E][ ] meeting (from: Jan 31 2025, 1600, to: Jan 31 2025, 1800)", event.toString());
    }

    @Test
    public void eventToString_meetingMarked_success() {
        Task event = new Event("meeting",
                LocalDateTime.parse("2025-01-31T16:00"),
                LocalDateTime.parse("2025-01-31T18:00"));
        event.markAsDone();
        assertEquals("[E][X] meeting (from: Jan 31 2025, 1600, to: Jan 31 2025, 1800)", event.toString());
    }

    @Test
    public void eventToNoteForm_returnBookMarked_success() {
        Task event = new Event("meeting",
                LocalDateTime.parse("2025-01-31T16:00"),
                LocalDateTime.parse("2025-01-31T18:00"));
        event.markAsDone();
        assertEquals("E | 1 | meeting | 2025-01-31T16:00 | 2025-01-31T18:00", event.toNoteForm());
    }
}
