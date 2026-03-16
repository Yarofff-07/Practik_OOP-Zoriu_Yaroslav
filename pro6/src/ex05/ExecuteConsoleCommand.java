package ex05;

import java.util.concurrent.TimeUnit;
import ex02.View;
import ex02.ViewResult;
import ex04.ConsoleCommand;

/**
 * Консольна команда запуску паралельної обробки.
 */
public class ExecuteConsoleCommand implements ConsoleCommand {

    /** Об'єкт для роботи з даними */
    private View view;

    /**
     * Конструктор.
     * @param view об'єкт перегляду
     */
    public ExecuteConsoleCommand(View view) {
        this.view = view;
    }

    @Override
    public char getKey() {
        return 'e';
    }

    @Override
    public String toString() {
        return "'e' - execute parallel processing";
    }

    /**
     * Запускає паралельну обробку через дві черги.
     */
    @Override
    public void execute() {
        CommandQueue queue1 = new CommandQueue();
        CommandQueue queue2 = new CommandQueue();

        MaxCommand maxCommand = new MaxCommand((ViewResult) view);
        AvgCommand avgCommand = new AvgCommand((ViewResult) view);
        MinMaxCommand minMaxCommand = new MinMaxCommand((ViewResult) view);

        System.out.println("Execute all threads...");

        queue1.put(minMaxCommand);
        queue2.put(maxCommand);
        queue2.put(avgCommand);

        try {
            while (avgCommand.running() ||
                   maxCommand.running() ||
                   minMaxCommand.running()) {
                TimeUnit.MILLISECONDS.sleep(100);
            }

            queue1.shutdown();
            queue2.shutdown();

            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            System.err.println(e);
            Thread.currentThread().interrupt();
        }

        System.out.println("All done.");
    }
}