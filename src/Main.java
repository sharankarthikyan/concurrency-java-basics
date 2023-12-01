import java.util.Random;
import java.util.concurrent.locks.*;

class MessageRepository {
    private String message;
    private boolean hasMessage = false;

    private final Lock lock  = new ReentrantLock();

    // To read a message from the repository
    public String read() {
        if(lock.tryLock()) {
            try {
                while (!hasMessage) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // Handle the InterruptedException appropriately
                        Thread.currentThread().interrupt(); // Preserve the interrupted status
                        throw new RuntimeException("Error - waiting for a message", e);
                    }
                }
                this.hasMessage = false;
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println("*** Read Blocked ***");
            this.hasMessage = false; // It indicates that the current thread is not actively reading a message. To maintain consistency.
        }
        return message;
    }

    // To write a message to the repository
    public void write(String message) {
        if(lock.tryLock()) {
            try {
                while (hasMessage) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Preserve the interrupted status
                        throw new RuntimeException("Error - waiting to write a message", e);
                    }
                }
                this.hasMessage = true;
                this.message = message;
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println("*** Write Blocked ***");
            this.hasMessage = true; // It indicates that the current thread is not actively writing a message. To maintain consistency.
        }
    }
}

class MessageProducer implements Runnable {
    private final MessageRepository messageRepository;
    private final String text = "Hello World,\n" +
            "This is my second sentence,\n" +
            "It is a producer-consumer problem,\n" +
            "We have reached the end.";

    public MessageProducer(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void run() {
        Random random = new Random();
        String[] lines = text.split("\n");

        for (String line : lines) {
            messageRepository.write(line);
            try {
                Thread.sleep(random.nextInt(500, 2000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // To preserve the interrupted status
                throw new RuntimeException("Error - sleeping in the producer", e);
            }
        }
        messageRepository.write("Finished");
    }
}

class MessageConsumer implements Runnable {

    private final MessageRepository messageRepository;

    public MessageConsumer(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void run() {
        Random random = new Random();
        String latestMessage = "";
        do {
            try {
                Thread.sleep(random.nextInt(500, 2000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // To preserve the interrupted status
                throw new RuntimeException("Error - sleeping in the consumer", e);
            }
            latestMessage = messageRepository.read();
            System.out.println(latestMessage);
        } while (!latestMessage.equals("Finished"));
    }
}

public class Main {
    public static void main(String[] args) {
        MessageRepository messageRepository = new MessageRepository();

        Thread consumerThread = new Thread(new MessageConsumer(messageRepository));
        Thread producerThread = new Thread(new MessageProducer(messageRepository));

        consumerThread.start();
        producerThread.start();

        producerThread.setUncaughtExceptionHandler((thread, ex)-> {
            System.out.println("Producer had exception: " + ex);
            if(consumerThread.isAlive()) {
                System.out.println("Going to interrupt the consumer.");
                consumerThread.interrupt();
            }
        });

        consumerThread.setUncaughtExceptionHandler((thread, ex)-> {
            System.out.println("Consumer had exception: " + ex);
            if(producerThread.isAlive()) {
                System.out.println("Going to interrupt the producer.");
                producerThread.interrupt();
            }
        });
    }
}
