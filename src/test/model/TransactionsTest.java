package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionsTest {
    private Transactions deposit;
    private Transactions withdrawal;

    @BeforeEach
    void runBefore(){
        deposit = new Transactions("Deposit", 50, "CAD");
        withdrawal = new Transactions("Withdrawal", 40, "CAD");
    }

    @Test
    void testConstructor(){
        assertEquals("Deposit", deposit.getTransactionType()) ;
        assertEquals("Withdrawal", withdrawal.getTransactionType());
        assertEquals(50, deposit.getTransactionAmount());
        assertEquals(40, withdrawal.getTransactionAmount());
        assertEquals("CAD", deposit.getTransactionCurrency());
        assertEquals("CAD", withdrawal.getTransactionCurrency());
    }

}
