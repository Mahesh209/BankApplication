package com.bank.bank;

import com.Messaging.Message;
import com.Messaging.MessageImpl;
import com.bank.bank.accounts.Account;
import com.bank.bank.accounts.ChequingAccount;
import com.bank.bank.accounts.ChequingAccountImpl;
import com.bank.bank.accounts.RestrictedSavingsAccount;
import com.bank.bank.accounts.RestrictedSavingsAccountImpl;
import com.bank.bank.accounts.SavingsAccount;
import com.bank.bank.accounts.SavingsAccountImpl;
import com.bank.bank.accounts.TFSA;
import com.bank.bank.accounts.TFSAImpl;
import com.bank.bank.user.Customer;
import com.bank.bank.user.Teller;
import com.bank.database.DatabaseInsertException;
import com.bank.databasehelper.DatabaseInsertHelper;
import com.bank.databasehelper.DatabaseSelectHelper;
import com.bank.databasehelper.DatabaseUpdateHelper;
import com.bank.exceptions.ConnectionFailedException;
import com.bank.generics.AccountTypes;
import com.bank.generics.EnumMapRolesAndAccounts;
import com.bank.generics.Roles;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

public class TellerTerminal extends ATM {
  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

  private Teller currentUser;
  private boolean currentUserAuthenticated;
  private Customer currentCustomer;
  private boolean currentCustomerAuthenticated;

  /**
   * Constructor for TellerTerminal, authorizes a user and customer.
   *
   * @param tellerId the ID of the teller.
   * @param password the password inputed for teller account.
   * @throws SQLException if connection fails.
   */
  public TellerTerminal(int tellerId, String password) throws SQLException,
         DatabaseInsertException {
    super(-1);
    this.currentUser = (Teller) DatabaseSelectHelper.getUserDetails(tellerId);
    this.currentUserAuthenticated = this.currentUser.authenticate(password);
  }

  /**
   * Makes a new account if current customer and user are authenticated.
   *
   * @param name the name of the account.
   * @param balance the balance of the account.
   * @param type the integer representation of the type.
   * @return true if the account creation is successful, false otherwise.
   * @throws SQLException if the connection fails
   * @throws ConnectionFailedException if auth busyorize fails.
   */
  public boolean makeNewAccount(String name, BigDecimal balance, int type)
      throws SQLException, ConnectionFailedException, DatabaseInsertException {
    if (currentCustomerAuthenticated && currentUserAuthenticated) {
      int id = DatabaseInsertHelper.insertAccount(name, balance, type);
      EnumMapRolesAndAccounts map = new EnumMapRolesAndAccounts();
      map.createEnumMap();
      // find out what account type the account is and create and add that account to the customer
      if (type == map.accountTypes.get(AccountTypes.CHEQUING)) {
        ChequingAccount chequing = new ChequingAccountImpl(id, name, balance);
        currentCustomer.addAccount(chequing);
        DatabaseInsertHelper.insertUserAccount(currentCustomer.getId(), id);
        System.out.println("Account ID - " + id);
        return true;
      } else if (type == map.accountTypes.get(AccountTypes.SAVINGS)) {
        SavingsAccount savings = new SavingsAccountImpl(id, name, balance);
        currentCustomer.addAccount(savings);
        DatabaseInsertHelper.insertUserAccount(currentCustomer.getId(), id);
        System.out.println("Account ID - " + id);
        return true;
      } else if (type == map.accountTypes.get(AccountTypes.TFSA)) {
        TFSA tfsa = new TFSAImpl(id, name, balance);
        currentCustomer.addAccount(tfsa);
        DatabaseInsertHelper.insertUserAccount(currentCustomer.getId(), id);
        System.out.println("Account ID - " + id);
        return true;
      } else if (type == map.accountTypes.get(AccountTypes.RESTRICTEDSAVING)) {
        RestrictedSavingsAccount rsavings = new RestrictedSavingsAccountImpl(id, name, balance);
        currentCustomer.addAccount(rsavings);
        DatabaseInsertHelper.insertUserAccount(currentCustomer.getId(), id);
        System.out.println("Account ID - " + id);
        return true;
      }
      return false;
    }
    System.out.println("reached here");
    throw new ConnectionFailedException();
  }

  /**
   * Sets the current customer.
   *
   * @param customer the customer to be set.
   */
  public void setCurrentCustomer(Customer customer) {
    this.currentCustomer = customer;
  }

  /**
   * Authenticates the current customer.
   *
   * @param password the password to be inputted for the current customer.
   * @throws SQLException if the connection fails.
   */
  public void authenticateCurrentCustomer(String password) throws SQLException,
        DatabaseInsertException {
    this.currentCustomerAuthenticated = this.currentCustomer.authenticate(password);
    if (authenticate(this.currentCustomer.getId(), password)) {
      System.out.println("*AUTHENTICATION SUCCESSFUL*");
    } else {
      System.out.println("*AUTHENTICATION FAILED*");
    }
  }

  /**
   * Makes a new user if the current user is authenticated.
   *
   * @param name the name of the new user.
   * @param age the age of the new user.
   * @param address the address of the new user.
   * @param password the password of the new user.
   * @throws SQLException if the connection fails.
   * @throws ConnectionFailedException if authorization fails.
   */
  public void makeNewUser(String name, int age, String address, String password)
      throws SQLException, ConnectionFailedException, DatabaseInsertException {
    if (this.currentUserAuthenticated) {
      // find the role ID that corresponds to customer
      EnumMapRolesAndAccounts map = new EnumMapRolesAndAccounts();
      map.createEnumMap();
      int roleId = map.roleIds.get(Roles.CUSTOMER);
      int userId = DatabaseInsertHelper.insertNewUser(name, age, address, roleId, password);
      System.out.println("User ID - " + userId);
      System.out.println("succesfully made user");
    } else {
      System.out.println("failed making user");
      throw new ConnectionFailedException();
    }
  }

  /**
   * Gives interest to the account based on its account ID if current user and customer are
   * authenticated and the account belongs to the customer.
   *
   * @param accountId the account ID to be given interest.
   * @throws SQLException if the connection fails.
   * @throws ConnectionFailedException if authorization fails.
   * @throws DatabaseInsertException if failed to insert to database.
   */
  public void giveInterest(int accountId) throws SQLException,
                                                 ConnectionFailedException,
                                                 DatabaseInsertException {
    if (this.currentUserAuthenticated && this.currentCustomerAuthenticated) {
      // find if the account belongs to the user
      //List<Integer> accountIds = DatabaseSelectHelper.getAccountIds(this.currentUser.getId());
      boolean contains = false;
      int i = 0;
      List<Account> customerAccountList = this.currentCustomer.getAccounts();
      for (Account e : customerAccountList) {
        if (e.getId() == accountId) {
          contains = true;
          break;
        }
        i++;
      }
        
      if (contains) {
        // find out which account type it is
        // add interest to the account and update the balance
        // update database
        int role = DatabaseSelectHelper.getAccountType(accountId);
        BigDecimal interestRate = new BigDecimal("0");
        // calculate balance after interest rate
        interestRate = DatabaseSelectHelper.getInterestRate(role).add(new BigDecimal("1"));
        BigDecimal oldBalance = DatabaseSelectHelper.getBalance(accountId);
        BigDecimal newBalance = oldBalance.multiply(interestRate);
        newBalance = newBalance.setScale(2, RoundingMode.HALF_UP);
        DatabaseSelectHelper.getAccountDetails(accountId).addInterest();
        DatabaseUpdateHelper.updateAccountBalance(newBalance, accountId);
        
        this.currentCustomer.getAccounts().get(i).setBalance(newBalance);
        
        String msg = "An interest rate has been added to your account: $" + interestRate;
        int msgId = DatabaseInsertHelper.insertMessage(currentCustomer.getId(), msg);
        
        Message message = new MessageImpl(msgId, msg, false);
        currentCustomer.getMessagingCentre().addMessage(message);
        
      }
    }
  }

  /**
   * Sets the current customer to be null and current customer authenticated to be false.
   */
  public void deAuthenticateCustomer() {
    this.currentCustomerAuthenticated = false;
    this.currentCustomer = null;
  }
  
  @Override
  /**
   * Gets the list of accounts of the current customer.
   *
   * @return the list of accounts of the current customer.
   * @throws ConnectionFailedException if the authentication fails
   * @throws SQLException if connection fails 
   */
  public List<Account> listAccounts() throws ConnectionFailedException, SQLException {
    if (currentCustomerAuthenticated) {
      this.currentCustomer.updateAccounts();
      return this.currentCustomer.getAccounts();
    } else {
      throw new ConnectionFailedException();
    }
  }
  
  public boolean currentCustomerAuthenticated() {
    return this.currentCustomerAuthenticated;  
  }
  
  /**
   * gets the current customer id.
   * @return current customer id.
   */
  public int currentCustomerId() {
    if (this.currentCustomerAuthenticated()) {
      return this.currentCustomer.getId();
    }
    return 0;
  }
  
  public int currentUserId() {
    return this.currentUser.getId();
  }
  
  /**
   * transfers money from one account to another specified by their IDs.
   * @param customerAccountId the account id that will be transferring.
   * @param accountId the account id that will be receiving.
   * @throws SQLException if querying database goes wrong.
   */
  public void transferMoney(int customerAccountId, int accountId) throws SQLException {
    // check if the customer has this account
    boolean customerOwnsAccount = false;
    for (int i = 0; i < this.currentCustomer.getAccounts().size(); i++) {
      if (customerAccountId == this.currentCustomer.getAccounts().get(i).getId()) {
        customerOwnsAccount = true;
        break;
      }
    }
    // it is safe to proceed since the customer accounts have been authenticated
    if (customerOwnsAccount) {
      Account customerAccount = DatabaseSelectHelper.getAccountDetails(customerAccountId);
      // try and catch for valid input (int) expected
      try {
        System.out.println("Enter amount you wish to transfer:");
        // take user input as a big decimal
        BigDecimal bigAmount = new BigDecimal(br.readLine());
        if (bigAmount.compareTo(customerAccount.getBalance()) <= 0) {
          // try to update accounts
          try {
            // update the account which received money in database
            DatabaseUpdateHelper.updateAccountBalance(
                bigAmount.add(DatabaseSelectHelper.getBalance(accountId)), accountId);
            // update account which transfered money in database
            DatabaseUpdateHelper.updateAccountBalance(
                customerAccount.getBalance().subtract(bigAmount), customerAccountId);
            // update the customer object for printing reasons for other options
            this.currentCustomer.updateAccounts();
            // Explicitly tell user money is transfered
            System.out.println("you have successfully transfered money to account " + accountId);
            
          } catch (Exception e) {
            System.out.println("The account ID you wish to transfer money to does not exist");
          }
          // error printing
        } else {
          System.out.println("This account does not have enough money to send this amount");
        }
        
      } catch (Exception e) {
        System.out.println("Invalid amount");
      }
      // error printing
    } else {
      System.out.println("You do not have access to this account");
    }
    
  }
}
