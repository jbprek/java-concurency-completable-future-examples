package baeldexs;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Handling Errors
 * use of
 * CompletableFuture.handle
 * CompletableFuture.whenComplete()
 * In summary, handle() allows you to provide a fallback value or recover from an exception, whereas whenComplete() is used for side-effects or cleanup actions after the computation completes, regardless of whether it completes normally or exceptionally.
 */
class Section9Test {
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


    @Test
    @DisplayName("CompletableFuture.handle method - happy path ")
    void test1() throws ExecutionException, InterruptedException {

        String name = "John";

        CompletableFuture<String> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            if (name == null || name.isBlank()) {
                throw new RuntimeException("Computation error!");
            }
            return "Hello, " + name;
        }).handle((s, t) -> {
            if (s != null)
                return s;
            else throw new RuntimeException(t);
        });

        assertEquals("Hello, John", completableFuture.get());
    }

    @Test
    @DisplayName("CompletableFuture.handle method - exception ")
    void test2() throws ExecutionException, InterruptedException {

        String name = "";

        CompletableFuture<String> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            if (name == null || name.isBlank()) {
                throw new RuntimeException("Computation error!");
            }
            return "Hello, " + name;
        }).handle((s, t) -> {
            if (s != null)
                return s;
            else throw new RuntimeException(t);
        });

        assertThrows(ExecutionException.class, completableFuture::get);


    }
    AtomicBoolean test3Success = new AtomicBoolean(false);

    private void test3SuccessAction() {
        test3Success.set(true);
    }

    private void test3FailureAction() {

        test3Success.set(false);
    }
    @Test
    @DisplayName("CompletableFuture.whenComplete method - happy path ")
    void test3() throws ExecutionException, InterruptedException {

        String name = "John";

        CompletableFuture<String> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            if (name == null || name.isBlank()) {
                throw new RuntimeException("Computation error!");
            }
            return "Hello, " + name;
        });

        assertEquals("Hello, John", completableFuture.get());

        completableFuture.whenComplete((s, t) -> {
            if (s != null) {
                System.out.println("Computation returned: " + s);
                test3SuccessAction();
            } else {
                System.out.println("Computation returned: " + t.getMessage());
                test3FailureAction();
            }
        });

        assertTrue(test3Success.get());
    }


    @Test
    @DisplayName("CompletableFuture.whenComplete method - Exception ")
    void test4() throws ExecutionException, InterruptedException {

        String name = null;

        CompletableFuture<String> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            if (name == null || name.isBlank()) {
                throw new RuntimeException("Computation error!");
            }
            return "Hello, " + name;
        });

        completableFuture.whenComplete((s, t) -> {
            if (s != null) {
                System.out.println("Computation returned: " + s);
            } else {
                System.out.println("Computation failed: " + t.getMessage());
            }
        });

        assertThrows(ExecutionException.class, completableFuture::get);
        assertEquals("Computation failed: java.lang.RuntimeException: Computation error!", outputStream.toString().trim());


    }

    @Test
    @DisplayName("CompletableFuture.whenComplete method - Exception ")
    void test4_1() {

        String name = null;

        CompletableFuture<String> completableFuture
                = CompletableFuture.supplyAsync(() -> {
            if (name == null || name.isBlank()) {
                throw new RuntimeException("Computation error!");
            }
            return "Hello, " + name;
        });

        completableFuture.whenComplete((s, t) -> {
            if (s != null) {
                test4SuccessAction(s);
            } else {
                test4FailAction(t);
            }
        });

//        assertThrows(ExecutionException.class, completableFuture::get);
        assertEquals("Computation failed: java.lang.RuntimeException: Computation error!", outputStream.toString().trim());


    }

    private void test4SuccessAction(String s) {
        System.out.println("Computation returned: " + s);
    }
    private void test4FailAction(Throwable t) {
        System.out.println("Computation failed: " + t.getMessage());
        throw new IllegalArgumentException(t.getMessage());

    }




}







