package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


// represents a user/customer of the bank
public class BankUserTest {

    private BankUser User1;

    @BeforeEach
    void runBefore(){
        User1 = new BankUser(1, "Jim");
    }

    @Test
    void testConstructor(){
        assertEquals(1, User1.getID());
        assertEquals("Jim", User1.getName());
        assertEquals(0, User1.getNumberOfAccounts());
    }

    @Test
    void testaddAccount(){
        User1.addAccount("Savings", 50 );
        assertEquals(1, User1.getNumberOfAccounts());
    }

    @Test
    void testaddMultipleAccounts(){
        User1.addAccount("Savings", 50);
        User1.addAccount("TFSA", 50);
        assertEquals(2, User1.getNumberOfAccounts());
    }

    @Test
    void testremoveAccount(){
        User1.addAccount("Savings", 50);
        User1.removeAccount("Savings");
        assertEquals(0, User1.getNumberOfAccounts());
    }

    @Test
    void testremovenonAccount(){
        User1.addAccount("Savings", 50);
        User1.removeAccount("TFSA");
        assertEquals(1, User1.getNumberOfAccounts());
    }

    @Test
    void testremoveMultipleAccounts(){
        User1.addAccount("Savings", 50);
        User1.addAccount("TFSA", 50);
        User1.addAccount("Bills", 50);
        User1.removeAccount("Savings");
        User1.removeAccount("Bills");
        assertEquals(1, User1.getNumberOfAccounts());
    }

    @Test
    void testremoveMiddleAccount(){
        User1.addAccount("Savings", 50);
        User1.addAccount("TFSA", 50);
        User1.addAccount("Bills", 50);
        User1.removeAccount("TFSA");
        assertEquals(2, User1.getNumberOfAccounts());
    }

    @Test
    void testtransferBalance(){
        User1.addAccount("Savings", 100);
        User1.addAccount("TFSA", 50);
        assertEquals(75, User1.transferBalance(1, 2, 25));
    }

    @Test
    void testtransferBalancenotEnough(){
        User1.addAccount("Savings", 100);
        User1.addAccount("TFSA", 50);
        assertEquals(50, User1.transferBalance(1, 2, 101));
    }

    @Test
    void testTransferBalanceInvalidNumber(){
        User1.addAccount("Savings", 100);
        User1.addAccount("TFSA", 50);
        assertEquals(0, User1.transferBalance(1, 3, 25));
        assertEquals(0, User1.transferBalance(3, 2, 2));
        assertEquals(0, User1.transferBalance(-1, 2, 30));
        assertEquals(0, User1.transferBalance(3, -2, 22));
    }

    @Test
    void testMultipleTransferBalance() {
        User1.addAccount("Savings", 100);
        User1.addAccount("TFSA", 50);
        User1.transferBalance(1, 2, 10);
        assertEquals(70, User1.transferBalance(1, 2, 10));
    }

    @Test
    void testgetBankaccounts(){
        User1.addAccount("Savings", 100);
        User1.addAccount("TFSA", 50);
        assertEquals("Savings", User1.getBankaccounts().get(0).getAccountName());
        assertEquals("TFSA", User1.getBankaccounts().get(1).getAccountName());
    }

}
