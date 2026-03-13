package ex04;

import ex02.View;
import ex02.ViewResult;

public class GenerateConsoleCommand implements ConsoleCommand {
    private View view;

    public GenerateConsoleCommand(View view) {
        this.view = view;
    }

    @Override
    public void execute() {
        ((ViewResult) view).viewInit();
        System.out.println("Data generated.");
        view.viewShow();
    }

    @Override
    public char getKey() {
        return 'g';
    }

    @Override
    public String toString() {
        return "'g' - generate data";
    }
}