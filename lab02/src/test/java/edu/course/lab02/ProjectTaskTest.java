package edu.course.lab02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProjectTaskTest {

    @Test
    @DisplayName("Создание корректной задачи и проверка полей")
    void createsValidProjectTask() {
        ProjectTask task = new ProjectTask("TASK-1", "Реализовать авторизацию", TaskStatus.NEW, 8);

        assertEquals("TASK-1", task.getId());
        assertEquals("Реализовать авторизацию", task.getTitle());
        assertEquals(TaskStatus.NEW, task.getStatus());
        assertEquals(8, task.getEstimatedHours());
        assertFalse(task.isDone());
    }

    @Test
    @DisplayName("Запрет создания задачи с null или пустым id")
    void throwsOnInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> new ProjectTask(null, "Title", TaskStatus.NEW, 5));
        assertThrows(IllegalArgumentException.class, () -> new ProjectTask("", "Title", TaskStatus.NEW, 5));
        assertThrows(IllegalArgumentException.class, () -> new ProjectTask("   ", "Title", TaskStatus.NEW, 5));
    }

    @Test
    @DisplayName("Запрет создания задачи с null или пустым title")
    void throwsOnInvalidTitle() {
        assertThrows(IllegalArgumentException.class, () -> new ProjectTask("TASK-1", null, TaskStatus.NEW, 5));
        assertThrows(IllegalArgumentException.class, () -> new ProjectTask("TASK-1", "", TaskStatus.NEW, 5));
        assertThrows(IllegalArgumentException.class, () -> new ProjectTask("TASK-1", "   \t", TaskStatus.NEW, 5));
    }

    @Test
    @DisplayName("Запрет создания задачи с null статусом")
    void throwsOnNullStatus() {
        assertThrows(IllegalArgumentException.class, () -> new ProjectTask("TASK-1", "Title", null, 5));
    }

    @Test
    @DisplayName("Запрет создания задачи с неположительной трудоемкостью")
    void throwsOnInvalidEstimatedHours() {
        assertThrows(IllegalArgumentException.class, () -> new ProjectTask("TASK-1", "Title", TaskStatus.NEW, 0));
        assertThrows(IllegalArgumentException.class, () -> new ProjectTask("TASK-1", "Title", TaskStatus.NEW, -3));
    }

    @Test
    @DisplayName("Изменение статуса через changeStatus")
    void changesStatusSuccessfully() {
        ProjectTask task = new ProjectTask("TASK-1", "Title", TaskStatus.NEW, 4);
        task.changeStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertFalse(task.isDone());

        task.changeStatus(TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, task.getStatus());
        assertTrue(task.isDone());
    }

    @Test
    @DisplayName("Запрет изменения статуса на null")
    void throwsOnChangeStatusToNull() {
        ProjectTask task = new ProjectTask("TASK-1", "Title", TaskStatus.NEW, 4);
        assertThrows(IllegalArgumentException.class, () -> task.changeStatus(null));
    }

    @Test
    @DisplayName("Увеличение оценки трудоемкости")
    void increasesEstimatedHours() {
        ProjectTask task = new ProjectTask("TASK-1", "Title", TaskStatus.NEW, 4);
        task.increaseEstimatedHours(3);
        assertEquals(7, task.getEstimatedHours());
    }

    @Test
    @DisplayName("Запрет увеличения оценки трудоемкости на неположительное значение")
    void throwsOnInvalidHourIncrease() {
        ProjectTask task = new ProjectTask("TASK-1", "Title", TaskStatus.NEW, 4);
        assertThrows(IllegalArgumentException.class, () -> task.increaseEstimatedHours(0));
        assertThrows(IllegalArgumentException.class, () -> task.increaseEstimatedHours(-2));
    }
}
