# Обробка колекцій

## Постановка задачі

### Завдання:

1. Реалізувати можливість скасування (`undo`) операцій (команд).
2. Продемонструвати поняття **макрокоманда**.
3. При розробці програми використати шаблон проєктування **Singleton**.
4. Забезпечити діалоговий інтерфейс із користувачем.
5. Розробити клас для тестування функціональності програми.

## Опис проєкту

У даній роботі було розроблено консольну програму на Java, у якій реалізовано шаблон проєктування **Command**.  
Кожна дія користувача подана у вигляді окремої команди. Програма підтримує генерацію даних, перегляд, зміну, збереження, відновлення та скасування останньої операції.

Для виконання кількох команд підряд реалізовано **макрокоманду**.  
Головний клас програми побудовано за шаблоном **Singleton**, що забезпечує існування лише одного екземпляра застосунку.  
Керування програмою відбувається через консольне меню.  
Також створено тестовий клас для перевірки правильності роботи основних функцій.

## Структура проєкту

![Структура проекту](https://github.com/Yarofff-07/Practik_OOP-Zoriu_Yaroslav/blob/task-5-(06.03.26)/pro5/img/STRUCT.png)

---

## Command.java

`src/ex04/Command.java` — містить базовий інтерфейс команди.  
У цьому файлі оголошується метод `execute()`, який повинна реалізовувати кожна команда.

<details>
<summary>Код</summary>

```java
package ex04;

public interface Command {
    void execute();
}
```
</details>

## ConsoleCommand.java

`src/ex04/ConsoleCommand.java` — інтерфейс консольної команди.
Розширює базовий інтерфейс Command і додає метод `getKey()`, який повертає символ для вибору команди в меню.

<details>
<summary>Код</summary>

```java
package ex04;

public interface ConsoleCommand extends Command {
    char getKey();
}
```
</details>

## UndoableCommand.java

`src/ex04/UndoableCommand.java` — інтерфейс команд, які підтримують скасування дії.
Містить метод `undo()` для повернення попереднього стану.

<details>
<summary>Код</summary>

```java
package ex04;

public interface UndoableCommand extends Command {
    void undo();
}
```
</details>

## Menu.java

`src/ex04/Menu.java` — реалізує консольне меню програми.
Цей клас відображає список доступних команд, приймає введення користувача та запускає відповідну дію.

<details>
<summary>Код</summary>

```java
package ex04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Menu implements Command {
    private ArrayList<ConsoleCommand> commands = new ArrayList<ConsoleCommand>();

    public void add(ConsoleCommand command) {
        commands.add(command);
    }

    @Override
    public void execute() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;

        while (true) {
            System.out.println("\n=== MENU ===");
            for (ConsoleCommand command : commands) {
                System.out.println(command.toString());
            }
            System.out.println("'q' - quit");
            System.out.print("Choose command: ");

            try {
                input = reader.readLine();
            } catch (IOException e) {
                System.out.println("Input error.");
                return;
            }

            if (input == null || input.length() != 1) {
                System.out.println("Wrong input. Enter one symbol.");
                continue;
            }

            char key = input.charAt(0);

            if (key == 'q') {
                System.out.println("Program finished.");
                break;
            }

            boolean found = false;

            for (ConsoleCommand command : commands) {
                if (command.getKey() == key) {
                    command.execute();
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Unknown command.");
            }
        }
    }
}
```
</details>

---

## MacroCommand.java

`src/ex04/MacroCommand.java` — реалізація макрокоманди.
Дозволяє об’єднати кілька команд в одну і виконати їх послідовно.

<details>
<summary>Код</summary>

```java
package ex04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Menu implements Command {
    private ArrayList<ConsoleCommand> commands = new ArrayList<ConsoleCommand>();

    public void add(ConsoleCommand command) {
        commands.add(command);
    }

    @Override
    public void execute() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;

        while (true) {
            System.out.println("\n=== MENU ===");
            for (ConsoleCommand command : commands) {
                System.out.println(command.toString());
            }
            System.out.println("'q' - quit");
            System.out.print("Choose command: ");

            try {
                input = reader.readLine();
            } catch (IOException e) {
                System.out.println("Input error.");
                return;
            }

            if (input == null || input.length() != 1) {
                System.out.println("Wrong input. Enter one symbol.");
                continue;
            }

            char key = input.charAt(0);

            if (key == 'q') {
                System.out.println("Program finished.");
                break;
            }

            boolean found = false;

            for (ConsoleCommand command : commands) {
                if (command.getKey() == key) {
                    command.execute();
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Unknown command.");
            }
        }
    }
}
```
</details>

## Application.java

`src/ex04/Application.java` — головний клас програми.
Реалізований за шаблоном Singleton.
Створює об’єкт представлення, меню, історію команд для undo та запускає виконання програми.

<details>
<summary>Код</summary>

```java
package ex04;

import java.util.Stack;

import ex02.View;
import ex03.ViewableTable;

public class Application {
    private static Application instance = new Application();

    private View view;
    private Menu menu;
    private Stack<UndoableCommand> history;

    private Application() {
        view = new ViewableTable().getView();
        menu = new Menu();
        history = new Stack<UndoableCommand>();
    }

    public static Application getInstance() {
        return instance;
    }

    public View getView() {
        return view;
    }

    public void addToHistory(UndoableCommand command) {
        history.push(command);
    }

    public void undoLast() {
        if (history.empty()) {
            System.out.println("Nothing to undo.");
            return;
        }
        history.pop().undo();
    }

    public void run() {
        menu.add(new GenerateConsoleCommand(view));
        menu.add(new ViewConsoleCommand(view));
        menu.add(new ChangeConsoleCommand(view));
        menu.add(new SaveConsoleCommand(view));
        menu.add(new RestoreConsoleCommand(view));
        menu.add(new UndoConsoleCommand());

        MacroCommand macro = new MacroCommand();
        macro.add(new GenerateConsoleCommand(view));
        macro.add(new ViewConsoleCommand(view));
        menu.add(macro);

        menu.execute();
    }
}
```
</details>

## Main.java

`src/ex04/Main.java` — містить статичний метод main(), з якого починається виконання програми.
У цьому класі відбувається запуск єдиного екземпляра застосунку.

<details>
<summary>Код</summary>

```java
package ex04;

public class Main {
    public static void main(String[] args) {
        Application.getInstance().run();
    }
}
```
</details>

## GenerateConsoleCommand.java

`src/ex04/GenerateConsoleCommand.java` — команда генерації даних.
Ініціалізує нові значення та виводить результат на екран.

<details>
<summary>Код</summary>

```java
package ex04;

import ex02.View;
import ex02.ViewResult;

public class GenerateConsoleCommand implements ConsoleCommand {
    private View view;

    public GenerateConsoleCommand(View view) {
        this.view = view;
    }

    @Override
    public void execute() {
        ((ViewResult) view).viewInit();
        System.out.println("Data generated.");
        view.viewShow();
    }

    @Override
    public char getKey() {
        return 'g';
    }

    @Override
    public String toString() {
        return "'g' - generate data";
    }
}
```
</details>

## ViewConsoleCommand.java

`src/ex04/ViewConsoleCommand.java` — команда перегляду поточних даних.
Використовується для виведення результатів у консоль.

<details>
<summary>Код</summary>

```java
package ex04;

import ex02.View;

public class ViewConsoleCommand implements ConsoleCommand {
    private View view;

    public ViewConsoleCommand(View view) {
        this.view = view;
    }

    @Override
    public void execute() {
        view.viewShow();
    }

    @Override
    public char getKey() {
        return 'v';
    }

    @Override
    public String toString() {
        return "'v' - view data";
    }
}
```
</details>

## ChangeItemCommand.java

`src/ex04/ChangeItemCommand.java` — команда зміни одного об’єкта Item2d.
Зберігає попередній стан елемента та дозволяє скасувати зміну через `undo()`.

<details>
<summary>Код</summary>

```java
package ex04;

import ex01.Item2d;

public class ChangeItemCommand implements UndoableCommand {
    private Item2d item;
    private double offset;

    private double oldX;
    private double oldY;

    public void setItem(Item2d item) {
        this.item = item;
    }

    public Item2d getItem() {
        return item;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public double getOffset() {
        return offset;
    }

    @Override
    public void execute() {
        if (item == null) {
            return;
        }

        oldX = item.getX();
        oldY = item.getY();

        item.setY(item.getY() * offset);
    }

    @Override
    public void undo() {
        if (item == null) {
            return;
        }

        item.setXY(oldX, oldY);
    }
}
```
</details>

## ChangeConsoleCommand.java

`src/ex04/ChangeConsoleCommand.java` — консольна команда зміни набору даних.
Змінює значення всіх елементів колекції, зберігає їх попередній стан і підтримує скасування останньої зміни.

<details>
<summary>Код</summary>

```java
package ex04;

import java.util.ArrayList;

import ex01.Item2d;
import ex02.View;
import ex02.ViewResult;

public class ChangeConsoleCommand implements ConsoleCommand, UndoableCommand {
    private View view;
    private ArrayList<Item2d> itemsBefore = new ArrayList<Item2d>();
    private double offset = 2.0;

    public ChangeConsoleCommand(View view) {
        this.view = view;
    }

    @Override
    public void execute() {
        itemsBefore.clear();

        ArrayList<Item2d> items = ((ViewResult) view).getItems();

        if (items.size() == 0) {
            System.out.println("No data to change.");
            return;
        }

        for (Item2d item : items) {
            Item2d copy = new Item2d();
            copy.setXY(item.getX(), item.getY());
            itemsBefore.add(copy);

            item.setY(item.getY() * offset);
        }

        System.out.println("Data changed.");
        view.viewShow();

        Application.getInstance().addToHistory(this);
    }

    @Override
    public void undo() {
        ArrayList<Item2d> items = ((ViewResult) view).getItems();

        for (int i = 0; i < items.size() && i < itemsBefore.size(); i++) {
            items.get(i).setXY(itemsBefore.get(i).getX(), itemsBefore.get(i).getY());
        }

        System.out.println("Last change canceled.");
        view.viewShow();
    }

    @Override
    public char getKey() {
        return 'c';
    }

    @Override
    public String toString() {
        return "'c' - change data";
    }
}
```
</details>

## UndoConsoleCommand.java

`src/ex04/UndoConsoleCommand.java` — команда скасування останньої операції.
Звертається до історії команд у класі Application та виконує `undo()` для останньої збереженої команди.

<details>
<summary>Код</summary>

```java
package ex04;

public class UndoConsoleCommand implements ConsoleCommand {

    @Override
    public void execute() {
        Application.getInstance().undoLast();
    }

    @Override
    public char getKey() {
        return 'u';
    }

    @Override
    public String toString() {
        return "'u' - undo last command";
    }
}
```
</details>

## SaveConsoleCommand.java

`src/ex04/SaveConsoleCommand.java` — команда збереження даних у файл.
Використовує методи серіалізації для запису поточного стану колекції.

<details>
<summary>Код</summary>

```java
package ex04;

import java.io.IOException;

import ex02.View;
import ex02.ViewResult;

public class SaveConsoleCommand implements ConsoleCommand {
    private View view;

    public SaveConsoleCommand(View view) {
        this.view = view;
    }

    @Override
    public void execute() {
        try {
            ((ViewResult) view).viewSave();
            System.out.println("Data saved.");
        } catch (IOException e) {
            System.out.println("Save error: " + e.getMessage());
        }
    }

    @Override
    public char getKey() {
        return 's';
    }

    @Override
    public String toString() {
        return "'s' - save data";
    }
}
```
</details>

## RestoreConsoleCommand.java

`src/ex04/RestoreConsoleCommand.java` — команда відновлення даних із файлу.
Завантажує раніше збережені результати та виводить їх на екран.

<details>
<summary>Код</summary>

```java
package ex04;

import ex02.View;
import ex02.ViewResult;

public class RestoreConsoleCommand implements ConsoleCommand {
    private View view;

    public RestoreConsoleCommand(View view) {
        this.view = view;
    }

    @Override
    public void execute() {
        try {
            ((ViewResult) view).viewRestore();
            System.out.println("Data restored.");
            view.viewShow();
        } catch (Exception e) {
            System.out.println("Restore error: " + e.getMessage());
        }
    }

    @Override
    public char getKey() {
        return 'r';
    }

    @Override
    public String toString() {
        return "'r' - restore data";
    }
}
```
</details>

## MainTest.java

`test/ex04/MainTest.java` — клас тестування основної функціональності програми.
Містить тести для перевірки зміни даних, скасування змін та коректності реалізації Singleton.

<details>
<summary>Код</summary>

```java
package ex04;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import ex01.Item2d;

public class MainTest {

    @Test
    public void testChangeItemExecute() {
        ChangeItemCommand command = new ChangeItemCommand();
        Item2d item = new Item2d();

        item.setXY(10.0, 5.0);
        command.setItem(item);
        command.setOffset(2.0);

        command.execute();

        assertEquals(10.0, item.getX(), 0.001);
        assertEquals(10.0, item.getY(), 0.001);
    }

    @Test
    public void testChangeItemUndo() {
        ChangeItemCommand command = new ChangeItemCommand();
        Item2d item = new Item2d();

        item.setXY(7.0, 4.0);
        command.setItem(item);
        command.setOffset(3.0);

        command.execute();
        command.undo();

        assertEquals(7.0, item.getX(), 0.001);
        assertEquals(4.0, item.getY(), 0.001);
    }

    @Test
    public void testSingleton() {
        Application app1 = Application.getInstance();
        Application app2 = Application.getInstance();

        assertSame(app1, app2);
    }
}
```
</details>

---

## Скриншоти роботи

![Робота програми](https://github.com/Yarofff-07/Practik_OOP-Zoriu_Yaroslav/blob/task-5-(06.03.26)/pro5/img/w1.png)

![Робота програми](https://github.com/Yarofff-07/Practik_OOP-Zoriu_Yaroslav/blob/task-5-(06.03.26)/pro5/img/w2.png)

![Робота програми](https://github.com/Yarofff-07/Practik_OOP-Zoriu_Yaroslav/blob/task-5-(06.03.26)/pro5/img/w3.png)

![Робота програми](https://github.com/Yarofff-07/Practik_OOP-Zoriu_Yaroslav/blob/task-5-(06.03.26)/pro5/img/w4.png)

![Робота програми](https://github.com/Yarofff-07/Practik_OOP-Zoriu_Yaroslav/blob/task-5-(06.03.26)/pro5/img/w5.png)

![Робота програми](https://github.com/Yarofff-07/Practik_OOP-Zoriu_Yaroslav/blob/task-5-(06.03.26)/pro5/img/w6.png)
