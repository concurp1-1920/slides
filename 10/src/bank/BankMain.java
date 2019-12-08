package ch.heiafr.bank;

public class BankMain {
    public static void main(String[] args) {
        Bank bank = new Bank(0);
        (new Thread(() -> { bank.withdraw(100); })).start();
        (new Thread(() -> { bank.withdraw(200); })).start();
        (new Thread(() -> { bank.withdraw(300); })).start();
        (new Thread(() -> { bank.withdraw(400); })).start();
        (new Thread(() -> { bank.withdraw(500); })).start();
        (new Thread(() -> { bank.withdraw(600); })).start();
        (new Thread(() -> { bank.deposit(100); })).start();
        (new Thread(() -> { bank.deposit(200); })).start();
        (new Thread(() -> { bank.deposit(300); })).start();
        (new Thread(() -> { bank.deposit(400); })).start();
        (new Thread(() -> { bank.deposit(500); })).start();
        (new Thread(() -> { bank.deposit(600); })).start();
    }
}
