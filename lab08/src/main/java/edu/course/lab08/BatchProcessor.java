package edu.course.lab08;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Обработчик пачки задач: последовательный и конкурентный режимы.
 * Не завершает переданный ExecutorService — им владеет вызывающий код.
 */
public final class BatchProcessor<T, R> {

    /**
     * Последовательная обработка входных элементов.
     * Порядок результатов строго соответствует порядку входных элементов.
     * Ошибка при обработке одного элемента фиксируется в TaskResult.failure
     * и не прерывает обработку остальных.
     */
    public List<TaskResult<R>> processSequentially(
            List<T> inputs,
            TaskFunction<T, R> function) {
        if (inputs == null || function == null) {
            throw new IllegalArgumentException("Inputs and function must not be null");
        }

        List<TaskResult<R>> results = new ArrayList<>(inputs.size());
        for (int i = 0; i < inputs.size(); i++) {
            T item = inputs.get(i);
            try {
                R value = function.apply(item);
                results.add(TaskResult.success(i, value));
            } catch (Exception e) {
                results.add(TaskResult.failure(i, e));
            }
        }
        return Collections.unmodifiableList(results);
    }

    /**
     * Конкурентная обработка входных элементов с использованием ExecutorService.
     * Порядок результатов в результирующем списке строго соответствует порядку входа.
     * Каждая задача возвращает значение через Future, не записывая в общую коллекцию.
     * При InterruptedException оно не подавляется, а пробрасывается вызывающему коду.
     */
    public List<TaskResult<R>> processConcurrently(
            List<T> inputs,
            TaskFunction<T, R> function,
            ExecutorService executor) throws InterruptedException {
        if (inputs == null || function == null || executor == null) {
            throw new IllegalArgumentException("Arguments must not be null");
        }

        if (inputs.isEmpty()) {
            return Collections.emptyList();
        }

        List<Future<TaskResult<R>>> futures = new ArrayList<>(inputs.size());

        for (int i = 0; i < inputs.size(); i++) {
            final int index = i;
            final T item = inputs.get(i);
            Callable<TaskResult<R>> task = () -> {
                try {
                    R val = function.apply(item);
                    return TaskResult.success(index, val);
                } catch (Exception e) {
                    return TaskResult.failure(index, e);
                }
            };
            futures.add(executor.submit(task));
        }

        List<TaskResult<R>> results = new ArrayList<>(inputs.size());
        for (int i = 0; i < futures.size(); i++) {
            Future<TaskResult<R>> future = futures.get(i);
            try {
                results.add(future.get());
            } catch (InterruptedException e) {
                // Отменяем оставшиеся задачи и пробрасываем прерывание
                for (Future<TaskResult<R>> f : futures) {
                    f.cancel(true);
                }
                throw e;
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                Exception err = (cause instanceof Exception) ? (Exception) cause : new RuntimeException(cause);
                results.add(TaskResult.failure(i, err));
            }
        }

        return Collections.unmodifiableList(results);
    }
}
