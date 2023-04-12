package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents a Bank User with an id, name, numberofAccounts, and a list of Accounts bankaccounts
public class BankUser implements Writable {
    private int id;
    private String name;
    private int numberOfAccounts;
    private ArrayList<Account> bankaccounts;

    // constructs a Bank User with given ID and Name
    public BankUser(int id, String name) {
        this.id = id;
        this.name = name;
        this.numberOfAccounts = 0;
        bankaccounts = new ArrayList<Account>();
    }

    // MODIFIES: this
    // EFFECTS: creates a new Account and adds it to the list bankaccounts and increment numberOfAccounts by 1
    public ArrayList<Account> addAccount(String accountName, double initialBalance) {
        this.numberOfAccounts++;
        bankaccounts.add(new Account(accountName, initialBalance));
        return bankaccounts;
    }

    // REQUIRES: bankaccounts.size() >= 1
    // MODIFIES: this
    // EFFECTS: removes an account from bankaccounts and subtracts 1 from numberOfAccounts
    public ArrayList<Account> removeAccount(String accountName) {
        for (int i = 0; i < bankaccounts.size(); i++) {
            Account account = this.bankaccounts.get(i);
            if (account.getAccountName() == accountName) {
                this.numberOfAccounts--;
                bankaccounts.remove(account);
                EventLog.getInstance().logEvent(new Event("Account Named " + accountName
                        + " has been removed from " + this.getName()));
                return bankaccounts;
            }
        }
        return bankaccounts;
    }

    // REQUIRES: Two Accounts.
    // MODIFIES: this
    // EFFECTS: withdraws an amount from one account and deposits it into another account
    public double transferBalance(int i1, int i2, double amount) {
        if (i1 >= 1 && i2 >= 1 && i1 <= bankaccounts.size() && i2 <= bankaccounts.size()) {
            Account account1 = this.bankaccounts.get(i1 - 1);
            Account account2 = this.bankaccounts.get(i2 - 1);
            if (account1.getBalance() >= amount) {
                account1.withdraw(amount);
                account2.deposit(amount);
                return account2.getBalance();
            }
            account1.withdraw(0);
            account2.deposit(0);
            return account2.getBalance();
        }
        return 0;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public int getNumberOfAccounts() {
        return this.numberOfAccounts;
    }

    public ArrayList<Account> getBankaccounts() {
        return this.bankaccounts;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", name);
        // json.put("Number of Accounts", numberOfAccounts);
        json.put("Accounts", bankaccountsToJson());
        return json;
    }

    // EFFECTS: returns Accounts in this bank as a JSON array
    private JSONArray bankaccountsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Account a : bankaccounts) {
            jsonArray.put(a.toJson());
        }

        return jsonArray;
    }

    // MODIFIES: this
    // EFFECTS: adds an account to bankaccounts
    public void addAccountOnly(Account account) {
        this.bankaccounts.add(account);
        EventLog.getInstance().logEvent(new Event("Account Named " + account.getAccountName()
                + " with initial balance of " + account.getBalance() + " " + account.getCurrency()
                    + " has been added to User " + this.getName()));
    }

}
