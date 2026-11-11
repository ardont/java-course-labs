package edu.course.lab06;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Обобщенный стек элементов типа T на основе ArrayList.
 * null элементы запрещены.
 */
public final class Stack<T> {

    private final ArrayList<T> storage = new ArrayList<>();

    public void push(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Null values are not allowed in Stack");
        }
        storage.add(value);
    }

    public T pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty");
        }
        return storage.remove(storage.size() - 1);
    }

    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty");
        }
        return storage.get(storage.size() - 1);
    }

    public int size() {
        return storage.size();
    }

    public boolean isEmpty() {
        return storage.isEmpty();
    }
}
