package ex05;

import java.util.concurrent.TimeUnit;
import ex02.ViewResult;
import ex04.Command;

/**
 * Команда для пошуку максимального значення y.
 * Зберігає індекс елемента з максимальним значенням.
 */
public class MaxCommand implements Command {

    /** Індекс результату */
    private int result = -1;

    /** Прогрес виконання */
    private int progress = 0;

    /** Дані для обробки */
    private ViewResult viewResult;

    /**
     * Конструктор.
     * @param viewResult об'єкт з колекцією
     */
    public MaxCommand(ViewResult viewResult) {
        this.viewResult = viewResult;
    }

    /**
     * Повертає індекс знайденого максимуму.
     * @return індекс
     */
    public int getResult() {
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
     * Виконує пошук максимального значення y.
     */
    @Override
    public void execute() {
        progress = 0;

        int size = viewResult.getItems().size();
        if (size == 0) {
            progress = 100;
            return;
        }

        System.out.println("Max executed...");

        result = 0;
        int step = Math.max(1, size / 3);

        for (int idx = 1; idx < size; idx++) {
            if (viewResult.getItems().get(result).getY() <
                viewResult.getItems().get(idx).getY()) {
                result = idx;
            }

            progress = idx * 100 / size;

            if (idx % step == 0) {
                System.out.println("Max " + progress + "%");
            }

            try {
                TimeUnit.MILLISECONDS.sleep(3000 / size);
            } catch (InterruptedException e) {
                System.err.println(e);
                Thread.currentThread().interrupt();
                return;
            }
        }

        System.out.println("Max done. Item #" + result +
                " found: " + viewResult.getItems().get(result));
        progress = 100;
    }
}