package kiaw;

import kiaw.exception.KiawException;
import kiaw.parser.ParsedCommand;
import kiaw.parser.Parser;
import kiaw.storage.Storage;
import kiaw.task.Deadline;
import kiaw.task.Event;
import kiaw.task.Task;
import kiaw.task.TaskList;
import kiaw.task.Todo;
import kiaw.ui.Ui;

import java.io.IOException;
import java.util.ArrayList;

public class Kiaw {

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

                    case "find":
                        findTasks(
                                command,
                                tasks,
                                ui
                        );
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

    private static void findTasks(
            ParsedCommand command,
            TaskList tasks,
            Ui ui
    ) {
        String keyword =
                command.getDescription().toLowerCase();

        ui.showMessage(
                "Here are the matching tasks in your list:"
        );

        int matchNumber = 1;

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);

            if (task.getDescription()
                    .toLowerCase()
                    .contains(keyword)) {

                ui.showMessage(
                        matchNumber
                                + ".["
                                + task.getTypeIcon()
                                + "]["
                                + task.getStatusIcon()
                                + "] "
                                + task.getDetails()
                );

                matchNumber++;
            }
        }
    }

}