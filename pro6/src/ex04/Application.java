package ex04;

import java.util.Stack;

import ex02.View;
import ex03.ViewableTable;

public class Application {
    private static Application instance = new Application();

    private View view;
    private Menu menu;
    private Stack<UndoableCommand> history;

    private Application() {
        view = new ViewableTable().getView();
        menu = new Menu();
        history = new Stack<UndoableCommand>();
    }

    public static Application getInstance() {
        return instance;
    }

    public View getView() {
        return view;
    }

    public void addToHistory(UndoableCommand command) {
        history.push(command);
    }

    public void undoLast() {
        if (history.empty()) {
            System.out.println("Nothing to undo.");
            return;
        }
        history.pop().undo();
    }

    public void run() {
        menu.add(new GenerateConsoleCommand(view));
        menu.add(new ViewConsoleCommand(view));
        menu.add(new ChangeConsoleCommand(view));
        menu.add(new SaveConsoleCommand(view));
        menu.add(new RestoreConsoleCommand(view));
        menu.add(new UndoConsoleCommand());

        MacroCommand macro = new MacroCommand();
        macro.add(new GenerateConsoleCommand(view));
        macro.add(new ViewConsoleCommand(view));
        menu.add(macro);

        menu.execute();
    }
}