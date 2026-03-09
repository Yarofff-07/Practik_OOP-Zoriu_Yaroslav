package ex01;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Головний клас програми.
 * Демонструє роботу обчислень і серіалізації.
 */
public class Main {

    public static void main(String[] args) {

        Calc calc = new Calc();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String s;

        while (true) {

            System.out.println("\nCommands:");
            System.out.println("i - individual task");
            System.out.println("v - view result");
            System.out.println("s - save result");
            System.out.println("r - restore result");
            System.out.println("q - quit");

            System.out.print("Enter command: ");

            try {
                s = in.readLine();
            } catch (Exception e) {
                System.out.println("Input error");
                continue;
            }

            if (s == null || s.isEmpty())
                continue;

            char c = s.charAt(0);

            try {

                if (c == 'q') {
                    System.out.println("Program finished.");
                    break;
                }

                else if (c == 'i') {

                    System.out.print("Enter alpha (degrees): ");
                    double a = Double.parseDouble(in.readLine());

                    int max = calc.initIndividual6(a);

                    long n = (long) calc.getResult().getY();

                    System.out.println("N = " + n);
                    System.out.println("Binary = " + Long.toBinaryString(Math.abs(n)));
                    System.out.println("Max sequence of 1 = " + max);
                }

                else if (c == 'v') {
                    calc.show();
                }

                else if (c == 's') {
                    calc.save();
                    System.out.println("Saved.");
                }

                else if (c == 'r') {
                    calc.restore();
                    System.out.println("Restored.");
                    calc.show();
                }

                else {
                    System.out.println("Unknown command.");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        }
    }
}