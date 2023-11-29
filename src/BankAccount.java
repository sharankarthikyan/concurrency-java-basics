public class BankAccount {
    private String name;
    private double balance;

    private final Object lockName = new Object();
    private final Object lockBalance = new Object();

    public BankAccount(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        synchronized (lockName) {
            this.name = name;
            System.out.println("Updated name = " + this.name);
        }
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        try {
            System.out.println("Deposit - Talking to the teller at the bank...");
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // synchronized the critical section of the code.
        Double boxedDouble = this.balance;
        synchronized (lockBalance) {
            double originalBalance = balance;
            balance += amount;
            System.out.printf("STARTING BALANCE: %.0f, DEPOSIT (%.0f) : NEW BALANCE: %.0f%n", originalBalance, amount, balance);
            // Reentrant synchronization - if a thread already holds a lock, it can acquire the same lock again without being blocked.
            addPromoDollars(amount);
        }
    }

    // Only one thread can access this method if we add synchronized.
    public synchronized void withdraw(double amount) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double originalBalance = balance;
        if (amount <= balance) {
            balance -= amount;
            System.out.printf("STARTING BALANCE: %.0f, WITHDRAWAL (%.0f) : NEW BALANCE: %.0f%n", originalBalance, amount, balance);
        } else {
            System.out.printf("STARTING BALANCE: %.0f, WITHDRAWAL (%.0f) : INSUFFICIENT FUNDS", originalBalance, amount);
        }
    }

    private void addPromoDollars(double amount) {
        if(amount >= 5000) {
            synchronized (lockBalance) {
                System.out.println("Congratulations, you earned a promotional deposit.");
                balance += 25;
            }
        }
    }
}
