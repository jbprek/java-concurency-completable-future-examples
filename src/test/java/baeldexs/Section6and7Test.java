package baeldexs;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Combining Futures
 * use of
 * - thenCompose
 * - theAcceptBoth
 * Difference between thenCompose and thenApply
 */
class Section6and7Test {
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

    @DisplayName("Pipeline returning results from before  - thenApply method")
    @Test
    void test0() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture
                = CompletableFuture.supplyAsync(() -> "Hello");

        CompletableFuture<String> future = completableFuture
                .thenApply(s -> s + " World");

        assertEquals("Hello World", future.get());
    }

    @DisplayName("Pipeline returning results - thenCompose method")
    @Test
    void test1() throws ExecutionException, InterruptedException {
        // Compare with the above no need to create second CompletableFuture
        CompletableFuture<String> completableFuture
                = CompletableFuture.supplyAsync(() -> "Hello")
                .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " World"));

        assertEquals("Hello World", completableFuture.get());
    }

    @DisplayName("Combine 2 independent CompletableFutures - thenCompose method")
    @Test
    void test2() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture1
                = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> completableFuture2
                = CompletableFuture.supplyAsync(() -> " World");

        CompletableFuture<String> completableFuture = completableFuture1.thenCombine(completableFuture2, (s1, s2) -> s1 + s2);


        assertEquals("Hello World", completableFuture.get());

    }

    @DisplayName("Combine 2 independent CompletableFutures - thenCompose method")
    @Test
    void test2_1() throws ExecutionException, InterruptedException {
        // Same as above short form
        CompletableFuture<String> completableFuture
                = CompletableFuture.supplyAsync(() -> "Hello")
                .thenCombine(CompletableFuture.supplyAsync(
                        () -> " World"), (s1, s2) -> s1 + s2);

        assertEquals("Hello World", completableFuture.get());
    }

    @DisplayName("Combine 2 independent CompletableFutures - thenAcceptBoth method")
    @Test
    void test3() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture1
                = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> completableFuture2
                = CompletableFuture.supplyAsync(() -> " World");

        BiConsumer<String,String> biConsumer = (s1, s2) -> System.out.println(s1 + s2);

        CompletableFuture<Void> completableFuture = completableFuture1.thenAcceptBoth(completableFuture2, biConsumer);


        assertEquals("Hello World", outputStream.toString().trim());

    }

    @DisplayName("Combine 2 independent CompletableFutures - thenAcceptBoth method")
    @Test
    void test3_1() throws ExecutionException, InterruptedException {
        // Same as above short form
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> "Hello")
                .thenAcceptBoth(CompletableFuture.supplyAsync(() -> " World"),
                        (s1, s2) -> System.out.println(s1 + s2));

        assertEquals("Hello World", outputStream.toString().trim());
    }




}
