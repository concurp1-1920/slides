package ch.heiafr.bb;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBuffer {

    public final static int N = 10;

    private int front = 0;
    private int rear = 0;
    private int count = 0;
    private int[] buffer = new int[N];

    private ReentrantLock mutex = new ReentrantLock(true);
    private Condition notFull = mutex.newCondition();
    private Condition notEmpty = mutex.newCondition();

    public void deposit(int data) {
        mutex.lock();
        try {
            while (count == N) notFull.await();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
        buffer[rear] = data;
        rear = ++rear % N;
        count++;
        System.out.println("deposit of " + data);
        notEmpty.signal();
        mutex.unlock();
    }

    public int fetch() {
        mutex.lock();
        try {
            while (count == 0) notEmpty.await();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
        int data = buffer[front];
        front = ++front % N;
        count--;
        System.out.println("fetch of " + data);
        notFull.signal();
        mutex.unlock();
        return data;
    }
}
