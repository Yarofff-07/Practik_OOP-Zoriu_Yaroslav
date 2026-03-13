package ex04;

import java.io.IOException;

import ex02.View;
import ex02.ViewResult;

public class SaveConsoleCommand implements ConsoleCommand {
    private View view;

    public SaveConsoleCommand(View view) {
        this.view = view;
    }

    @Override
    public void execute() {
        try {
            ((ViewResult) view).viewSave();
            System.out.println("Data saved.");
        } catch (IOException e) {
            System.out.println("Save error: " + e.getMessage());
        }
    }

    @Override
    public char getKey() {
        return 's';
    }

    @Override
    public String toString() {
        return "'s' - save data";
    }
}