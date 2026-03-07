# Практична робота №3  
## Тема: Використання шаблону проєктування Factory Method

---

## Мета роботи

Ознайомитись з використанням шаблонів проєктування у об'єктно-орієнтованому програмуванні  
та реалізувати програму з використанням **патерну Factory Method**.

---

## Постановка задачі

1. Як основа використовується вихідний код попередньої лабораторної роботи.
Необхідно забезпечити збереження результатів обчислень у колекції з
можливістю їх збереження та відновлення.

2. Використовуючи шаблон проектування Factory Method (Virtual Constructor),
побудувати ієрархію класів, яка дозволяє розширювати систему шляхом
додавання нових класів для відображення результатів.

3. Розширити ієрархію інтерфейсом "фабрикованих" об'єктів, який визначає
методи для відображення результатів обчислень.

4. Реалізувати текстове виведення результатів та створити інтерфейс фабрики,
що відповідає за створення об'єктів відображення.

---

## Реалізація

У програмі використано патерн **Factory Method**, який дозволяє:

- створювати об'єкти через фабричний метод
- розширювати систему новими типами об'єктів без зміни існуючого коду
- відокремити створення об'єктів від їх використання

---

## Структура проєкту

![Структура проекту](https://github.com/Yarofff-07/Practik_OOP-Zoriu_Yaroslav/blob/task-2-(03.03.26)/pro2/img/str.png)

---

## Main.java

`src/ex02/Main.java` — головний клас програми.  
Відповідає за запуск програми, роботу діалогового меню та виклик методів
для відображення результатів обчислень. Є модифікованою версією класу з пакета
`ex01`, створеного у попередній практичній роботі.
<details>
<summary>Код</summary>

```java
package ex02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/** Виконання обчислень та відображення результатів
 * Містить реалізацію статичного методу main()
 * @author Зорій Ярослав
 * @version 2.0
 * @see Main#main(String[])
 */

public class Main {
    
    /** Об'єкт, що реалізує інтерфейс View */
    private View view;

    public Main(View view) {
        this.view = view;
    }

    protected void menu() {
        String s = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        do {
            do {
                System.out.println("Enter command...");
                System.out.print("'q'uit, 'v'iew, 'g'enerate, 's'ave, 'r'estore: ");

                try {
                    s = in.readLine();
                } catch (IOException e) {
                    System.out.println("Error: " + e);
                    return;
                }
            } while (s == null || s.length() != 1);

            switch (s.charAt(0)) {
                case 'q':
                    System.out.println("Exit.");
                    break;

                case 'v':
                    System.out.println("View current.");
                    view.viewShow();
                    break;

                case 'g':
                    System.out.println("Random generation.");
                    view.viewInit();
                    view.viewShow();
                    break;

                case 's':
                    System.out.println("Save current.");
                    try {
                        view.viewSave();
                    } catch (IOException e) {
                        System.out.println("Serialization error: " + e);
                    }
                    view.viewShow();
                    break;

                case 'r':
                    System.out.println("Restore last saved.");
                    try {
                        view.viewRestore();
                    } catch (Exception e) {
                        System.out.println("Serialization error: " + e);
                    }
                    view.viewShow();
                    break;

                default:
                    System.out.println("Wrong command.");
            }

        } while (s.charAt(0) != 'q');
    }

    public static void main(String[] args) {
        Main main = new Main(new ViewableResult().getView());
        main.menu();
    }
}
```
</details>

## View.java
`src/ex02/View.java` — інтерфейс, який визначає набір методів для
відображення результатів обчислень. Використовується як базовий тип
для всіх класів, що реалізують логіку виведення даних.
<details>
<summary>Код</summary>

```java
package ex02;

import java.io.IOException;

/**
 * Інтерфейс для відображення результатів.
 */
public interface View {
    void viewHeader();
    void viewBody();
    void viewFooter();
    void viewShow();
    void viewInit();
    void viewSave() throws IOException;
    void viewRestore() throws Exception;
}
```
</details>

## Viewable.java
`src/ex02/Viewable.java` — інтерфейс фабрики, що оголошує метод
створення об'єктів відображення. Є частиною реалізації шаблону
проектування Factory Method.
<details>
<summary>Код</summary>

```java
package ex02;

/**
 * Інтерфейс фабрики для створення об'єкта View.
 */
public interface Viewable {
    View getView();
}
```
</details>

## ViewableResult.java
`src/ex02/ViewableResult.java` — клас, що реалізує інтерфейс фабрики
та відповідає за створення об'єктів типу `ViewResult`.
<details>
<summary>Код</summary>

```java
package ex02;

/**
 * Фабрика для створення ViewResult.
 */
public class ViewableResult implements Viewable {

    @Override
    public View getView() {
        return new ViewResult();
    }
}
```
</details>

## ViewResult.java
`src/ex02/ViewResult.java` — клас, що реалізує інтерфейс `View`.
Містить логіку обчислення, збереження, відновлення та відображення
результатів роботи програми.
<details>
<summary>Код</summary>

```java
package ex02;

import ex01.Item2d;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Клас для обчислення, збереження та відображення результатів.
 */
public class ViewResult implements View {

    private static final String FNAME = "items.bin";
    private static final int DEFAULT_NUM = 10;

    private ArrayList<Item2d> items = new ArrayList<Item2d>();

    public ViewResult() {
        this(DEFAULT_NUM);
    }

    public ViewResult(int n) {
        for (int i = 0; i < n; i++) {
            items.add(new Item2d());
        }
    }

    public ArrayList<Item2d> getItems() {
        return items;
    }

    private double calc(double x) {
        return Math.sin(x * Math.PI / 180.0);
    }

    public void init(double stepX) {
        double x = 0.0;

        for (Item2d item : items) {
            item.setXY(x, calc(x));
            x += stepX;
        }
    }

    @Override
    public void viewInit() {
        init(Math.random() * 360.0);
    }

    @Override
    public void viewSave() throws IOException {
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(FNAME));
        os.writeObject(items);
        os.close();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void viewRestore() throws Exception {
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(FNAME));
        items = (ArrayList<Item2d>) is.readObject();
        is.close();
    }

    @Override
    public void viewHeader() {
        System.out.println("Results:");
    }

    @Override
    public void viewBody() {
        for (Item2d item : items) {
            System.out.printf("(%.0f; %.3f) ", item.getX(), item.getY());
        }
        System.out.println();
    }

    @Override
    public void viewFooter() {
        System.out.println("End.");
    }

    @Override
    public void viewShow() {
        viewHeader();
        viewBody();
        viewFooter();
    }
}
```
</details>

## Item2d.java
`src/ex01/Item2d.java` — клас для зберігання параметрів та результатів
обчислень. Використовується для формування елементів колекції
результатів.
<details>
<summary>Код</summary>

```java
package ex01;

import java.io.Serializable;

public class Item2d implements Serializable {
    private static final long serialVersionUID = 1L;

    private double x;
    private double y;

    public Item2d() {
        x = 0.0;
        y = 0.0;
    }

    public Item2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
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
        return "(" + x + "; " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Item2d)) {
            return false;
        }

        Item2d other = (Item2d) obj;

        return Double.compare(x, other.x) == 0 &&
               Double.compare(y, other.y) == 0;
    }
}
```
</details>

## MainTest.java
`test/ex02/MainTest.java` — клас для тестування роботи програми.
Містить тести для перевірки правильності обчислень та роботи
механізмів серіалізації і десеріалізації.
<details>
<summary>Код</summary>

```java
package ex02;

import ex01.Item2d;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Тести для ViewResult.
 */
public class MainTest {

    @Test
    public void testCalc() {
        ViewResult view = new ViewResult(5);
        view.init(90.0);

        Item2d item = new Item2d();
        int i = 0;

        item.setXY(0.0, 0.0);
        assertTrue(view.getItems().get(i).equals(item));
        i++;

        item.setXY(90.0, 1.0);
        assertTrue(view.getItems().get(i).equals(item));
        i++;

        item.setXY(180.0, 0.0);
        assertTrue(view.getItems().get(i).equals(item));
        i++;

        item.setXY(270.0, -1.0);
        assertTrue(view.getItems().get(i).equals(item));
        i++;

        item.setXY(360.0, 0.0);
        assertTrue(view.getItems().get(i).equals(item));
    }

    @Test
    public void testRestore() {
        ViewResult view1 = new ViewResult(1000);
        ViewResult view2 = new ViewResult();

        view1.init(Math.random() * 100.0);

        try {
            view1.viewSave();
        } catch (IOException e) {
            fail(e.getMessage());
        }

        try {
            view2.viewRestore();
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals(view1.getItems().size(), view2.getItems().size());
        assertTrue(view1.getItems().containsAll(view2.getItems()));
    }
}
```
</details>

---

##Запуск програми

[Робота програми](https://github.com/Yarofff-07/Practik_OOP-Zoriu_Yaroslav/blob/task-3-(04.03.26)/pro3/img/robota.png)
