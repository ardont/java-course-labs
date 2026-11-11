package edu.course.lab02;

/**
 * Проектная задача (вариант "Программная инженерия").
 * Инкапсулирует состояние задачи и обеспечивает соблюдение инвариантов.
 */
public class ProjectTask {

    private final String id;
    private final String title;
    private TaskStatus status;
    private int estimatedHours;

    /**
     * Конструктор проектной задачи.
     * Проверяет обязательные инварианты:
     * - id не null и не пустой;
     * - title не null и не пустой;
     * - status не null;
     * - estimatedHours строго больше 0.
     */
    public ProjectTask(String id, String title, TaskStatus status, int estimatedHours) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Task id must not be null or blank");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Task title must not be null or blank");
        }
        if (status == null) {
            throw new IllegalArgumentException("Task status must not be null");
        }
        if (estimatedHours <= 0) {
            throw new IllegalArgumentException("Estimated hours must be positive: " + estimatedHours);
        }

        this.id = id.trim();
        this.title = title.trim();
        this.status = status;
        this.estimatedHours = estimatedHours;
    }

    /**
     * Изменяет статус задачи. Запрещен null.
     */
    public void changeStatus(TaskStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("New status must not be null");
        }
        this.status = newStatus;
    }

    /**
     * Проверяет, завершена ли задача.
     */
    public boolean isDone() {
        return this.status == TaskStatus.DONE;
    }

    /**
     * Увеличивает оценку трудоемкости на положительное количество часов.
     */
    public void increaseEstimatedHours(int additionalHours) {
        if (additionalHours <= 0) {
            throw new IllegalArgumentException("Additional hours must be positive: " + additionalHours);
        }
        this.estimatedHours += additionalHours;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public int getEstimatedHours() {
        return estimatedHours;
    }
}
