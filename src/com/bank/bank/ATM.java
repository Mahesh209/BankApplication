package com.bank.bank;

import com.Messaging.Message;
import com.Messaging.MessageImpl;
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

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * A class to represent an ATM.
 * @author group 0742
 *
 */
public class ATM {

  private Customer currentCustomer;
  private boolean authenticated;

  /**
   * Constructor for ATM without authentication.
   *
   * @param customerId the customer ID.
   * @throws SQLException if the connection is unsuccessful.
   */
  public ATM(int customerId) throws SQLException, DatabaseInsertException {
    this.currentCustomer = (Customer) DatabaseSelectHelper.getUserDetails(customerId);
  }

  /**
   * Constructor for ATM with authentication.
   *
   * @param customerId the customer ID.
   * @param password the password inputted.
   * @throws SQLException if connection is unsuccessful.
   */
  public ATM(int customerId, String password) throws SQLException, DatabaseInsertException {
    this.currentCustomer = (Customer) DatabaseSelectHelper.getUserDetails(customerId);
    this.authenticated = this.currentCustomer.authenticate(password);
    if (this.authenticated) {
      //log them in and display
      display();
    }
  }



  /**
   * Authenticates the customer given their ID and password.
   *
   * @param userId the user ID.
   * @param password the inputted password.
   * @return whether the authentication is successful or not.
   * @throws SQLException if the connection is unsuccessful.
   */
  public boolean authenticate(int userId, String password) throws SQLException,
        DatabaseInsertException {
    this.currentCustomer = (Customer) DatabaseSelectHelper.getUserDetails(userId);
    this.authenticated = this.currentCustomer.authenticate(password);
    return this.authenticated;
  }

  /**
   * Gets the list of accounts of the current customer.
   *
   * @return the list of accounts of the current customer.
   * @throws ConnectionFailedException if the authentication fails
   * @throws SQLException if connection fails 
   */
  public List<Account> listAccounts() throws ConnectionFailedException, SQLException {
    if (authenticated) {
      return this.currentCustomer.getAccounts();
    } else {
      throw new ConnectionFailedException();
    }
  }

  /**
   * Makes a deposit and updates balance if authenticated.
   *
   * @param amount the amount to be deposited.
   * @param accountId the account ID.
   * @return if the deposit was successful or not.
   * @throws ConnectionFailedException if the authentication fails.
   * @throws SQLException if the connection fails.
   * @throws IllegalAmountException if the amount entered is negative.
   */
  public boolean makeDeposit(BigDecimal amount, int accountId)
      throws ConnectionFailedException, SQLException, IllegalAmountException, DatabaseInsertException {
    if (this.authenticated) {
      if (amount.doubleValue() < 0) {
        throw new IllegalAmountException();
      }
      BigDecimal currentBalance = this.checkBalance(accountId);
      BigDecimal desiredBalance = currentBalance.add(amount);
      String msg = "Your account received a deposit: $" + amount;
        int msgId = DatabaseInsertHelper.insertMessage(currentCustomer.getId(), msg);
        
        Message message = new MessageImpl(msgId, msg, false);
        currentCustomer.getMessagingCentre().addMessage(message);
      return DatabaseUpdateHelper.updateAccountBalance(desiredBalance, accountId);
    } else {
      throw new ConnectionFailedException();
    }
  }

  /**
   * Checks the balance of the account.
   *
   * @param accountId the account ID.
   * @return the balance of the account.
   * @throws SQLException if the connection fails.
   * @throws ConnectionFailedException if the authentication fails.
   */
  public BigDecimal checkBalance(int accountId) throws SQLException, ConnectionFailedException {
    if (this.authenticated) {
      return DatabaseSelectHelper.getBalance(accountId);
    } else {
      throw new ConnectionFailedException();
    }
  }

  /**
   * Makes a withdrawal from the account based on its account ID.
   *
   * @param amount the amount to be deposited.
   * @param accountId the account ID.
   * @return if the withdrawal is successful or not.
   * @throws SQLException if the connection fails.
   * @throws ConnectionFailedException if the authentication fails.
   * @throws InsufficientFundsException if withdrawal amount is greater than balance.
   * @throws IllegalAmountException if negative amount is being withdrawn or the
   * balance will be too low.
   */
  public boolean makeWithdrawal(BigDecimal amount, int accountId) throws SQLException,
      ConnectionFailedException, InsufficientFundsException, IllegalAmountException, DatabaseInsertException {
    EnumMapRolesAndAccounts map = new EnumMapRolesAndAccounts();
    map.createEnumMap();
    if (this.authenticated) {
      if (amount.doubleValue() < 0) {
        throw new IllegalAmountException();
      }
      // calculate the new balance by subtracting the amount from the current balance
      BigDecimal currentBalance = this.checkBalance(accountId);
      BigDecimal desiredBalance = currentBalance.subtract(amount);
      String msg = "A withdrawal has been made on your account: $" + amount;
        int msgId = DatabaseInsertHelper.insertMessage(currentCustomer.getId(), msg);
        
        Message message = new MessageImpl(msgId, msg, false);
        currentCustomer.getMessagingCentre().addMessage(message);
      try {
        if (desiredBalance.compareTo(new BigDecimal("1000.00")) == -1
            && DatabaseSelectHelper.getAccountType(accountId) == map.accountTypes.get(AccountTypes.SAVINGS)) {
          throw new InsufficientFundsException();
        }
        if (desiredBalance.doubleValue() < 0) {
          throw new InsufficientFundsException();
        }
      } catch (InsufficientFundsException e) {
        return false;
      }
      return DatabaseUpdateHelper.updateAccountBalance(desiredBalance, accountId);
    } else {
      throw new ConnectionFailedException();
    }
  }
  
  /**
   * Method to print this customer's info.
   * @throws SQLException if connection fails
   * @throws DatabaseInsertException 
   */
  public void display() throws SQLException, DatabaseInsertException {
    if (this.authenticated) {
      System.out.println("Name: " + this.currentCustomer.getName());
      System.out.println("Address: " + this.currentCustomer.getAddress());
      Customer userInfo = (Customer) DatabaseSelectHelper.getUserDetails(currentCustomer.getId());
      for (int accountnum = 0; accountnum < userInfo.getAccounts().size(); accountnum++) {
        System.out.println("Account " + accountnum
                                      + ": "  + userInfo.getAccounts().get(accountnum).getName());
        // print the type of the account
        System.out.println("\t Type: " + DatabaseSelectHelper
                                                            .getAccountTypeName(DatabaseSelectHelper
                                                            .getAccountType(userInfo
                                                            .getAccounts()
                                                            .get(accountnum)
                                                            .getId())));
        // print the balance on the account
        System.out.println("\t Balance: " + "$" + userInfo.getAccounts().get(accountnum).getBalance());
      }
    }
  }
  
  public Customer getCustomer() {
    return currentCustomer;
  }

}
