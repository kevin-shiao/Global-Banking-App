# Global Banking

## An Efficient, Informative, and Easy Banking system

**Global Banking** is a project that is targeted towards people of all ages who are interested in travelling and in
other cultures. What makes Global Banking different from other banking apps is that you can easily convert between
different currencies without an added premium. As it currently stands, Global Banking only supports 4 different currencies
(CAD, USD, GBP, and JPY) but more can be added in the future. All conversion rates are updates as of February 2023.

This project is interesting to me because I love travelling and learning about other cultures. With this program, I
am hoping that this will provide others with a more accurate sense of how far their money can take them in other
countries. There is nothing worse than buying something you believe to be cheap in another country only to find out
that it is more expensive than expected in your home currency. This project will allow you to perform basic banking
necessities and convert your money into different currencies. This is targeted towards people of all ages who are
interested in learning how general banking works and those who are interested in travelling.

*User Stories*
- As a user, I want to be able to deposit/withdraw money from/to my accounts
- As a user, I want to be able to open multiple accounts
- As a user, I want to be able to close accounts
- As a user, I want to be able to be able to convert my money to different currencies
- As a user, I want to be able to see a history of my transactions
- As a user, I want my past transactions to be saved even after exiting the program
- As a user, I want to be able to save all BankUsers and accounts to file
- As a user, I want my past transactions to load my past accounts and transaction history from file


*Instructions for User*
- You can deposit/withdraw money from/to your accounts by clicking on either the Deposit Button or the Withdraw Button on the BankOperations Panel
- You can open multiple accounts to your user by clicking on the Add Account button on the BankOperations Panel.
- You can locate my visual component by starting the application as it is a background iamge. 
- You can save the state of my application by clicking on the Save Bank Bution on the BankOperations Panel
- You can reload the state of my application by clicking on the Existing Bank Button when the application first starts up

*Project Reflection*
- If given more time to complete my personal project, I would improve my design by making the BankApp class and the 
BankAppPanel class only depend on a Bank instead of a Bank and a BankUser. Specifically, I would want to change the code
so that I could achieve a Singleton Pattern with one instance of Bank and then operate with the users within the global
instance of Bank. This would improve my project as it will reduce coupling.

- Another aspect that I would like to improve in my personal project is the amount of duplication. With the BankAppPanel
GUI class, I noticed that there was a lot of duplicated code in the initialization of JPanels and the ActionListeners 
that could be refactored to reduce duplication and potential errors that may arise. 
