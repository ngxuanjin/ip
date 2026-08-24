import java.util.Scanner;

public class Kiaw {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] tasks = new String[100];
        boolean[] taskStatus = new boolean[100];
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
                if (taskCount == 0) {
                    System.out.println("Your task list is empty.");
                } else {
                    System.out.println("Here are the tasks in your list:");

                    for (int i = 0; i < taskCount; i++) {
                        String status = taskStatus[i] ? "X" : " ";
                        System.out.println((i + 1) + ".[" + status + "] " + tasks[i]);
                    }
                }

            } else if (input.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(input.substring(5));
                int index = taskNumber - 1;

                taskStatus[index] = true;

                System.out.println("Nice! I've marked this task as done:");
                System.out.println("[X] " + tasks[index]);

            } else if (input.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(input.substring(7));
                int index = taskNumber - 1;

                taskStatus[index] = false;

                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("[ ] " + tasks[index]);

            } else {
                tasks[taskCount] = input;
                taskStatus[taskCount] = false;
                taskCount++;

                System.out.println("added: " + input);
            }
        }

        scanner.close();
    }
}