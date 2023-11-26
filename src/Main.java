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

        Thread installThread = new Thread(() -> {
            try {
                for (int i = 0; i < 3; i++) {
                    Thread.sleep(250);
                    System.out.println("Installation Step " + (i + 1) + " is completed.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "InstallThread");

        Thread thread = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " should take 10 dots to run.");
            for (int i = 0; i < 10; i++) {
                System.out.print(". ");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("\nWhoops!! " + threadName + " interrupted.");
                    Thread.currentThread().interrupt(); // We need to call
                    // interrupt our thread self to work some methods like (isInterrupted())
                    return;
                }
            }
            System.out.println("\n" + threadName + " completed.");
        });

        long now = System.currentTimeMillis();
        Thread threadMonitor = new Thread(() -> {
            while (thread.isAlive()) {
                try {
                    Thread.sleep(1000);
                    if (System.currentTimeMillis() - now > 2000) {
                        thread.interrupt();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        });

        System.out.println(thread.getName() + " is starting.");
        thread.start();
        threadMonitor.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (!thread.isInterrupted()) {
            installThread.start();
        } else {
            System.out.println("Previous thread was interrupted, " + installThread.getName() + " can't run.");
        }
    }
}