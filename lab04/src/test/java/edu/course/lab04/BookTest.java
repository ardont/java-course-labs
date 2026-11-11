package edu.course.lab04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookTest {

    @Test
    @DisplayName("Значение в HashMap находится по логически равному ключу с тем же ISBN")
    void findsValueInMapByLogicallyEqualBook() {
        Map<Book, String> catalog = new HashMap<>();

        Book book1 = new Book("978-0134685991", "Effective Java (3rd Edition)");
        catalog.put(book1, "Available");

        Book lookupKey = new Book("978-0134685991", "Different Title (Same ISBN)");

        assertEquals(book1, lookupKey);
        assertEquals(book1.hashCode(), lookupKey.hashCode());
        assertEquals("Available", catalog.get(lookupKey));
    }

    @Test
    @DisplayName("Книги с разными ISBN не равны")
    void booksWithDifferentIsbnAreNotEqual() {
        Book bookA = new Book("ISBN-1", "Java Concurrency in Practice");
        Book bookB = new Book("ISBN-2", "Java Concurrency in Practice");

        assertNotEquals(bookA, bookB);
        assertNotEquals(bookA.hashCode(), bookB.hashCode());
    }

    @Test
    @DisplayName("Валидация полей Book")
    void validatesBookFields() {
        assertThrows(IllegalArgumentException.class, () -> new Book(null, "Title"));
        assertThrows(IllegalArgumentException.class, () -> new Book("", "Title"));
        assertThrows(IllegalArgumentException.class, () -> new Book("ISBN", null));
        assertThrows(IllegalArgumentException.class, () -> new Book("ISBN", "   "));
    }
}
