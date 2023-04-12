package persistence;

import model.Account;
import model.Bank;
import model.BankUser;
import model.Transactions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import static java.lang.Double.valueOf;

public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads bank from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Bank read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseBank(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses Bank from JSON object and returns it
    private Bank parseBank(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Bank bank = new Bank(name);
        addBankUsers(bank, jsonObject);
        return bank;
    }

    // MODIFIES: bank
    // EFFECTS: parses bankUsers from JSON object and adds them to Bank
    private void addBankUsers(Bank bank, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("bankUsers");
        for (Object json: jsonArray) {
            JSONObject nextBankUser = (JSONObject) json;
            addUser(bank, nextBankUser);
        }
    }

   // MODIFIES: bank
    // EFFECTS: parses BankUsers from JSON object and adds them to bank
    private void addUser(Bank bank, JSONObject jsonObject) {
        int id = jsonObject.getInt("id");
        String name = jsonObject.getString("name");
        JSONArray jsonAccountArray = jsonObject.getJSONArray("Accounts");
        BankUser user = new BankUser(id, name);
        for (Object json : jsonAccountArray) {
            JSONObject nextAccount = (JSONObject) json;
            addAccount(user, nextAccount);
        }

        bank.addBankUser(user);

    }

   // MODIFIES: user
    // EFFECTS: parses accounts from JSON object and adds them to user
    private void addAccount(BankUser user, JSONObject jsonObject) {
        String accountName = jsonObject.getString("Account Name");
        double balance = valueOf(jsonObject.getDouble("Account Balance"));
        String currency = jsonObject.getString("Account Currency");
        JSONArray jsonTransactionArray = jsonObject.getJSONArray("Transactions");
        Account account = new Account(accountName, balance);
        for (Object json: jsonTransactionArray) {
            JSONObject nextTransaction = (JSONObject) json;
            addTransactions(account, nextTransaction);
        }
        user.addAccountOnly(account);
    }

    // MODIFIES: account
    // EFFECTS: parses Transactions from JSON object and adds them to account
    private void addTransactions(Account account, JSONObject jsonObject) {
        String type = jsonObject.getString("Transaction Type");
        double amount = valueOf(jsonObject.getDouble("Transaction Amount"));
        String transactionCurrency = jsonObject.getString("Transaction Currency");
        Transactions transaction = new Transactions(type, amount, transactionCurrency);
        account.addTransaction(transaction);
    }
}




