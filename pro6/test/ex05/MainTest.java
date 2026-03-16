package ex05;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ex01.Item2d;
import ex02.ViewResult;

/**
 * Тести для класів ex05.
 */
public class MainTest {

    private static ViewResult view;

    private static MaxCommand max1;
    private static MaxCommand max2;

    private static AvgCommand avg1;
    private static AvgCommand avg2;

    private static MinMaxCommand min1;
    private static MinMaxCommand min2;

    /**
     * Виконується перед усіма тестами.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        view = new ViewResult();
        view.getItems().clear();

        view.getItems().add(new Item2d(1.0, 2.0));
        view.getItems().add(new Item2d(5.0, -3.0));
        view.getItems().add(new Item2d(3.0, 4.0));
        view.getItems().add(new Item2d(2.0, -1.0));
        view.getItems().add(new Item2d(4.0, 1.5));

        max1 = new MaxCommand(view);
        max2 = new MaxCommand(view);

        avg1 = new AvgCommand(view);
        avg2 = new AvgCommand(view);

        min1 = new MinMaxCommand(view);
        min2 = new MinMaxCommand(view);
    }

    /**
     * Виконується після всіх тестів.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        assertEquals(max1.getResult(), max2.getResult());
        assertEquals(avg1.getResult(), avg2.getResult(), 0.0001);
        assertEquals(min1.getResultMin(), min2.getResultMin());
        assertEquals(min1.getResultMax(), min2.getResultMax());
    }

    /**
     * Перевірка MaxCommand.
     */
    @Test
    public void testMax() {
        max1.execute();
        assertTrue(max1.getResult() > -1);
        assertEquals(2, max1.getResult());
    }

    /**
     * Перевірка AvgCommand.
     */
    @Test
    public void testAvg() {
        avg1.execute();
        assertEquals(0.7, avg1.getResult(), 0.0001);
    }

    /**
     * Перевірка MinMaxCommand.
     */
    @Test
    public void testMin() {
        min1.execute();
        assertEquals(4, min1.getResultMin());
        assertEquals(3, min1.getResultMax());
    }

    /**
     * Перевірка MaxCommand через чергу.
     */
    @Test
    public void testMaxQueue() {
        CommandQueue queue = new CommandQueue();
        queue.put(max2);

        try {
            while (max2.running()) {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            queue.shutdown();
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }

        assertEquals(2, max2.getResult());
    }

    /**
     * Перевірка AvgCommand через чергу.
     */
    @Test
    public void testAvgQueue() {
        CommandQueue queue = new CommandQueue();
        queue.put(avg2);

        try {
            while (avg2.running()) {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            queue.shutdown();
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }

        assertEquals(0.7, avg2.getResult(), 0.0001);
    }

    /**
     * Перевірка MinMaxCommand через чергу.
     */
    @Test
    public void testMinQueue() {
        CommandQueue queue = new CommandQueue();
        queue.put(min2);

        try {
            while (min2.running()) {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            queue.shutdown();
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }

        assertEquals(4, min2.getResultMin());
        assertEquals(3, min2.getResultMax());
    }
}