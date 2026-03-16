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