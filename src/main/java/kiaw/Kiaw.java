package kiaw;

import java.io.IOException;
import java.util.ArrayList;

import kiaw.exception.KiawException;
import kiaw.parser.ParsedCommand;
import kiaw.parser.Parser;
import kiaw.storage.Storage;
import kiaw.task.*;
import kiaw.ui.Ui;

/**
 * Represents the Kiaw chatbot application.
 *
 * Kiaw coordinates user interaction, command parsing, task management,
 * and persistent storage.
 */
public class Kiaw {

    /**
     * Starts the Kiaw chatbot.
     *
     * @param args command-line arguments; currently unused
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage =
                new Storage("data", "kiaw.txt");

        TaskList tasks;

        try {
            ArrayList<Task> loadedTasks =
                    storage.load();

            tasks = new TaskList(loadedTasks);
        } catch (IOException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }

        ui.showWelcome();

        boolean isExit = false;

        while (!isExit) {
            String input = ui.readCommand();

            try {
                ParsedCommand command =
                        Parser.parse(input);

                String commandType =
                        command.getCommandType();

                switch (commandType) {
                    case "bye":
                        ui.showGoodbye();
                        isExit = true;
                        break;

                    case "list":
                        showTaskList(tasks, ui);
                        break;

                    case "mark":
                        markTask(
                                command,
                                tasks,
                                storage,
                                ui
                        );
                        break;

                    case "unmark":
                        unmarkTask(
                                command,
                                tasks,
                                storage,
                                ui
                        );
                        break;

                    case "delete":
                        deleteTask(
                                command,
                                tasks,
                                storage,
                                ui
                        );
                        break;

                    case "todo":
                        addTodo(
                                command,
                                tasks,
                                storage,
                                ui
                        );
                        break;

                    case "deadline":
                        addDeadline(
                                command,
                                tasks,
                                storage,
                                ui
                        );
                        break;

                    case "event":
                        addEvent(
                                command,
                                tasks,
                                storage,
                                ui
                        );
                        break;

                    default:
                        throw new KiawException(
                                "I don't recognise that command."
                        );
                }

            } catch (KiawException e) {
                ui.showError(e.getMessage());

            } catch (IOException e) {
                ui.showError(
                        "I couldn't save your tasks."
                );

            } catch (Exception e) {
                ui.showError(
                        "Something went wrong. "
                                + "Please check your command."
                );
            }
        }

        ui.close();
    }

    /**
     * Displays all tasks currently stored in the task list.
     *
     * @param tasks task list to display
     * @param ui user interface used to display the tasks
     */
    private static void showTaskList(
            TaskList tasks,
            Ui ui
    ) {
        if (tasks.isEmpty()) {
            ui.showMessage(
                    "Your task list is empty."
            );
            return;
        }

        ui.showMessage(
                "Here are the tasks in your list:"
        );

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);

            ui.showMessage(
                    (i + 1)
                            + ".["
                            + task.getTypeIcon()
                            + "]["
                            + task.getStatusIcon()
                            + "] "
                            + task.getDetails()
            );
        }
    }

    /**
     * Marks the specified task as done and saves the updated task list.
     *
     * @param command parsed mark command
     * @param tasks task list containing the task
     * @param storage storage used to save the updated task list
     * @param ui user interface used to display feedback
     * @throws KiawException if the specified task number does not exist
     * @throws IOException if the updated task list cannot be saved
     */
    private static void markTask(
            ParsedCommand command,
            TaskList tasks,
            Storage storage,
            Ui ui
    ) throws KiawException, IOException {

        int index = getTaskIndex(
                command.getTaskNumber(),
                tasks
        );

        tasks.mark(index);
        storage.save(tasks.getTasks());

        Task task = tasks.get(index);

        ui.showMessage(
                "Nice! I've marked this task as done:"
        );
        ui.showMessage(
                "["
                        + task.getStatusIcon()
                        + "] "
                        + task.getDetails()
        );
    }

    /**
     * Marks the specified task as not done and saves the updated task list.
     *
     * @param command parsed unmark command
     * @param tasks task list containing the task
     * @param storage storage used to save the updated task list
     * @param ui user interface used to display feedback
     * @throws KiawException if the specified task number does not exist
     * @throws IOException if the updated task list cannot be saved
     */
    private static void unmarkTask(
            ParsedCommand command,
            TaskList tasks,
            Storage storage,
            Ui ui
    ) throws KiawException, IOException {

        int index = getTaskIndex(
                command.getTaskNumber(),
                tasks
        );

        tasks.unmark(index);
        storage.save(tasks.getTasks());

        Task task = tasks.get(index);

        ui.showMessage(
                "OK, I've marked this task as not done yet:"
        );
        ui.showMessage(
                "["
                        + task.getStatusIcon()
                        + "] "
                        + task.getDetails()
        );
    }

    /**
     * Deletes the specified task and saves the updated task list.
     *
     * @param command parsed delete command
     * @param tasks task list containing the task
     * @param storage storage used to save the updated task list
     * @param ui user interface used to display feedback
     * @throws KiawException if the specified task number does not exist
     * @throws IOException if the updated task list cannot be saved
     */
    private static void deleteTask(
            ParsedCommand command,
            TaskList tasks,
            Storage storage,
            Ui ui
    ) throws KiawException, IOException {

        int index = getTaskIndex(
                command.getTaskNumber(),
                tasks
        );

        Task deletedTask =
                tasks.delete(index);

        storage.save(tasks.getTasks());

        ui.showMessage(
                "Noted. I've removed this task:"
        );
        ui.showMessage(
                "["
                        + deletedTask.getTypeIcon()
                        + "]["
                        + deletedTask.getStatusIcon()
                        + "] "
                        + deletedTask.getDetails()
        );
        ui.showMessage(
                "Now you have "
                        + tasks.size()
                        + " tasks in the list."
        );
    }

    /**
     * Adds a todo task and saves the updated task list.
     *
     * @param command parsed todo command
     * @param tasks task list to update
     * @param storage storage used to save the updated task list
     * @param ui user interface used to display feedback
     * @throws IOException if the updated task list cannot be saved
     */
    private static void addTodo(
            ParsedCommand command,
            TaskList tasks,
            Storage storage,
            Ui ui
    ) throws IOException {

        Todo todo =
                new Todo(
                        command.getDescription()
                );

        tasks.add(todo);
        storage.save(tasks.getTasks());

        ui.showMessage(
                "Got it. I've added this task:"
        );
        ui.showMessage(
                "[T][ ] "
                        + todo.getDetails()
        );
        ui.showMessage(
                "Now you have "
                        + tasks.size()
                        + " tasks in the list."
        );
    }

    /**
     * Adds a deadline task and saves the updated task list.
     *
     * @param command parsed deadline command
     * @param tasks task list to update
     * @param storage storage used to save the updated task list
     * @param ui user interface used to display feedback
     * @throws IOException if the updated task list cannot be saved
     */
    private static void addDeadline(
            ParsedCommand command,
            TaskList tasks,
            Storage storage,
            Ui ui
    ) throws IOException {

        Deadline deadline =
                new Deadline(
                        command.getDescription(),
                        command.getDate()
                );

        tasks.add(deadline);
        storage.save(tasks.getTasks());

        ui.showMessage(
                "Got it. I've added this task:"
        );
        ui.showMessage(
                "[D][ ] "
                        + deadline.getDetails()
        );
        ui.showMessage(
                "Now you have "
                        + tasks.size()
                        + " tasks in the list."
        );
    }

    /**
     * Adds an event task and saves the updated task list.
     *
     * @param command parsed event command
     * @param tasks task list to update
     * @param storage storage used to save the updated task list
     * @param ui user interface used to display feedback
     * @throws IOException if the updated task list cannot be saved
     */
    private static void addEvent(
            ParsedCommand command,
            TaskList tasks,
            Storage storage,
            Ui ui
    ) throws IOException {

        Event event =
                new Event(
                        command.getDescription(),
                        command.getFrom(),
                        command.getTo()
                );

        tasks.add(event);
        storage.save(tasks.getTasks());

        ui.showMessage(
                "Got it. I've added this task:"
        );
        ui.showMessage(
                "[E][ ] "
                        + event.getDetails()
        );
        ui.showMessage(
                "Now you have "
                        + tasks.size()
                        + " tasks in the list."
        );
    }

    /**
     * Converts a one-based task number into a zero-based task index.
     *
     * @param taskNumber task number supplied by the user
     * @param tasks current task list
     * @return zero-based index of the task
     * @throws KiawException if the task number is outside the task list
     */
    private static int getTaskIndex(
            int taskNumber,
            TaskList tasks
    ) throws KiawException {

        if (taskNumber < 1
                || taskNumber > tasks.size()) {
            throw new KiawException(
                    "That task number does not exist."
            );
        }

        return taskNumber - 1;
    }
}