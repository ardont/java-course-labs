package edu.course.lab03;

/**
 * Политика расчета скидки (подготовительная часть).
 */
public interface DiscountPolicy {
    double apply(double price);
}
