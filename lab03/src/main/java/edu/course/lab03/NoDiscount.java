package edu.course.lab03;

/**
 * Политика без скидки: возвращает исходную цену.
 */
public class NoDiscount implements DiscountPolicy {

    @Override
    public double apply(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative: " + price);
        }
        return price;
    }
}
