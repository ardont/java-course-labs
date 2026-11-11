package edu.course.lab05;

/**
 * Исключение при ошибках загрузки источника данных (недоступный или некорректный файл).
 */
public final class DataLoadingException extends RuntimeException {

    public DataLoadingException(String message) {
        super(message);
    }

    public DataLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
