package ex04;

public class UndoConsoleCommand implements ConsoleCommand {

    @Override
    public void execute() {
        Application.getInstance().undoLast();
    }

    @Override
    public char getKey() {
        return 'u';
    }

    @Override
    public String toString() {
        return "'u' - undo last command";
    }
}