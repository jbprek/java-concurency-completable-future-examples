package baeldexs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * CompletableFuture with Encapsulated Computation Logic
 * use of
 * supplyAsync with Supplier
 * runAsync with Runnable
 */
class Section4Test {

    @Test
    @DisplayName("CompletableFuture.supplyAsync method ")
    void test1() throws ExecutionException, InterruptedException {

        Supplier<String> supplier = () -> {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Hello";
        };
        CompletableFuture<String> completableFuture
                = CompletableFuture.supplyAsync(supplier);

        assertEquals("Hello", completableFuture.get());
    }

    @Test
    @DisplayName("CompletableFuture.runAsync method ")
    void test2() throws ExecutionException, InterruptedException {

        Runnable runnable = () -> {
            try {
                sleep(1000);
                System.out.println("Hello from Runnable");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        CompletableFuture.runAsync(runnable).get();

        assertEquals("Hello from Runnable", outputStream.toString().trim());
    }

}


