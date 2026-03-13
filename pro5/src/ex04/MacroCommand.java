package ex04;

import java.util.ArrayList;

public class MacroCommand implements ConsoleCommand {
    private ArrayList<Command> commands = new ArrayList<Command>();

    public void add(Command command) {
        commands.add(command);
    }

    @Override
    public void execute() {
        System.out.println("Executing macro command...");
        for (Command command : commands) {
            command.execute();
        }
    }

    @Override
    public char getKey() {
        return 'm';
    }

    @Override
    public String toString() {
        return "'m' - execute macro command";
    }
}