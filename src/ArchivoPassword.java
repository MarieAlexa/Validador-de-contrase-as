import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ArchivoPassword {
    private static final int THREAD_POOL_SIZE = 5;
    private static final String LOG_FILE_PATH = "password_log.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        System.out.println("Ingrese contraseñas a validar (ingrese 'exit' para salir):");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH))) {
            while (true) {
                String password = scanner.nextLine();

                if (password.equalsIgnoreCase("exit")) {
                    break;
                }

                executor.execute(() -> {
                    boolean isValid = validatePassword(password);
                    String resultMessage = isValid ? "válida" : "no válida";
                    String logEntry = String.format("Contraseña: %s - Resultado: %s%n", password, resultMessage);

                    try {
                        writer.write(logEntry);
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (isValid) {
                        System.out.println("La contraseña es válida: " + password);
                    } else {
                        System.out.println("La contraseña no es válida: " + password);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
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
