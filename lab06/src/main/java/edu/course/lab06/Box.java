package edu.course.lab06;

/**
 * Обобщенный контейнер Box<T> (подготовительная часть).
 */
public class Box<T> {

    private T value;

    public Box(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
