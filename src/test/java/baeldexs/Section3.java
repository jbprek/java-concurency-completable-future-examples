package baeldexs;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Using CompletableFuture as a Simple Future
 */
class Section3 {

    private static ExecutorService executorService;

    @BeforeAll
    static void setup() {
        executorService =  Executors.newCachedThreadPool();
    }

    @AfterAll
    static void tearDown() {
        executorService.shutdown();
    }

    @Test
     void test1() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        executorService.submit(() -> {
            Thread.sleep(2000);
            System.out.println("--- After Sleep end");
            completableFuture.complete("Hello");
            System.out.println("--- After completableFuture.complete");

            return null;
        });

        assertEquals("Hello", completableFuture.get());
    }

    @Test
    void test2() throws ExecutionException, InterruptedException {
        Future<String> completableFuture =
                CompletableFuture.completedFuture("Hello");

        assertEquals("Hello", completableFuture.get());
    }

}
