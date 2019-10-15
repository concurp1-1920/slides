# Concurrency in Java

### Concurrent programming 1 - Lecture 5

.center[<img src="img/java.png" width="350"/>]

---
# Creating and Starting Threads in Java

An application that creates an instance of `Thread` must provide the code that will run in that thread.

---
## Provide a `Runnable` object

```java
public class HelloRunnable implements Runnable {
  public void run() {
    System.out.println("Hello from a thread!");
  }
  public static void main(String args[]) {
    (new Thread(new HelloRunnable())).start();
  }
}
```

---
## Subclass `Thread`

```java
public class HelloThread extends Thread {
  public void run() {
    System.out.println("Hello from a thread!");
  }
}

// somewhere else
public static void main(String args[]) {
  (new HelloThread()).start();
}
```
# Warning ! Sharing variables without synchronization

```java
public class ouch {
  private static boolean ready = false; // quit thread when ready
  private static int number = 0; // print the resulting number when ready
  // a simple class to run an equally simple thread
  private static class RunningThread extends Thread {
    // simply loop until main class decides it is ready
    public void run() {
      while (!ready)
      Thread.yield(); // give processor to whomever wants it
      System.out.printf("number is now %d\n", number);
    }
  }
  // just running a thread, so no arguments
  public static void main(String args[]) {
    (new RunningThread()).start();
    try {
      Thread.sleep(1);
    } // wait a (long) while
    catch (InterruptedException dont_care) {
      /* for this example, it doesn't matter */
    }
    number = 42; // the answer to life, the universe, everything...
    ready = true;
    System.out.printf("main has performed its tasks, and is now quitting.\n");
  }
}
```
