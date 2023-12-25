import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.*;

public class ParallelThreadsWithTimeout {
    private static final int THREAD_TIMEOUT = 10000; // 10000ms timeout

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int userInput = 0;

        try {
            System.out.print("Enter a number: ");
            userInput = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid integer.");
            return;
        }

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        int finalUserInput = userInput;
        Future<Integer> resultA = executorService.submit(() -> A(finalUserInput));
        Future<Integer> resultB = executorService.submit(() -> B(finalUserInput));

        int maxOutput = 0;
        try {
            int outputA = resultA.get(THREAD_TIMEOUT, TimeUnit.MILLISECONDS);
            int outputB = resultB.get(THREAD_TIMEOUT, TimeUnit.MILLISECONDS);
            maxOutput = Math.max(outputA, outputB);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            System.out.println("Thread execution timed out or encountered an exception.");
        }

        executorService.shutdown();

        System.out.println("Max of A and B: " + maxOutput);
    }

    private static int A(int input) {
        if (input < 0) {
            throw new InputMismatchException("A(): Input must be a non-negative integer");
        }
        // Simulate some workload
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return input * 2;
    }

    private static int B(int input) {
        if (input < 0) {
            throw new InputMismatchException("B(): Input must be a non-negative integer");
        }
        // Simulate some workload
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return input * input;
    }
}