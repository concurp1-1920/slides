# Concurrency in Java

### Concurrent programming 1 - Lecture 6

.center[<img src="img/java.png" width="350"/>]
---
# Java Threads

```java
public class Example {

  public static void main(String[] args) {
    Thread thread = new Thread () {
      public void run() {
        // Some code
      }
    };

    thread.start();
  }

}
```
---
# Java Threads

```java
public class Example {

  public static void main(String[] args) {
    Runnable runnable = new Runnable() {
      public void run() {
        // Some code
      }
    };

    (new Thread(runnable)).start();
  }
}
```
---
# Java 8 - Runnable

`Runnable` is a functional interface.

```java
public interface Runnable {
  public void run();
}
```

We can use lambda expression !

```java
Runnable r = () -> { /* Some Code */ }
```

Easy to do simple things asynchronously.
---
# Volatile

```java
int res;
int main(void) {
    res = 1;
    int max = 10;
    for (int i = 1; i <= max; i++) {
        res *= i;
    }
    int a = res;
    return 1;
}
```
---
# Volatile

When compiling with -O0

```
>>> gcc -O0 main.c
>>> objdump -d a.out

0000000000001125 <main>:
    1125:  55                    push   %rbp
    1126:  48 89 e5              mov    %rsp,%rbp
    1129:  c7 05 e1 2e 00 00 01  movl   $0x1,0x2ee1(%rip)   # 4014 <res>
    1130:  00 00 00
    1133:  c7 45 f8 0a 00 00 00  movl   $0xa,-0x8(%rbp)
    113a:  c7 45 f4 01 00 00 00  movl   $0x1,-0xc(%rbp)
    1141:  eb 14                 jmp    1157 <main+0x32>
    1143:  8b 05 cb 2e 00 00     mov    0x2ecb(%rip),%eax   # 4014 <res>
    1149:  0f af 45 f4           imul   -0xc(%rbp),%eax
    114d:  89 05 c1 2e 00 00     mov    %eax,0x2ec1(%rip)   # 4014 <res>
    1153:  83 45 f4 01           addl   $0x1,-0xc(%rbp)
    1157:  8b 45 f4              mov    -0xc(%rbp),%eax
    115a:  3b 45 f8              cmp    -0x8(%rbp),%eax
    115d:  7e e4                 jle    1143 <main+0x1e>
    115f:  8b 05 af 2e 00 00     mov    0x2eaf(%rip),%eax   # 4014 <res>
    1165:  89 45 fc              mov    %eax,-0x4(%rbp)
    1168:  90                    nop
    1169:  5d                    pop    %rbp
    116a:  c3                    retq   
    116b:  0f 1f 44 00 00        nopl   0x0(%rax,%rax,1)
```


---
# Volatile

When compiling with -O3

```
>>> gcc -O3 main.c
>>> objdump -d a.out


0000000000001040 <main>:
    1040:       c7 05 ca 2f 00 00 00    movl   $0x375f00,0x2fca(%rip)        # 4014 <res>
    1047:       5f 37 00
    104a:       c3                      retq   
    104b:       0f 1f 44 00 00          nopl   0x0(%rax,%rax,1)




>>> echo "obase=10; ibase=16; 375F00" | bc

3628800
```
---
# Volatile

```java
volatile int res;
void main(void) {
  res = 1;
  int max = 10;
  for (int i = 1; i <= max; i++) {
    res *= i;
  }
  int a = res;
}
```
---
# Volatile

When using the `volatile` keyword, the compiler is not allowed to optimise the variable.

```
0000000000001040 <main>:
movl   $0x1,0x2fca(%rip)        # 4014 <res>
mov    0x2fc4(%rip),%eax        # 4014 <res>
mov    %eax,0x2fbe(%rip)        # 4014 <res>
mov    0x2fb8(%rip),%eax        # 4014 <res>
add    %eax,%eax
mov    %eax,0x2fb0(%rip)        # 4014 <res>
mov    0x2faa(%rip),%eax        # 4014 <res>
lea    (%rax,%rax,2),%eax
mov    %eax,0x2fa1(%rip)        # 4014 <res>
mov    0x2f9b(%rip),%eax        # 4014 <res>

...

lea    (%rax,%rax,4),%eax
add    %eax,%eax
mov    %eax,0x2f2e(%rip)        # 4014 <res>
mov    0x2f28(%rip),%eax        # 4014 <res>
retq   
nopl   (%rax)
```

---
# Synchronization in Java

```java
public class Account {

  private volatile int total = 0;

  public void withdraw(int amount) { total -= amount; }

  public void deposit(int amount) { total += amount; }

  public int money() { return total; }
}
```

---

```java
public class Main {

  private static final int NB_THREAD = 10;
  private static final int NB_ITR_THREAD = 1000;

  public static void main(String[] args) {

    Bank account = new Account();
    CyclicBarrier finish = new CyclicBarrier(NB_THREAD + 1);

    for (int pid = 0; pid < NB_THREAD; pid++) { (new Thread(() -> {
      for (int j = 0; j < NB_ITR_THREAD; j++) {
        account.deposit(50);
        account.withdraw(25);
        account.deposit(25);
        account.withdraw(50);
      }

      finish.await(); // missing try/catch
    })).start();
  }

  finish.await(); // missing try/catch
  System.out.println("Account amount : " + account.money());
}
}
```

---
# Synchronized

There is no Synchronization, how to solve this problem ?

```
$> java Main
Bank amount : 3300

$> java Main
Bank amount : 0

$> java Main
Bank amount : 31900

$> java Main
Bank amount : 675
```

---
# Synchronized

Synchronized blocks are a way to mutually exclude the execution of specific blocks of code.

```java
public class Account {

  private volatile int total = 0;

  public synchronized void withdraw(int amount) {
    total -= amount;
  }

  public synchronized void deposit(int amount) {
    total += amount;
  }

  public int money() { return total; }
}
```
---
# Double Account

```java
public class DoubleAccount {

  private volatile int total1 = 0;
  private volatile int total2 = 0;

  public synchronized void withdraw1(int amount) {
    total1 -= amount;
  }
  public synchronized void deposit1(int amount) {
    total1 += amount;
  }

  public synchronized void withdraw2(int amount) {
    total2 -= amount;
  }
  public synchronized void deposit2(int amount) {
    total2 += amount;
  }

  public int totalMoney() { return total1 + total2; }
}
```

---
# Objects as synchronized locks

This way, `lock1` and `lock2` are not mutually exclude.

```java
private final Object lock1 = new Object();
private final Object lock2 = new Object();

public void withdraw1(int amount) {
  synchronized (lock1) {
    total1 -= amount;
  }
}

public void withdraw2(int amount) {
  synchronized (lock2) {
    total2 -= amount;
  }
}
```

---
# Atomic Objects



---
# Locks

---
# In class exercise
