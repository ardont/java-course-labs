package edu.course.lab01;

/**
 * Небольшие методы для первой лабораторной работы.
 */
public final class CourseToolkit {

    private CourseToolkit() {
        // Утилитарный класс не должен иметь экземпляров.
    }

    /**
     * Возвращает true, если число четное.
     */
    public static boolean isEven(int number) {
        return number % 2 == 0;
    }

    /**
     * Возвращает true, если число простое.
     * Для чисел меньше 2 возвращает false.
     * Корректно обрабатывает 2, составные числа и квадраты простых чисел.
     */
    public static boolean isPrime(int number) {
        if (number < 2) {
            return false;
        }
        for (long divisor = 2; divisor * divisor <= number; divisor++) {
            if (number % divisor == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Возвращает true, если переданная строка является палиндромом.
     * Сравнение строгое: регистр и пробелы значимы.
     * Для null выбрасывает IllegalArgumentException.
     */
    public static boolean isPalindrome(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }
        int left = 0;
        int right = text.length() - 1;
        while (left < right) {
            if (text.charAt(left) != text.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    /**
     * Вычисляет среднее арифметическое элементов массива.
     * Не изменяет входной массив.
     * Для null или пустого массива выбрасывает IllegalArgumentException.
     */
    public static double average(int[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Values array must not be null or empty");
        }
        long sum = 0;
        for (int value : values) {
            sum += value;
        }
        return (double) sum / values.length;
    }
}
