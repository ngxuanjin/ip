import java.time.LocalDate;

public class ParsedCommand {
    private final String commandType;
    private final String description;
    private final int taskNumber;
    private final LocalDate date;
    private final LocalDate from;
    private final LocalDate to;

    public ParsedCommand(
            String commandType,
            String description,
            int taskNumber,
            LocalDate date,
            LocalDate from,
            LocalDate to
    ) {
        this.commandType = commandType;
        this.description = description;
        this.taskNumber = taskNumber;
        this.date = date;
        this.from = from;
        this.to = to;
    }

    public String getCommandType() {
        return commandType;
    }

    public String getDescription() {
        return description;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }
}