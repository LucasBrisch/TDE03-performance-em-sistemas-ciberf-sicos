package raceConditions;// Versão correta com Semaphore binário justo

import java.util.concurrent.*;

// all of this stuff at the start is the same except for the Semaphore
public class CorridaComSemaphore {
    static int count = 0;
    // alright here's how semaphore works, you have two parameters: permits and fair
    // permits are kinda like a "ticket" that will allow a thread to run
    // the threads are going to call a function called acquire(),
    // this function will acquire said "ticket" and block all other threads from taking it
    // and when release() is called the permit becomes available again and the cycle repeats
    // in practice this just means one thread can run at a time

    // fair controls which thread gets the permit, true means we have FIFO ordering
    // so threads are guaranteed the permit in the order they requested it
    // unfair (false) is technically faster but, it means a thread could theoretically wait forever
    static final Semaphore sem = new Semaphore(1, true); // FIFO
    public static void main(String[] args) throws Exception {
        int T = 8, M = 250_000;

        ExecutorService pool = Executors.newFixedThreadPool(T);

        Runnable r = () -> {
            for (int i = 0; i < M; i++) {
                try {
                    sem.acquire(); // asks for the permit
                    count++; // only the thread with the permit actually executes this
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    sem.release(); // gives back the permit
                }
            }
        };
        long t0 = System.nanoTime();
        for (int i = 0; i < T; i++) pool.submit(r);
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);
        long t1 = System.nanoTime();
        // this setup guarantees that all threads are synchronized by eliminating the race condition
        // that means that every read-modify-write flow is successful,
        // however it also slows the execution considerably both from making the threads wait and Semaphore overhead
        System.out.printf("Esperado=%d, Obtido=%d, Tempo=%.3fs%n",
                T * M, count, (t1 - t0) / 1e9);
    }
}