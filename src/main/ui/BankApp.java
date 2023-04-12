package ui;

import model.Account;
import model.Bank;
import model.BankUser;
import model.Transactions;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Bank App
public class BankApp {
    private ArrayList<BankUser> listofusers;
    private Bank bank;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/bank.json";

    // EFFECTS: runs the banking application
    public BankApp() throws FileNotFoundException {
        input = new Scanner(System.in);
        bank = new Bank("");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runBankApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runBankApp() {
        boolean keepGoing = true;
        String command = null;

        chooseBank();

        displayLoginMenu();
        command = null;
        command = input.next();
        command = command.toLowerCase();

        while (keepGoing) {
            if (command.equals("l")) {
                logIn();
                keepGoing = false;
            } else if (command.equals("a")) {
                addUser();
                keepGoing = false;
            } else if (command.equals("q")) {
                System.out.println("Goodbye! See you Next Time!");
                keepGoing = false;
            } else {
                System.out.println("Invalid Selection");
                keepGoing = false;
            }
        }
    }

    private void chooseBank() {
        boolean chooseBank = true;
        String command = null;

        displayChooseBankMenu();
        command = input.next();
        command = command.toLowerCase();
        while (chooseBank) {
            if (command.equals("e")) {
                selectBank();
                chooseBank = false;
            } else if (command.equals("n")) {
                System.out.println("Name of new Bank");
                String name = input.next();
                name = name.toLowerCase();
                bank = new Bank(name);
                chooseBank = false;
            } else {
                System.out.println("Invalid Selection. Proceeding with New Bank");
                chooseBank = false;
            }
        }
    }

    private void displayChooseBankMenu() {
        System.out.println("\nWhere do you want to bank at today?:");
        System.out.println("\te -> Existing Bank");
        System.out.println("\tn -> New Bank");
    }

    private void selectBank() {
        try {
            bank = jsonReader.read();
            System.out.println("Loaded " + bank.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: displays menu of login login options to user
    private void displayLoginMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tl -> Log In");
        System.out.println("\ta -> Add User");
        System.out.println("\tq -> Quit");
    }

    // MODIFIES: this
    // EFFECTS: Checks to see if there is already a user that exists in the Bank and Logs in if there is.
    //             If not, then it prints out Account not Found and App is terminated
    private void logIn() {
        System.out.println("Enter Account ID");
        int inputID = input.nextInt();
        System.out.println("Enter Account Name");
        String inputName = input.next();

        for (BankUser user : bank.getBankUsers()) {
            if (inputID == user.getID() && inputName.equals(user.getName())) {
                bankOperations(user);
            } else {
                System.out.println("User not Found");
            }
        }
    }


    // MODIFIES: this
    // EFFECTS: Takes in inputs from user and runs checkExists
    private void addUser() {
        System.out.println("Enter your desired User ID");
        int inputID = input.nextInt();
        System.out.println("Enter your Name");
        String inputName = input.next();

        listofusers = new ArrayList<>();
        for (BankUser user : bank.getBankUsers()) {
            listofusers.add(user);
        }


        checkExists(inputID, inputName);
    }

    // MODIFIES this
    // EFFECTS: Takes in and integer id and String name and checks if there is a user with the same id and name that
    //            exists in listofusers.
    //          If the user already exists then it prints out "User already exists" and "Please Log In instead" and
    //              terminates the app
    //          If the user does not already exist, then it creates a new User with the given id and given name,
    //             adds it to listofusers and runs bankOperations with the newUser
    private boolean checkExists(int id, String name) {
        ArrayList<Boolean> exists = new ArrayList<>();
        setListofusers(bank);

        if (listofusers == null) {
            listofusers = new ArrayList<BankUser>();
        }

        for (int i = 0; i < listofusers.size(); i++) {
            BankUser user = listofusers.get(i);
            if (id == user.getID() && name.equals(user.getName())) {
                exists.add(true);
            } else {
                exists.add(false);
            }
        }

        if (exists.contains(true)) {
            System.out.println("User already exists");
            System.out.println("Please Log In instead");
        } else {
            BankUser newUser = new BankUser(id, name);
            bank.addBankUser(newUser);
            listofusers.add(newUser);
            bankOperations(newUser);
        }

        return false;
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void bankOperations(BankUser user) {
        String command = "q";

        displayUserMenu();
        command = input.next();
        command = command.toLowerCase();

        if (command.equals("q")) {
            System.out.println("Logged Out. Goodbye!");
        } else {
            processBankOperations(command, user);
        }
    }

    // EFFECTS: Displays a list of operations available to the user once logged in
    private void displayUserMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\td -> deposit");
        System.out.println("\tw -> withdraw");
        System.out.println("\tc -> convert currency");
        System.out.println("\tt -> view transactions");
        System.out.println("\ta -> add account");
        System.out.println("\tr -> remove account");
        System.out.println("\ts -> save bank");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processBankOperations(String command, BankUser user) {
        if (command.equals("d")) {
            doDeposit(user);
        } else if (command.equals("w")) {
            doWithdrawal(user);
        } else if (command.equals("c")) {
            doConversion(user);
        } else if (command.equals("t")) {
            viewTransactions(user);
        } else if (command.equals("a")) {
            addNewAccount(user);
        } else if (command.equals("r")) {
            removeAccount(user);
        } else if (command.equals("s")) {
            saveBank();
        } else {
            System.out.println("Invalid Selection");
        }
    }

    // EFFECTS: saves the bank to file
    private void saveBank() {
        try {
            jsonWriter.open();
            jsonWriter.write(bank);
            jsonWriter.close();
            System.out.println("Saved " + bank.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    private ArrayList<BankUser> setListofusers(Bank bank) {
        for (BankUser user : bank.getBankUsers()) {
            ArrayList<BankUser> listofusers = new ArrayList<BankUser>();
            listofusers.add(user);
        }
        return listofusers;

    }

    // MODIFIES: this
    // EFFECTS: conducts a deposit transaction
    private void doDeposit(BankUser user) {
        System.out.println("Choose the Account you want to Deposit to");
        Account chosenAccount = chooseAccount(user);
        System.out.println("Enter amount to be Deposited");
        double depositAmount = input.nextDouble();
        if (depositAmount < 0) {
            System.out.println("Invalid Deposit Amount");
            bankOperations(user);
        }
        System.out.println(chosenAccount.deposit(depositAmount) + chosenAccount.getCurrency());
        bankOperations(user);
    }

    // MODIFIES: this
    // EFFECTS: conducts a withdrawal transaction
    private void doWithdrawal(BankUser user) {
        System.out.println("Choose the Account that you want to Withdraw From");
        Account chosenAccount = chooseAccount(user);
        System.out.println("Enter amount to be Withdrawn");
        double withdrawalAmount = input.nextDouble();
        if (withdrawalAmount < 0 && withdrawalAmount > chosenAccount.getBalance()) {
            System.out.println("Invalid Withdrawal Amount");
            bankOperations(user);
        }
        System.out.println("Withdrawal Complete. Current Account Balance is");
        System.out.println(chosenAccount.withdraw(withdrawalAmount) + " " + chosenAccount.getCurrency());
        bankOperations(user);
    }

    // MODIFIES: this
    // EFFECTS: Conducts a currency conversion of an account that a user selects
    private void doConversion(BankUser user) {
        System.out.println("Choose the Account that you want to convert");
        Account chosenAccount = chooseAccount(user);
        System.out.println("Enter Currency you want to Convert to");
        System.out.println("CAD, USD, JPY, GBP");

        String wantedCurrency = input.next().toUpperCase();

        if (wantedCurrency.equals("USD") || wantedCurrency.equals("CAD") || wantedCurrency.equals("GBP")
                || wantedCurrency.equals("JPY")) {
            chosenAccount.convertToCAD();
            convertTo(chosenAccount, wantedCurrency);
            System.out.println(chosenAccount.getBalance() + " " + chosenAccount.getCurrency());
            bankOperations(user);
        } else {
            System.out.println("Unsupported Currency. Balance remains in CAD");
            System.out.println(chosenAccount.getBalance() + " " + chosenAccount.getCurrency());
            bankOperations(user);
        }
    }

    // EFFECTS: Displays a list of transactions of a selected Account
    private void viewTransactions(BankUser user) {
        if (user.getBankaccounts().size() <= 0) {
            System.out.println("No Available Accounts");
            bankOperations(user);
        } else {
            System.out.println("Choose the Account that you want to view");
            Account chosenAccount = chooseAccount(user);
            ArrayList<Transactions> listOfTransactions = chosenAccount.getHistory();
            if (listOfTransactions.size() == 0) {
                System.out.println("No Transactions Found");
                bankOperations(user);
            }
            for (Transactions transactions : listOfTransactions) {
                System.out.println((listOfTransactions.indexOf(transactions) + 1) + "." + " "
                        + transactions.getTransactionType() + " "
                        + transactions.getTransactionAmount());
            }
            bankOperations(user);
        }
    }

    // MODIFIES: this
    // EFFECTS: construct a new account with the given user inputs
    private void addNewAccount(BankUser user) {
        System.out.println("Enter Account Name");
        String accountName = input.next();
        System.out.println("Enter Initial Balance");
        double initialBalance = input.nextDouble();
        user.addAccount(accountName, initialBalance);
        System.out.println("Account added");
        bankOperations(user);
    }

    // MODIFIES: this
    // EFFECTS: removes the account that matches the given user inputs
    private void removeAccount(BankUser user) {
        ArrayList<Account> listofAccounts = user.getBankaccounts();
        if (listofAccounts.size() >= 2) {
            removeAccountBalance(user);
        } else if (listofAccounts.size() == 1) {
            System.out.println("Choose the Account to Remove");
            Account removeAccount = chooseAccount(user);
            listofAccounts.remove(removeAccount);
            System.out.println("Account has been Removed. Please visit a nearby branch to pick up your balance");
            bankOperations(user);
        } else {
            System.out.println("No Accounts available");
            bankOperations(user);
        }
    }

    // MODIFIES: this
    // EFFECTS: Removes the selected account and transfers the balance within it to another selected account by the user
    private void removeAccountBalance(BankUser user) {
        ArrayList<Account> listofAccounts = user.getBankaccounts();
        System.out.println("Choose the Account to Remove");
        Account removeAccount = chooseAccount(user);
        removeAccount = removeAccount.convertToCAD();
        double removeAccountBalance = removeAccount.getBalance();

        System.out.println("Choose the Account you want to transfer the remaining balance to");
        Account transferAccount = chooseAccount(user);
        String transferAccountCurrency = transferAccount.getCurrency();
        Account transferAccountConverted = transferAccount.convertToCAD();
        transferAccountConverted.deposit(removeAccountBalance);
        convertTo(transferAccountConverted, transferAccountCurrency);

        System.out.println("Account Removed and Balance has been transferred to the chosen account");

        listofAccounts.remove(removeAccount);
        bankOperations(user);
    }

    // EFFECTS: takes in user input and selects the corresponding accounts at the given index + 1
    private Account chooseAccount(BankUser user) {
        ArrayList<Account> accounts = user.getBankaccounts();
        int selection = -1; //force entry into loop

        for (Account account : accounts) {
            System.out.println((accounts.indexOf(account) + 1) + "." + " " + account.getAccountName() + " "
                    + account.getBalance() + " " + account.getCurrency());
        }

        selection = input.nextInt();


        if (selection < 0 || selection > accounts.size()) {
            System.out.println("Account not Found");
            bankOperations(user);
        }
        int accountNumber = selection - 1;
        return accounts.get(accountNumber);
    }

    // MODIFIES: this
    // EFFECTS: Changes the account currency and multiplies the account balance by the appropriate amount.
    public double convertTo(Account account, String currency) {
        if (currency.equals("USD")) {
            account.cadtousd();
            System.out.println("Conversion Complete");
        } else if (currency.equals("JPY")) {
            account.cadtojpy();
            System.out.println("Conversion Complete");
        } else if (currency.equals("GBP")) {
            account.cadtogbp();
            System.out.println("Conversion Complete");
        } else if (currency.equals("CAD")) {
            System.out.println("Balance converted to CAD");
            return account.getBalance();
        } else {
            System.out.println("Unsupported Currency. Balance converted to CAD");
            return account.getBalance();
        }
        return 0;
    }

}