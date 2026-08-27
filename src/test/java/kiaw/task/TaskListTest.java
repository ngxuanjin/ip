package kiaw.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TaskListTest {

    @Test
    public void add_task_taskAdded() {
        TaskList tasks = new TaskList();

        tasks.add(new Todo("read book"));

        assertEquals(1, tasks.size());
        assertEquals(
                "read book",
                tasks.get(0).getDescription()
        );
    }

    @Test
    public void add_multipleTasks_allTasksAdded() {
        TaskList tasks = new TaskList();

        tasks.add(new Todo("read book"));
        tasks.add(new Todo("buy bread"));

        assertEquals(2, tasks.size());
        assertEquals(
                "read book",
                tasks.get(0).getDescription()
        );
        assertEquals(
                "buy bread",
                tasks.get(1).getDescription()
        );
    }

    @Test
    public void delete_existingTask_correctTaskRemoved() {
        TaskList tasks = new TaskList();

        tasks.add(new Todo("read book"));
        tasks.add(new Todo("buy bread"));

        Task deletedTask = tasks.delete(0);

        assertEquals(
                "read book",
                deletedTask.getDescription()
        );
        assertEquals(1, tasks.size());
        assertEquals(
                "buy bread",
                tasks.get(0).getDescription()
        );
    }

    @Test
    public void delete_middleTask_remainingTasksShiftCorrectly() {
        TaskList tasks = new TaskList();

        tasks.add(new Todo("first"));
        tasks.add(new Todo("second"));
        tasks.add(new Todo("third"));

        Task deletedTask = tasks.delete(1);

        assertEquals(
                "second",
                deletedTask.getDescription()
        );
        assertEquals(2, tasks.size());
        assertEquals(
                "first",
                tasks.get(0).getDescription()
        );
        assertEquals(
                "third",
                tasks.get(1).getDescription()
        );
    }

    @Test
    public void mark_existingTask_taskMarkedDone() {
        TaskList tasks = new TaskList();

        tasks.add(new Todo("read book"));

        tasks.mark(0);

        assertEquals(
                "X",
                tasks.get(0).getStatusIcon()
        );
    }

    @Test
    public void unmark_doneTask_taskMarkedNotDone() {
        TaskList tasks = new TaskList();

        tasks.add(new Todo("read book"));
        tasks.mark(0);

        tasks.unmark(0);

        assertEquals(
                " ",
                tasks.get(0).getStatusIcon()
        );
    }

    @Test
    public void isEmpty_newTaskList_returnsTrue() {
        TaskList tasks = new TaskList();

        assertTrue(tasks.isEmpty());
    }

    @Test
    public void isEmpty_taskAdded_returnsFalse() {
        TaskList tasks = new TaskList();

        tasks.add(new Todo("read book"));

        assertFalse(tasks.isEmpty());
    }
}