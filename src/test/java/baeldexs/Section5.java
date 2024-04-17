package baeldexs;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Processing Results of Asynchronous Computations
 * use of methods
 * - thenApply
 * - thenAccept
 * - thenRun
 */
class Section5 {
    public static ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


    @BeforeAll
    static void setup() {
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @BeforeEach
    void init() {
        outputStream.reset();
    }

    @DisplayName("Pipeline returning results - thenApply method")
    @Test
    void test1() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture
                = CompletableFuture.supplyAsync(() -> "Hello");

        CompletableFuture<String> future = completableFuture
                .thenApply(s -> s + " World");

        assertEquals("Hello World", future.get());
    }

    @DisplayName("Pipeline using intermediate results without return - thenAccept method")
    @Test
    void test2() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture
                = CompletableFuture.supplyAsync(() -> "Hello");

        CompletableFuture<Void> future = completableFuture
                .thenAccept(s -> System.out.println("Computation returned: " + s));

        future.get();
        assertEquals("Computation returned: Hello", outputStream.toString().trim());
    }


    @DisplayName("Pipeline not using intermediate results without return - thenRun method")
    @Test
    void test3() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture
                = CompletableFuture.supplyAsync(() -> "Hello");

        CompletableFuture<Void> future = completableFuture
                .thenRun(() -> System.out.println("Computation finished."));

        future.get();
        assertEquals("Computation finished.", outputStream.toString().trim());
    }

}
