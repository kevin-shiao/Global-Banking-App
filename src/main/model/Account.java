package model;


import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents an account having a Name, balance,
//                  currency (represents currency of balance), and a history of transactions
public class Account implements Writable {
    private String accountName; // Name of Account
    private double balance;    // The balance of the account
    private String currency;   // The currency that the balance is in
    private ArrayList<Transactions> history; // History of Transactions made in this Account

    /*
    * EFFECTS: creates an account with the given name that starts with a balance given by initial Balance
     */
    public Account(String name, double initialBalance) {
        this.accountName = name;
        this.balance = initialBalance;
        this.currency = "CAD";
        this.history = new ArrayList<Transactions>();
    }

    // REQUIRES: amount >= 0
    //MODIFIES: this
    // EFFECTS: adds the given amount to the account balance
    public double deposit(double amount) {
        if (amount >= 0) {
            this.balance = balance + amount;
            this.history.add(new Transactions("Deposit", amount, this.currency));
            EventLog.getInstance().logEvent(new Event(amount
                    + " has been added to " + this.getAccountName() + " account"));
            return this.balance;
        }
        return this.balance;
    }

    // REQUIRES: account.getBalance >= amount >=0
    // MODIFIES: this
    // EFFECTS: subtracts the account balance by the given amount
    public double withdraw(double amount) {
        if (amount <= this.getBalance()) {
            this.balance = balance - amount;
            this.history.add(new Transactions("Withdrawal", amount, this.currency));
            EventLog.getInstance().logEvent(new Event(amount
                    + " has been withdrawn from " + this.getAccountName() + " account"));
            return this.balance;
        }
        return this.balance;
    }

    //REQUIRES: account.getCurrency() = CAD, USD, JPY, or GBP
    // MODIFIES: this
    // EFFECTS: converts the account balance to CAD
    public Account convertToCAD() {
        if (this.currency.equals("USD")) {
            this.balance = balance / 0.75;
            this.currency = "CAD";
        } else if (this.currency.equals("JPY")) {
            this.balance = balance / 98.15;
            this.currency = "CAD";
        } else if (this.currency.equals("GBP")) {
            this.balance = balance / 0.62;
            this.currency = "CAD";
        }
        EventLog.getInstance().logEvent(new Event("Account " + this.getAccountName()
                + " has been converted to CAD"));
        return this;
    }

    // MODIFIES: this
    // EFFECTS: converts the balance from CAD to USD and changes the currency to USD
    public double cadtousd() {
        this.balance = balance * 0.75;
        this.currency = "USD";
        EventLog.getInstance().logEvent(new Event("Account " + this.getAccountName()
                + " has been converted to USD"));
        return this.balance;
    }

    // MODIFIES: this
    // EFFECTS: converts the balance from USD to CAD and changes the currency to CAD
    public double usdtocad() {
        this.balance = balance / 0.75;
        this.currency = "CAD";
        return this.balance;
    }

    // MODIFIES: this
    // EFFECTS: converts the balance from CAD to JPY and changes the currency to JPY
    public double cadtojpy() {
        this.balance = balance * 98.15;
        this.currency = "JPY";
        EventLog.getInstance().logEvent(new Event("Account " + this.getAccountName()
                + " has been converted to JPY"));
        return this.balance;
    }

    // MODIFIES: this
    // EFFECTS: converts the balance from JPY to CAD and changes the currency to CAD
    public double jpytocad() {
        this.balance = balance / 98.15;
        this.currency = "CAD";
        return this.balance;
    }

    // MODIFIES: this
    // EFFECTS: converts the balance from CAD to GBP and changes the currency to GBP
    public double cadtogbp() {
        this.balance = balance * 0.62;
        this.currency = "GBP";
        EventLog.getInstance().logEvent(new Event("Account " + this.getAccountName()
                + " has been converted to GBP"));
        return this.balance;
    }

    // MODIFIES: this
    // EFFECTS: converts the balance from GBP to CAD and changes the currency to CAD
    public double gbptocad() {
        this.balance = balance / 0.62;
        this.currency = "CAD";
        return this.balance;
    }

    public String getAccountName() {
        return accountName;
    }

    public double getBalance() {
        return this.balance;
    }

    public String getCurrency() {
        return this.currency;
    }

    public ArrayList<Transactions> getHistory() {
        return this.history;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Account Name", accountName);
        json.put("Account Balance", balance);
        json.put("Account Currency", currency);
        json.put("Transactions", historyToJson());
        return json;
    }

    // EFFECTS: returns Accounts in this bank as a JSON array
    private JSONArray historyToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Transactions t : history) {
            jsonArray.put(t.toJson());
        }

        return jsonArray;
    }

    public void addTransaction(Transactions transaction) {
        this.history.add(transaction);
    }


}
