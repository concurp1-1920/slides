package ch.heiafr.bank;

public class Bank {
    private int balance;

    public Bank(int initialBalance) {
        this.balance = initialBalance;
    }

    public synchronized void deposit(int amount) {
        System.out.println("deposit of " + amount + ", the balance is " + balance);
        balance += amount;
        notifyAll();
        assert balance >= 0;
    }

    public synchronized void withdraw(int amount) {
        while (balance < amount) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace(); /* ... */
            }
        }
        System.out.println("withdraw of " + amount + ", the balance is " + balance);
        balance -= amount;
        assert balance >= 0;
    }
}
