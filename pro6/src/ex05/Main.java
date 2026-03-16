package ex05;

import ex02.View;
import ex02.ViewableResult;
import ex04.ChangeConsoleCommand;
import ex04.GenerateConsoleCommand;
import ex04.Menu;
import ex04.ViewConsoleCommand;

/**
 * Головний клас програми.
 */
public class Main {

    /** Об'єкт для роботи з результатами */
    private View view = new ViewableResult().getView();

    /** Меню команд */
    private Menu menu = new Menu();

    /**
     * Запуск програми.
     */
    public void run() {
        menu.add(new ViewConsoleCommand(view));
        menu.add(new GenerateConsoleCommand(view));
        menu.add(new ChangeConsoleCommand(view));
        menu.add(new ExecuteConsoleCommand(view));
        menu.execute();
    }

    /**
     * Точка входу.
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }
}