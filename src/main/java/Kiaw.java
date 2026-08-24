import java.util.Scanner;

public class Kiaw {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Task[] tasks = new Task[100];
        int taskCount = 0;

        System.out.println("Hello! I'm Kiaw.");
        System.out.println("What can I do for you?");

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            }

            if (input.equals("list")) {
                System.out.println("Here are the tasks in your list:");

                for (int i = 0; i < taskCount; i++) {
                    System.out.println(
                            (i + 1) + ".[" + tasks[i].getTypeIcon() + "]"
                                    + "[" + tasks[i].getStatusIcon() + "] "
                                    + tasks[i].getDetails()
                    );
                }

            } else if (input.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(input.substring(5));
                int index = taskNumber - 1;

                tasks[index].markAsDone();

                System.out.println("Nice! I've marked this task as done:");
                System.out.println(
                        "[" + tasks[index].getStatusIcon() + "] "
                                + tasks[index].getDetails()
                );

            } else if (input.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(input.substring(7));
                int index = taskNumber - 1;

                tasks[index].markAsNotDone();

                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println(
                        "[" + tasks[index].getStatusIcon() + "] "
                                + tasks[index].getDetails()
                );

            } else if (input.startsWith("todo ")) {
                String description = input.substring(5);

                tasks[taskCount] = new Todo(description);
                taskCount++;

                System.out.println("Got it. I've added this task:");
                System.out.println(
                        "[T][ ] " + description
                );
                System.out.println("Now you have " + taskCount + " tasks in the list.");

            } else if (input.startsWith("deadline ")) {
                String content = input.substring(9);

                int separatorIndex = content.indexOf(" /by ");

                String description = content.substring(0, separatorIndex);
                String by = content.substring(separatorIndex + 5);

                tasks[taskCount] = new Deadline(description, by);
                taskCount++;

                System.out.println("Got it. I've added this task:");
                System.out.println(
                        "[D][ ] " + description + " (by: " + by + ")"
                );
                System.out.println("Now you have " + taskCount + " tasks in the list.");

            } else if (input.startsWith("event ")) {
                String content = input.substring(6);

                int fromIndex = content.indexOf(" /from ");
                int toIndex = content.indexOf(" /to ");

                String description = content.substring(0, fromIndex);
                String from = content.substring(fromIndex + 7, toIndex);
                String to = content.substring(toIndex + 5);

                tasks[taskCount] = new Event(description, from, to);
                taskCount++;

                System.out.println("Got it. I've added this task:");
                System.out.println(
                        "[E][ ] " + description
                                + " (from: " + from + " to: " + to + ")"
                );
                System.out.println("Now you have " + taskCount + " tasks in the list.");

            } else {
                tasks[taskCount] = new Todo(input);
                taskCount++;

                System.out.println("added: " + input);
            }
        }

        scanner.close();
    }
}