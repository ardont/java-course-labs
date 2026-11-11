package edu.course.lab05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskFileLoaderTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Подготовительная часть: чтение UTF-8 файла и NumberFormatException")
    void preparatoryExerciseWithTempDir() throws IOException {
        Path file = tempDir.resolve("numbers.txt");
        Files.write(file, List.of("10", "bad", "20"), StandardCharsets.UTF_8);

        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        assertEquals(3, lines.size());
        assertEquals("10", lines.get(0));
        assertEquals("bad", lines.get(1));
        assertEquals("20", lines.get(2));

        List<Integer> parsedNumbers = new ArrayList<>();
        boolean caughtException = false;

        for (String line : lines) {
            try {
                parsedNumbers.add(Integer.parseInt(line));
            } catch (NumberFormatException e) {
                caughtException = true;
            }
        }

        assertTrue(caughtException, "Должно быть поймано NumberFormatException для строки 'bad'");
        assertEquals(2, parsedNumbers.size());
        assertEquals(10, parsedNumbers.get(0));
        assertEquals(20, parsedNumbers.get(1));
    }

    @Test
    @DisplayName("Чтение корректного файла с задачами")
    void loadsValidFileSuccessfully() throws IOException {
        Path file = tempDir.resolve("tasks.txt");
        List<String> lines = List.of(
                "id;title;category;estimatedHours;status",
                "T-101;Prepare release;backend;8;READY",
                "T-102;Fix login bug;frontend;4;IN_PROGRESS"
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        TaskFileLoader loader = new TaskFileLoader();
        LoadResult result = loader.load(file);

        assertTrue(result.getErrors().isEmpty());
        assertEquals(2, result.getTasks().size());

        ProjectTask task1 = result.getTasks().get(0);
        assertEquals("T-101", task1.getId());
        assertEquals("Prepare release", task1.getTitle());
        assertEquals("backend", task1.getCategory());
        assertEquals(8, task1.getEstimatedHours());
        assertEquals(TaskStatus.READY, task1.getStatus());
    }

    @Test
    @DisplayName("Поддержка кириллицы и Unicode в UTF-8 кодировке")
    void loadsUnicodeCyrillicFile() throws IOException {
        Path file = tempDir.resolve("unicode_tasks.txt");
        List<String> lines = List.of(
                "id;title;category;estimatedHours;status",
                "ПР-01;Интеграция с платежной системой;бэкенд;12;READY",
                "ПР-02;Верстка интерфейса;дизайн;6;NEW"
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        TaskFileLoader loader = new TaskFileLoader();
        LoadResult result = loader.load(file);

        assertTrue(result.getErrors().isEmpty());
        assertEquals(2, result.getTasks().size());
        assertEquals("Интеграция с платежной системой", result.getTasks().get(0).getTitle());
        assertEquals("бэкенд", result.getTasks().get(0).getCategory());
    }

    @Test
    @DisplayName("Ошибка валидации: пустое поле")
    void recordsErrorOnEmptyField() throws IOException {
        Path file = tempDir.resolve("empty_field.txt");
        List<String> lines = List.of(
                "id;title;category;estimatedHours;status",
                "T-1;;backend;8;READY"
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        TaskFileLoader loader = new TaskFileLoader();
        LoadResult result = loader.load(file);

        assertEquals(0, result.getTasks().size());
        assertEquals(1, result.getErrors().size());
        assertEquals(2, result.getErrors().get(0).getLineNumber());
        assertEquals("title", result.getErrors().get(0).getField());
    }

    @Test
    @DisplayName("Ошибка валидации: некорректное число часов")
    void recordsErrorOnInvalidNumber() throws IOException {
        Path file = tempDir.resolve("invalid_number.txt");
        List<String> lines = List.of(
                "id;title;category;estimatedHours;status",
                "T-1;Build API;backend;not_a_number;READY",
                "T-2;Write Docs;docs;-5;READY"
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        TaskFileLoader loader = new TaskFileLoader();
        LoadResult result = loader.load(file);

        assertEquals(0, result.getTasks().size());
        assertEquals(2, result.getErrors().size());
        assertEquals("estimatedHours", result.getErrors().get(0).getField());
        assertEquals("estimatedHours", result.getErrors().get(1).getField());
    }

    @Test
    @DisplayName("Ошибка валидации: неизвестный статус задачи")
    void recordsErrorOnUnknownStatus() throws IOException {
        Path file = tempDir.resolve("unknown_status.txt");
        List<String> lines = List.of(
                "id;title;category;estimatedHours;status",
                "T-1;Build API;backend;8;UNKNOWN_STATUS"
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        TaskFileLoader loader = new TaskFileLoader();
        LoadResult result = loader.load(file);

        assertEquals(0, result.getTasks().size());
        assertEquals(1, result.getErrors().size());
        assertEquals("status", result.getErrors().get(0).getField());
    }

    @Test
    @DisplayName("Ошибка валидации: неправильное число столбцов")
    void recordsErrorOnWrongColumnCount() throws IOException {
        Path file = tempDir.resolve("wrong_columns.txt");
        List<String> lines = List.of(
                "id;title;category;estimatedHours;status",
                "T-1;Build API;backend;8" // всего 4 столбца
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        TaskFileLoader loader = new TaskFileLoader();
        LoadResult result = loader.load(file);

        assertEquals(0, result.getTasks().size());
        assertEquals(1, result.getErrors().size());
        assertEquals("line", result.getErrors().get(0).getField());
    }

    @Test
    @DisplayName("Совместная обработка корректных и ошибочных строк без прерывания")
    void loadsMixedFileSuccessfully() throws IOException {
        Path file = tempDir.resolve("mixed.txt");
        List<String> lines = List.of(
                "id;title;category;estimatedHours;status",
                "T-1;First valid task;backend;5;READY",
                "T-2;;backend;8;READY", // ошибка: пустое название
                "T-3;Third valid task;frontend;3;DONE",
                "T-4;Invalid status;qa;2;NOT_REAL" // ошибка: статус
        );
        Files.write(file, lines, StandardCharsets.UTF_8);

        TaskFileLoader loader = new TaskFileLoader();
        LoadResult result = loader.load(file);

        assertEquals(2, result.getTasks().size());
        assertEquals(2, result.getErrors().size());

        assertEquals("T-1", result.getTasks().get(0).getId());
        assertEquals("T-3", result.getTasks().get(1).getId());

        assertEquals(3, result.getErrors().get(0).getLineNumber());
        assertEquals("title", result.getErrors().get(0).getField());

        assertEquals(5, result.getErrors().get(1).getLineNumber());
        assertEquals("status", result.getErrors().get(1).getField());
    }

    @Test
    @DisplayName("Отсутствующий файл выбрасывает DataLoadingException с причиной")
    void throwsDataLoadingExceptionOnMissingFile() {
        TaskFileLoader loader = new TaskFileLoader();
        Path nonExistent = tempDir.resolve("does_not_exist.txt");

        DataLoadingException ex = assertThrows(DataLoadingException.class, () -> loader.load(nonExistent));
        assertNotNull(ex.getCause(), "Исключение должно сохранять исходную причину (IOException)");
    }
}
