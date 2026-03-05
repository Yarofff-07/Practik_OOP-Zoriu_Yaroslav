package ex01;

/**
 * Клас для тестування коректності обчислень і серіалізації/десеріалізації.
 * Запускається як звичайна програма.
 */
public class MainTest {

    public static void main(String[] args) throws Exception {

        Calc calc = new Calc();

        // ---- Тест 1: перевірка обчислення (alpha = 0) ----
        // cos(0)=1 -> v=10
        // N = 10^2 + 10^3 = 1100
        // binary(1100)=10001001100, max run of 1 = 2
        int max = calc.initIndividual6(0.0);
        long n = (long) calc.getResult().getY();

        boolean okCalc = (n == 1100) && (max == 2);

        if (okCalc) {
            System.out.println("testCalc: OK");
        } else {
            System.out.println("testCalc: FAIL");
            System.out.println("N = " + n + " (expected 1100)");
            System.out.println("max = " + max + " (expected 2)");
        }

        // ---- Тест 2: серіалізація/десеріалізація ----
        calc.save();

        // змінюємо стан
        calc.initIndividual6(10.0);

        // відновлюємо
        calc.restore();

        long restoredN = (long) calc.getResult().getY();
        double restoredAlpha = calc.getResult().getX(); // transient -> має стати 0.0

        boolean okRestoreY = (restoredN == 1100);
        boolean okTransientX = (Math.abs(restoredAlpha - 0.0) < 1e-9);

        if (okRestoreY) {
            System.out.println("testRestoreY: OK");
        } else {
            System.out.println("testRestoreY: FAIL (restoredN=" + restoredN + ")");
        }

        if (okTransientX) {
            System.out.println("testTransientX: OK");
        } else {
            System.out.println("testTransientX: FAIL (restoredAlpha=" + restoredAlpha + ")");
        }
    }
}