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