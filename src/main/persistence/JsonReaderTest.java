package persistence;

import model.Bank;
import model.BankUser;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class JsonReaderTest extends JsonTest {

    @Test
    public void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Bank bank = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyBank.json");
        try {
            Bank bank = reader.read();
            assertEquals("BMO", bank.getName());
            assertEquals(0, bank.getBankUsers().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    public void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralBank.json");
        try {
            Bank bank = reader.read();
            assertEquals("TD", bank.getName());
            List<BankUser> bankUsers = bank.getBankUsers();
            assertEquals(2, bankUsers.size());
            checkBankUser(55, "John",bankUsers.get(0));
            checkBankUser(44, "Jim", bankUsers.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }


}
