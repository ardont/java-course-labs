package edu.course.lab05;

import java.util.Objects;

/**
 * Проектная задача с категорией (вариант Программная инженерия).
 */
public class ProjectTask {

    private final String id;
    private final String title;
    private final String category;
    private final int estimatedHours;
    private final TaskStatus status;

    public ProjectTask(String id, String title, String category, int estimatedHours, TaskStatus status) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id must not be null or blank");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be null or blank");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category must not be null or blank");
        }
        if (estimatedHours <= 0) {
            throw new IllegalArgumentException("Estimated hours must be positive: " + estimatedHours);
        }
        if (status == null) {
            throw new IllegalArgumentException("Status must not be null");
        }

        this.id = id.trim();
        this.title = title.trim();
        this.category = category.trim();
        this.estimatedHours = estimatedHours;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public int getEstimatedHours() {
        return estimatedHours;
    }

    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectTask that = (ProjectTask) o;
        return estimatedHours == that.estimatedHours &&
                Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(category, that.category) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, category, estimatedHours, status);
    }
}
