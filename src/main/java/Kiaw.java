import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Kiaw {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Storage storage = new Storage("data", "kiaw.txt");

        ArrayList<Task> tasks;

        try {
            tasks = storage.load();
        } catch (IOException e) {
            System.out.println(
                    "OOPS!!! I couldn't load your saved tasks."
            );
            tasks = new ArrayList<>();
        }

        System.out.println("Hello! I'm Kiaw.");
        System.out.println("What can I do for you?");

        while (true) {
            String input = scanner.nextLine().trim();

            try {
                if (input.equals("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    break;
                }

                if (input.equals("list")) {
                    if (tasks.isEmpty()) {
                        System.out.println("Your task list is empty.");
                    } else {
                        System.out.println("Here are the tasks in your list:");

                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println(
                                    (i + 1) + ".["
                                            + tasks.get(i).getTypeIcon()
                                            + "]["
                                            + tasks.get(i).getStatusIcon()
                                            + "] "
                                            + tasks.get(i).getDetails()
                            );
                        }
                    }

                } else if (input.startsWith("mark ")) {
                    int taskNumber = parseTaskNumber(
                            input.substring(5),
                            tasks.size()
                    );

                    int index = taskNumber - 1;

                    tasks.get(index).markAsDone();
                    storage.save(tasks);

                    System.out.println(
                            "Nice! I've marked this task as done:"
                    );
                    System.out.println(
                            "[" + tasks.get(index).getStatusIcon()
                                    + "] "
                                    + tasks.get(index).getDetails()
                    );

                } else if (input.startsWith("unmark ")) {
                    int taskNumber = parseTaskNumber(
                            input.substring(7),
                            tasks.size()
                    );

                    int index = taskNumber - 1;

                    tasks.get(index).markAsNotDone();
                    storage.save(tasks);

                    System.out.println(
                            "OK, I've marked this task as not done yet:"
                    );
                    System.out.println(
                            "[" + tasks.get(index).getStatusIcon()
                                    + "] "
                                    + tasks.get(index).getDetails()
                    );

                } else if (input.startsWith("delete ")) {
                    int taskNumber = parseTaskNumber(
                            input.substring(7),
                            tasks.size()
                    );

                    int index = taskNumber - 1;

                    Task deletedTask = tasks.remove(index);
                    storage.save(tasks);

                    System.out.println(
                            "Noted. I've removed this task:"
                    );
                    System.out.println(
                            "[" + deletedTask.getTypeIcon()
                                    + "]["
                                    + deletedTask.getStatusIcon()
                                    + "] "
                                    + deletedTask.getDetails()
                    );
                    System.out.println(
                            "Now you have "
                                    + tasks.size()
                                    + " tasks in the list."
                    );

                } else if (input.equals("todo")) {
                    throw new KiawException(
                            "The description of a todo cannot be empty."
                    );

                } else if (input.startsWith("todo ")) {
                    String description =
                            input.substring(5).trim();

                    if (description.isEmpty()) {
                        throw new KiawException(
                                "The description of a todo cannot be empty."
                        );
                    }

                    tasks.add(new Todo(description));
                    storage.save(tasks);

                    System.out.println(
                            "Got it. I've added this task:"
                    );
                    System.out.println(
                            "[T][ ] " + description
                    );
                    System.out.println(
                            "Now you have "
                                    + tasks.size()
                                    + " tasks in the list."
                    );

                } else if (input.equals("deadline")) {
                    throw new KiawException(
                            "A deadline needs a description and a /by date."
                    );

                } else if (input.startsWith("deadline ")) {
                    String content =
                            input.substring(9).trim();

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

                    String by =
                            content.substring(
                                    separatorIndex + 5
                            ).trim();

                    if (description.isEmpty()) {
                        throw new KiawException(
                                "The description of a deadline cannot be empty."
                        );
                    }

                    if (by.isEmpty()) {
                        throw new KiawException(
                                "The /by date of a deadline cannot be empty."
                        );
                    }

                    tasks.add(
                            new Deadline(description, by)
                    );
                    storage.save(tasks);

                    System.out.println(
                            "Got it. I've added this task:"
                    );
                    System.out.println(
                            "[D][ ] "
                                    + description
                                    + " (by: "
                                    + by
                                    + ")"
                    );
                    System.out.println(
                            "Now you have "
                                    + tasks.size()
                                    + " tasks in the list."
                    );

                } else if (input.equals("event")) {
                    throw new KiawException(
                            "An event needs a description, "
                                    + "/from time and /to time."
                    );

                } else if (input.startsWith("event ")) {
                    String content =
                            input.substring(6).trim();

                    int fromIndex =
                            content.indexOf(" /from ");

                    int toIndex =
                            content.indexOf(" /to ");

                    if (fromIndex == -1) {
                        throw new KiawException(
                                "An event must contain a /from time."
                        );
                    }

                    if (toIndex == -1) {
                        throw new KiawException(
                                "An event must contain a /to time."
                        );
                    }

                    if (toIndex < fromIndex) {
                        throw new KiawException(
                                "The /from time must come before "
                                        + "the /to time."
                        );
                    }

                    String description =
                            content.substring(
                                    0,
                                    fromIndex
                            ).trim();

                    String from =
                            content.substring(
                                    fromIndex + 7,
                                    toIndex
                            ).trim();

                    String to =
                            content.substring(
                                    toIndex + 5
                            ).trim();

                    if (description.isEmpty()) {
                        throw new KiawException(
                                "The description of an event cannot be empty."
                        );
                    }

                    if (from.isEmpty()) {
                        throw new KiawException(
                                "The /from time of an event cannot be empty."
                        );
                    }

                    if (to.isEmpty()) {
                        throw new KiawException(
                                "The /to time of an event cannot be empty."
                        );
                    }

                    tasks.add(
                            new Event(
                                    description,
                                    from,
                                    to
                            )
                    );
                    storage.save(tasks);

                    System.out.println(
                            "Got it. I've added this task:"
                    );
                    System.out.println(
                            "[E][ ] "
                                    + description
                                    + " (from: "
                                    + from
                                    + " to: "
                                    + to
                                    + ")"
                    );
                    System.out.println(
                            "Now you have "
                                    + tasks.size()
                                    + " tasks in the list."
                    );

                } else {
                    throw new KiawException(
                            "I don't recognise that command."
                    );
                }

            } catch (KiawException e) {
                System.out.println(
                        "OOPS!!! " + e.getMessage()
                );

            } catch (IOException e) {
                System.out.println(
                        "OOPS!!! I couldn't save your tasks."
                );

            } catch (Exception e) {
                System.out.println(
                        "OOPS!!! Something went wrong. "
                                + "Please check your command."
                );
            }
        }

        scanner.close();
    }

    private static int parseTaskNumber(
            String input,
            int taskCount
    ) throws KiawException {

        int taskNumber;

        try {
            taskNumber =
                    Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new KiawException(
                    "Please enter a valid task number."
            );
        }

        if (taskNumber < 1
                || taskNumber > taskCount) {
            throw new KiawException(
                    "That task number does not exist."
            );
        }

        return taskNumber;
    }
}