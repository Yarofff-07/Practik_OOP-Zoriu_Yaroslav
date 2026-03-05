# Класи та об'єкти

## Постановка задачі

### **Завдання:**

1. **Серіалізація**
   * Розробити клас, що серіалізується, для зберігання параметрів і результатів обчислень.  
   * Використовуючи **агрегування**, розробити клас для знаходження рішення задачі.

2. **Transient поля**
   * Розробити клас для демонстрації в діалоговому режимі збереження та відновлення стану об'єкта, використовуючи серіалізацію.  
   * Показати особливості використання **transient** полів.

3. **Десеріалізація**
   * Розробити клас для тестування коректності результатів обчислень та серіалізації/десеріалізації.  
   * Використовувати докладні коментарі для автоматичної генерації документації засобами **javadoc**.

4. **Індивідуальне завдання (варіант 6)**
   * Визначити найбільшу довжину послідовності `1` в подвійному поданні цілісної суми квадрата і куба `10*cos(α)`.

---

## Структура проекту

![Структура проекту](https://github.com/Yarofff-07/Praktik_OOP-Zoriu_Yaroslav/blob/task-2/pro2/img/str.png?raw=true)

---

## Calc.java
```java
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
```
## Item2d.java
```java
package ex01;

import java.io.Serializable;

/**
 * Клас для зберігання параметрів і результатів обчислень.
 * Демонструє використання Serializable і transient.
 */
public class Item2d implements Serializable {

    private static final long serialVersionUID = 1L;

    /** параметр (кут alpha) */
    private transient double x;

    /** результат обчислення */
    private double y;

    public Item2d() {
        x = 0;
        y = 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Item2d{x=" + x + ", y=" + y + "}";
    }
}
```
## Main.java
```java
package ex01;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Головний клас програми.
 * Демонструє роботу обчислень і серіалізації.
 */
public class Main {

    public static void main(String[] args) {

        Calc calc = new Calc();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String s;

        while (true) {

            System.out.println("\nCommands:");
            System.out.println("i - individual task");
            System.out.println("v - view result");
            System.out.println("s - save result");
            System.out.println("r - restore result");
            System.out.println("q - quit");

            System.out.print("Enter command: ");

            try {
                s = in.readLine();
            } catch (Exception e) {
                System.out.println("Input error");
                continue;
            }

            if (s == null || s.isEmpty())
                continue;

            char c = s.charAt(0);

            try {

                if (c == 'q') {
                    System.out.println("Program finished.");
                    break;
                }

                else if (c == 'i') {

                    System.out.print("Enter alpha (degrees): ");
                    double a = Double.parseDouble(in.readLine());

                    int max = calc.initIndividual6(a);

                    long n = (long) calc.getResult().getY();

                    System.out.println("N = " + n);
                    System.out.println("Binary = " + Long.toBinaryString(Math.abs(n)));
                    System.out.println("Max sequence of 1 = " + max);
                }

                else if (c == 'v') {
                    calc.show();
                }

                else if (c == 's') {
                    calc.save();
                    System.out.println("Saved.");
                }

                else if (c == 'r') {
                    calc.restore();
                    System.out.println("Restored.");
                    calc.show();
                }

                else {
                    System.out.println("Unknown command.");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        }
    }
}
```
## MainTest.java
```java
package ex01;

/**
 * Клас для тестування коректності обчислень і серіалізації/десеріалізації.
 * Запускається як звичайна програма.
 */
public class MainTest {

    public static void main(String[] args) throws Exception {

        Calc calc = new Calc();

        // ---- Тест 1: перевірка обчислення (alpha = 0) ----
        // cos(0)=1 -> v=10
        // N = 10^2 + 10^3 = 1100
        // binary(1100)=10001001100, max run of 1 = 2
        int max = calc.initIndividual6(0.0);
        long n = (long) calc.getResult().getY();

        boolean okCalc = (n == 1100) && (max == 2);

        if (okCalc) {
            System.out.println("testCalc: OK");
        } else {
            System.out.println("testCalc: FAIL");
            System.out.println("N = " + n + " (expected 1100)");
            System.out.println("max = " + max + " (expected 2)");
        }

        // ---- Тест 2: серіалізація/десеріалізація ----
        calc.save();

        // змінюємо стан
        calc.initIndividual6(10.0);

        // відновлюємо
        calc.restore();

        long restoredN = (long) calc.getResult().getY();
        double restoredAlpha = calc.getResult().getX(); // transient -> має стати 0.0

        boolean okRestoreY = (restoredN == 1100);
        boolean okTransientX = (Math.abs(restoredAlpha - 0.0) < 1e-9);

        if (okRestoreY) {
            System.out.println("testRestoreY: OK");
        } else {
            System.out.println("testRestoreY: FAIL (restoredN=" + restoredN + ")");
        }

        if (okTransientX) {
            System.out.println("testTransientX: OK");
        } else {
            System.out.println("testTransientX: FAIL (restoredAlpha=" + restoredAlpha + ")");
        }
    }
}
```
---
