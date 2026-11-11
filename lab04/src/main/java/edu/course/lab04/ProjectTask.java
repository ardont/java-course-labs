package edu.course.lab04;

import java.util.Objects;

/**
 * Минимальная версия проектной задачи для lab04.
 */
public class ProjectTask {

    private final String id;
    private final String title;
    private final int estimatedHours;

    public ProjectTask(String id, String title, int estimatedHours) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id must not be null or blank");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be null or blank");
        }
        if (estimatedHours <= 0) {
            throw new IllegalArgumentException("Estimated hours must be positive: " + estimatedHours);
        }
        this.id = id.trim();
        this.title = title.trim();
        this.estimatedHours = estimatedHours;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getEstimatedHours() {
        return estimatedHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectTask that = (ProjectTask) o;
        return estimatedHours == that.estimatedHours &&
                Objects.equals(id, that.id) &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, estimatedHours);
    }

    @Override
    public String toString() {
        return "ProjectTask{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", estimatedHours=" + estimatedHours +
                '}';
    }
}
