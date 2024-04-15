import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Password {
    private static final int THREAD_POOL_SIZE = 5;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        System.out.println("Ingrese contraseñas a validar (ingrese 'exit' para salir):");

        while (true) {
            String password = scanner.nextLine();

            if (password.equalsIgnoreCase("exit")) {
                break;
            }

            executor.execute(() -> {
                if (validatePassword(password)) {
                    System.out.println("La contraseña es válida: " + password);
                } else {
                    System.out.println("La contraseña no es válida: " + password);
                }
            });
        }

        executor.shutdown();
        scanner.close();
    }

    private static boolean validatePassword(String password) {

        if (password.length() < 8) {
            return false;
        }


        Pattern specialCharPattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher specialCharMatcher = specialCharPattern.matcher(password);
        if (!specialCharMatcher.find()) {
            return false;
        }


        Pattern uppercasePattern = Pattern.compile("[A-Z]");
        Matcher uppercaseMatcher = uppercasePattern.matcher(password);
        int uppercaseCount = 0;
        while (uppercaseMatcher.find()) {
            uppercaseCount++;
        }
        if (uppercaseCount < 2) {
            return false;
        }


        Pattern lowercasePattern = Pattern.compile("[a-z]");
        Matcher lowercaseMatcher = lowercasePattern.matcher(password);
        int lowercaseCount = 0;
        while (lowercaseMatcher.find()) {
            lowercaseCount++;
        }
        if (lowercaseCount < 3) {
            return false;
        }


        Pattern digitPattern = Pattern.compile("[0-9]");
        Matcher digitMatcher = digitPattern.matcher(password);
        if (!digitMatcher.find()) {
            return false;
        }

        return true;
    }
}


