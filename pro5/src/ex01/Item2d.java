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