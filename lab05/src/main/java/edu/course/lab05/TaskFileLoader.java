package edu.course.lab05;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Загрузчик проектных задач из текстового файла формата:
 * id;title;category;estimatedHours;status
 */
public class TaskFileLoader {

    private static final String EXPECTED_HEADER = "id;title;category;estimatedHours;status";

    public LoadResult load(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("Path must not be null");
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new DataLoadingException("Cannot read file: " + path, e);
        }

        if (lines.isEmpty()) {
            throw new DataLoadingException("File is empty: " + path);
        }

        String header = lines.get(0).trim();
        if (!EXPECTED_HEADER.equalsIgnoreCase(header)) {
            throw new DataLoadingException("Invalid file header: expected '" + EXPECTED_HEADER + "' but got '" + header + "'");
        }

        List<ProjectTask> tasks = new ArrayList<>();
        List<ValidationError> errors = new ArrayList<>();

        for (int i = 1; i < lines.size(); i++) {
            int lineNumber = i + 1;
            String rawLine = lines.get(i);
            if (rawLine.isBlank()) {
                continue;
            }

            // Split with limit -1 to preserve trailing empty tokens
            String[] tokens = rawLine.split(";", -1);
            if (tokens.length != 5) {
                errors.add(new ValidationError(lineNumber, "line", "Expected 5 fields but got " + tokens.length));
                continue;
            }

            String id = tokens[0].trim();
            String title = tokens[1].trim();
            String category = tokens[2].trim();
            String hoursStr = tokens[3].trim();
            String statusStr = tokens[4].trim();

            if (id.isEmpty()) {
                errors.add(new ValidationError(lineNumber, "id", "Id cannot be empty"));
                continue;
            }

            if (title.isEmpty()) {
                errors.add(new ValidationError(lineNumber, "title", "Title cannot be empty"));
                continue;
            }

            if (category.isEmpty()) {
                errors.add(new ValidationError(lineNumber, "category", "Category cannot be empty"));
                continue;
            }

            int hours;
            try {
                hours = Integer.parseInt(hoursStr);
                if (hours <= 0) {
                    errors.add(new ValidationError(lineNumber, "estimatedHours", "Estimated hours must be positive: " + hoursStr));
                    continue;
                }
            } catch (NumberFormatException e) {
                errors.add(new ValidationError(lineNumber, "estimatedHours", "Invalid number format: " + hoursStr));
                continue;
            }

            TaskStatus status;
            try {
                status = TaskStatus.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                errors.add(new ValidationError(lineNumber, "status", "Unknown task status: " + statusStr));
                continue;
            }

            try {
                ProjectTask task = new ProjectTask(id, title, category, hours, status);
                tasks.add(task);
            } catch (Exception e) {
                errors.add(new ValidationError(lineNumber, "domain", e.getMessage()));
            }
        }

        return new LoadResult(tasks, errors);
    }
}
