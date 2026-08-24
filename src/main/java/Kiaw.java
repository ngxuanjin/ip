import java.util.Scanner;

public class Kiaw {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello! I'm Kiaw.");
        System.out.println("What can I do for you?");

        String input = scanner.nextLine();

        while (!input.equals("bye")) {
            System.out.println(input);
            input = scanner.nextLine();
        }

        System.out.println("Bye. Hope to see you again soon!");
    }
}