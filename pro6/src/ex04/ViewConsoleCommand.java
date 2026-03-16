package ex04;

import ex02.View;

public class ViewConsoleCommand implements ConsoleCommand {
    private View view;

    public ViewConsoleCommand(View view) {
        this.view = view;
    }

    @Override
    public void execute() {
        view.viewShow();
    }

    @Override
    public char getKey() {
        return 'v';
    }

    @Override
    public String toString() {
        return "'v' - view data";
    }
}