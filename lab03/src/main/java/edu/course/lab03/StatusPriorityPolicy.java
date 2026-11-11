package edu.course.lab03;

/**
 * Политика приоритета на основе текущего статуса задачи:
 * IN_PROGRESS = 3 (наивысший приоритет)
 * NEW = 2
 * CANCELLED = 1
 * DONE = 0 (завершено)
 */
public class StatusPriorityPolicy implements PriorityPolicy {

    @Override
    public int calculatePriority(ProjectTask task) {
        if (task == null) {
            throw new IllegalArgumentException("Task must not be null");
        }
        return switch (task.getStatus()) {
            case IN_PROGRESS -> 3;
            case NEW -> 2;
            case CANCELLED -> 1;
            case DONE -> 0;
        };
    }
}
