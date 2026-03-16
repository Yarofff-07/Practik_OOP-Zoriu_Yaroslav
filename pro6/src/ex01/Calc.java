package ex01;

import java.io.*;

/**
 * Клас для обчислення (агрегує Item2d) і серіалізації/десеріалізації результату.
 * Індивідуальне завдання №6:
 * знайти найбільшу довжину послідовності 1 у двійковому поданні N,
 * де N = ціла частина ( v^2 + v^3 ), v = 10*cos(alpha), alpha в градусах.
 */
public class Calc {

    private static final String FNAME = "Item2d.bin";

    /** Агрегування: Calc "має" об'єкт Item2d */
    private Item2d result;

    public Calc() {
        result = new Item2d();
    }

    public Item2d getResult() {
        return result;
    }

    /**
     * Обчислення індивідуального завдання №6.
     * @param alphaDeg кут alpha в градусах
     * @return найбільша довжина послідовності 1 у двійковому поданні |N|
     */
    public int initIndividual6(double alphaDeg) {
        // v = 10*cos(alpha)
        double v = 10.0 * Math.cos(Math.toRadians(alphaDeg));

        // S = v^2 + v^3
        double s = v * v + v * v * v;

        // N = ціла частина S
        long n = (long) s;

        // зберігаємо параметр і результат
        result.setX(alphaDeg); // transient: після restore стане 0.0
        result.setY(n);        // y збережеться (як double)

        return maxRunOfOnes(Math.abs(n));
    }

    /** Максимальна довжина підряд ідучих 1 у двійковому поданні числа n. */
    private int maxRunOfOnes(long n) {
        int best = 0;
        int cur = 0;

        while (n > 0) {
            if ((n & 1L) == 1L) {
                cur++;
                if (cur > best) best = cur;
            } else {
                cur = 0;
            }
            n >>= 1;
        }
        return best;
    }

    /** Показати поточний стан об'єкта. */
    public void show() {
        System.out.println(result);
    }

    /** Зберегти стан (серіалізація). */
    public void save() throws IOException {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(FNAME))) {
            os.writeObject(result);
        }
    }

    /** Відновити стан (десеріалізація). */
    public void restore() throws IOException, ClassNotFoundException {
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(FNAME))) {
            result = (Item2d) is.readObject();
        }
    }
}