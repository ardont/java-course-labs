package edu.course.lab03;

/**
 * Обработчик задач, выбирающий наиболее приоритетную задачу
 * на основе внедренной политики PriorityPolicy.
 */
public class TaskPrioritizer {

    private final PriorityPolicy policy;

    public TaskPrioritizer(PriorityPolicy policy) {
        if (policy == null) {
            throw new IllegalArgumentException("Priority policy must not be null");
        }
        this.policy = policy;
    }

    /**
     * Выбирает более приоритетную из двух задач.
     * При равенстве приоритетов возвращает первую задачу.
     */
    public ProjectTask chooseHigherPriority(ProjectTask task1, ProjectTask task2) {
        if (task1 == null || task2 == null) {
            throw new IllegalArgumentException("Tasks must not be null");
        }
        int priority1 = policy.calculatePriority(task1);
        int priority2 = policy.calculatePriority(task2);

        if (priority2 > priority1) {
            return task2;
        }
        return task1;
    }

    public PriorityPolicy getPolicy() {
        return policy;
    }
}
