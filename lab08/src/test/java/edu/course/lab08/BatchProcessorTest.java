package edu.course.lab08;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BatchProcessorTest {

    // --- Подготовительная часть ---

    @Test
    @DisplayName("Подготовительное задание: 3 Callable<Integer>, пул из 2 потоков, сбор результатов и shutdown в finally")
    void preparatoryExerciseExecutorService() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            Callable<Integer> task1 = () -> 10;
            Callable<Integer> task2 = () -> 20;
            Callable<Integer> task3 = () -> 30;

            List<Future<Integer>> futures = new ArrayList<>();
            futures.add(executor.submit(task1));
            futures.add(executor.submit(task2));
            futures.add(executor.submit(task3));

            List<Integer> results = new ArrayList<>();
            for (Future<Integer> future : futures) {
                results.add(future.get());
            }

            assertEquals(List.of(10, 20, 30), results);
        } finally {
            executor.shutdown();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        }
        assertTrue(executor.isShutdown());
    }

    // --- Обязательное задание: Последовательная обработка ---

    @Test
    @DisplayName("Последовательная обработка: пустой список")
    void processSequentiallyEmptyList() {
        BatchProcessor<String, Integer> processor = new BatchProcessor<>();
        List<TaskResult<Integer>> results = processor.processSequentially(List.of(), String::length);
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Последовательная обработка: успешные задачи с сохранением порядка")
    void processSequentiallySuccess() {
        BatchProcessor<String, Integer> processor = new BatchProcessor<>();
        List<String> inputs = List.of("apple", "banana", "kiwi");

        List<TaskResult<Integer>> results = processor.processSequentially(inputs, String::length);

        assertEquals(3, results.size());
        for (int i = 0; i < inputs.size(); i++) {
            assertEquals(i, results.get(i).index());
            assertTrue(results.get(i).isSuccess());
            assertNull(results.get(i).error());
        }
        assertEquals(5, results.get(0).value());
        assertEquals(6, results.get(1).value());
        assertEquals(4, results.get(2).value());
    }

    @Test
    @DisplayName("Последовательная обработка: частичная ошибка не прерывает остальные задачи")
    void processSequentiallyWithErrors() {
        BatchProcessor<String, Integer> processor = new BatchProcessor<>();
        List<String> inputs = List.of("10", "bad", "30");

        List<TaskResult<Integer>> results = processor.processSequentially(inputs, Integer::parseInt);

        assertEquals(3, results.size());
        assertTrue(results.get(0).isSuccess());
        assertEquals(10, results.get(0).value());

        assertFalse(results.get(1).isSuccess());
        assertNotNull(results.get(1).error());
        assertTrue(results.get(1).error() instanceof NumberFormatException);

        assertTrue(results.get(2).isSuccess());
        assertEquals(30, results.get(2).value());
    }

    // --- Обязательное задание: Конкурентная обработка ---

    @Test
    @DisplayName("Конкурентная обработка: пустой список")
    void processConcurrentlyEmptyList() throws InterruptedException {
        BatchProcessor<String, Integer> processor = new BatchProcessor<>();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            List<TaskResult<Integer>> results = processor.processConcurrently(List.of(), String::length, executor);
            assertTrue(results.isEmpty());
        } finally {
            executor.shutdown();
        }
    }

    @Test
    @DisplayName("Конкурентная обработка: сохранение порядка при разном времени выполнения")
    void processConcurrentlyPreservesOrder() throws Exception {
        BatchProcessor<Integer, String> processor = new BatchProcessor<>();
        List<Integer> inputs = List.of(3, 1, 2);

        // Используем CountDownLatch или разный порядок готовности без Thread.sleep
        ExecutorService executor = Executors.newFixedThreadPool(3);
        try {
            List<TaskResult<String>> results = processor.processConcurrently(
                    inputs,
                    num -> "Result-" + num,
                    executor
            );

            assertEquals(3, results.size());
            assertEquals(0, results.get(0).index());
            assertEquals("Result-3", results.get(0).value());

            assertEquals(1, results.get(1).index());
            assertEquals("Result-1", results.get(1).value());

            assertEquals(2, results.get(2).index());
            assertEquals("Result-2", results.get(2).value());
        } finally {
            executor.shutdown();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        }
    }

    @Test
    @DisplayName("Конкурентная обработка: одна ошибка не влияет на другие задачи")
    void processConcurrentlyHandlesPartialFailure() throws Exception {
        BatchProcessor<String, Integer> processor = new BatchProcessor<>();
        List<String> inputs = List.of("100", "error_item", "300");

        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            List<TaskResult<Integer>> results = processor.processConcurrently(
                    inputs,
                    str -> {
                        if ("error_item".equals(str)) {
                            throw new IllegalArgumentException("Simulated task failure");
                        }
                        return Integer.parseInt(str);
                    },
                    executor
            );

            assertEquals(3, results.size());
            assertTrue(results.get(0).isSuccess());
            assertEquals(100, results.get(0).value());

            assertFalse(results.get(1).isSuccess());
            assertEquals("Simulated task failure", results.get(1).error().getMessage());

            assertTrue(results.get(2).isSuccess());
            assertEquals(300, results.get(2).value());
        } finally {
            executor.shutdown();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        }
    }

    @Test
    @DisplayName("Входной список не изменяется при обработке")
    void inputsListIsNotMutated() throws Exception {
        BatchProcessor<String, Integer> processor = new BatchProcessor<>();
        List<String> inputs = new ArrayList<>(List.of("alpha", "beta", "gamma"));
        List<String> copy = List.copyOf(inputs);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            processor.processSequentially(inputs, String::length);
            assertEquals(copy, inputs);

            processor.processConcurrently(inputs, String::length, executor);
            assertEquals(copy, inputs);
        } finally {
            executor.shutdown();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        }
    }

    @Test
    @DisplayName("BatchProcessor не завершает переданный ExecutorService (владелец — вызывающий код)")
    void batchProcessorDoesNotShutdownExecutor() throws Exception {
        BatchProcessor<String, Integer> processor = new BatchProcessor<>();
        ExecutorService executor = Executors.newFixedThreadPool(1);
        try {
            processor.processConcurrently(List.of("test"), String::length, executor);
            assertFalse(executor.isShutdown(), "ExecutorService не должен быть завершен процессором");
        } finally {
            executor.shutdown();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        }
        assertTrue(executor.isShutdown());
    }

    @Test
    @DisplayName("Валидация аргументов на null")
    void validatesNullInputs() {
        BatchProcessor<String, Integer> processor = new BatchProcessor<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            assertThrows(IllegalArgumentException.class, () -> processor.processSequentially(null, String::length));
            assertThrows(IllegalArgumentException.class, () -> processor.processSequentially(List.of(), null));
            assertThrows(IllegalArgumentException.class, () -> processor.processConcurrently(null, String::length, executor));
            assertThrows(IllegalArgumentException.class, () -> processor.processConcurrently(List.of(), null, executor));
            assertThrows(IllegalArgumentException.class, () -> processor.processConcurrently(List.of(), String::length, null));
        } finally {
            executor.shutdown();
        }
    }
}
