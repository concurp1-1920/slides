package ch.heiafr.barber;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BarberShop {
    private final Lock mutex = new ReentrantLock();

    // CC
    private final Condition ??? = mutex.newCondition();

    // Var
    private int ???=???

    // call by a customer
    public void getHairCut() {

    }

    // call by the barber
    public void nextCustomer() {

    }

    // call by the barber
    public void finishCut() {

    }
}
