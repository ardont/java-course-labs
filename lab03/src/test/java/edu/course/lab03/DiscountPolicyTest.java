package edu.course.lab03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DiscountPolicyTest {

    @Test
    @DisplayName("NoDiscount возвращает исходную цену")
    void noDiscountReturnsOriginalPrice() {
        DiscountPolicy policy = new NoDiscount();
        assertEquals(100.0, policy.apply(100.0), 1e-9);
        assertEquals(0.0, policy.apply(0.0), 1e-9);
    }

    @Test
    @DisplayName("NoDiscount выбрасывает исключение для отрицательной цены")
    void noDiscountThrowsOnNegativePrice() {
        DiscountPolicy policy = new NoDiscount();
        assertThrows(IllegalArgumentException.class, () -> policy.apply(-1.0));
    }

    @Test
    @DisplayName("PercentDiscount корректно рассчитывает скидку")
    void percentDiscountAppliesCorrectly() {
        DiscountPolicy policy = new PercentDiscount(20.0);
        // Скидка 20% на 100 должна дать 80
        assertEquals(80.0, policy.apply(100.0), 1e-9);
        assertEquals(0.0, policy.apply(0.0), 1e-9);
    }

    @Test
    @DisplayName("PercentDiscount валидирует процент скидки")
    void percentDiscountValidatesPercent() {
        assertThrows(IllegalArgumentException.class, () -> new PercentDiscount(-5.0));
        assertThrows(IllegalArgumentException.class, () -> new PercentDiscount(105.0));
    }

    @Test
    @DisplayName("PriceCalculator демонстрирует полиморфизм и заменяемость политик")
    void priceCalculatorDemonstratesPolymorphism() {
        PriceCalculator calcNoDiscount = new PriceCalculator(new NoDiscount());
        assertEquals(200.0, calcNoDiscount.calculate(200.0), 1e-9);

        PriceCalculator calcPercentDiscount = new PriceCalculator(new PercentDiscount(15.0));
        assertEquals(170.0, calcPercentDiscount.calculate(200.0), 1e-9);
    }

    @Test
    @DisplayName("PriceCalculator не принимает null политику")
    void priceCalculatorThrowsOnNullPolicy() {
        assertThrows(IllegalArgumentException.class, () -> new PriceCalculator(null));
    }
}
