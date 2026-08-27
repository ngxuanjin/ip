package kiaw.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import kiaw.task.Deadline;
import kiaw.task.Event;
import kiaw.task.Task;
import kiaw.task.Todo;

/**
 * Handles loading tasks from and saving tasks to persistent storage.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates a storage manager for the specified folder and file.
     *
     * @param folderName folder containing the data file
     * @param fileName name of the data file
     */
    public Storage(String folderName, String fileName) {
        this.filePath = Path.of(folderName, fileName);
    }

    /**
     * Saves all tasks to the configured data file.
     *
     * Creates the parent directory when it does not already exist.
     *
     * @param tasks tasks to save
     * @throws IOException if the tasks cannot be written to the file
     */
    public void save(ArrayList<Task> tasks) throws IOException {
        Path parentDirectory = filePath.getParent();

        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        ArrayList<String> lines = new ArrayList<>();

        for (Task task : tasks) {
            lines.add(taskToString(task));
        }

        Files.write(filePath, lines);
    }

    /**
     * Loads tasks from the configured data file.
     *
     * If the directory or file does not exist, it is created and an empty
     * task list is returned.
     *
     * @return tasks loaded from storage
     * @throws IOException if the data file cannot be read or created
     */
    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();

        Path parentDirectory = filePath.getParent();

        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
            return tasks;
        }

        List<String> lines = Files.readAllLines(filePath);

        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }

            Task task = stringToTask(line);

            if (task != null) {
                tasks.add(task);
            }
        }

        return tasks;
    }

    /**
     * Converts a task into its persistent text representation.
     *
     * @param task task to convert
     * @return text representation suitable for saving
     */
    private String taskToString(Task task) {
        String status =
                task.getStatusIcon().equals("X")
                        ? "1"
                        : "0";

        if (task instanceof Deadline) {
            Deadline deadline =
                    (Deadline) task;

            return "D | "
                    + status
                    + " | "
                    + deadline.getDescription()
                    + " | "
                    + deadline.getBy();
        }

        if (task instanceof Event) {
            Event event =
                    (Event) task;

            return "E | "
                    + status
                    + " | "
                    + event.getDescription()
                    + " | "
                    + event.getFrom()
                    + " | "
                    + event.getTo();
        }

        return "T | "
                + status
                + " | "
                + task.getDescription();
    }

    /**
     * Converts a stored text representation back into a task.
     *
     * @param line line read from the data file
     * @return reconstructed task, or null if the line is invalid
     */
    private Task stringToTask(String line) {
        String[] parts =
                line.split(" \\| ");

        if (parts.length < 3) {
            return null;
        }

        String type = parts[0];
        boolean isDone =
                parts[1].equals("1");
        String description = parts[2];

        Task task;

        switch (type) {
            case "T":
                task =
                        new Todo(description);
                break;

            case "D":
                if (parts.length < 4) {
                    return null;
                }

                task =
                        new Deadline(
                                description,
                                LocalDate.parse(parts[3])
                        );
                break;

            case "E":
                if (parts.length < 5) {
                    return null;
                }

                task =
                        new Event(
                                description,
                                LocalDate.parse(parts[3]),
                                LocalDate.parse(parts[4])
                        );
                break;

            default:
                return null;
        }

        if (isDone) {
            task.markAsDone();
        }

        return task;
    }
}