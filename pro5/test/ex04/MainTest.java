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