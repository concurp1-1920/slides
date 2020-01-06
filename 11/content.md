# The `pthread` Programming Model

### Concurrent programming 1 - Lecture 12

.footnote[source : https://computing.llnl.gov/tutorials/pthreads/]

---
class: middle

- IEEE has defined a standard interface for threads.

- The thread package it defines is call Pthreads and most UNIX systems support it.

- Not available natively on windows but some implementation offer an interface.

---
# Why `pthread` ?

- Light Weight
  - Managing process requires a lot more ressources than managing threads.
  - We create process using the `fork()` function, and create threads using the `pthread_create`.

- Efficient Communications/Data Exchange
  - Used in high performance computing environment to achieve optimum performance.
  - No memory copy between threads, they share the same address space within a single process.

---
### Time usage between `fork` and `pthread_create`

<table border="1" cellspacing="0" cellpadding="5" width="100%">
<tbody><tr>
<th rowspan="2">Platform</th>
<th colspan="3"><tt>fork()</tt></th>
<th colspan="3"><tt>pthread_create()</tt></th>
</tr><tr>
<th>real</th>
<th>user</th>
<th>sys</th>
<th>real</th>
<th>user</th>
<th>sys</th>

</tr><tr valign="top" align="right">
<td align="left"><b>Intel 2.6 GHz Xeon E5-2670 (16 cores/node)</b>
</td><td>8.1</td>
<td>0.1</td>
<td>2.9</td>
<td>0.9</td>
<td>0.2</td>
<td>0.3</td>

</tr><tr valign="top" align="right">
<td align="left"><b>Intel 2.8 GHz Xeon 5660 (12 cores/node)</b>
</td><td>4.4</td>
<td>0.4</td>
<td>4.3</td>
<td>0.7</td>
<td>0.2</td>
<td>0.5</td>

</tr><tr valign="top" align="right">
<td align="left"><b>AMD 2.3 GHz Opteron (16 cores/node)</b>
</td><td>12.5</td>
<td>1.0</td>
<td>12.5</td>
<td>1.2</td>
<td>0.2</td>
<td>1.3</td>

</tr><tr valign="top" align="right">
<td align="left"><b>AMD 2.4 GHz Opteron (8 cores/node)</b>
</td><td>17.6</td>
<td>2.2</td>
<td>15.7</td>
<td>1.4</td>
<td>0.3</td>
<td>1.3</td>

</tr><tr valign="top" align="right">
<td align="left"><b>IBM 4.0 GHz POWER6 (8 cpus/node)</b>
</td><td>9.5</td>
<td>0.6</td>
<td>8.8</td>
<td>1.6</td>
<td>0.1</td>
<td>0.4</td>

</tr><tr valign="top" align="right">
<td align="left"><b>IBM 1.9 GHz POWER5 p5-575 (8 cpus/node)</b>
</td><td>64.2</td>
<td>30.7</td>
<td>27.6</td>
<td>1.7</td>
<td>0.6</td>
<td>1.1</td>

</tr><tr valign="top" align="right">
<td align="left"><b>IBM 1.5 GHz POWER4 (8 cpus/node)</b>
</td><td>104.5</td>
<td>48.6</td>
<td>47.2</td>
<td>2.1</td>
<td>1.0</td>
<td>1.5</td>

</tr><tr valign="top" align="right">
<td align="left"><b>INTEL 2.4 GHz Xeon (2 cpus/node)</b>
</td><td>54.9</td>
<td>1.5</td>
<td>20.8</td>
<td>1.6</td>
<td>0.7</td>
<td>0.9</td>

</tr><tr valign="top" align="right">
<td align="left"><b>INTEL 1.4 GHz Itanium2 (4 cpus/node)</b>
</td><td>54.5</td>
<td>1.1</td>
<td>22.2</td>
<td>2.0</td>
<td>1.2</td>
<td>0.6</td>

</tr></tbody></table>

---

### Memory transfer speed between processes (`MPI`) and threads (`pthread`)

<table border="1" cellspacing="0" cellpadding="5">
<tbody><tr>
<th>Platform</th>
<th>MPI Shared Memory Bandwidth<br>(GB/sec)</th>
<th>Pthreads Worst Case<br>Memory-to-CPU Bandwidth <br>(GB/sec)</th>
</tr><tr valign="top">
<td><b>Intel 2.6 GHz Xeon E5-2670 </b></td>
<td align="right">4.5</td>
<td align="right">51.2</td>
</tr><tr valign="top">
<td><b>Intel 2.8 GHz Xeon 5660 </b></td>
<td align="right">5.6</td>
<td align="right">32</td>
</tr><tr valign="top">
<td><b>AMD 2.3 GHz Opteron </b></td>
<td align="right">1.8</td>
<td align="right">5.3</td>
</tr><tr valign="top">
<td><b>AMD 2.4 GHz Opteron </b></td>
<td align="right">1.2</td>
<td align="right">5.3</td>
</tr><tr valign="top">
<td><b>IBM 1.9 GHz POWER5 p5-575 </b></td>
<td align="right">4.1</td>
<td align="right">16</td>
</tr><tr valign="top">
<td><b>IBM 1.5 GHz POWER4</b></td>
<td align="right">2.1</td>
<td align="right">4</td>
</tr><tr valign="top">
<td><b>Intel 2.4 GHz Xeon</b></td>
<td align="right">0.3</td>
<td align="right">4.3</td>
</tr><tr valign="top">
<td><b>Intel 1.4 GHz Itanium 2</b></td>
<td align="right">1.8</td>
<td align="right">6.4</td>
</tr></tbody></table>

---
class: middle

They are around 100 threads procedures which can be categorized into 4 groups :
- Thread management.
- Mutexes.
- Condition variables.
- Synchronization between threads using read/write locks and barriers.

---
class: middle

| Routine Prefix       |                                 Functional Group |
|:-------------------- | ------------------------------------------------:|
| `pthread_`           | Threads themselves and miscellaneous subroutines |
| `pthread_attr`       |                        Thread attributes objects |
| `pthread_mutex`      |                                          Mutexes |
| `pthread_mutexattr_` |                         Mutex attributes objects |
| `pthread_cond_`      |                              Condition variables |
| `pthread_condattr_`  |                     Condition attributes objects |
| `pthread_key`        |                         Mutex attributes objects |
| `pthread_rwlock_`    |                                 Read/Write locks |
| `pthread_barrier_`   |                         Synchronization barriers |

---
# Creating and Terminating Threads
```c
pthread_create (thread,attr,start_routine,arg);

pthread_exit (status);

pthread_cancel (thread);

pthread_attr_init (attr);

pthread_attr_destroy (attr);
```

---
# Joining and Detaching Threads
```c
pthread_join (threadid,status);

pthread_detach (threadid);

pthread_attr_setdetachstate (attr,detachstate);

pthread_attr_getdetachstate (attr,detachstate);
```

---
# Creating and Destroying Mutexes
```c
pthread_mutex_init (mutex,attr);

pthread_mutex_destroy (mutex);

pthread_mutexattr_init (attr);

pthread_mutexattr_destroy (attr);
```

---
# Locking and Unlocking Mutexes
```c
pthread_mutex_lock (mutex);

pthread_mutex_trylock (mutex);

pthread_mutex_unlock (mutex);
```

---
# Creating and Destroying Condition Variables
```c
pthread_cond_init (condition,attr);

pthread_cond_destroy (condition);

pthread_condattr_init (attr);

pthread_condattr_destroy (attr);
```

---
# Waiting and Signaling on Condition Variables
```c
pthread_cond_wait (condition,mutex);

pthread_cond_signal (condition);

pthread_cond_broadcast (condition);
```

---
# Thread exit handler

"Cleanup" routines can be installed that are executed once the thread exits

```c
void pthread_cleanup_push(exit_routine, arg);

void pthread_cleanup_pop(exec);
```
Be aware to always use the push and the pop routine in the same function block!


---
```c
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>

#define NUMBER_OF_THREADS 10

// This function prints the thread’s identifier and then exits.
void *print_hello_world(void *tid) {
  printf("Hello World. Greetings from thread %d\n", tid);
  pthread_exit(NULL); // <- return value when joining
}

// The main program creates 10 threads and then exits.
int main(int argc, char *argv[]) {
  pthread_t threads[NUMBER_OF_THREADS];
  int status, i;
  for(i=0; i < NUMBER_OF_THREADS; i++) {
    printf("Main here. Creating thread %d\n", i);
    status = pthread_create(&threads[i], NULL, print_hello_world, (void *)i);
    if (status != 0) {
      printf("Oops. Pthread_create returned error code %d\n", status);
      exit(-1);
    }
  }
  exit(NULL);
}
```
