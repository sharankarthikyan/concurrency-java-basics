import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        CustomThread customThread1 = new CustomThread();
        customThread1.start();
        
        long now = System.currentTimeMillis();
        Thread customThread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.print((2 * (i + 1)) - 1 + " ");

                if (System.currentTimeMillis() - now > 1000) {
                    customThread1.interrupt();
                }

                if (System.currentTimeMillis() - now > 3000) {
                    Thread.currentThread().interrupt();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("\nThread 2 interrupted");
                    return;
                }
            }
        });


        customThread2.start();
    }
}