package persistence;

import model.BankUser;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class JsonTest {
    protected void checkBankUser(int id, String name, BankUser user) {
        assertEquals(id, user.getID());
        assertEquals(name, user.getName());
    }
}
