import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

record Order(long orderId, String item, int qty) {};

public class Main {

    private final static Random random = new Random();

    public static void main(String[] args) {
        ShoeWarehouse shoeWarehouse = new ShoeWarehouse();

        ExecutorService singleExecutor = Executors.newSingleThreadExecutor();

        for (int i = 0; i < 10; i++) {
            singleExecutor.execute(() -> shoeWarehouse.receiveOrder(new Order(random.nextInt(100000, 999999),
                    ShoeWarehouse.PRODUCT_LIST[random.nextInt(0, 5)],
                    random.nextInt(1, 4))));
        }

        ExecutorService multiExecutor = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 5; j++) {
                    multiExecutor.execute(shoeWarehouse::fullfillOrder);
                }
        }

        singleExecutor.shutdown();
        multiExecutor.shutdown();
    }
}
