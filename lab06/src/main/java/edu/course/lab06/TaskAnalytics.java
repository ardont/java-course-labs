package edu.course.lab06;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Аналитика проектных задач на основе коллекций и циклов (без Stream API).
 */
public final class TaskAnalytics {

    private TaskAnalytics() {
    }

    /**
     * Считает количество задач по каждому статусу через Map.
     */
    public static Map<TaskStatus, Integer> countTasksByStatus(List<ProjectTask> tasks) {
        if (tasks == null) {
            throw new IllegalArgumentException("Tasks list must not be null");
        }
        Map<TaskStatus, Integer> counts = new HashMap<>();
        for (ProjectTask task : tasks) {
            if (task == null) continue;
            TaskStatus status = task.getStatus();
            counts.put(status, counts.getOrDefault(status, 0) + 1);
        }
        return counts;
    }

    /**
     * Возвращает множество уникальных категорий задач через Set.
     */
    public static Set<String> findUniqueCategories(List<ProjectTask> tasks) {
        if (tasks == null) {
            throw new IllegalArgumentException("Tasks list must not be null");
        }
        Set<String> categories = new LinkedHashSet<>();
        for (ProjectTask task : tasks) {
            if (task == null) continue;
            categories.add(task.getCategory());
        }
        return categories;
    }

    /**
     * Возвращает новый список задач, отсортированный по трудоемкости и идентификатору.
     * Исходный список не изменяется.
     * Для сортировки используется Comparator, а не вычитание чисел.
     */
    public static List<ProjectTask> sortByEstimatedHoursAndId(List<ProjectTask> tasks) {
        if (tasks == null) {
            throw new IllegalArgumentException("Tasks list must not be null");
        }
        List<ProjectTask> sortedList = new ArrayList<>(tasks);
        sortedList.sort(
                Comparator.comparingInt(ProjectTask::getEstimatedHours)
                          .thenComparing(ProjectTask::getId)
        );
        return sortedList;
    }
}
