package deadlock;

import java.util.concurrent.*;

public class DeadlockDemo {
    // Defines the two shared objects
    static final Object LOCK_A = new Object();
    static final Object LOCK_B = new Object();

    public static void main(String[] args) {
        // Both threads have similar behaviors,
        Thread t1 = new Thread(() -> {
            // first a thread acquires the LOCK using synchronized which means that only one thread at a time can interact with the monitor
            synchronized (LOCK_A) {
                // then it waits 50 ms while holding the LOCK so the other thread can go,
                dormir(50);
                // and finally it tries acquiring the other LOCK.
                // But the other lock is held by the other thread which means neither thread can complete, and we get a deadlock
                synchronized (LOCK_B) {
                    System.out.println("T1 concluiu");
                }
            }
        });

        /*
        Thread t2 = new Thread(() -> {
            synchronized (LOCK_B) {
                dormir(50);
                synchronized (LOCK_A) {
                    System.out.println("T2 concluiu");
                }
            }
        });
        */

        // simply flipping the order of the locks in one of the threads is enough to fix the deadlock
        Thread t2_corrected = new Thread(() -> {
            synchronized (LOCK_A) {
                dormir(50);
                synchronized (LOCK_B) {
                    System.out.println("T2 concluiu");
                }
            }
        });
        t1.start();
        // t2.start();
        t2_corrected.start();
    }

    // This method is used so that threads shut down properly
    static void dormir(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); }
    }
}