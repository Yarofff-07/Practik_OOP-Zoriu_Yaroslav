public class Prac1 {
    public static void main(String[] args) {

        System.out.println("=== Вивід аргументів командного рядка ===");

        if (args.length == 0) {
            System.out.println("Аргументи не передані.");
            System.out.println("Приклад запуску:");
            System.out.println("java Prac1 hello world 123");
            return;
        }

        System.out.println("Кількість аргументів: " + args.length);
        System.out.println("Список аргументів:");

        for (int i = 0; i < args.length; i++) {
            System.out.println((i + 1) + ") " + args[i]);
        }
    }
}