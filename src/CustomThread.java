public class CustomThread extends Thread{
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.print(" 1 ");
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
