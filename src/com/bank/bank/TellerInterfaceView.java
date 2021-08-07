package com.bank.bank;

import com.Messaging.Message;
import com.bank.bank.accounts.Account;
import com.bank.bank.user.Customer;
import com.bank.database.DatabaseInsertException;
import com.bank.databasehelper.DatabaseInsertHelper;
import com.bank.databasehelper.DatabaseSelectHelper;
import com.bank.databasehelper.DatabaseUpdateHelper;
import com.bank.exceptions.ConnectionFailedException;
import com.bank.exceptions.IllegalAmountException;
import com.bank.exceptions.InsufficientFundsException;
import com.bank.generics.AccountTypes;
import com.bank.generics.EnumMapRolesAndAccounts;
import com.bank.generics.Roles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class TellerInterfaceView {
  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
  EnumMapRolesAndAccounts map = new EnumMapRolesAndAccounts();
  
  /**
   * Login a teller and give them access to all the features a teller should
   * have access to.
   * 
   * @throws SQLException if connection fails
   * @throws IOException if IO fails
   * @throws ConnectionFailedException if not authenticated
   * @throws IllegalAmountException if amount of funds is illegal
   * @throws InsufficientFundsException if amount of funds is not enough
   * @throws DatabaseInsertException if insert failed
   */
  public void mainMenu() throws SQLException, IOException, ConnectionFailedException,
        IllegalAmountException, InsufficientFundsException, DatabaseInsertException {
    map.createEnumMap();
    System.out.println("*TELLER INTERFACE*");
    // prompt the user to login to a teller account
    System.out.println("Please enter your user ID:");
    try {
      int userId = Integer.valueOf(br.readLine());
      // check if the account is of type teller
      if (DatabaseSelectHelper.getUserRole(userId) == map.roleIds.get(Roles.TELLER)) {
        System.out.println("Please enter your password:");
        String password = br.readLine();
        if (DatabaseSelectHelper.getUserDetails(userId).authenticate(password)) {
          System.out.println("*AUTHENTICATION SUCCESS*");
          // access granted, create a teller terminal
          TellerTerminal terminal = new TellerTerminal(userId, password);
          boolean exitTeller = false;
          // loop through the teller prompts until an exit command is given
          while (!exitTeller) {
            // prompt the teller to enter in an option
            System.out.println("Please select an option:");
            System.out.println("1 - Authenticate new user");
            System.out.println("2 - Make new user");
            System.out.println("3 - Make new account");
            System.out.println("4 - Give interest");
            System.out.println("5 - Make a deposit");
            System.out.println("6 - Make a withdrawal");
            System.out.println("7 - Check balance");
            System.out.println("8 - Close customer session");
            System.out.println("9 - Display account details");
            System.out.println("10 - Update user info");
            System.out.println("11 - Message Centre");
            System.out.println("12 - Transfer Money");
            System.out.println("13 - Exit");
            System.out.println("Your selection:");
            try {
              int prompt = Integer.valueOf(br.readLine());
              exitTeller = selector(prompt, terminal, 1);
              prompt = 0;
            } catch (Exception e) {
              System.out.println("Please enter an integer from the list.");
            }
          }
        }
      } 
    } catch (Exception e) {
      System.out.println("Invalid ID");
    }
  }

  /**
   * Select the correct functionality for a teller based on input prompt.
   * 
   * @param prompt To select the correct functionality
   * @param terminal The current terminal being worked in
   * @return true if the teller wishes to close the terminal
   * @throws SQLException if connection failed
   * @throws IOException if IO failed 
   * @throws ConnectionFailedException if not authenticated 
   * @throws IllegalAmountException if amount of funds is illegal
   * @throws InsufficientFundsException if not enough funds
   * @throws DatabaseInsertException if insert failed
   */
  private boolean selector(int prompt, TellerTerminal terminal, int menu) throws SQLException,
                                                                                 IOException,
      ConnectionFailedException, IllegalAmountException, InsufficientFundsException,
      DatabaseInsertException {
    if (menu == 1) {
      if (prompt == 1) {
        authenticateUser(terminal);
      } else if (prompt == 2) {
        makeNewUser(terminal);
      } else if (prompt == 3) {
        makeNewAccount(terminal);
      } else if (prompt == 4) {
        giveInterest(terminal);
      } else if (prompt == 5) {
        makeDeposit(terminal);
      } else if (prompt == 6) {
        makeWithdrawal(terminal);
      } else if (prompt == 7) {
        checkBalance(terminal);
      } else if (prompt == 8) {
        closeSession(terminal);
      } else if (prompt == 9) {
        displayAccountDetails(terminal);
      } else if (prompt == 10) {
        updateUserInfo(terminal);
      } else if (prompt == 11) {
        messaging(terminal);
      } else if (prompt == 12) {
        transferMoney(terminal);
      } else if (prompt == 13) {
        return true;
      }
    } else if (menu == 2) {
      if (prompt == 1) {
        viewInbox(terminal);
      } else if (prompt == 2) {
        sendMessage(terminal);
      } else if (prompt == 3) {
        viewOtherMessages(terminal);
      } else if (prompt == 4) {
        return true;
      }
    }
    return false;
  }

  private void transferMoney(TellerTerminal terminal) {
    if (terminal.currentCustomerAuthenticated()) {
      try {
        System.out.println("Enter the Account ID of the account you wish to transfer money with:");
        int customerAccountId = Integer.valueOf(br.readLine());
        
        System.out.println("Enter the Account ID you wish to transfer money to:");
        int accountId = Integer.valueOf(br.readLine());
        terminal.transferMoney(customerAccountId, accountId);
      } catch (Exception e) {
        System.out.println("Please enter a valid account ID");
      }
    } else {
      System.out.println("Please login as a customer");
    }
    
  }

  private void updateUserInfo(TellerTerminal terminal) throws SQLException, IOException {
    if (terminal.currentCustomerAuthenticated()) {
      System.out.println("Please enter your new name or type nothing to keep your current name: ");
      String name = br.readLine();
      System.out.println(
          "Please enter your new password or type nothing to keep your current password: ");
      String password = br.readLine();
      System.out.println(
          "Please enter your new address or type nothing to keep your current address: ");
      String address = br.readLine();
      if (!name.equals("")) {
        try {
          DatabaseUpdateHelper.updateUserName(name, terminal.currentCustomerId());
        } catch (SQLException e) {
          System.out.println("Name not updated.");
        }
      }
      if (!password.equals("")) {
        DatabaseUpdateHelper.updateUserPassword(password, terminal.currentCustomerId());
      }
      if (!address.equals("")) {
        DatabaseUpdateHelper.updateUserAddress(address, terminal.currentCustomerId());
      }
      System.out.println("Update Successful");
    } else {
      System.out.println("Please login a customer.");
    }
  }

  /**
   * Display the account details of every account associated with the current
   * customer.
   * 
   * @param terminal The terminal being operated by the teller
   * @throws SQLException if connection fails
   * @throws ConnectionFailedException if not authenticated
   */
  private void displayAccountDetails(TellerTerminal terminal) throws SQLException,
      ConnectionFailedException {
    if (terminal.currentCustomerAuthenticated()) {
      // display all the accounts type/name/balance
      List<Account> accounts = terminal.listAccounts();
      System.out.println("These are the accounts associated with the current customer:");
      for (Account account : accounts) {
        System.out.println("Type: " + DatabaseSelectHelper.getAccountTypeName(account.getType())
                                    + "/ Name: "
                                    + account.getName()
                                    + " / Balance: $"
                                    + account.getBalance()
                                    + " / ID: "
                                    + account.getId());
      }
    } else {
      System.out.println("Please login a customer.");
    } 
  }

  /**
   * Authenticate the current user for the teller.
   * 
   * @param terminal The terminal being operated by the teller.
   * @throws SQLException if connection fails
   * @throws IOException if IO fails
   * @throws ConnectionFailedException if not authenticated
   * @throws IllegalAmountException if amount of funds is illegal
   * @throws InsufficientFundsException if not enough funds
   * @throws DatabaseInsertException if insert fails
   */
  private void authenticateUser(TellerTerminal terminal) throws SQLException, IOException,
      ConnectionFailedException, IllegalAmountException, InsufficientFundsException,
      DatabaseInsertException {
    map.createEnumMap();  
    // prompt the authentication of the customer with their ID and password
    System.out.println("Please enter customer ID:");
    try {
      int customerId = Integer.valueOf(br.readLine());
      // check if it is a customer
      if (DatabaseSelectHelper.getUserRole(customerId) == map.roleIds.get(Roles.CUSTOMER)) {
        Customer customer =
            (Customer) DatabaseSelectHelper.getUserDetails(customerId);
        terminal.setCurrentCustomer(customer);
        System.out.println("Please enter customer password:");
        String customerPassword = br.readLine();
        terminal.authenticateCurrentCustomer(customerPassword);
        
      } else {
        // if ID is not of a customer's, exit
        System.out
            .println("You entered a " + DatabaseSelectHelper.getRole(customerId));
        System.out.println("Not a valid customer, exiting...");
      }
    } catch (Exception e) {
      System.out.print("Invalid ID");
    }
  }

  /**
   * Make a new customer for the teller.
   * 
   * @param terminal The terminal being operated by the teller.
   * @throws SQLException if connection fails
   * @throws IOException if IO fails
   * @throws ConnectionFailedException if not authenticated
   * @throws IllegalAmountException if amount of funds is illegal
   * @throws InsufficientFundsException if not enough funds
   * @throws DatabaseInsertException if insert failed
   */
  private void makeNewUser(TellerTerminal terminal) throws SQLException, IOException,
      ConnectionFailedException, IllegalAmountException, InsufficientFundsException,
      DatabaseInsertException {
    System.out.println("You selected 2 - Make new user");
    System.out.println("Please enter user's name:");
    String newName = br.readLine();
    System.out.println("Please enter user's age:");
    int newAge = Integer.valueOf(br.readLine());
    System.out.println("Please enter user's address:");
    String newAddress = br.readLine();
    System.out.println("Please enter user's new password:");
    String newPassword = br.readLine();
    // use the terminal to make a new user (customer)
    System.out.println("Created customer account with the following details:");
    terminal.makeNewUser(newName, newAge, newAddress, newPassword);
    System.out.println("Name - " + newName);
    System.out.println("Age - " + newAge);
    System.out.println("Address - " + newAddress);
    System.out.println("Exiting...");
  }

  /**
   * Make a new account for a customer.
   * @param terminal The terminal being operated by the teller.
   * @throws SQLException if connection fails
   * @throws IOException if IO fails
   * @throws ConnectionFailedException if not authenticated
   * @throws IllegalAmountException if amount of funds is illegal
   * @throws InsufficientFundsException if not enough funds
   * @throws DatabaseInsertException if insert fails
   */
  private void makeNewAccount(TellerTerminal terminal) throws SQLException, IOException,
      ConnectionFailedException, IllegalAmountException, InsufficientFundsException,
      DatabaseInsertException {
    if (terminal.currentCustomerAuthenticated()) {
      map.createEnumMap();  
      System.out.println("You selected 3 - Make new account");
      // prompt the teller to create account
      System.out.println("Please enter account name:");
      String newAccountName = br.readLine();
      System.out.println("Please enter account balance:");
      BigDecimal newBalance = new BigDecimal(br.readLine());
      
      boolean validSelection = false;
      // keep prompting the user until a valid selection is made
      while (!validSelection) {
        // choose the type of the account
        System.out.println("Enter type of account...");
        System.out.println("1 - Chequing Account");
        System.out.println("2 - Savings Account");
        System.out.println("3 - TFSA");
        System.out.println("4 - Restricted Savings");
        System.out.println("5 - Balance Owing");
        System.out.println("Please select:");
        int newType = Integer.valueOf(br.readLine());
        if (newType == 1) {
          // make a new chequing account
          if (terminal.makeNewAccount(newAccountName, newBalance,
              map.accountTypes.get(AccountTypes.CHEQUING))) {
            System.out.println("You have made a chequing account");
            validSelection = true;
          }
        } else if (newType == 2) {
          // make a new savings account
          if (terminal.makeNewAccount(newAccountName, newBalance,
              map.accountTypes.get(AccountTypes.SAVINGS))) {
            System.out.println("You have made a savings account");
            validSelection = true;
          }
        } else if (newType == 3) {
          // make a new tfsa
          if (terminal.makeNewAccount(newAccountName, newBalance,
              map.accountTypes.get(AccountTypes.TFSA))) {
            System.out.println("You have made a TFSA account");
            validSelection = true;
          }
        } else if (newType == 4) {
          if (terminal.makeNewAccount(newAccountName, newBalance,
              map.accountTypes.get(AccountTypes.RESTRICTEDSAVING))) {
            System.out.println("You have made a Restricted Savings account");
            validSelection = true;
          }
        } else if (newType == 5) {
          if (terminal.makeNewAccount(newAccountName, newBalance,
              map.accountTypes.get(AccountTypes.BALANCEOWING))) {
            System.out.println("You have made a Balance Owing account");
            validSelection = true;
          }
        } else {
          // prompt again
          System.out.println("Error - not a valid input");
        }
        if (validSelection) {
          System.out.println("Balance - " + newBalance);
          System.out.println("Name - " + newAccountName);
          System.out.println("Successful! Exiting...");
        }
      }
    } else {
      System.out.println("Please login a customer.");
    }
  }

  /**
   * Give interest to the account of a customer.
   * 
   * @param terminal The terminal being operated by the teller.
   * @throws SQLException if connection fails
   * @throws IOException if IO fails
   * @throws ConnectionFailedException if not authenticated
   * @throws IllegalAmountException if amount of funds is illegal
   * @throws InsufficientFundsException if not enough funds
   * @throws DatabaseInsertException if inserting fails.
   */
  private void giveInterest(TellerTerminal terminal) throws SQLException, IOException,
      ConnectionFailedException, IllegalAmountException, InsufficientFundsException,
      DatabaseInsertException {
    if (terminal.currentCustomerAuthenticated()) {
      System.out.println("You selected 4 - Give interest");
      // prompt the user to select the account to give interest to
      System.out.println(
          "Please enter the account ID of the account you wish to give interest to:");
      int accountIdInterest = Integer.valueOf(br.readLine());
      if (terminal.getCustomer().possessAccount(accountIdInterest)) {
        terminal.giveInterest(accountIdInterest);
        System.out.println("New balance: $" + DatabaseSelectHelper.getBalance(accountIdInterest));
        System.out.println("Exiting...");
      } else {
        System.out.println("Account does not belong to customer");
      }
    } else {
      System.out.println("Please login a customer.");
    }
  }

  /**
   * Make a deposit into an account of the current customer.
   * 
   * @param terminal The terminal being operated by the teller.
   * @throws SQLException if connection fails
   * @throws IOException if IO fails
   * @throws ConnectionFailedException if not authenticated
   * @throws IllegalAmountException if amount of funds is illegal
   * @throws InsufficientFundsException if not enough funds
   * @throws DatabaseInsertException if inserting into database fails.
   */
  private void makeDeposit(TellerTerminal terminal) throws SQLException, IOException,
      ConnectionFailedException, IllegalAmountException, InsufficientFundsException,
      DatabaseInsertException {
    if (terminal.currentCustomerAuthenticated()) {
      System.out.println("You selected 5 - Make deposit");
      // prompt the user for the account and amount
      System.out.println(
          "Please enter the account ID of the account you wish to make deposit to:");
      int accountIdDeposit = Integer.valueOf(br.readLine());
      if (terminal.getCustomer().possessAccount(accountIdDeposit)) {
        System.out.println("Please enter the amount you wish to deposit:");
        BigDecimal depositAmount = new BigDecimal(br.readLine());
        terminal.makeDeposit(depositAmount, accountIdDeposit);
        System.out.println("You deposited: $" + depositAmount);
        System.out.println("Your new balance is: $" + terminal.checkBalance(accountIdDeposit));
        
        System.out.println("Exiting...");
      } else {
        System.out.println("Account does not belong to customer");
      }
    } else {
      System.out.println("Please login a customer.");
    }
  }

  /**
   * Withdraw money from an account of a customer.
   * 
   * @param terminal The terminal being operated by the teller.
   * @throws SQLException if connection fails
   * @throws IOException if IO fails
   * @throws ConnectionFailedException if not authenticated
   * @throws IllegalAmountException if amount of funds is illegal
   * @throws InsufficientFundsException if not enough funds
   * @throws DatabaseInsertException if insert into database fails
   */
  private void makeWithdrawal(TellerTerminal terminal) throws SQLException, IOException,
      ConnectionFailedException, IllegalAmountException, InsufficientFundsException,
      DatabaseInsertException {
    if (terminal.currentCustomerAuthenticated()) {
      System.out.println("You selected 6 - Make withdrawal");
      // prompt the user for the account and amount
      System.out.println(
          "Please enter the account ID of the account you wish to make withdrawal from:");
      int accountIdWithdrawal = Integer.valueOf(br.readLine());
      if (terminal.getCustomer().possessAccount(accountIdWithdrawal)) {
        DatabaseSelectHelper.getBalance(accountIdWithdrawal);
        System.out.println("Please enter the amount you wish to withdraw:");
        BigDecimal withdrawalAmount = new BigDecimal(br.readLine());
        terminal.makeWithdrawal(withdrawalAmount, accountIdWithdrawal);
        System.out.println("You withdrew: $" + withdrawalAmount);
        System.out.println("Your new balance is: $" + terminal.checkBalance(accountIdWithdrawal));
        
        System.out.println("Exiting...");
      } else {
        System.out.println("Account does not belong to customer");
      }
    } else {
      System.out.println("Please login a customer.");
    }
  }

  /**
   * Check the balance of an account that belongs to the customer.
   * 
   * @param terminal The terminal being operated by the teller.
   * @throws SQLException if connection fails
   * @throws IOException if IO fails
   * @throws ConnectionFailedException if not authenticated
   * @throws IllegalAmountException if amount of funds is illegal
   * @throws InsufficientFundsException if not enough funds
   */
  private void checkBalance(TellerTerminal terminal) throws SQLException, IOException,
      ConnectionFailedException, IllegalAmountException, InsufficientFundsException {
    if (terminal.currentCustomerAuthenticated()) {
      System.out.println("You selected 7 - Check balance");
      // prompt the user to enter in the account to check its balance
      System.out.println(
          "Please enter the account ID of the account you wish to check the balance of:");
      int accountIdBalance = Integer.valueOf(br.readLine());
      if (terminal.getCustomer().possessAccount(accountIdBalance)) {
        System.out.println("Balance: $" + terminal.checkBalance(accountIdBalance));
        System.out.println("Exiting...");
      } else {
        System.out.println("Account does not belong to customer");
      }
    } else {
      System.out.println("Please login a customer.");
    }
  }
  
  /**
   * Close the session with the customer and log them out.
   * @param terminal The terminal being operated by the teller.
   * @throws SQLException if connection fails
   * @throws IOException if IO fails
   * @throws ConnectionFailedException if not authenticated
   * @throws IllegalAmountException if amount of funds is illegal
   * @throws InsufficientFundsException if not enough funds
   */
  private void closeSession(TellerTerminal terminal) throws SQLException, IOException,
      ConnectionFailedException, IllegalAmountException, InsufficientFundsException {
    if (terminal.currentCustomerAuthenticated()) {
      terminal.deAuthenticateCustomer();
      System.out.println("Customer deauthenticated");
    } else {
      System.out.println("No session was open.");
    }
  }
  
  public void messaging(TellerTerminal terminal) throws NumberFormatException,
      IOException, SQLException, ConnectionFailedException,
      IllegalAmountException, InsufficientFundsException, DatabaseInsertException {
    System.out.println("*MESSAGING CENTRE*");
    boolean exit = false;
    while (!exit) {
      System.out.println("Please select an option:");
      System.out.println("1 - View inbox");
      System.out.println("2 - Create new message");
      System.out.println("3 - View customer inbox");
      System.out.println("4 - Exit");
      int prompt = Integer.valueOf(br.readLine());
      exit = selector(prompt, terminal, 2);
    }
  }
  
  public void viewInbox(TellerTerminal terminal) throws SQLException, IOException {
    System.out.println("Your Inbox:");
    List<Integer> messageIds = new ArrayList<>();
    List<Message> messages = DatabaseSelectHelper.getAllMessages(terminal.currentUserId());
    for (Message message : messages) {
      System.out.println(message);
      messageIds.add(message.getId());
    }
    System.out.println("Please enter in the ID of the message you want to view");
    try {
      int messageId = Integer.valueOf(br.readLine());
      
      if (messageIds.contains(messageId)) {
        Message thisMessage = null;
        for (Message message : messages) {
          if (message.getId() == messageId) {
            thisMessage = message;
                    
          }
        }
        System.out.println("--BEGIN--");
        System.out.println(DatabaseSelectHelper.getSpecificMessage(messageId));
        System.out.println("--END--");
        thisMessage.setViewed();
        DatabaseUpdateHelper.updateUserMessageState(messageId);
      }
    } catch (Exception e) {
      System.out.println("Invalid ID");
    }
      
      
      
      
  }
  
  public void sendMessage(TellerTerminal terminal) throws NumberFormatException, IOException, DatabaseInsertException, SQLException {
    System.out.println("Creating message");
    map.createEnumMap();
    System.out.println("Enter the id of the customer you want to message:");
    try {
      int id = Integer.valueOf(br.readLine());
      if (DatabaseSelectHelper.getUserRole(id) == map.roleIds.get(Roles.CUSTOMER)) {
        System.out.println("Enter your message:");
        String message = br.readLine();
        DatabaseInsertHelper.insertMessage(id, message);
        System.out.println("Your message has been sent.");
      } else {
        System.out.println("Not a valid ID.");
      }
    } catch (NumberFormatException e) {
      System.out.println("Invalid ID.");
    }
  }
  
  public void viewOtherMessages(TellerTerminal terminal) throws NumberFormatException,
      IOException, SQLException {
    System.out.println("Enter a customer ID:");
    try {
      int userId = Integer.valueOf(br.readLine());
  
      map.createEnumMap();
      if (DatabaseSelectHelper.getUserRole(userId) == map.roleIds.get(Roles.CUSTOMER)) {
        List<Message> messages = DatabaseSelectHelper.getAllMessages(userId);
        List<Integer> messageIds = new ArrayList<>();
        for (Message message : messages) {
          System.out.println(message);
          messageIds.add(message.getId());
        }
        System.out.println("Please enter in the ID of the message you want to view");
        int messageId = Integer.valueOf(br.readLine());
  
        if (messageIds.contains(messageId)) {
          System.out.println("--BEGIN--");
          System.out.println(DatabaseSelectHelper.getSpecificMessage(messageId));
          System.out.println("--END--");
          System.out.println("Exiting...");
        }
  
          
      } else {
        System.out.println("Not a customer account");
      }
    } catch (NumberFormatException e) {
      System.out.print("Invalid ID.");
    }
  }
}
