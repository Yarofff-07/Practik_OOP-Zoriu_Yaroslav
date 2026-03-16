# Паралельне виконання. Шаблон Worker Thread

## Постановка задачі

### **Завдання:**

1. Продемонструвати можливість **паралельної обробки елементів колекції**  
   (пошук максимуму, мінімуму, обчислення середнього значення тощо).

2. Реалізувати **керування чергою задач (команд)** за допомогою шаблону **Worker Thread**.

3. Забезпечити **діалоговий інтерфейс користувача** для виконання команд програми.

4. Розробити **клас для тестування функціональності програми**.

5. Використовувати **коментарі javadoc** для можливості автоматичного створення документації.

---

## Опис проекту

У цьому проекті реалізовано консольну програму на мові **Java**, яка демонструє роботу  
шаблону проектування **Worker Thread** для виконання задач у паралельному режимі.

Програма працює з колекцією об'єктів `Item2d`, що містять координати `(x, y)`.  
Для обробки цієї колекції використовуються окремі команди:

- пошук **максимального значення**
- обчислення **середнього значення**
- пошук **мінімального додатного та максимального від'ємного значення**

Ці команди виконуються через **чергу задач**, яка обслуговується окремим робочим потоком.

Під час виконання команди `execute` створюються дві черги задач, що дозволяє  
виконувати обробку колекції **паралельно**.

---

![Структура проекту](https://github.com/Yarofff-07/Practik_OOP-Zoriu_Yaroslav/blob/task-6-(09.03.26)/pro6/img/struct.png)

---

## Queue.java

`src/ex05/Queue.java` – інтерфейс черги задач, який визначає методи для  
додавання та отримання команд із черги.

<details>
<summary>Код</summary>

```java
package ex05;

import ex04.Command;

/**
 * Інтерфейс черги задач.
 * Використовується в шаблоні Worker Thread.
 */
public interface Queue {
    /**
     * Додає команду в чергу.
     * @param cmd команда
     */
    void put(Command cmd);

    /**
     * Забирає команду з черги.
     * @return команда або null, якщо роботу завершено
     */
    Command take();
}
```
</details>

## CommandQueue.java

`src/ex05/CommandQueue.java` – клас черги задач, який реалізує шаблон
Worker Thread.

Створює робочий потік, що постійно перевіряє чергу та виконує команди.

<details>
<summary>Код</summary>

```java
package ex05;

import java.util.Vector;
import ex04.Command;

/**
 * Черга команд з робочим потоком.
 * Реалізація шаблону Worker Thread.
 */
public class CommandQueue implements Queue {

    /** Черга задач */
    private Vector<Command> tasks;

    /** Ознака очікування */
    private boolean waiting;

    /** Ознака завершення */
    private boolean shutdown;

    /**
     * Конструктор.
     * Ініціалізує чергу і запускає робочий потік.
     */
    public CommandQueue() {
        tasks = new Vector<Command>();
        waiting = false;
        shutdown = false;
        new Thread(new Worker()).start();
    }

    /**
     * Завершує роботу черги.
     */
    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }

    /**
     * Додає команду в чергу.
     * @param cmd команда
     */
    @Override
    public void put(Command cmd) {
        tasks.add(cmd);
        if (waiting) {
            synchronized (this) {
                notifyAll();
            }
        }
    }

    /**
     * Повертає команду з черги.
     * Якщо задач немає, потік очікує.
     * @return команда або null, якщо черга завершена
     */
    @Override
    public Command take() {
        while (tasks.isEmpty() && !shutdown) {
            synchronized (this) {
                waiting = true;
                try {
                    wait();
                } catch (InterruptedException e) {
                    waiting = false;
                    Thread.currentThread().interrupt();
                    return null;
                }
                waiting = false;
            }
        }

        if (tasks.isEmpty()) {
            return null;
        }

        return tasks.remove(0);
    }

    /**
     * Внутрішній робочий потік.
     */
    private class Worker implements Runnable {
        @Override
        public void run() {
            while (!shutdown || !tasks.isEmpty()) {
                Command command = take();
                if (command != null) {
                    command.execute();
                }
            }
        }
    }
}
```
</details>

## MaxCommand.java

`src/ex05/MaxCommand.java` – команда, що виконує пошук
максимального значення y у колекції об'єктів.

<details>
<summary>Код</summary>

```java
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
```
</details>

## AvgCommand.java

`src/ex05/AvgCommand.java` – команда, що обчислює
середнє арифметичне значення y для всієї колекції.

<details>
<summary>Код</summary>

```java
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
```
</details>

## MinMaxCommand.java

`src/ex05/MinMaxCommand.java` – команда для пошуку:

- мінімального додатного значення
- максимального від'ємного значення
- у колекції об'єктів.

<details>
<summary>Код</summary>

```java
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
```
</details>

## ExecuteConsoleCommand.java

`src/ex05/ExecuteConsoleCommand.java` – консольна команда, яка запускає
паралельну обробку колекції.

При виконанні цієї команди:

- створюються дві черги задач
- у черги додаються команди обробки
- програма очікує завершення виконання всіх задач

<details>
<summary>Код</summary>

```java
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
```
</details>

## Main.java

`src/ex05/Main.java` – головний клас програми.

Містить метод main() та реалізує запуск консольного меню.

У меню доступні команди:

- v - view data
- g - generate data
- c - change data
- e - execute parallel processing
- q - quit

<details>
<summary>Код</summary>

```java
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
```
</details>

## MainTest.java

`test/ex05/MainTest.java` – клас для модульного тестування програми.

Тести перевіряють:

- коректність роботи MaxCommand
- коректність роботи AvgCommand
- коректність роботи MinMaxCommand
- правильність виконання команд через CommandQueue

<details>
<summary>Код</summary>

```java
package ex05;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ex01.Item2d;
import ex02.ViewResult;

/**
 * Тести для класів ex05.
 */
public class MainTest {

    private static ViewResult view;

    private static MaxCommand max1;
    private static MaxCommand max2;

    private static AvgCommand avg1;
    private static AvgCommand avg2;

    private static MinMaxCommand min1;
    private static MinMaxCommand min2;

    /**
     * Виконується перед усіма тестами.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        view = new ViewResult();
        view.getItems().clear();

        view.getItems().add(new Item2d(1.0, 2.0));
        view.getItems().add(new Item2d(5.0, -3.0));
        view.getItems().add(new Item2d(3.0, 4.0));
        view.getItems().add(new Item2d(2.0, -1.0));
        view.getItems().add(new Item2d(4.0, 1.5));

        max1 = new MaxCommand(view);
        max2 = new MaxCommand(view);

        avg1 = new AvgCommand(view);
        avg2 = new AvgCommand(view);

        min1 = new MinMaxCommand(view);
        min2 = new MinMaxCommand(view);
    }

    /**
     * Виконується після всіх тестів.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        assertEquals(max1.getResult(), max2.getResult());
        assertEquals(avg1.getResult(), avg2.getResult(), 0.0001);
        assertEquals(min1.getResultMin(), min2.getResultMin());
        assertEquals(min1.getResultMax(), min2.getResultMax());
    }

    /**
     * Перевірка MaxCommand.
     */
    @Test
    public void testMax() {
        max1.execute();
        assertTrue(max1.getResult() > -1);
        assertEquals(2, max1.getResult());
    }

    /**
     * Перевірка AvgCommand.
     */
    @Test
    public void testAvg() {
        avg1.execute();
        assertEquals(0.7, avg1.getResult(), 0.0001);
    }

    /**
     * Перевірка MinMaxCommand.
     */
    @Test
    public void testMin() {
        min1.execute();
        assertEquals(4, min1.getResultMin());
        assertEquals(3, min1.getResultMax());
    }

    /**
     * Перевірка MaxCommand через чергу.
     */
    @Test
    public void testMaxQueue() {
        CommandQueue queue = new CommandQueue();
        queue.put(max2);

        try {
            while (max2.running()) {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            queue.shutdown();
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }

        assertEquals(2, max2.getResult());
    }

    /**
     * Перевірка AvgCommand через чергу.
     */
    @Test
    public void testAvgQueue() {
        CommandQueue queue = new CommandQueue();
        queue.put(avg2);

        try {
            while (avg2.running()) {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            queue.shutdown();
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }

        assertEquals(0.7, avg2.getResult(), 0.0001);
    }

    /**
     * Перевірка MinMaxCommand через чергу.
     */
    @Test
    public void testMinQueue() {
        CommandQueue queue = new CommandQueue();
        queue.put(min2);

        try {
            while (min2.running()) {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            queue.shutdown();
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }

        assertEquals(4, min2.getResultMin());
        assertEquals(3, min2.getResultMax());
    }
}
```
</details>

---

## Приклад роботи програми

Генерація даних

![Тест програми](https://github.com/Yarofff-07/Practik_OOP-Zoriu_Yaroslav/blob/task-6-(09.03.26)/pro6/img/w1.png)

Паралельне виконання задач

![Тест програми](https://github.com/Yarofff-07/Practik_OOP-Zoriu_Yaroslav/blob/task-6-(09.03.26)/pro6/img/w2.png)
