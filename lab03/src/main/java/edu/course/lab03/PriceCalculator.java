package edu.course.lab03;

/**
 * Калькулятор цены, использующий внедряемую политику скидок.
 */
public class PriceCalculator {

    private final DiscountPolicy policy;

    public PriceCalculator(DiscountPolicy policy) {
        if (policy == null) {
            throw new IllegalArgumentException("Discount policy must not be null");
        }
        this.policy = policy;
    }

    public double calculate(double price) {
        return policy.apply(price);
    }
}
