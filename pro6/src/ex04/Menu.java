package ex04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Menu implements Command {
    private ArrayList<ConsoleCommand> commands = new ArrayList<ConsoleCommand>();

    public void add(ConsoleCommand command) {
        commands.add(command);
    }

    @Override
    public void execute() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;

        while (true) {
            System.out.println("\n=== MENU ===");
            for (ConsoleCommand command : commands) {
                System.out.println(command.toString());
            }
            System.out.println("'q' - quit");
            System.out.print("Choose command: ");

            try {
                input = reader.readLine();
            } catch (IOException e) {
                System.out.println("Input error.");
                return;
            }

            if (input == null || input.length() != 1) {
                System.out.println("Wrong input. Enter one symbol.");
                continue;
            }

            char key = input.charAt(0);

            if (key == 'q') {
                System.out.println("Program finished.");
                break;
            }

            boolean found = false;

            for (ConsoleCommand command : commands) {
                if (command.getKey() == key) {
                    command.execute();
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Unknown command.");
            }
        }
    }
}