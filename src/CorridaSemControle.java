// Versão com condição de corrida (sem sincronização)

// This code is a race condition so all the threads are going to race
// for resources and because of the lack of synchronization we are going
// to get incorrect results

import java.util.concurrent.*;

public class CorridaSemControle {
    static int count = 0; // counter shared by all the threads that will count up to 2.000.000
    public static void main(String[] args) throws Exception {
        // T are the threads and M is the amount each thread will increment the counter by,
        // so the counter should be 2m total 8 * 250.000 = 2.000.000
        int T = 8, M = 250_000;
        ExecutorService pool = Executors.newFixedThreadPool(T); // Creates the thread pool with executor for better management
        Runnable r = () -> {
            for (int i = 0; i < M; i++) {
                // The issue is in the count++ operation itself, it has three steps
                // read count value -> increment count value -> write countv value
                // threads can read the value before any of them write back so we lose increments
                count++; // atualização não atômica: sujeito a perda de incrementos
            }
        };
        long t0 = System.nanoTime();
        for (int i = 0; i < T; i++) pool.submit(r); // this just schedules the threads to the increment loops
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);
        long t1 = System.nanoTime();

        // To sum it all up the threads will race in fron of each other ruining the read-modify-write flow,
        // so we are lucky if we get a quarter of the expected increments
        // really high throughput tho
        System.out.printf("Esperado=%d, Obtido=%d, Tempo=%.3fs%n",
                T * M, count, (t1 - t0) / 1e9);
    }
}