package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

// Represents a Transaction having a type and amount
public class Transactions implements Writable {
    private String type; // type of transaction
    private double amount; // amount of transaction
    private String currency; // currency of transaction


    // EFFECTS: constructs a Transactions with the given type and given amount
    public Transactions(String type, double amount, String currency) {
        this.type =  type;
        this.amount = amount;
        this.currency = currency;
    }

    public String getTransactionType() {
        return this.type;
    }

    public double getTransactionAmount() {
        return this.amount;
    }

    public String getTransactionCurrency() {
        return this.currency;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Transaction Type", type);
        json.put("Transaction Amount", amount);
        json.put("Transaction Currency", currency);
        return json;
    }
}
