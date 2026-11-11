package edu.course.lab03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskPrioritizerTest {

    private final ProjectTask shortTask = new ProjectTask("T1", "Fix typo", TaskStatus.IN_PROGRESS, 2);
    private final ProjectTask longTask = new ProjectTask("T2", "Refactor backend", TaskStatus.NEW, 16);
    private final ProjectTask doneTask = new ProjectTask("T3", "Deploy app", TaskStatus.DONE, 20);

    @Test
    @DisplayName("EffortPriorityPolicy вычисляет приоритет по трудоемкости")
    void effortPriorityPolicyCalculatesCorrectly() {
        PriorityPolicy policy = new EffortPriorityPolicy();
        assertEquals(2, policy.calculatePriority(shortTask));
        assertEquals(16, policy.calculatePriority(longTask));
        assertEquals(20, policy.calculatePriority(doneTask));
    }

    @Test
    @DisplayName("EffortPriorityPolicy выбрасывает исключение при null задаче")
    void effortPriorityPolicyThrowsOnNull() {
        PriorityPolicy policy = new EffortPriorityPolicy();
        assertThrows(IllegalArgumentException.class, () -> policy.calculatePriority(null));
    }

    @Test
    @DisplayName("StatusPriorityPolicy вычисляет приоритет по статусу")
    void statusPriorityPolicyCalculatesCorrectly() {
        PriorityPolicy policy = new StatusPriorityPolicy();
        assertEquals(3, policy.calculatePriority(shortTask)); // IN_PROGRESS
        assertEquals(2, policy.calculatePriority(longTask));  // NEW
        assertEquals(0, policy.calculatePriority(doneTask));  // DONE
    }

    @Test
    @DisplayName("StatusPriorityPolicy выбрасывает исключение при null задаче")
    void statusPriorityPolicyThrowsOnNull() {
        PriorityPolicy policy = new StatusPriorityPolicy();
        assertThrows(IllegalArgumentException.class, () -> policy.calculatePriority(null));
    }

    @Test
    @DisplayName("TaskPrioritizer с EffortPriorityPolicy выбирает более трудоемкую задачу")
    void prioritizerSelectsTaskWithHigherEffort() {
        TaskPrioritizer prioritizer = new TaskPrioritizer(new EffortPriorityPolicy());
        ProjectTask chosen = prioritizer.chooseHigherPriority(shortTask, longTask);
        assertSame(longTask, chosen);
    }

    @Test
    @DisplayName("TaskPrioritizer со StatusPriorityPolicy выбирает задачу со статусом выше")
    void prioritizerSelectsTaskWithHigherStatus() {
        // shortTask имеет статус IN_PROGRESS (3), longTask имеет статус NEW (2), хотя трудоемкость у longTask выше
        TaskPrioritizer prioritizer = new TaskPrioritizer(new StatusPriorityPolicy());
        ProjectTask chosen = prioritizer.chooseHigherPriority(shortTask, longTask);
        assertSame(shortTask, chosen);
    }

    @Test
    @DisplayName("Заменяемость стратегий в TaskPrioritizer без изменения класса")
    void swappableStrategiesDemonstratePolymorphism() {
        ProjectTask taskA = new ProjectTask("A", "Task A", TaskStatus.IN_PROGRESS, 4);
        ProjectTask taskB = new ProjectTask("B", "Task B", TaskStatus.NEW, 12);

        TaskPrioritizer effortPrioritizer = new TaskPrioritizer(new EffortPriorityPolicy());
        assertSame(taskB, effortPrioritizer.chooseHigherPriority(taskA, taskB));

        TaskPrioritizer statusPrioritizer = new TaskPrioritizer(new StatusPriorityPolicy());
        assertSame(taskA, statusPrioritizer.chooseHigherPriority(taskA, taskB));
    }

    @Test
    @DisplayName("TaskPrioritizer валидирует аргументы")
    void taskPrioritizerValidatesInput() {
        assertThrows(IllegalArgumentException.class, () -> new TaskPrioritizer(null));

        TaskPrioritizer prioritizer = new TaskPrioritizer(new EffortPriorityPolicy());
        assertThrows(IllegalArgumentException.class, () -> prioritizer.chooseHigherPriority(null, shortTask));
        assertThrows(IllegalArgumentException.class, () -> prioritizer.chooseHigherPriority(shortTask, null));
    }
}
