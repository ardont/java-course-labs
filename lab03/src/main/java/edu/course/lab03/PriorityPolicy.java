package edu.course.lab03;

/**
 * Интерфейс политики вычисления приоритета задачи.
 */
public interface PriorityPolicy {

    /**
     * Вычисляет приоритет задачи в виде целого числа.
     * Чем больше число, тем выше приоритет.
     */
    int calculatePriority(ProjectTask task);
}
