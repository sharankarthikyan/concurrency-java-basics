import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        System.out.println("Main thread running");

        try {
            System.out.println("Main thread pauses for 2 seconds");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Thread thread = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " should take 10 dots to run.");
            for (int i = 0; i < 10; i++) {
                System.out.print(". ");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("\nWhoops!! " + threadName + " interrupted.");
                }
            }
            System.out.println("\n" + threadName + " completed.");
        });

        System.out.println(thread.getName() + " is starting.");
        thread.start();

        System.out.println("Main thread would continue here");
    }
}