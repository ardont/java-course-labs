package edu.course.lab03;

/**
 * Политика приоритета на основе оценки трудоемкости.
 * Чем больше часов требуется на задачу, тем выше ее приоритет.
 */
public class EffortPriorityPolicy implements PriorityPolicy {

    @Override
    public int calculatePriority(ProjectTask task) {
        if (task == null) {
            throw new IllegalArgumentException("Task must not be null");
        }
        return task.getEstimatedHours();
    }
}
