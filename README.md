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
