import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

class ColorThreadFactory implements ThreadFactory {

    private String threadName;

    public ColorThreadFactory(ThreadColor color) {
        this.threadName = color.name();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(threadName);
        return thread;
    }
}

public class Main {
    public static void main(String[] args) {
        ExecutorService blueExecutor = Executors.newSingleThreadExecutor(new ColorThreadFactory(ThreadColor.ANSI_BLUE));
        blueExecutor.execute(Main::countDown);
        blueExecutor.shutdown();

        ExecutorService yellowExecutor = Executors.newSingleThreadExecutor(new ColorThreadFactory(ThreadColor.ANSI_YELLOW));
        yellowExecutor.execute(Main::countDown);
        yellowExecutor.shutdown();

        ExecutorService redExecutor = Executors.newSingleThreadExecutor(new ColorThreadFactory(ThreadColor.ANSI_RED));
        redExecutor.execute(Main::countDown);
        redExecutor.shutdown();
    }

    public static void notmain(String[] args) {
        Thread blue = new Thread(Main::countDown, ThreadColor.ANSI_BLUE.name());
        Thread yellow = new Thread(Main::countDown, ThreadColor.ANSI_YELLOW.name());
        Thread red = new Thread(Main::countDown, ThreadColor.ANSI_RED.name());

        blue.start();
        try {
            blue.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        yellow.start();
        try {
            yellow.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        red.start();
        try {
            red.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void countDown() {
        String threadName = Thread.currentThread().getName();
        ThreadColor threadColor = ThreadColor.ANSI_RESET;

        try {
            threadColor = ThreadColor.valueOf(threadName.toUpperCase());
        } catch (IllegalArgumentException e) {

        }

        String color = threadColor.color();
        for (int i = 20; i >= 0; i--) {
            System.out.println(color + " " +
                    threadName.replace("ANSI_", "") + " " + i);
        }
    }
}
