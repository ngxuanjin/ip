package kiaw.parser;

import kiaw.exception.KiawException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Parser {

    public static ParsedCommand parse(String input)
            throws KiawException {

        if (input.equals("bye")) {
            return createSimpleCommand("bye");
        }

        if (input.equals("list")) {
            return createSimpleCommand("list");
        }

        if (input.equals("mark")) {
            throw new KiawException(
                    "Please specify which task to mark."
            );
        }

        if (input.startsWith("mark ")) {
            int taskNumber =
                    parseTaskNumber(input.substring(5));

            return new ParsedCommand(
                    "mark",
                    null,
                    taskNumber,
                    null,
                    null,
                    null
            );
        }

        if (input.equals("unmark")) {
            throw new KiawException(
                    "Please specify which task to unmark."
            );
        }

        if (input.startsWith("unmark ")) {
            int taskNumber =
                    parseTaskNumber(input.substring(7));

            return new ParsedCommand(
                    "unmark",
                    null,
                    taskNumber,
                    null,
                    null,
                    null
            );
        }

        if (input.equals("delete")) {
            throw new KiawException(
                    "Please specify which task to delete."
            );
        }

        if (input.startsWith("delete ")) {
            int taskNumber =
                    parseTaskNumber(input.substring(7));

            return new ParsedCommand(
                    "delete",
                    null,
                    taskNumber,
                    null,
                    null,
                    null
            );
        }

        if (input.equals("todo")) {
            throw new KiawException(
                    "The description of a todo cannot be empty."
            );
        }

        if (input.startsWith("todo ")) {
            String description =
                    input.substring(5).trim();

            if (description.isEmpty()) {
                throw new KiawException(
                        "The description of a todo cannot be empty."
                );
            }

            return new ParsedCommand(
                    "todo",
                    description,
                    0,
                    null,
                    null,
                    null
            );
        }

        if (input.equals("deadline")) {
            throw new KiawException(
                    "A deadline needs a description and a /by date."
            );
        }

        if (input.startsWith("deadline ")) {
            return parseDeadline(input);
        }

        if (input.equals("event")) {
            throw new KiawException(
                    "An event needs a description, "
                            + "/from date and /to date."
            );
        }

        if (input.startsWith("event ")) {
            return parseEvent(input);
        }

        throw new KiawException(
                "I don't recognise that command."
        );
    }

    private static ParsedCommand parseDeadline(String input)
            throws KiawException {

        String content = input.substring(9).trim();

        int separatorIndex =
                content.indexOf(" /by ");

        if (separatorIndex == -1) {
            throw new KiawException(
                    "A deadline must contain a /by date."
            );
        }

        String description =
                content.substring(
                        0,
                        separatorIndex
                ).trim();

        String byString =
                content.substring(
                        separatorIndex + 5
                ).trim();

        if (description.isEmpty()) {
            throw new KiawException(
                    "The description of a deadline cannot be empty."
            );
        }

        if (byString.isEmpty()) {
            throw new KiawException(
                    "The /by date of a deadline cannot be empty."
            );
        }

        LocalDate by;

        try {
            by = LocalDate.parse(byString);
        } catch (DateTimeParseException e) {
            throw new KiawException(
                    "Please enter the deadline date "
                            + "in yyyy-MM-dd format."
            );
        }

        return new ParsedCommand(
                "deadline",
                description,
                0,
                by,
                null,
                null
        );
    }

    private static ParsedCommand parseEvent(String input)
            throws KiawException {

        String content = input.substring(6).trim();

        int fromIndex =
                content.indexOf(" /from ");

        int toIndex =
                content.indexOf(" /to ");

        if (fromIndex == -1) {
            throw new KiawException(
                    "An event must contain a /from date."
            );
        }

        if (toIndex == -1) {
            throw new KiawException(
                    "An event must contain a /to date."
            );
        }

        if (toIndex < fromIndex) {
            throw new KiawException(
                    "The /from date must come before "
                            + "the /to date."
            );
        }

        String description =
                content.substring(
                        0,
                        fromIndex
                ).trim();

        String fromString =
                content.substring(
                        fromIndex + 7,
                        toIndex
                ).trim();

        String toString =
                content.substring(
                        toIndex + 5
                ).trim();

        if (description.isEmpty()) {
            throw new KiawException(
                    "The description of an event cannot be empty."
            );
        }

        if (fromString.isEmpty()) {
            throw new KiawException(
                    "The /from date of an event cannot be empty."
            );
        }

        if (toString.isEmpty()) {
            throw new KiawException(
                    "The /to date of an event cannot be empty."
            );
        }

        LocalDate from;
        LocalDate to;

        try {
            from = LocalDate.parse(fromString);
            to = LocalDate.parse(toString);
        } catch (DateTimeParseException e) {
            throw new KiawException(
                    "Please enter event dates "
                            + "in yyyy-MM-dd format."
            );
        }

        if (to.isBefore(from)) {
            throw new KiawException(
                    "The event end date cannot be "
                            + "before the start date."
            );
        }

        return new ParsedCommand(
                "event",
                description,
                0,
                null,
                from,
                to
        );
    }

    private static int parseTaskNumber(String input)
            throws KiawException {

        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new KiawException(
                    "Please enter a valid task number."
            );
        }
    }

    private static ParsedCommand createSimpleCommand(
            String commandType
    ) {
        return new ParsedCommand(
                commandType,
                null,
                0,
                null,
                null,
                null
        );
    }
}