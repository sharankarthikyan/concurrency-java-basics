public class BankAccount {
    private String name;
    private double balance;

    public BankAccount(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        synchronized (this.name) {
            this.name = name;
            System.out.println("Updated name = " + this.name);
        }
    }

    public double getBalance() {
        return balance;
    }

    public synchronized void deposit(double amount) {
        try {
            System.out.println("Deposit - Talking to the teller at the bank...");
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // synchronized the critical section of the code.
//        synchronized (this) {
        double originalBalance = balance;
        balance += amount;
        System.out.printf("STARTING BALANCE: %.0f, DEPOSIT (%.0f) : NEW BALANCE: %.0f%n", originalBalance, amount, balance);
//        }
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
}
