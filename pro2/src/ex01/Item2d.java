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