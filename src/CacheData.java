import java.util.concurrent.TimeUnit;

public class CacheData {

    private volatile boolean flag = false;

    public void toggleFlag () {
        flag = !flag;
    }

    public boolean isReady() {
        return flag;
    }

    public static void main(String[] args) {
        CacheData cacheData = new CacheData();

        Thread writerThread = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cacheData.toggleFlag();
            System.out.println("A. Flag set to " + cacheData.isReady());
        });

        Thread readerThread = new Thread(() -> {
            while(!cacheData.isReady()) {

            }
            System.out.println("B. Flag is " + cacheData.isReady());
        });

        writerThread.start();
        readerThread.start();
    }
}
