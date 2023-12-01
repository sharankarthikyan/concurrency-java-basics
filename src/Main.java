import java.util.Random;

record Order(long orderId, String item, int qty) {};

public class Main {

    private final static Random random = new Random();

    public static void main(String[] args) {
        ShoeWarehouse shoeWarehouse = new ShoeWarehouse();

        Thread producerThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shoeWarehouse.receiveOrder(new Order(random.nextInt(100000, 999999),
                        ShoeWarehouse.PRODUCT_LIST[random.nextInt(0, 5)],
                        random.nextInt(1, 4)));
            }
        });
        producerThread.start();

        for (int i = 0; i < 2; i++) {
            Thread consumerThread = new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    shoeWarehouse.fullfillOrder();
                }
            });
            consumerThread.start();
        }
    }
}
