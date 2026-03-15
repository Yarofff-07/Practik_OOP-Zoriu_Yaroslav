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