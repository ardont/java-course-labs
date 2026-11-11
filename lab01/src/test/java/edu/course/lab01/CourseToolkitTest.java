package edu.course.lab01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CourseToolkitTest {

    // --- isEven tests ---

    @Test
    void returnsTrueForEvenNumber() {
        boolean result = CourseToolkit.isEven(8);
        assertTrue(result);
    }

    @Test
    void returnsFalseForOddNumber() {
        boolean result = CourseToolkit.isEven(7);
        assertFalse(result);
    }

    @Test
    @DisplayName("Подготовительный тест: проверка нуля")
    void returnsTrueForZero() {
        assertTrue(CourseToolkit.isEven(0));
    }

    @Test
    @DisplayName("Подготовительный тест: проверка отрицательного четного числа -8")
    void returnsTrueForNegativeEvenNumber() {
        assertTrue(CourseToolkit.isEven(-8));
    }

    @Test
    void returnsFalseForNegativeOddNumber() {
        assertFalse(CourseToolkit.isEven(-7));
    }

    // --- isPrime tests ---

    @Test
    void isPrimeReturnsFalseForNumbersLessThanTwo() {
        assertFalse(CourseToolkit.isPrime(-10));
        assertFalse(CourseToolkit.isPrime(-1));
        assertFalse(CourseToolkit.isPrime(0));
        assertFalse(CourseToolkit.isPrime(1));
    }

    @Test
    void isPrimeReturnsTrueForTwoAndThree() {
        assertTrue(CourseToolkit.isPrime(2));
        assertTrue(CourseToolkit.isPrime(3));
    }

    @Test
    void isPrimeReturnsFalseForCompositeNumbers() {
        assertFalse(CourseToolkit.isPrime(4));
        assertFalse(CourseToolkit.isPrime(6));
        assertFalse(CourseToolkit.isPrime(8));
        assertFalse(CourseToolkit.isPrime(15));
    }

    @Test
    void isPrimeReturnsFalseForSquareOfPrimes() {
        assertFalse(CourseToolkit.isPrime(4));   // 2^2
        assertFalse(CourseToolkit.isPrime(9));   // 3^2
        assertFalse(CourseToolkit.isPrime(25));  // 5^2
        assertFalse(CourseToolkit.isPrime(49));  // 7^2
        assertFalse(CourseToolkit.isPrime(121)); // 11^2
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 7, 11, 13, 17, 19, 23, 29, 31, 7919})
    @DisplayName("Дополнительная часть: параметризованный тест простых чисел")
    void isPrimeReturnsTrueForKnownPrimes(int prime) {
        assertTrue(CourseToolkit.isPrime(prime));
    }

    // --- isPalindrome tests ---

    @Test
    void isPalindromeThrowsExceptionForNull() {
        assertThrows(IllegalArgumentException.class, () -> CourseToolkit.isPalindrome(null));
    }

    @Test
    void isPalindromeReturnsTrueForEmptyAndSingleCharString() {
        assertTrue(CourseToolkit.isPalindrome(""));
        assertTrue(CourseToolkit.isPalindrome("a"));
    }

    @Test
    void isPalindromeReturnsTrueForExactPalindromes() {
        assertTrue(CourseToolkit.isPalindrome("radar"));
        assertTrue(CourseToolkit.isPalindrome("abba"));
        assertTrue(CourseToolkit.isPalindrome("racecar"));
        assertTrue(CourseToolkit.isPalindrome("a b a"));
    }

    @Test
    void isPalindromeReturnsFalseWhenCaseDiffers() {
        assertFalse(CourseToolkit.isPalindrome("Radar"));
        assertFalse(CourseToolkit.isPalindrome("Abba"));
    }

    @Test
    void isPalindromeReturnsFalseWhenSpacesDiffer() {
        assertFalse(CourseToolkit.isPalindrome("a ba"));
        assertFalse(CourseToolkit.isPalindrome("ab a"));
    }

    @Test
    void isPalindromeReturnsFalseForNonPalindrome() {
        assertFalse(CourseToolkit.isPalindrome("hello"));
        assertFalse(CourseToolkit.isPalindrome("java"));
    }

    // --- average tests ---

    @Test
    void averageThrowsExceptionForNullOrEmptyArray() {
        assertThrows(IllegalArgumentException.class, () -> CourseToolkit.average(null));
        assertThrows(IllegalArgumentException.class, () -> CourseToolkit.average(new int[0]));
    }

    @Test
    void averageComputesAccuratelyForSingleElement() {
        assertEquals(42.0, CourseToolkit.average(new int[]{42}), 1e-9);
    }

    @Test
    void averageComputesAccuratelyForMultipleElements() {
        assertEquals(2.0, CourseToolkit.average(new int[]{1, 2, 3}), 1e-9);
        assertEquals(1.5, CourseToolkit.average(new int[]{1, 2}), 1e-9);
        assertEquals(0.0, CourseToolkit.average(new int[]{-5, 5}), 1e-9);
        assertEquals(-2.5, CourseToolkit.average(new int[]{-2, -3}), 1e-9);
    }

    @Test
    void averageDoesNotMutateInputArray() {
        int[] original = new int[]{5, 2, 9, 1};
        int[] copy = original.clone();

        CourseToolkit.average(original);

        for (int i = 0; i < original.length; i++) {
            assertEquals(copy[i], original[i], "Element at index " + i + " was modified");
        }
    }
}
