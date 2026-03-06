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