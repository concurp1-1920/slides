package ch.heiafr.barber;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BarberShopSol {

    final Lock lock = new ReentrantLock();
    final Condition bAvail = lock.newCondition();      // signaled when barber > 0
    final Condition chOccupied = lock.newCondition();  // signaled when chair > 0
    final Condition dOpen = lock.newCondition();       // signaled when open > 0
    final Condition cuLeft = lock.newCondition();      // signaled when open = 0

    int barber = 0,    // incremented by barber when he's ready
            chair = 0,     // incremented by customer when sitting in chair
            open = 0;      // incremented by barber, decremented by cust. when leaving

    // called by customers
    public void getHaircut() {
        lock.lock();
        try {
            while (barber == 0) bAvail.await();
            barber--;
            chair++;
            chOccupied.signal();
            while (open == 0) dOpen.await();
            open--;
            cuLeft.signal();
        } catch (InterruptedException e) { e.printStackTrace(); } finally {
            lock.unlock();
        }
    }

    // called by the barber
    public void getNextCustomer() {
        lock.lock();
        try {
            barber++;
            bAvail.signal();
            while (chair == 0) chOccupied.awaitUninterruptibly();
            chair--;
        } finally {
            lock.unlock();
        }
    }

    // called by the barber
    public void finishedCut() {
        lock.lock();
        try {
            open++;
            dOpen.signal();
            while (open > 0) cuLeft.awaitUninterruptibly();
        } finally {
            lock.unlock();
        }
    }
}
