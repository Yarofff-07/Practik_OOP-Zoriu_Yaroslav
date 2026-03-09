package ex03;

import java.util.Formatter;
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
     * @param stepX крок зміни x
     */
    @Override
    public void init(double stepX) {
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