package ch.heiafr.bb;

import java.util.Random;

public class BBMain {

    public static final int NB_PRODUCER = 10;
    public static final int NB_CONSUMER = 1;
    private static final Random r = new Random();

    public static void main(String[] args) {

        BoundedBuffer boundedBuffer = new BoundedBuffer();

        for (int i = 0; i < NB_PRODUCER; i++) {
            (new Thread(() -> {
                while (true) {
                    boundedBuffer.deposit(r.nextInt(100));
                }
            })).start();
        }

        for (int i = 0; i < NB_CONSUMER; i++) {
            (new Thread(() -> {
                while (true) {
                    sleep();
                    boundedBuffer.fetch();
                }
            })).start();
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.err.println("Thread has been interrupted while sleeping");
        }
    }
}
