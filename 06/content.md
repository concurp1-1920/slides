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

---

```java
public class Outch {
  private static boolean ready = false; // quit thread when ready
  private static int number = 0; // print the resulting number when ready
  // a simple class to run an equally simple thread
  private static class RunningThread extends Thread {
    public void run() {
      while (!ready)
      Thread.yield(); // give processor to whomever wants it
      System.out.printf("number is now %d\n", number);
    }
  }
  public static void main(String args[]) {
    (new RunningThread()).start();
    try { Thread.sleep(1); } // wait a (long) while
  catch (InterruptedException dont_care) { /* for this example doesn't matter */ }
    number = 42; // the answer to life, the universe, everything...
    ready = true;
    System.out.printf("main has performed its tasks, quit now.\n");
  }
}
```

---
# Semaphores in Java
```java
package java.util.concurrent;

public class Semaphore extends Object implements Serializable { /*...*/ }
```

---
# Semaphores in Java

A counting semaphore. Conceptually, a semaphore maintains a set of permits.

Each `acquire()` blocks if necessary until a permit is available. Each `release()` add
a permit.

---
# Semaphores in Java

```java
// Creates a Semaphore with the given number of permits and nonfair fairness setting.
Semaphore(int permits)
// Creates a Semaphore with the given number of permits and the given fairness setting.
Semaphore(int permits, boolean fair)
```

Why unfair ?
