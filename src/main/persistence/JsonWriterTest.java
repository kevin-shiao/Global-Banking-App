package persistence;

import model.Bank;
import model.BankUser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class JsonWriterTest extends JsonTest {
    @Test
    public void testWriterInvalidFile() {
        try {
            Bank bank = new Bank("Bank of London");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testWriterEmptyWorkroom() {
        try {
            Bank bank = new Bank("Bank of Japan");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyBank.json");
            writer.open();
            writer.write(bank);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyBank.json");
            bank = reader.read();
            assertEquals("Bank of Japan", bank.getName());
            assertEquals(0, bank.getBankUsers().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testWriterGeneralWorkroom() {
        try {
            Bank bank = new Bank("Bank of America");
            bank.addBankUser(new BankUser(12, "Mike"));
            bank.addBankUser(new BankUser(60, "Pat"));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralBank.json");
            writer.open();
            writer.write(bank);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralBank.json");
            bank = reader.read();
            assertEquals("Bank of America", bank.getName());
            List<BankUser> bankUsers = bank.getBankUsers();
            assertEquals(2, bankUsers.size());
            checkBankUser(12, "Mike", bankUsers.get(0));
            checkBankUser(60, "Pat", bankUsers.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
