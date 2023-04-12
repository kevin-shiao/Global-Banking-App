package ui;

import model.*;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

public class BankAppPanel extends JFrame {
    private JFrame frame;
    Bank bank;
    ArrayList<BankUser> listofusers;
    BankUser user;

    private JPanel chooseBankPanel;
    private JLabel imageLabel;

    private JPanel chooseNewBankPanel;
    private JTextField enterNewBankNameField;

    // Panels for Log in System
    private JPanel logInMenuPanel;

    // Creates the logInPanel and the associated Text Fields
    private JPanel logInPanel;
    private JTextField enterAccountID;
    private JTextField enterAccountName;

    private JPanel addNewUserPanel;
    private JTextField enterUserID;
    private JTextField enterUserName;

    //Panel for the BankOperations
    private JPanel bankOperationsPanel;

    //Panel and TextFields for deposit
    private JPanel depositPanel;
    private JTextField accountSelectionTextField;
    private JTextField depositAmount;


    private JPanel withdrawPanel;
    private JTextField accountSelectionWitTextField;
    private JTextField withdrawAmount;


    private JPanel convertCurrencyPanel;
    private JTextField accountSelectionConvTextField;

    private JPanel viewTransactionsPanel;
    private JTextField accountSelectionViewTextField;
    private Account account;
    private JPanel showChosenAccountTransactionsPanel;

    private JPanel addAccountPanel;
    private JTextField addAccountNameField;
    private JTextField addAccountAmountField;

    private JPanel removeAccountPanel;
    private JTextField accountSelectionRemoveField;

    private static final String JSON_STORE = "./data/bank.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // MODIFIES: frame, bank, listofusers, user, jsonWriter, jsonReader
    // EFFECTS: instantiates all of the above fields
    public BankAppPanel() {
        frame = new JFrame();
        frame.setTitle("Global Banking");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1500, 800));
        frame.setLayout(new FlowLayout());
        addEventLog();

        chooseBank();

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        setResizable(false);

        bank = new Bank("");
        listofusers = new ArrayList<BankUser>();
        user = new BankUser(-500, "DNE");

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    public void addEventLog() {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                EventLog el = EventLog.getInstance();
                for (Event next : el) {
                    System.out.println(next);
                }
                System.exit(0);
            }
        });
    }

    // MODIFIES: frame
    // EFFECTS: runs initChooseBank() method and adds chooseBankPanel to frame
    public void chooseBank() {
        initChooseBank();
        frame.add(chooseBankPanel);
    }

    // MODIFIES: chooseBankPanel
    // EFFECTS:
    public void initChooseBank() {
        chooseBankPanel = new JPanel();
        chooseNewBankPanel = new JPanel();

        //Creates the Text for the inital chooseBank Screen
        JLabel chooseBankLabel = new JLabel("Where would you like to bank at today?");
        chooseBankPanel.add(chooseBankLabel);

        // Creates the Button for Existing Bank Option on chooseBank Screen
        JButton ebtn = new JButton("Existing Bank");
        ebtn.addActionListener(new ExistingBankListener());
        chooseBankPanel.add(ebtn);

        // Creates the Button for New Bank Option on chooseBank Screen
        JButton nbtn = new JButton("New Bank");
        nbtn.addActionListener(new NewBankListener());
        chooseBankPanel.add(nbtn);

        ImageIcon img = new ImageIcon("./data/global.jpg");
        imageLabel = new JLabel(img);
        chooseBankPanel.add(imageLabel);


    }

    class ExistingBankListener implements ActionListener {

        // MODIFIES: bank, listofusers, frame
        // EFFECTS: Makes bank as the bank read from the saved JSON File
        //          adds each bankUser in bank to listofusers
        //          removes chooseBankPanel from frame and then runs logIn() method
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                bank = jsonReader.read();

                for (BankUser u : bank.getBankUsers()) {
                    listofusers.add(u);
                }

            } catch (IOException e1) {
                System.out.println();
            }
            frame.remove(chooseBankPanel);
            logIn();
        }
    }

    class NewBankListener implements ActionListener {

        // MODIFIES: frame
        // EFFECTS: removes chooseBankPanel from frame, runs initNewBanknamePanel() method and then adds
        //            chooseNewBankPanel
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.remove(chooseBankPanel);
            initNewBankNamePanel();
            frame.add(chooseNewBankPanel);
            resetFrame();
        }
    }

    // MODIFIES: chooseNewBankPanel
    // EFFECTS: adds Labels and Buttons to chooseNewBankPanel
    public void initNewBankNamePanel() {
        JLabel enterNewBankName = new JLabel("Enter Name of New Bank");
        chooseNewBankPanel.add(enterNewBankName);

        enterNewBankNameField = new JTextField("", 10);
        chooseNewBankPanel.add(enterNewBankNameField);

        JButton enterNewBankNameDone = new JButton("Done");
        enterNewBankNameDone.addActionListener(new NewBankNameListener());
        chooseNewBankPanel.add(enterNewBankNameDone);
    }

    class NewBankNameListener implements ActionListener {

        // MODIFIES: frame, bank
        // EFFECTS: creates a newBank with the inputted String and removes chooseNewBankPanel from frame.
        //          Runs logIn() method
        @Override
        public void actionPerformed(ActionEvent e) {
            String newBankNames = enterNewBankNameField.getText();
            bank = new Bank(newBankNames);
            frame.remove(chooseNewBankPanel);
            logIn();
        }
    }

    // MODIFIES: frame
    // EFFECTS: runs initLogInMenuPanel() method and adds logInMenuPanel to frame
    public void logIn() {
        initLogInMenuPanel();
        frame.add(logInMenuPanel);
        resetFrame();
    }

    // MODIFIES: logInMenuPanel
    // EFFECTS: creates logInMenuPanel
    public void initLogInMenuPanel() {
        logInMenuPanel = new JPanel();
        JLabel logInMenuText = new JLabel("Select From");
        JButton logInBtn = new JButton("LogIn");
        logInBtn.addActionListener(new LogInBtnListener());
        JButton addUserBtn = new JButton("Add User");
        addUserBtn.addActionListener(new AddUserBtnListener());
        logInMenuPanel.add(logInMenuText);
        logInMenuPanel.add(logInBtn);
        logInMenuPanel.add(addUserBtn);
    }

    class LogInBtnListener implements ActionListener {

        // MODIFIES: frame
        // EFFECTS: logInMenuPanel is removed and logInScreen() method is run
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.remove(logInMenuPanel);
            logInScreen();
        }
    }

    // MODIFIES: frame, logInPanel
    // EFFECTS: creates logInPanel and adds it to frame
    public void logInScreen() {
        logInPanel = new JPanel();

        JLabel accountIDLabel = new JLabel("Enter Account ID");
        enterAccountID = new JTextField("0", 6);

        JLabel accountNameLabel = new JLabel("Enter Account Name");
        enterAccountName = new JTextField("", 12);

        JButton checkAccountButton = new JButton("Log In");
        checkAccountButton.addActionListener(new CheckAccountListener());
        logInPanel.add(accountIDLabel);
        logInPanel.add(enterAccountID);
        logInPanel.add(accountNameLabel);
        logInPanel.add(enterAccountName);
        logInPanel.add(checkAccountButton);

        frame.add(logInPanel);
        resetFrame();
    }

    class CheckAccountListener implements ActionListener {

        // MODIFIES: frame, bank, user
        // EFFECTS: checks if the inputted ID and Name match a user in the bank
        //           If there is a user that matches inputted ID and Name,
        //              then logInPanel is removed from frame and then user is set
        //              to the matching user and runs bankOperations() method
        @Override
        public void actionPerformed(ActionEvent e) {
            int checkAccountID = Integer.parseInt(enterAccountID.getText());
            String checkAccountName = enterAccountName.getText();
            Boolean exists = false;

            for (BankUser u : listofusers) {
                if (checkAccountID == u.getID() && checkAccountName.equals(u.getName())) {
                    user = u;
                    bank.addBankUser(user);
                    frame.remove(logInPanel);
                    resetFrame();
                    bankOperations();
                }
            }

            if (exists == false) {
                JLabel dne = new JLabel("User Not Found");
                logInPanel.add(dne);
                resetFrame();
            }
        }
    }

    class AddUserBtnListener implements ActionListener {

        // MODIFIES: frame
        // EFFECTS: removes logInMenuPanel and runs addNewUserScreen()
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.remove(logInMenuPanel);
            addNewUserScreen();
        }
    }

    // MODIFIES: frame, addNewUserPanel
    // EFFECTS: creates addNewUserPanel and adds it to frame
    public void addNewUserScreen() {
        addNewUserPanel = new JPanel();

        JLabel userIDLabel = new JLabel("Enter your desired User ID");
        enterUserID = new JTextField("0", 6);

        JLabel userNameLabel = new JLabel("Enter your desired User Name");
        enterUserName = new JTextField("", 12);

        JButton makeNewUserBtn = new JButton("Enter");
        makeNewUserBtn.addActionListener(new MakeNewUserListener());
        addNewUserPanel.add(userIDLabel);
        addNewUserPanel.add(enterUserID);
        addNewUserPanel.add(userNameLabel);
        addNewUserPanel.add(enterUserName);
        addNewUserPanel.add(makeNewUserBtn);

        frame.add(addNewUserPanel);
        resetFrame();
    }

    class MakeNewUserListener implements ActionListener {

        // MODIFIES: bank, user
        // EFFECTS: Checks to see if the given ID and Name correspond to a user in bank
        //            If there is no user that matches ID and Name, then creates a new BankUser and adds it to bank
        //               then addNewUserPanel is removed from frame and bankOperations() method is run
        @Override
        public void actionPerformed(ActionEvent e) {

            int checkAccountIDNewUser = Integer.parseInt(enterUserID.getText());
            String checkAccountNameNewUser = enterUserName.getText();
            Boolean exists = false;

            for (BankUser u : listofusers) {
                if (checkAccountIDNewUser == u.getID() && checkAccountNameNewUser.equals(u.getName())) {
                    exists = true;
                }
            }

            if (exists == false) {
                user = new BankUser(checkAccountIDNewUser, checkAccountNameNewUser);
                bank.addBankUser(user);
                frame.remove(addNewUserPanel);
                resetFrame();
                bankOperations();
            }
        }
    }

    // MODIFIES: frame
    // EFFECTS: runs initbankOperationsPanel and adds bankOperationsPanel to frame
    public void bankOperations() {
        initbankOperationsPanel();
        frame.add(bankOperationsPanel);
        resetFrame();
    }


    // MODIFIES: frame, bankOperationsPanel
    // EFFECTS: creates bankOperationsPanel and adds it to frame
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public void initbankOperationsPanel() {
        bankOperationsPanel = new JPanel();

        JLabel bankOperationsLabel = new JLabel("Select from:");
        JButton depositBtn = new JButton("Deposit");
        depositBtn.addActionListener(new DepositListener());
        JButton withdrawBtn = new JButton("Withdraw");
        withdrawBtn.addActionListener(new WithdrawListener());
        JButton convertCurrencyBtn = new JButton("Convert Currency");
        convertCurrencyBtn.addActionListener(new CurrencyCovListener());
        JButton viewTransactionsBtn = new JButton("View Transactions");
        viewTransactionsBtn.addActionListener(new ViewTransactionsListener());
        JButton addAccountBtn = new JButton("Add Account");
        addAccountBtn.addActionListener(new AddAccountListener());
        JButton removeAccountBtn = new JButton("Remove Account");
        removeAccountBtn.addActionListener(new RemoveAccountListener());
        JButton saveBtn = new JButton("Save Bank");
        saveBtn.addActionListener(new SaveBankListener());

        DefaultListModel<String> accountList = new DefaultListModel<>();

        for (Account a : user.getBankaccounts()) {
            int accountIndex = user.getBankaccounts().indexOf(a);
            int accountPosition = accountIndex + 1;
            String accountName = a.getAccountName();
            String accountBalance = String.valueOf(a.getBalance());
            String accountCurrency = a.getCurrency();

            String accountInfo = (accountPosition + "." + " "
                    + accountName + " " + accountBalance + " " + accountCurrency);
            accountList.addElement(accountInfo);
        }

        JList<String> listofAccounts = new JList<>(accountList);

        bankOperationsPanel.add(bankOperationsLabel);
        bankOperationsPanel.add(depositBtn);
        bankOperationsPanel.add(withdrawBtn);
        bankOperationsPanel.add(convertCurrencyBtn);
        bankOperationsPanel.add(viewTransactionsBtn);
        bankOperationsPanel.add(addAccountBtn);
        bankOperationsPanel.add(removeAccountBtn);
        bankOperationsPanel.add(saveBtn);
        bankOperationsPanel.add(listofAccounts);
    }

    public void chooseBankAccountMethod() {

    }

    // MODIFIES: frame
    // EFFECTS: removes bankOperationsPanel from frame and runs deposit() method
    class DepositListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.remove(bankOperationsPanel);
            deposit();
        }
    }

    // MODIFIES: frame, depositPanel
    // EFFECTS: creates the depositPanel and adds it to frame
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public void deposit() {
        depositPanel = new JPanel();

        JLabel accountSelectionLabelDep = new JLabel("Select Account you wish to Deposit into");
        accountSelectionTextField = new JTextField("1", 2);

        JLabel amountLabelDep = new JLabel("Enter Amount you wish to Deposit");
        depositAmount = new JTextField("0", 8);

        JButton depositFinish = new JButton("Deposit");
        depositFinish.addActionListener(new FinishDepositListener());
        depositPanel.add(accountSelectionLabelDep);
        depositPanel.add(accountSelectionTextField);
        depositPanel.add(amountLabelDep);
        depositPanel.add(depositAmount);
        depositPanel.add(depositFinish);

        DefaultListModel<String> accountList = new DefaultListModel<>();

        for (Account a : user.getBankaccounts()) {
            int accountIndex = user.getBankaccounts().indexOf(a);
            int accountPosition = accountIndex + 1;
            String accountName = a.getAccountName();
            String accountBalance = String.valueOf(a.getBalance());
            String accountCurrency = a.getCurrency();

            String accountInfo = (accountPosition + "." + " "
                    + accountName + " " + accountBalance + " " + accountCurrency);
            accountList.addElement(accountInfo);
        }

        JList<String> listofAccounts = new JList<>(accountList);
        depositPanel.add(listofAccounts);

        frame.add(depositPanel);
        resetFrame();
    }

    class FinishDepositListener implements ActionListener {

        // MODIFIES: frame, user
        // EFFECTS: deposits amount into chosen user account and removes depositPanel from frame and runs bankOperations
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedAccountIDDep = Integer.parseInt(accountSelectionTextField.getText());
            double amount = Double.parseDouble(depositAmount.getText());
            int index = selectedAccountIDDep - 1;
            Account account = user.getBankaccounts().get(index);

            if (amount < 0) {
                amount = 0;
            }

            account.deposit(amount);

            frame.remove(depositPanel);
            bankOperations();
        }
    }


    class WithdrawListener implements ActionListener {

        // MODIFIES: frame
        // EFFECTS: removes bankOperationsPanel and runs withdrawal() method
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.remove(bankOperationsPanel);
            withdrawal();
        }
    }

    // MODIFIES: frame, withdrawPanel
    // EFFECTS: creates the withdraw panel and adds it to frame
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public void withdrawal() {
        withdrawPanel = new JPanel();

        JLabel accountSelectionLabelWit = new JLabel("Select Account you wish to Withdraw from");
        accountSelectionWitTextField = new JTextField("1", 3);

        JLabel amountLabelWit = new JLabel("Enter Amount you wish to Withdraw");
        withdrawAmount = new JTextField(8);

        JButton withdrawFinish = new JButton("Withdraw");
        withdrawFinish.addActionListener(new WithdrawFinishedListener());

        DefaultListModel<String> accountList = new DefaultListModel<>();

        for (Account a : user.getBankaccounts()) {
            int accountIndex = user.getBankaccounts().indexOf(a);
            int accountPosition = accountIndex + 1;
            String accountName = a.getAccountName();
            String accountBalance = String.valueOf(a.getBalance());
            String accountCurrency = a.getCurrency();

            String accountInfo = (accountPosition + "." + " "
                    + accountName + " " + accountBalance + " " + accountCurrency);
            accountList.addElement(accountInfo);
        }

        JList<String> listofAccounts = new JList<>(accountList);

        withdrawPanel.add(accountSelectionLabelWit);
        withdrawPanel.add(accountSelectionWitTextField);
        withdrawPanel.add(amountLabelWit);
        withdrawPanel.add(withdrawAmount);
        withdrawPanel.add(withdrawFinish);
        withdrawPanel.add(listofAccounts);

        frame.add(withdrawPanel);
        resetFrame();
    }


    class WithdrawFinishedListener implements ActionListener {

        // MODIFIES: frame, user
        // EFFECTS: withdraws amount from chosen user account and removes withdrawPanel
        //           from frame and runs bankOperations
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedAccountIDWit = Integer.parseInt(accountSelectionWitTextField.getText());
            int index = selectedAccountIDWit - 1;
            double amount = Double.parseDouble(withdrawAmount.getText());
            Account account = user.getBankaccounts().get(index);

            if (amount > account.getBalance() || amount < 0) {
                amount = 0;
            }

            account.withdraw(amount);

            frame.remove(withdrawPanel);
            bankOperations();

        }
    }


    class CurrencyCovListener implements ActionListener {

        // MODIFIES: frame
        // EFFECTS: removes bankOperationsPanel and adds it to frame
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.remove(bankOperationsPanel);
            convertCurrency();
        }
    }

    // MODIFIES: frame, convertCurrencyPanel
    // EFFECTS: creates the convertCurrencyPanel and adds it to frame
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public void convertCurrency() {
        convertCurrencyPanel = new JPanel();

        JLabel accountSelectionLabelConv = new JLabel("Select Account you wish to convert");
        accountSelectionConvTextField = new JTextField("1", 3);

        String[] listOfCurrencies = {"CAD", "USD", "JPY", "GBP"};

        JLabel currenciesLabel = new JLabel("Select Currency you want to convert to");
        JComboBox currencies = new JComboBox(listOfCurrencies);
        currencies.setSelectedIndex(1);
        currencies.addActionListener(new FinishConversionListener());

        DefaultListModel<String> accountList = new DefaultListModel<>();

        for (Account a : user.getBankaccounts()) {
            int accountIndex = user.getBankaccounts().indexOf(a);
            int accountPosition = accountIndex + 1;
            String accountName = a.getAccountName();
            String accountBalance = String.valueOf(a.getBalance());
            String accountCurrency = a.getCurrency();

            String accountInfo = (accountPosition + "." + " "
                    + accountName + " " + accountBalance + " " + accountCurrency);
            accountList.addElement(accountInfo);
        }

        JList<String> listofAccounts = new JList<>(accountList);

        convertCurrencyPanel.add(accountSelectionLabelConv);
        convertCurrencyPanel.add(accountSelectionConvTextField);
        convertCurrencyPanel.add(currenciesLabel);
        convertCurrencyPanel.add(currencies);
        convertCurrencyPanel.add(listofAccounts);

        frame.add(convertCurrencyPanel);
        resetFrame();
    }


    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    class FinishConversionListener implements ActionListener {

        // REQUIRES: accountSelectionConvTextField.getText() >= 1
        // MODIFIES: user
        // EFFECTS: converts the selected account of user into the chosen currency
        @Override
        public void actionPerformed(ActionEvent e) {
            int accountSelectionConvID = Integer.parseInt(accountSelectionConvTextField.getText());
            int index = accountSelectionConvID - 1;

            JComboBox cb = (JComboBox)e.getSource();
            String currencyChosen = (String)cb.getSelectedItem();

            Account account = user.getBankaccounts().get(index);
            String accountCurrency = account.getCurrency();

            if (currencyChosen.equals("CAD")) {
                account.convertToCAD();
                accountCurrency = currencyChosen;
            } else if (currencyChosen.equals("USD")) {
                account.convertToCAD();
                account.cadtousd();
                accountCurrency = currencyChosen;
            } else if (currencyChosen.equals("GBP")) {
                account.convertToCAD();
                account.cadtogbp();
                accountCurrency = currencyChosen;
            } else if (currencyChosen.equals("JPY")) {
                account.convertToCAD();
                account.cadtojpy();
                accountCurrency = currencyChosen;
            }

            frame.remove(convertCurrencyPanel);
            bankOperations();
        }
    }


    class ViewTransactionsListener implements ActionListener {

        // MODIFIES: frame
        // EFFECTS: removes bankOperationsPanel and runs viewTransactions() method
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.remove(bankOperationsPanel);
            viewTransactions();
        }
    }

    // MODIFIES: frame, viewTransactionsPanel
    // EFFECTS: Creates viewTransactionsPanel and adds it to the frame.
    public void viewTransactions() {
        viewTransactionsPanel = new JPanel();

        JLabel accountSelectionViewConv = new JLabel("Select Account you wish to view the Transactions of");
        accountSelectionViewTextField = new JTextField("1", 3);


        JButton accountSelectedView = new JButton("Choose Account");
        accountSelectedView.addActionListener(new ShowChosenAccountTransactionsListener());

        viewTransactionsPanel.add(accountSelectionViewConv);
        viewTransactionsPanel.add(accountSelectionViewTextField);
        viewTransactionsPanel.add(accountSelectedView);

        DefaultListModel<String> accountList = new DefaultListModel<>();

        for (Account a : user.getBankaccounts()) {
            int accountIndex = user.getBankaccounts().indexOf(a);
            int accountPosition = accountIndex + 1;
            String accountName = a.getAccountName();
            String accountBalance = String.valueOf(a.getBalance());
            String accountCurrency = a.getCurrency();

            String accountInfo = (accountPosition + "." + " "
                    + accountName + " " + accountBalance + " " + accountCurrency);
            accountList.addElement(accountInfo);
        }

        JList<String> listofAccounts = new JList<>(accountList);
        viewTransactionsPanel.add(listofAccounts);

        frame.add(viewTransactionsPanel);
        resetFrame();
    }


    class ShowChosenAccountTransactionsListener implements ActionListener {

        // REQUIRES: accountSelectionViewTextField.getText() >= 1
        // MODIFIES: frame, account
        // EFFECTS: removes viewTransactionsPanel and runs showAccountTransactionsPanel() method;
        //           sets account to be the chosen account by user.
        @Override
        public void actionPerformed(ActionEvent e) {
            int viewAccountTransactionID = Integer.parseInt(accountSelectionViewTextField.getText());
            int index = viewAccountTransactionID - 1;
            account = user.getBankaccounts().get(index);

            frame.remove(viewTransactionsPanel);
            showAccountTransactions();
        }
    }

    // MODIFIES: frame, showChosenAccountTransactionsPanel
    // EFFECTS: creates showChosenAccountTransactionsPanel and adds it to frame.
    public void showAccountTransactions() {
        showChosenAccountTransactionsPanel = new JPanel();

        DefaultListModel<String> transactionList = new DefaultListModel<>();

        for (Transactions t : account.getHistory()) {
            String transactionType = t.getTransactionType();
            String transactionAmount = String.valueOf(t.getTransactionAmount());
            String transactionCurrency = t.getTransactionCurrency();
            String transaction = (transactionType + " " + transactionAmount + " " + transactionCurrency);
            transactionList.addElement(transaction);
        }

        JList<String> list = new JList<>(transactionList);

        JButton finishViewTransaction = new JButton("Done");
        finishViewTransaction.addActionListener(new FinishViewTransactionListener());

        showChosenAccountTransactionsPanel.add(list);
        showChosenAccountTransactionsPanel.add(finishViewTransaction);

        frame.add(showChosenAccountTransactionsPanel);
        resetFrame();
    }


    class FinishViewTransactionListener implements ActionListener {

        // MODIFIES: frame
        // EFFECTS: removes showChosenAccountTransactionsPanel from frame and runs bankOperations() method;
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.remove(showChosenAccountTransactionsPanel);
            bankOperations();
        }
    }


    class AddAccountListener implements ActionListener {

        // MODIFIES: frame
        // EFFECTS: removes bankOperationsPanel and runs addAccount() method
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.remove(bankOperationsPanel);
            addAccount();
        }
    }

    // MODIFIES: frame, addAccountPanel
    // EFFECTS: Creates addAccountPanel and adds it to frame
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public void addAccount() {
        addAccountPanel = new JPanel();

        JLabel addAccountLabel = new JLabel("Enter Account Name");
        addAccountNameField = new JTextField("", 12);

        JLabel addAccountAmountLabel = new JLabel("Enter Initial Balance");
        addAccountAmountField = new JTextField("0", 8);

        JButton addAccountFinish = new JButton("Add");
        addAccountFinish.addActionListener(new AddAccountFinishListener());
        addAccountPanel.add(addAccountLabel);
        addAccountPanel.add(addAccountNameField);
        addAccountPanel.add(addAccountAmountLabel);
        addAccountPanel.add(addAccountAmountField);
        addAccountPanel.add(addAccountFinish);

        DefaultListModel<String> accountList = new DefaultListModel<>();

        for (Account a : user.getBankaccounts()) {
            int accountIndex = user.getBankaccounts().indexOf(a);
            int accountPosition = accountIndex + 1;
            String accountName = a.getAccountName();
            String accountBalance = String.valueOf(a.getBalance());
            String accountCurrency = a.getCurrency();

            String accountInfo = (accountPosition + "." + " "
                    + accountName + " " + accountBalance + " " + accountCurrency);
            accountList.addElement(accountInfo);
        }

        JList<String> listofAccounts = new JList<>(accountList);
        addAccountPanel.add(listofAccounts);

        frame.add(addAccountPanel);
        resetFrame();
    }


    class AddAccountFinishListener implements ActionListener {

        // MODIFIES: frame, user
        // EFFECTS: Creates a new account for user and changes the frame back to bankOperationsPanel
        @Override
        public void actionPerformed(ActionEvent e) {
            double initialBalOfAddedAccount = Double.parseDouble(addAccountAmountField.getText());
            String nameOfAddedAccount = addAccountNameField.getText();

            Account account = new Account(nameOfAddedAccount, initialBalOfAddedAccount);
            user.addAccountOnly(account);

            frame.remove(addAccountPanel);
            bankOperations();
        }
    }


    class RemoveAccountListener implements ActionListener {

        // MODIFIES: frame
        // EFFECTS: Removes bankOperationsPanel from frame and runs removeAccount() method
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.remove(bankOperationsPanel);
            removeAccount();
        }
    }

    // MODIFIES: frame, removeAccountPanel
    // EFFECTS: Creates the removeAccountPanel and adds it to the frame.
    public void removeAccount() {
        removeAccountPanel = new JPanel();

        JLabel accountSelectionViewConv = new JLabel("Select Account you wish to remove");
        accountSelectionRemoveField = new JTextField("1", 3);

        JButton removeAccountBtn = new JButton("Remove Account");
        removeAccountBtn.addActionListener(new RemoveAccountFinishListener());

        removeAccountPanel.add(accountSelectionViewConv);
        removeAccountPanel.add(accountSelectionRemoveField);
        removeAccountPanel.add(removeAccountBtn);

        DefaultListModel<String> accountList = new DefaultListModel<>();

        for (Account a : user.getBankaccounts()) {
            int accountIndex = user.getBankaccounts().indexOf(a);
            int accountPosition = accountIndex + 1;
            String accountName = a.getAccountName();
            String accountBalance = String.valueOf(a.getBalance());
            String accountCurrency = a.getCurrency();

            String accountInfo = (accountPosition + "." + " "
                    + accountName + " " + accountBalance + " " + accountCurrency);
            accountList.addElement(accountInfo);
        }

        JList<String> listofAccounts = new JList<>(accountList);
        removeAccountPanel.add(listofAccounts);

        frame.add(removeAccountPanel);
        resetFrame();
    }

    class RemoveAccountFinishListener implements ActionListener {

        // REQUIRES: accountSelectionRemoveField.getText() >= 1
        // MODIFIES: frame, user
        // EFFECTS: Removes the selected account from the User and changes frame back to bankOperationsPanel
        @Override
        public void actionPerformed(ActionEvent e) {
            int accountSelectedNumber = Integer.parseInt(accountSelectionRemoveField.getText());
            int index = accountSelectedNumber - 1;

            Account accountSelected = user.getBankaccounts().get(index);
            String nameOfAccountSelected = accountSelected.getAccountName();

            user.removeAccount(nameOfAccountSelected);

            frame.remove(removeAccountPanel);
            bankOperations();
        }
    }


    class SaveBankListener implements ActionListener {

        // MODIFIES: JSON Save files
        // EFFECTS: Saves the current State of GUI
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                jsonWriter.open();
                jsonWriter.write(bank);
                jsonWriter.close();
            } catch (IOException ie) {
                System.out.println();
            }
        }
    }

    // MODIFIES: frame
    // EFFECTS: Updates the frame
    public void resetFrame() {
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    // EFFECTS: Starts the BankAppPanel;
    public static void main(String[] args) {
        new BankAppPanel();
    }
}