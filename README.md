# Factory Method, текстова таблиця та тестування

## Постановка задачі

Використовуючи вихідний код попередньої лабораторної роботи, необхідно було розширити ієрархію класів за допомогою шаблону проєктування **Factory Method (Virtual Constructor)**.

У програмі реалізовано подання результатів обчислень у вигляді **текстової таблиці**.  
Параметри відображення таблиці, зокрема її ширина, визначаються користувачем у діалоговому режимі через консоль.

У ході виконання роботи було:
- реалізовано шаблон **Factory Method**;
- продемонстровано **перевизначення методів (overriding)**;
- продемонстровано **перевантаження методів і конструкторів (overloading)**;
- показано **поліморфізм і динамічне зв’язування**;
- забезпечено **діалоговий інтерфейс користувача**;
- створено **клас для тестування основної функціональності**;
- використано **javadoc-коментарі** для автоматичної генерації документації.

## Опис проєкту

Цей проєкт є продовженням попередньої лабораторної роботи.  
У ньому реалізовано виведення результатів обчислень у вигляді текстової таблиці з використанням шаблону **Factory Method**.

Програма працює у консольному режимі та підтримує такі команди:
- `g` — генерація даних;
- `v` — перегляд поточних даних;
- `s` — збереження результатів;
- `r` — відновлення збережених даних;
- `q` — завершення роботи програми.

Під час генерації результатів користувач може задати **ширину таблиці**, яка використовується для форматування виводу.

## Структура проєкту

![Структура проекту](https://github.com/Yarofff-07/Practik_OOP-Zoriu_Yaroslav/blob/task-4-(05.03.26)/pro4/img/struct.png)

---

## Main.java

`src/ex03/Main.java` — головний клас програми.
Наслідує клас Main із попередньої лабораторної роботи та запускає програму з новим способом відображення результатів через ViewableTable.

<details>
<summary>Код</summary>

```java
package ex03;

import ex02.View;

/**
 * Обчислення і відображення результатів.
 * Містить реалізацію статичного методу main().
 */
public class Main extends ex02.Main {

    /**
     * Конструктор.
     * @param view об'єкт для відображення результатів
     */
    public Main(View view) {
        super(view);
    }

    /**
     * Точка входу в програму.
     * @param args параметри командного рядка
     */
    public static void main(String[] args) {
        Main main = new Main(new ViewableTable().getView());
        main.menu();
    }
}
```
</details>

## ViewableTable.java

`src/ex03/ViewableTable.java` — клас-фабрика, який реалізує шаблон Factory Method.
Він створює об’єкт ViewTable, що використовується для подання результатів у вигляді текстової таблиці.

<details>
<summary>Код</summary>

```java
package ex03;

import ex02.View;
import ex02.ViewableResult;

/**
 * ConcreteCreator для шаблону Factory Method.
 * Створює об'єкти типу ViewTable.
 */
public class ViewableTable extends ViewableResult {

    /**
     * Повертає новий об'єкт для відображення у вигляді таблиці.
     * @return об'єкт View
     */
    @Override
    public View getView() {
        return new ViewTable();
    }
}
```
</details>

## ViewTable.java

`src/ex03/ViewTable.java` — клас для подання результатів у вигляді текстової таблиці.
У ньому реалізовано:
- збереження ширини таблиці;
- форматований вивід заголовка, тіла та нижньої межі таблиці;
- перевантажені методи init(...);
- перевизначені методи viewHeader(), viewBody(), viewFooter();
- введення ширини таблиці користувачем через консоль.

<details>
<summary>Код</summary>

```juava
package ex03;

import java.util.Formatter;
import java.util.Scanner;
import ex01.Item2d;
import ex02.ViewResult;

/**
 * ConcreteProduct для шаблону Factory Method.
 * Відображає результати у вигляді таблиці.
 */
public class ViewTable extends ViewResult {

    /** Ширина таблиці за замовчуванням */
    private static final int DEFAULT_WIDTH = 20;

    /** Поточна ширина таблиці */
    private int width;

    /**
     * Конструктор без параметрів.
     */
    public ViewTable() {
        width = DEFAULT_WIDTH;
    }

    /**
     * Конструктор із шириною таблиці.
     * @param width ширина таблиці
     */
    public ViewTable(int width) {
        this.width = width;
    }

    /**
     * Конструктор із шириною таблиці та кількістю елементів.
     * @param width ширина таблиці
     * @param n кількість елементів
     */
    public ViewTable(int width, int n) {
        super(n);
        this.width = width;
    }

    /**
     * Встановлює нову ширину таблиці.
     * @param width нова ширина
     * @return встановлене значення
     */
    public int setWidth(int width) {
        this.width = width;
        return this.width;
    }

    /**
     * Повертає поточну ширину таблиці.
     * @return ширина таблиці
     */
    public int getWidth() {
        return width;
    }

    /**
     * Виводить горизонтальну лінію таблиці.
     */
    private void outLine() {
        for (int i = 0; i < width; i++) {
            System.out.print("-");
        }
    }

    /**
     * Виводить горизонтальну лінію і перехід на новий рядок.
     */
    private void outLineLn() {
        outLine();
        System.out.println();
    }

    /**
     * Виводить заголовок таблиці.
     */
    private void outHeader() {
        Formatter fmt = new Formatter();
        fmt.format("%s%d%s%2$d%s", "%", (width - 3) / 2, "s | %", "s\n");
        System.out.printf(fmt.toString(), "x", "y");
        fmt.close();
    }

    /**
     * Виводить тіло таблиці.
     */
    private void outBody() {
        Formatter fmt = new Formatter();
        fmt.format("%s%d%s%2$d%s", "%", (width - 3) / 2, ".0f | %", ".3f\n");

        for (Item2d item : getItems()) {
            System.out.printf(fmt.toString(), item.getX(), item.getY());
        }

        fmt.close();
    }

    /**
     * Перевантаження методу init().
     * @param width ширина таблиці
     */
    public final void init(int width) {
        this.width = width;
        viewInit();
    }

    /**
     * Перевантаження методу init().
     * @param width ширина таблиці
     * @param stepX крок зміни x
     */
    public final void init(int width, double stepX) {
        this.width = width;
        init(stepX);
    }

    /**
     * Перевизначення методу init().
     * Користувач задає ширину таблиці з клавіатури.
     * @param stepX крок зміни x
     */
    @Override
    public void init(double stepX) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter table width: ");
        width = sc.nextInt();

        System.out.print("Initialization... ");
        super.init(stepX);
        System.out.println("done.");
    }

    /**
     * Перевизначення виводу заголовка.
     */
    @Override
    public void viewHeader() {
        outHeader();
        outLineLn();
    }

    /**
     * Перевизначення виводу тіла таблиці.
     */
    @Override
    public void viewBody() {
        outBody();
    }

    /**
     * Перевизначення виводу нижньої частини таблиці.
     */
    @Override
    public void viewFooter() {
        outLineLn();
    }
}
```
</details>

## MainTest.java

`test/ex03/MainTest.java` — клас для тестування основної функціональності програми.
У тестах перевіряються:
- правильність обчислень;
- коректність роботи таблиці;
- серіалізація та десеріалізація даних.

<details>
<summary>Код</summary>

```java
package ex03;

import ex01.Item2d;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Клас для тестування основної функціональності.
 */
public class MainTest {

    /**
     * Перевірка обчислень і параметрів таблиці.
     */
    @Test
    public void testCalc() {
        ViewTable tbl = new ViewTable(10, 5);

        assertEquals(10, tbl.getWidth());
        assertEquals(5, tbl.getItems().size());

        tbl.init(40, 90.0);

        Item2d item = new Item2d();
        int ctr = 0;

        item.setXY(0.0, 0.0);
        assertTrue(tbl.getItems().get(ctr).equals(item));
        ctr++;

        item.setXY(90.0, 1.0);
        assertTrue(tbl.getItems().get(ctr).equals(item));
        ctr++;

        item.setXY(180.0, 0.0);
        assertTrue(tbl.getItems().get(ctr).equals(item));
        ctr++;

        item.setXY(270.0, -1.0);
        assertTrue(tbl.getItems().get(ctr).equals(item));
        ctr++;

        item.setXY(360.0, 0.0);
        assertTrue(tbl.getItems().get(ctr).equals(item));
    }

    /**
     * Перевірка серіалізації та відновлення даних.
     */
    @Test
    public void testRestore() {
        ViewTable tbl1 = new ViewTable(10, 1000);
        ViewTable tbl2 = new ViewTable();

        tbl1.init(30, Math.random() * 100.0);

        try {
            tbl1.viewSave();
        } catch (IOException e) {
            throw new AssertionError(e.getMessage());
        }

        try {
            tbl2.viewRestore();
        } catch (Exception e) {
            throw new AssertionError(e.getMessage());
        }

        assertEquals(tbl1.getItems().size(), tbl2.getItems().size());
        assertTrue(tbl1.getItems().containsAll(tbl2.getItems()));
    }
}
```
</details>

---

## Скриншоти роботи

![скрін1](https://github.com/Yarofff-07/Practik_OOP-Zoriu_Yaroslav/blob/task-4-(05.03.26)/pro4/img/w1.png)

![скрін2](https://github.com/Yarofff-07/Practik_OOP-Zoriu_Yaroslav/blob/task-4-(05.03.26)/pro4/img/w2.png)
