package edu.course.lab07;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataAnalyticsTest {

    // --- Подготовительная часть ---

    @Test
    @DisplayName("Подготовительное задание: фильтрация непустых, toUpperCase, сортировка по длине и алфавиту")
    void preparatoryExerciseStream() {
        List<String> mutableList = new ArrayList<>(List.of("java", "", "git", "maven", "api"));
        List<String> snapshot = List.copyOf(mutableList);

        List<String> result = DataAnalytics.processStrings(mutableList);

        // Ожидаемый результат: ["API", "GIT", "JAVA", "MAVEN"]
        assertEquals(List.of("API", "GIT", "JAVA", "MAVEN"), result);

        // Проверяем, что исходный список не был модифицирован
        assertEquals(snapshot, mutableList);
    }

    // --- Обязательное задание ---

    @Test
    @DisplayName("1. filterByStatus корректно фильтрует задачи и возвращает пустой список, если совпадений нет")
    void filterByStatusWorksCorrectly() {
        List<ProjectTask> tasks = List.of(
                new ProjectTask("T1", "Title 1", "backend", 4, TaskStatus.READY),
                new ProjectTask("T2", "Title 2", "frontend", 6, TaskStatus.IN_PROGRESS),
                new ProjectTask("T3", "Title 3", "backend", 8, TaskStatus.READY)
        );

        List<ProjectTask> readyTasks = DataAnalytics.filterByStatus(tasks, TaskStatus.READY);
        assertEquals(2, readyTasks.size());
        assertEquals("T1", readyTasks.get(0).getId());
        assertEquals("T3", readyTasks.get(1).getId());

        List<ProjectTask> cancelledTasks = DataAnalytics.filterByStatus(tasks, TaskStatus.CANCELLED);
        assertTrue(cancelledTasks.isEmpty());
    }

    @Test
    @DisplayName("2. getNormalizedSortedCategories возвращает отсортированные уникальные категории в нижнем регистре")
    void getNormalizedSortedCategoriesWorksCorrectly() {
        List<ProjectTask> tasks = List.of(
                new ProjectTask("T1", "Task 1", "  Backend  ", 4, TaskStatus.READY),
                new ProjectTask("T2", "Task 2", "frontend", 6, TaskStatus.READY),
                new ProjectTask("T3", "Task 3", "BACKEND", 8, TaskStatus.READY),
                new ProjectTask("T4", "Task 4", "api", 2, TaskStatus.READY)
        );

        List<String> categories = DataAnalytics.getNormalizedSortedCategories(tasks);
        assertEquals(List.of("api", "backend", "frontend"), categories);
    }

    @Test
    @DisplayName("3. countTasksByCategory группирует задачи по категориям и считает количество")
    void countTasksByCategoryWorksCorrectly() {
        List<ProjectTask> tasks = List.of(
                new ProjectTask("T1", "Task 1", "backend", 4, TaskStatus.READY),
                new ProjectTask("T2", "Task 2", "frontend", 6, TaskStatus.READY),
                new ProjectTask("T3", "Task 3", "backend", 8, TaskStatus.READY),
                new ProjectTask("T4", "Task 4", "devops", 2, TaskStatus.READY)
        );

        Map<String, Long> counts = DataAnalytics.countTasksByCategory(tasks);
        assertEquals(3, counts.size());
        assertEquals(2L, counts.get("backend"));
        assertEquals(1L, counts.get("frontend"));
        assertEquals(1L, counts.get("devops"));
    }

    @Test
    @DisplayName("4. findTaskWithMaxEstimatedHours находит максимум либо возвращает пустой Optional")
    void findTaskWithMaxEstimatedHoursWorksCorrectly() {
        ProjectTask t1 = new ProjectTask("T1", "Small task", "backend", 2, TaskStatus.READY);
        ProjectTask t2 = new ProjectTask("T2", "Huge refactoring", "backend", 40, TaskStatus.READY);
        ProjectTask t3 = new ProjectTask("T3", "Medium task", "backend", 10, TaskStatus.READY);

        Optional<ProjectTask> maxTask = DataAnalytics.findTaskWithMaxEstimatedHours(List.of(t1, t2, t3));
        assertTrue(maxTask.isPresent());
        assertEquals("T2", maxTask.get().getId());
        assertEquals(40, maxTask.get().getEstimatedHours());

        // Проверка для пустого списка
        Optional<ProjectTask> emptyResult = DataAnalytics.findTaskWithMaxEstimatedHours(List.of());
        assertFalse(emptyResult.isPresent());
    }

    @Test
    @DisplayName("5. sortTasksByEstimatedHoursAndTitle сортирует по двум ключам (часы, затем заголовок)")
    void sortTasksByEstimatedHoursAndTitleWorksCorrectly() {
        ProjectTask t1 = new ProjectTask("T1", "Beta task", "backend", 10, TaskStatus.READY);
        ProjectTask t2 = new ProjectTask("T2", "Alpha task", "backend", 10, TaskStatus.READY);
        ProjectTask t3 = new ProjectTask("T3", "Quick fix", "backend", 2, TaskStatus.READY);

        List<ProjectTask> original = List.of(t1, t2, t3);
        List<ProjectTask> sorted = DataAnalytics.sortTasksByEstimatedHoursAndTitle(original);

        // Сначала 2h ("Quick fix"), затем 10h "Alpha task", затем 10h "Beta task"
        assertEquals(3, sorted.size());
        assertEquals("T3", sorted.get(0).getId());
        assertEquals("T2", sorted.get(1).getId());
        assertEquals("T1", sorted.get(2).getId());

        // Исходный список не изменился
        assertEquals("T1", original.get(0).getId());
    }

    @Test
    @DisplayName("Проверка валидации входных аргументов на null")
    void validatesNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> DataAnalytics.processStrings(null));
        assertThrows(IllegalArgumentException.class, () -> DataAnalytics.filterByStatus(null, TaskStatus.READY));
        assertThrows(IllegalArgumentException.class, () -> DataAnalytics.filterByStatus(List.of(), null));
        assertThrows(IllegalArgumentException.class, () -> DataAnalytics.getNormalizedSortedCategories(null));
        assertThrows(IllegalArgumentException.class, () -> DataAnalytics.countTasksByCategory(null));
        assertThrows(IllegalArgumentException.class, () -> DataAnalytics.findTaskWithMaxEstimatedHours(null));
        assertThrows(IllegalArgumentException.class, () -> DataAnalytics.sortTasksByEstimatedHoursAndTitle(null));
    }
}
