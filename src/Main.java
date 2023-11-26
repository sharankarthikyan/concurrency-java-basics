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
                    System.out.println("A. State = " + Thread.currentThread().getState());
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("\nWhoops!! " + threadName + " interrupted.");
                    System.out.println("A1. State = " + Thread.currentThread().getState());
                    return;
                }
            }
            System.out.println("\n" + threadName + " completed.");
        });

        System.out.println(thread.getName() + " is starting.");
        thread.start();

        long now = System.currentTimeMillis();
        while (thread.isAlive()) {
            System.out.println("\n Waiting for thread to complete");
            try {
                Thread.sleep(1000);
                System.out.println("B. State = " + thread.getState());

                if (System.currentTimeMillis() - now > 2000) {
                    thread.interrupt();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("C. State = " + thread.getState());
    }
}