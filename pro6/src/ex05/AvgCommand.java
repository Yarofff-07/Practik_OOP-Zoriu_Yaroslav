package ex05;

import java.util.concurrent.TimeUnit;
import ex01.Item2d;
import ex02.ViewResult;
import ex04.Command;

/**
 * Команда для обчислення середнього значення y.
 */
public class AvgCommand implements Command {

    /** Результат */
    private double result = 0.0;

    /** Прогрес виконання */
    private int progress = 0;

    /** Дані для обробки */
    private ViewResult viewResult;

    /**
     * Конструктор.
     * @param viewResult об'єкт з колекцією
     */
    public AvgCommand(ViewResult viewResult) {
        this.viewResult = viewResult;
    }

    /**
     * Повертає середнє значення.
     * @return результат
     */
    public double getResult() {
        return result;
    }

    /**
     * Перевіряє, чи ще виконується команда.
     * @return true, якщо ще виконується
     */
    public boolean running() {
        return progress < 100;
    }

    /**
     * Виконує обчислення середнього арифметичного y.
     */
    @Override
    public void execute() {
        progress = 0;
        result = 0.0;

        int size = viewResult.getItems().size();
        if (size == 0) {
            progress = 100;
            return;
        }

        System.out.println("Average executed...");

        int idx = 1;
        int step = Math.max(1, size / 2);

        for (Item2d item : viewResult.getItems()) {
            result += item.getY();
            progress = idx * 100 / size;

            if (idx % step == 0) {
                System.out.println("Average " + progress + "%");
            }

            try {
                TimeUnit.MILLISECONDS.sleep(2000 / size);
            } catch (InterruptedException e) {
                System.err.println(e);
                Thread.currentThread().interrupt();
                return;
            }

            idx++;
        }

        result /= size;
        System.out.println("Average done. Result = " + String.format("%.2f", result));
        progress = 100;
    }
}