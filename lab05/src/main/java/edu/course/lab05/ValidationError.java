package edu.course.lab05;

/**
 * Ошибка валидации строки файла.
 */
public final class ValidationError {

    private final int lineNumber;
    private final String field;
    private final String message;

    public ValidationError(int lineNumber, String field, String message) {
        this.lineNumber = lineNumber;
        this.field = field;
        this.message = message;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationError that = (ValidationError) o;
        return lineNumber == that.lineNumber &&
                java.util.Objects.equals(field, that.field) &&
                java.util.Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(lineNumber, field, message);
    }

    @Override
    public String toString() {
        return "ValidationError{" +
                "lineNumber=" + lineNumber +
                ", field='" + field + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
