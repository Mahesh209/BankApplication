package com.bank.bank;

import com.Messaging.Message;
import com.bank.bank.accounts.Account;
import com.bank.bank.user.Customer;
import com.bank.database.DatabaseInsertException;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The user interface for the ATM.
 * @author group 0742
 *
 */
public class ATMInterfaceView {
  //initialize buffered reader for input
  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
  EnumMapRolesAndAccounts map = new EnumMapRolesAndAccounts();
  
  /**
   * Method to display the main menu options.
   * @throws NumberFormatException if number format is incorrect
   * @throws IOException if IO fails
   * @throws SQLException if connection fails
   * @throws ConnectionFailedException if not authenticated
   * @throws IllegalAmountException if amount of funds is illegal
   * @throws InsufficientFundsException if amount of funds is insufficient
   * @throws DatabaseInsertException if insert fails
   */
  public void mainMenu() throws NumberFormatException,
                                                      IOException,
                                                      SQLException,
                                                      ConnectionFailedException,
                                                      IllegalAmountException,
                                                      InsufficientFundsException,
                                                      DatabaseInsertException {
    System.out.println("*ATM INTERFACE*");
    int userId = -1;
    String password = null;
    boolean authenticated = false;
    map.createEnumMap();
    try {
      // prompt the user for password authentication, until success
      while (!authenticated) {
        System.out.println("Please enter your ID:");
        userId = Integer.valueOf(br.readLine());
          
        if (DatabaseSelectHelper.getUserRole(userId) == map.roleIds.get(Roles.CUSTOMER)) {
          System.out.println("Please enter your password:");
          password = br.readLine();
          
          if (DatabaseSelectHelper.getUserDetails(userId).authenticate(password)) {
            authenticated = true;
          } else {
            System.out.println("Authentication failure...incorrect ID or password");
          }
        } else {
          System.out.println("Please enter a customer id.");
        }
      }
      if (authenticated) {
        // create new atm object
        System.out.println("Authentication successful!");
        boolean exitATM = false;
        ATM atm = new ATM(userId, password);
        int prompt = 0;
        // loop through atm until exit condition
        while (!exitATM) {
          // prompt the user for a selection choice
          System.out.println("Please select an option:");
          System.out.println("1 - List all accounts + balances");
          System.out.println("2 - Make deposit");
          System.out.println("3 - Check balance");
          System.out.println("4 - Make withdrawal");
          System.out.println("5 - View inbox");
          System.out.println("6 - Exit");
          System.out.println("Your selection:");
          prompt = Integer.valueOf(br.readLine());
          exitATM = selector(prompt, atm);
        }
      }
    } catch (SQLException e) {
      System.out.println("Invalid ID.");
    }
  }
  
  private boolean selector(int prompt, ATM atm) throws SQLException, IOException,
      ConnectionFailedException, IllegalAmountException, InsufficientFundsException {
    if (prompt == 1) {
      listAllAccounts(atm);
    } else if (prompt == 2) {
      makeDeposit(atm);
    } else if (prompt == 3) {
      checkBalance(atm);
    } else if (prompt == 4) {
      makeWithdrawal(atm);
    } else if (prompt == 5) {
      viewInbox(atm);
    } else if (prompt == 6) {
      return true;
    } else {
      System.out.println("Invalid selection.");
    }
    return false;
  }

  private void listAllAccounts(ATM atm) throws ConnectionFailedException,
      SQLException {
    // list all accounts and balances
    System.out.println("You selected 1 - List all accounts + balances");
    // list all of the accounts with their appropriate balances
    List<Account> listOfAccounts = atm.listAccounts();
      
    for (Account e : listOfAccounts) {
      System.out.println("Account ID: " + e.getId() + " / Name: " + e.getName() + " - $" + e.getBalance());
      
    }
    System.out.println("Exiting...");
  }
  
  private void makeDeposit(ATM atm) throws NumberFormatException, IOException,
      ConnectionFailedException, SQLException, IllegalAmountException {
    // make a deposit
    System.out.println("You selected 2 - Make deposit");
    // prompt the user for their account ID and amount
    System.out.println("Please enter your account ID:");
    try {
      int accountId = Integer.valueOf(br.readLine());
      if (atm.getCustomer().possessAccount(accountId)) {
        System.out.println("Please enter the amount you wish to deposit:");
        BigDecimal amount = new BigDecimal(br.readLine());
        atm.makeDeposit(amount, accountId);
        System.out.println("You deposited: $" + amount);
        System.out.println("Your new balance is: $" + atm.checkBalance(accountId));
      } else {
        System.out.println("This is not your account.");
      }
      System.out.println("Exiting...");
    } catch (Exception e) {
    System.out.println("Invalid amount");
    }
  } 
  
  private void checkBalance(ATM atm) throws SQLException, ConnectionFailedException,
      NumberFormatException, IOException {
    // check the balance
    System.out.println("You selected 3 - Check balance");
    // prompt the user to enter their account ID to return the
    // balance associated with that account
    System.out.println("Please enter your account ID:");
    try {
      int accountId = Integer.valueOf(br.readLine());
      
      if (atm.getCustomer().possessAccount(accountId)) {
        System.out.println("Your account balance is $" + atm.checkBalance(accountId));
      } else {
        System.out.println("This is not your account.");
      }
      System.out.println("Exiting...");
    } catch (Exception e) {
      System.out.println("Invalid ID");
    }
  }
  
  private void makeWithdrawal(ATM atm) throws SQLException, ConnectionFailedException,
      IOException, InsufficientFundsException, IllegalAmountException {
    map.createEnumMap();  
    // make withdrawal
    System.out.println("You selected 4 - Make withdrawal");
    // prompt the user for their account ID and amount to withdrawSystem.out.println("*MESSAGES*");
    System.out.println("Please enter your account ID:");
    try {
      int accountId = Integer.valueOf(br.readLine());
      if (atm.getCustomer().possessAccount(accountId)) {
        if (DatabaseSelectHelper.getAccountDetails(accountId).getType()
            != map.accountTypes.get(AccountTypes.RESTRICTEDSAVING)) {
          System.out.println("Please enter the amount you wish to withdraw:");
          BigDecimal amount = new BigDecimal(br.readLine());
          if (atm.makeWithdrawal(amount, accountId)) {
            System.out.println("You withdrew: $" + amount);
            System.out.println("Your new balance is: $" + atm.checkBalance(accountId));
          } else {
            System.out.println("Insufficient Funds.");
          }
        } else {
          System.out.println("Only tellers may withdraw from a Restricted Savings Account.");
        }
      } else {
        System.out.println("This is not your account.");
      }
      System.out.println("Exiting...");
    } catch (Exception e) {
      System.out.println("Invalid ID");
    }
  }
  
  private void viewInbox(ATM atm) throws SQLException, IOException {
    System.out.println("Your Inbox:");
    List<Integer> messageIds = new ArrayList<>();
    List<Message> messages = DatabaseSelectHelper.getAllMessages(atm.getCustomer().getId());
    for (Message message : messages) {
      System.out.println(message);
      messageIds.add(message.getId());
    }
      System.out.println("Please enter in the ID of the message you want to view");
      try {
        int messageId = Integer.valueOf(br.readLine());
        
        if(messageIds.contains(messageId)){
          Message thisMessage = null;
            for (Message message : messages) {
                if(message.getId() == messageId){
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
  
}
