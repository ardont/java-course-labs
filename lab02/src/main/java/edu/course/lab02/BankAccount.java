package edu.course.lab02;

/**
 * Учебный банковский счет с инкапсуляцией и проверкой инвариантов.
 */
public class BankAccount {

    private int balance;

    /**
     * Создает счет с указанным неотрицательным начальным балансом.
     */
    public BankAccount(int initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative: " + initialBalance);
        }
        this.balance = initialBalance;
    }

    /**
     * Пополняет счет на положительную сумму.
     */
    public void deposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive: " + amount);
        }
        this.balance += amount;
    }

    /**
     * Списывает средства со счета при наличии достаточного остатка.
     */
    public void withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be positive: " + amount);
        }
        if (amount > this.balance) {
            throw new IllegalArgumentException("Insufficient funds: current balance " + this.balance + ", requested " + amount);
        }
        this.balance -= amount;
    }

    /**
     * Возвращает текущий баланс.
     */
    public int getBalance() {
        return this.balance;
    }
}
