package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bank implements Writable {
    private String name;
    private List<BankUser> bankUsers;

    // EFFECTS: constructs bank with a name and empty list of BankUser
    public Bank(String name) {
        this.name = name;
        bankUsers = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    // MODIFIES: this
    // EFFECTS: adds BankUser to this Bank
    public void addBankUser(BankUser user) {
        bankUsers.add(user);
        EventLog.getInstance().logEvent(new Event("User named " + user.getName()
                + " has been added to " + this.getName()));
    }

    // EFFECTS: returns an unmodifiable list of BankUsers in this Bank
    public List<BankUser> getBankUsers() {
        return Collections.unmodifiableList(bankUsers);
    }

    // EFFECTS: returns the number of BankUsers in this Bank
    public int numBankUsers() {
        return bankUsers.size();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("bankUsers", bankUsersToJson());
        return json;
    }

    // EFFECTS: returns BankUsers in this bank as a JSON array
    private JSONArray bankUsersToJson() {
        JSONArray jsonArray = new JSONArray();

        for (BankUser u : bankUsers) {
            jsonArray.put(u.toJson());
        }

        return jsonArray;
    }


}
