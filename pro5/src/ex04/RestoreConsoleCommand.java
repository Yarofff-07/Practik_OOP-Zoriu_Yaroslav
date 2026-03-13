package ex04;

import ex02.View;
import ex02.ViewResult;

public class RestoreConsoleCommand implements ConsoleCommand {
    private View view;

    public RestoreConsoleCommand(View view) {
        this.view = view;
    }

    @Override
    public void execute() {
        try {
            ((ViewResult) view).viewRestore();
            System.out.println("Data restored.");
            view.viewShow();
        } catch (Exception e) {
            System.out.println("Restore error: " + e.getMessage());
        }
    }

    @Override
    public char getKey() {
        return 'r';
    }

    @Override
    public String toString() {
        return "'r' - restore data";
    }
}