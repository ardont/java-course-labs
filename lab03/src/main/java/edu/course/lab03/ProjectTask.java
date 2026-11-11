package edu.course.lab03;

/**
 * Минимальная версия проектной задачи для модуля lab03.
 */
public class ProjectTask {

    private final String id;
    private final String title;
    private final TaskStatus status;
    private final int estimatedHours;

    public ProjectTask(String id, String title, TaskStatus status, int estimatedHours) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id must not be null or blank");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be null or blank");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status must not be null");
        }
        if (estimatedHours <= 0) {
            throw new IllegalArgumentException("Estimated hours must be positive: " + estimatedHours);
        }
        this.id = id.trim();
        this.title = title.trim();
        this.status = status;
        this.estimatedHours = estimatedHours;
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
