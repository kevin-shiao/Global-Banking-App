package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account account;
    private Account account2;

    @BeforeEach
    void runBefore(){
        account = new Account("Savings", 100);
        account2 = new Account("TFSA", 50);
    }

    @Test
    void testConstructor(){
        assertEquals("Savings", account.getAccountName());
        assertEquals(100, account.getBalance());
        assertEquals("CAD", account.getCurrency());
    }

    @Test
    void testDeposit() {
        account.deposit(50);
        assertEquals(150, account.getBalance());
    }

    @Test
    void testDepositNegative() {
        account.deposit(-2);
        assertEquals(100, account.getBalance());
    }

    @Test
    void testMultipleDeposit() {
        account.deposit(50);
        account.deposit(51);
        assertEquals(201, account.getBalance());
    }

    @Test
    void testWithdraw() {
        account.withdraw(25);
        assertEquals(75, account.getBalance());
    }

    @Test
    void testWithdrawTooMuch() {
        account.withdraw(101);
        assertEquals(100, account.getBalance());
    }

    @Test
    void testMultipleWithdrawals() {
        account.withdraw(10);
        account.withdraw(10);
        assertEquals(80, account.getBalance());
    }

    @Test
    void testCadtoUsd() {
        assertEquals(75, account.cadtousd());
    }

    @Test
    void testUsdtoCad() {
        account.cadtousd();
        assertEquals(100, account.usdtocad());
    }

    @Test
    void testCadtoJpy() {
        assertEquals(9815, account.cadtojpy());
    }

    @Test
    void testJpytoCad() {
        account.cadtojpy();
        assertEquals(100, account.jpytocad());
    }

    @Test
    void testCadtoGbp() {
        assertEquals(62, account.cadtogbp());
    }

    @Test
    void testGbptoCad() {
        account.cadtogbp();
        assertEquals(100, account.gbptocad());
    }

    @Test
    void testgetHistory() {
        account.deposit(50);
        account.withdraw(20);
        ArrayList<Transactions> history = account.getHistory();
        assertEquals(50, history.get(0).getTransactionAmount());
        assertEquals(20, history.get(1).getTransactionAmount());
        assertEquals("Deposit", history.get(0).getTransactionType());
        assertEquals("Withdrawal", history.get(1).getTransactionType());
    }

    @Test
    void testconvertToCADfromUSD() {
        account.cadtousd();
        assertEquals("USD", account.getCurrency());
        account.convertToCAD();
        assertEquals(100, account.getBalance());
        assertEquals("CAD", account.getCurrency());
    }

    @Test
    void testconvertToCADfromJPY() {
        account.cadtojpy();
        assertEquals("JPY", account.getCurrency());
        account.convertToCAD();
        assertEquals(100, account.getBalance());
        assertEquals("CAD", account.getCurrency());
    }

    @Test
    void testconvertToCADfromGBP() {
        account.cadtogbp();
        assertEquals("GBP", account.getCurrency());
        account.convertToCAD();
        assertEquals(100, account.getBalance());
        assertEquals("CAD", account.getCurrency());
    }

    @Test
    void testconvertToCADfromsmt() {
        assertEquals("CAD", account.getCurrency());
        account.convertToCAD();
        assertEquals(100, account.getBalance());
        assertEquals("CAD", account.getCurrency());
    }
}