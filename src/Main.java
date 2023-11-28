public class Main {
    public static void main(String[] args) {
        BankAccount sharanAccount = new BankAccount(10000);

        Thread thread1 = new Thread(() -> sharanAccount.withdraw(2500));
        Thread thread2 = new Thread(() -> sharanAccount.deposit(5000));
        Thread thread3 = new Thread(() -> sharanAccount.withdraw(2500));
        Thread thread4 = new Thread(() -> sharanAccount.withdraw(5000));

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        try {
            // When we invoke the join() method on a thread,
            // the calling thread (HERE Main thread) goes into a waiting state.
            // It remains in a waiting state until the referenced thread terminates.
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final Balance: " + sharanAccount.getBalance());
    }
}
