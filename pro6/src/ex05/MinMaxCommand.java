package ex05;

import java.util.concurrent.TimeUnit;
import ex01.Item2d;
import ex02.ViewResult;
import ex04.Command;

/**
 * Команда для пошуку:
 * мінімального додатного y
 * і максимального від'ємного y.
 */
public class MinMaxCommand implements Command {

    /** Індекс мінімального додатного */
    private int resultMin = -1;

    /** Індекс максимального від'ємного */
    private int resultMax = -1;

    /** Прогрес виконання */
    private int progress = 0;

    /** Дані для обробки */
    private ViewResult viewResult;

    /**
     * Конструктор.
     * @param viewResult об'єкт з колекцією
     */
    public MinMaxCommand(ViewResult viewResult) {
        this.viewResult = viewResult;
    }

    /**
     * Повертає індекс мінімального додатного.
     * @return індекс
     */
    public int getResultMin() {
        return resultMin;
    }

    /**
     * Повертає індекс максимального від'ємного.
     * @return індекс
     */
    public int getResultMax() {
        return resultMax;
    }

    /**
     * Перевіряє, чи ще виконується команда.
     * @return true, якщо ще виконується
     */
    public boolean running() {
        return progress < 100;
    }

    /**
     * Виконує пошук мінімального додатного і максимального від'ємного y.
     */
    @Override
    public void execute() {
        progress = 0;
        resultMin = -1;
        resultMax = -1;

        int size = viewResult.getItems().size();
        if (size == 0) {
            progress = 100;
            return;
        }

        System.out.println("MinMax executed...");

        int idx = 0;
        int step = Math.max(1, size / 5);

        for (Item2d item : viewResult.getItems()) {
            if (item.getY() < 0) {
                if (resultMax == -1 ||
                    viewResult.getItems().get(resultMax).getY() < item.getY()) {
                    resultMax = idx;
                }
            } else if (item.getY() > 0) {
                if (resultMin == -1 ||
                    viewResult.getItems().get(resultMin).getY() > item.getY()) {
                    resultMin = idx;
                }
            }

            idx++;
            progress = idx * 100 / size;

            if (idx % step == 0) {
                System.out.println("MinMax " + progress + "%");
            }

            try {
                TimeUnit.MILLISECONDS.sleep(5000 / size);
            } catch (InterruptedException e) {
                System.err.println(e);
                Thread.currentThread().interrupt();
                return;
            }
        }

        System.out.print("MinMax done. ");

        if (resultMin > -1) {
            System.out.print("Min positive #" + resultMin + " found: "
                    + String.format("%.2f", viewResult.getItems().get(resultMin).getY()));
        } else {
            System.out.print("Min positive not found.");
        }

        if (resultMax > -1) {
            System.out.println(" Max negative #" + resultMax + " found: "
                    + String.format("%.2f", viewResult.getItems().get(resultMax).getY()));
        } else {
            System.out.println(" Max negative item not found.");
        }

        progress = 100;
    }
}