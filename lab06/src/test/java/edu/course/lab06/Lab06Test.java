package edu.course.lab06;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Lab06Test {

    // --- Подготовительная часть: Box<T> ---

    @Test
    @DisplayName("Box<T> типобезопасно работает со String и Integer")
    void boxTypeSafety() {
        Box<String> stringBox = new Box<>("Java 25");
        assertEquals("Java 25", stringBox.get());
        stringBox.set("Generics");
        assertEquals("Generics", stringBox.get());

        Box<Integer> intBox = new Box<>(100);
        assertEquals(100, intBox.get());
        intBox.set(200);
        assertEquals(200, intBox.get());

        // Некомпилируемая строка (ошибка компилятора Incompatible types):
        // Box<String> invalidBox = new Box<>(123);
    }

    // --- Обязательное задание: Stack<T> ---

    @Test
    @DisplayName("Stack<T> операции push, pop, peek, size, isEmpty для String и Integer")
    void stackOperationsWithMultipleTypes() {
        Stack<String> stack = new Stack<>();
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());

        stack.push("first");
        stack.push("second");
        assertFalse(stack.isEmpty());
        assertEquals(2, stack.size());
        assertEquals("second", stack.peek());
        assertEquals(2, stack.size());

        assertEquals("second", stack.pop());
        assertEquals("first", stack.pop());
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());

        // Проверка с другим типом данных (Integer)
        Stack<Integer> intStack = new Stack<>();
        intStack.push(10);
        intStack.push(20);
        assertEquals(20, intStack.pop());
        assertEquals(10, intStack.pop());
    }

    @Test
    @DisplayName("Stack<T> запрещает null элементы")
    void stackRejectsNull() {
        Stack<String> stack = new Stack<>();
        assertThrows(IllegalArgumentException.class, () -> stack.push(null));
    }

    @Test
    @DisplayName("Stack<T> выбрасывает NoSuchElementException на пустом стеке")
    void stackThrowsOnEmptyPopAndPeek() {
        Stack<String> stack = new Stack<>();
        assertThrows(NoSuchElementException.class, stack::pop);
        assertThrows(NoSuchElementException.class, stack::peek);
    }

    // --- Обязательное задание: Аналитика TaskAnalytics ---

    @Test
    @DisplayName("TaskAnalytics.countTasksByStatus группирует задачи по статусам")
    void countsTasksByStatus() {
        List<ProjectTask> tasks = List.of(
                new ProjectTask("T-1", "Task 1", "backend", 5, TaskStatus.READY),
                new ProjectTask("T-2", "Task 2", "frontend", 3, TaskStatus.READY),
                new ProjectTask("T-3", "Task 3", "backend", 8, TaskStatus.IN_PROGRESS),
                new ProjectTask("T-4", "Task 4", "qa", 2, TaskStatus.DONE)
        );

        Map<TaskStatus, Integer> counts = TaskAnalytics.countTasksByStatus(tasks);
        assertEquals(2, counts.get(TaskStatus.READY));
        assertEquals(1, counts.get(TaskStatus.IN_PROGRESS));
        assertEquals(1, counts.get(TaskStatus.DONE));
        assertFalse(counts.containsKey(TaskStatus.CANCELLED));
    }

    @Test
    @DisplayName("TaskAnalytics.findUniqueCategories возвращает множество категорий без дубликатов")
    void findsUniqueCategories() {
        List<ProjectTask> tasks = List.of(
                new ProjectTask("T-1", "Task 1", "backend", 5, TaskStatus.READY),
                new ProjectTask("T-2", "Task 2", "frontend", 3, TaskStatus.READY),
                new ProjectTask("T-3", "Task 3", "backend", 8, TaskStatus.IN_PROGRESS),
                new ProjectTask("T-4", "Task 4", "backend", 2, TaskStatus.DONE)
        );

        Set<String> categories = TaskAnalytics.findUniqueCategories(tasks);
        assertEquals(2, categories.size());
        assertTrue(categories.contains("backend"));
        assertTrue(categories.contains("frontend"));
    }

    @Test
    @DisplayName("TaskAnalytics.sortByEstimatedHoursAndId сортирует по трудоемкости и id, не меняя входной список")
    void sortsByEstimatedHoursAndIdWithoutMutatingInput() {
        ProjectTask t1 = new ProjectTask("T-3", "Task C", "backend", 10, TaskStatus.READY);
        ProjectTask t2 = new ProjectTask("T-1", "Task A", "backend", 4, TaskStatus.READY);
        ProjectTask t3 = new ProjectTask("T-2", "Task B", "backend", 4, TaskStatus.READY);

        List<ProjectTask> originalList = List.of(t1, t2, t3);
        List<ProjectTask> sortedList = TaskAnalytics.sortByEstimatedHoursAndId(originalList);

        // Порядок в отсортированном списке: 4h (T-1), 4h (T-2), 10h (T-3)
        assertEquals(3, sortedList.size());
        assertEquals("T-1", sortedList.get(0).getId());
        assertEquals("T-2", sortedList.get(1).getId());
        assertEquals("T-3", sortedList.get(2).getId());

        // Исходный список остался неизменным
        assertEquals("T-3", originalList.get(0).getId());
        assertEquals("T-1", originalList.get(1).getId());
        assertEquals("T-2", originalList.get(2).getId());
    }
}
