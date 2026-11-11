package edu.course.lab07;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Обработка данных через последовательный конвейер Stream API.
 * Методы не имеют побочных эффектов, не изменяют входные коллекции и не используют циклов.
 */
public final class DataAnalytics {

    private DataAnalytics() {
    }

    /**
     * Подготовительная часть:
     * Для списка строк получает новый список непустых строк в верхнем регистре,
     * отсортированный сначала по длине, затем по алфавиту.
     */
    public static List<String> processStrings(List<String> strings) {
        if (strings == null) {
            throw new IllegalArgumentException("Strings list must not be null");
        }
        return strings.stream()
                .filter(s -> s != null && !s.isEmpty())
                .map(String::toUpperCase)
                .sorted(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()))
                .toList();
    }

    /**
     * 1. Отфильтровать задачи по статусу.
     */
    public static List<ProjectTask> filterByStatus(List<ProjectTask> tasks, TaskStatus status) {
        if (tasks == null || status == null) {
            throw new IllegalArgumentException("Arguments must not be null");
        }
        return tasks.stream()
                .filter(t -> t.getStatus() == status)
                .toList();
    }

    /**
     * 2. Получить отсортированный список нормализованных текстовых категорий.
     */
    public static List<String> getNormalizedSortedCategories(List<ProjectTask> tasks) {
        if (tasks == null) {
            throw new IllegalArgumentException("Tasks list must not be null");
        }
        return tasks.stream()
                .map(t -> t.getCategory().trim().toLowerCase())
                .distinct()
                .sorted()
                .toList();
    }

    /**
     * 3. Посчитать число объектов по категории через groupingBy и counting.
     */
    public static Map<String, Long> countTasksByCategory(List<ProjectTask> tasks) {
        if (tasks == null) {
            throw new IllegalArgumentException("Tasks list must not be null");
        }
        return tasks.stream()
                .collect(Collectors.groupingBy(ProjectTask::getCategory, Collectors.counting()));
    }

    /**
     * 4. Найти задачу с максимальной трудоемкостью и вернуть Optional.
     */
    public static Optional<ProjectTask> findTaskWithMaxEstimatedHours(List<ProjectTask> tasks) {
        if (tasks == null) {
            throw new IllegalArgumentException("Tasks list must not be null");
        }
        return tasks.stream()
                .max(Comparator.comparingInt(ProjectTask::getEstimatedHours));
    }

    /**
     * 5. Получить новый список задач, отсортированный по двум ключам:
     * трудоемкости (по возрастанию) и названию (по алфавиту).
     */
    public static List<ProjectTask> sortTasksByEstimatedHoursAndTitle(List<ProjectTask> tasks) {
        if (tasks == null) {
            throw new IllegalArgumentException("Tasks list must not be null");
        }
        return tasks.stream()
                .sorted(Comparator.comparingInt(ProjectTask::getEstimatedHours)
                        .thenComparing(ProjectTask::getTitle))
                .toList();
    }
}
