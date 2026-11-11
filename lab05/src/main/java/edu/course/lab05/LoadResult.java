package edu.course.lab05;

import java.util.Collections;
import java.util.List;

/**
 * Результат загрузки файла: список успешно созданных задач и список ошибок валидации.
 */
public final class LoadResult {

    private final List<ProjectTask> tasks;
    private final List<ValidationError> errors;

    public LoadResult(List<ProjectTask> tasks, List<ValidationError> errors) {
        this.tasks = Collections.unmodifiableList(tasks);
        this.errors = Collections.unmodifiableList(errors);
    }

    public List<ProjectTask> getTasks() {
        return tasks;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}
