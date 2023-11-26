public class CustomThread extends Thread {
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.print(2 * (i + 1) + " ");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("\nThread 1 interrupted");
                return;
            }
        }
    }
}
