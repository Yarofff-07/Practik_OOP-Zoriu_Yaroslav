# Практична робота №2
## Тема: Класи, об'єкти та серіалізація в Java

---

## Мета роботи

Ознайомитись із використанням класів та об'єктів у Java,  
вивчити механізм **серіалізації та десеріалізації**, а також
розглянути особливості використання **transient полів**.

---

## Постановка задачі

1. Розробити клас, що серіалізується, для зберігання параметрів
та результатів обчислень.

2. Використовуючи **агрегування**, створити клас для обчислення
та збереження результату задачі.

3. Реалізувати механізм **серіалізації та десеріалізації об'єкта**
у файл.

4. Продемонструвати використання **transient поля**, яке не
зберігається під час серіалізації.

5. Реалізувати тестовий клас для перевірки правильності обчислень
та роботи серіалізації.

6. Індивідуальне завдання (варіант 6):
   визначити найбільшу довжину послідовності `1` у двійковому
   поданні числа  


---

## Реалізація

У програмі використано:

- **класи та об'єкти**
- **агрегування**
- **серіалізацію та десеріалізацію**
- **transient поля**
- **консольний діалоговий режим роботи**

Програма дозволяє:

- виконувати обчислення для заданого значення `α`
- переглядати результат
- зберігати результат у файл
- відновлювати результат із файлу
- перевіряти коректність роботи через тестовий клас

---

## Структура проєкту

![Структура проекту](https://github.com/Yarofff-07/Practik_OOP-Zoriu_Yaroslav/blob/task-2-(03.03.26)/pro2/img/str.png)

---

## Calc.java

`src/ex01/Calc.java` — клас, що виконує обчислення індивідуального
завдання та реалізує серіалізацію і десеріалізацію результатів.

Містить:

- обчислення значення `N`
- пошук максимальної послідовності `1` у двійковому поданні
- збереження результату у файл
- відновлення результату з файлу

<details>
<summary>Код</summary>

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
</details>

## Item2d.java

`src/ex01/Item2d.java` — клас для зберігання параметрів та результатів
обчислення.

Клас реалізує інтерфейс Serializable та демонструє використання
transient поля, яке не зберігається під час серіалізації.

<details> 
<summary>Код</summary>
  
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
</details>

## Main.java

`src/ex01/Main.java` — головний клас програми.

Відповідає за:
- запуск програми
- обробку команд користувача
- виклик методів обчислення
- збереження та відновлення результатів
- Програма працює у консольному діалоговому режимі.

<details> 
<summary>Код</summary>

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
</details> 

## MainTest.java

`src/ex01/MainTest.java` — тестовий клас для перевірки
правильності обчислень та роботи серіалізації.

Тести перевіряють:
- правильність розрахунку значення N
- максимальну довжину послідовності 1
- коректність серіалізації та десеріалізації
- роботу transient поля

<details>
<summary>Код</summary>

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
</details>

## Робота програми

![Program](https://github.com/Yarofff-07/Practik_OOP-Zoriu_Yaroslav/blob/task-2-(03.03.26)/pro2/img/rob1.png)

--- 

![Result](https://github.com/Yarofff-07/Practik_OOP-Zoriu_Yaroslav/blob/task-2-(03.03.26)/pro2/img/rob2.png)
