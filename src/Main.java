import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        // Custom Thread 1
        CustomThread customThread = new CustomThread();
        customThread.start();

        // Custom Thread 2
        Runnable myRunnable = () -> {
            for(int i = 0; i < 8; i++) {
                System.out.print(" 2 ");
                try {
                    TimeUnit.MILLISECONDS.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread myThread = new Thread(myRunnable);
        myThread.start();

        // Main Thread
        for(int i = 0; i < 3; i++) {
            System.out.print(" 0 ");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}