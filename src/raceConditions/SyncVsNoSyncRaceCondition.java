package raceConditions;

public class SyncVsNoSyncRaceCondition {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Race Condition (No Sync) ===");
        CorridaSemControle.main(args);

        System.out.println("\n=== With Semaphore (Synchronized) ===");
        CorridaComSemaphore.main(args);
    }
}
