import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path filePath;

    public Storage(String folderName, String fileName) {
        this.filePath = Path.of(folderName, fileName);
    }

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

    private String taskToString(Task task) {
        String status = task.getStatusIcon().equals("X") ? "1" : "0";

        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;

            return "D | "
                    + status
                    + " | "
                    + deadline.getDescription()
                    + " | "
                    + deadline.getBy();
        }

        if (task instanceof Event) {
            Event event = (Event) task;

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

    private Task stringToTask(String line) {
        String[] parts = line.split(" \\| ");

        if (parts.length < 3) {
            return null;
        }

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task;

        switch (type) {
            case "T":
                task = new Todo(description);
                break;

            case "D":
                if (parts.length < 4) {
                    return null;
                }

                task = new Deadline(
                        description,
                        LocalDate.parse(parts[3])
                );
                break;

            case "E":
                if (parts.length < 5) {
                    return null;
                }

                task = new Event(
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