import java.util.Random;

class MessageRepository {
    private String message;
    private boolean hasMessage = false;

    // To read a message from the repository
    public synchronized String read() {
        while (!hasMessage) {
            try {
                wait();
            } catch (InterruptedException e) {
                // Handle the InterruptedException appropriately
                Thread.currentThread().interrupt(); // Preserve the interrupted status
                throw new RuntimeException("Error - waiting for a message", e);
            }
        }
        this.hasMessage = false;
        notifyAll();
        return message;
    }

    // To write a message to the repository
    public synchronized void write(String message) {
        while (hasMessage) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Preserve the interrupted status
                throw new RuntimeException("Error - waiting to write a message", e);
            }
        }
        this.hasMessage = true;
        this.message = message;
        notifyAll();
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
    }
}
