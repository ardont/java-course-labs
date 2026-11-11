package edu.course.lab02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BankAccountTest {

    @Test
    @DisplayName("Успешное создание счета и получение баланса")
    void createsAccountWithValidInitialBalance() {
        BankAccount account = new BankAccount(100);
        assertEquals(100, account.getBalance());
    }

    @Test
    @DisplayName("Запрет создания счета с отрицательным балансом")
    void throwsOnNegativeInitialBalance() {
        assertThrows(IllegalArgumentException.class, () -> new BankAccount(-1));
    }

    @Test
    @DisplayName("Успешное пополнение счета")
    void depositsValidAmount() {
        BankAccount account = new BankAccount(50);
        account.deposit(30);
        assertEquals(80, account.getBalance());
    }

    @Test
    @DisplayName("Запрет пополнения неположительной суммой")
    void throwsOnInvalidDeposit() {
        BankAccount account = new BankAccount(50);
        assertThrows(IllegalArgumentException.class, () -> account.deposit(0));
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-10));
    }

    @Test
    @DisplayName("Успешное списание средств")
    void withdrawsValidAmount() {
        BankAccount account = new BankAccount(100);
        account.withdraw(40);
        assertEquals(60, account.getBalance());
    }

    @Test
    @DisplayName("Исключение при превышении остатка (овердрафт)")
    void throwsOnOverdraft() {
        BankAccount account = new BankAccount(100);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(101));
    }

    @Test
    @DisplayName("Запрет списания неположительной суммы")
    void throwsOnNonPositiveWithdrawal() {
        BankAccount account = new BankAccount(100);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(0));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-5));
    }
}
