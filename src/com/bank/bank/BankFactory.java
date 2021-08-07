package com.bank.bank;

import com.bank.database.DatabaseInsertException;
import com.bank.databasehelper.DatabaseInsertHelper;
import com.bank.databasehelper.DatabaseSelectHelper;
import com.bank.exceptions.ConnectionFailedException;
import com.bank.exceptions.IllegalAmountException;
import com.bank.exceptions.InsufficientFundsException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

/** 
 * A class that makes Bank objects.
 * @author group 0742
 *
 */
public class BankFactory {

  /**
   * Method to decide what action to perform and calls the corresponding function.
   * @param num number corresponding to an action to perform
   * @throws SQLException if connection fails
   * @throws IOException if IO fails
   * @throws ConnectionFailedException if not authenticated
   * @throws IllegalAmountException if amount of funds is illegal
   * @throws InsufficientFundsException if not enough funds
   * @throws DatabaseInsertException idf insert fails
   */
  public void initialWhatToDo(int num) throws SQLException, IOException, ConnectionFailedException,
        IllegalAmountException, InsufficientFundsException, DatabaseInsertException {
    if (num == -1) {
      adminAccountCreate();
    } else if (num == 1) {
      adminAccountAuthenticate();
    } else {
      goToUserMenu();
    }
  }
  
  /**
   * Method to create an admin.
   * @throws SQLException if connection fails
   * @throws IOException if IO fails
   * @throws ConnectionFailedException if not authenticated
   * @throws IllegalAmountException if amount is illegal
   * @throws InsufficientFundsException if not enough funds
   * @throws DatabaseInsertException if insert fails
   */
  private void adminAccountCreate() throws SQLException, IOException, ConnectionFailedException,
      IllegalAmountException, InsufficientFundsException, DatabaseInsertException {
    // initialize buffered reader for input
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("*CREATING ADMINISTRATOR ACCOUNT*");
    // prompt user for name, age, address, and password
    System.out.println("Please enter your name:");
    String name = br.readLine();
    System.out.println("Please enter your age:");
    int age = Integer.valueOf(br.readLine());
    System.out.println("Please enter your address:");
    String address = br.readLine();
    int adminId = 0; 
    List<Integer> ints = DatabaseSelectHelper.getRoles();
    for (Integer ids : ints) {
      String admin = DatabaseSelectHelper.getRole(ids.intValue());
      if (admin.equals("ADMIN")) {
        adminId = ids.intValue(); 
      }
    }
    System.out.println("Set a new password:");
    String password = br.readLine();
    // insert the user into the database
    int userId = DatabaseInsertHelper.insertNewUser(name, age, address, adminId, password);
    // print a verify message
    System.out.println("Created admin account with the following details:");
    System.out.println("Name - " + name);
    System.out.println("Age - " + age);
    System.out.println("Address - " + address);
    System.out.println("User ID - " + userId);
  }
  
  private void adminAccountAuthenticate() throws SQLException, IOException,
       ConnectionFailedException, IllegalAmountException, InsufficientFundsException,
       DatabaseInsertException {
    // initialize buffered reader for input
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    System.out.println("*ADMIN MODE*");
    int userId = -1;
    int adminId = 1;
    List<Integer> ints = DatabaseSelectHelper.getRoles();
    for (Integer ids : ints) {
      String admin = DatabaseSelectHelper.getRole(ids.intValue());
      if (admin.equals("ADMIN")) {
        adminId = ids.intValue(); 
      }
    }
    boolean tellerAuthenticated = false;
    while (!tellerAuthenticated) {
      // prompt the user with login instructions for admin
      System.out.println("Please enter your user ID:");
      userId = Integer.valueOf(br.readLine());
        
      // }
        
      // verify that the account is of type admin
      if (DatabaseSelectHelper.getUserRole(userId) == adminId) {

        System.out.println("Please enter your password:");
        String password = br.readLine();
        if (DatabaseSelectHelper.getUserDetails(userId).authenticate(password)) {
          System.out.println("*AUTHENTICATION SUCCESS*");
          tellerAuthenticated = true;
          tellerAccountCreate();
        } else {
          System.out.println("Authentication error, try again...");
        }
      } else {
        System.out.println("Not an admin account, try again");
      }
    }
  }
    
  private void tellerAccountCreate() throws SQLException, IOException,
      ConnectionFailedException, IllegalAmountException, InsufficientFundsException,
      DatabaseInsertException {
    // initialize buffered reader for input
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
 
    // access granted, prompt the admin to be able to create teller accounts
    System.out.println("*CREATING TELLER ACCOUNT*");
    // prompt admin for name, age, address, and password
    System.out.println("Please enter teller name:");
    String tellerName = br.readLine();
    System.out.println("Please enter teller age:");
    int tellerAge = Integer.valueOf(br.readLine());
    System.out.println("Please enter teller address:");
    String tellerAddress = br.readLine();
    int tellerId = 0;
    List<Integer> ints = DatabaseSelectHelper.getRoles();
    for (Integer ids : ints) {
      String teller = DatabaseSelectHelper.getRole(ids.intValue());
      if (teller.equals("TELLER")) {
        tellerId = ids.intValue(); 
      }
    }
    System.out.println("Set a new password:");
    String tellerPassword = br.readLine();
    // insert the user into the database
    int newTellerId = DatabaseInsertHelper.insertNewUser(tellerName, tellerAge, tellerAddress,
        tellerId, tellerPassword);
    // print a verify message
    System.out.println("Created teller account with the following details:");
    System.out.println("Name - " + tellerName);
    System.out.println("Age - " + tellerAge);
    System.out.println("Address - " + tellerAddress);
    System.out.println("User ID - " + newTellerId);
  }

  private void goToUserMenu() throws SQLException, IOException,
      ConnectionFailedException, IllegalAmountException, InsufficientFundsException,
      DatabaseInsertException {
    // initialize buffered reader for input
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    
    //keep prompting user until they type in the exit command
    boolean exit = false;
    while (!exit) {
      // prompt the user for selecting between teller terminal,
      // atm, and exiting
      System.out.println("WELCOME!");
      System.out.println("Please select an option:");
      System.out.println("1 - TELLER Interface");
      System.out.println("2 - ATM Interface");
      System.out.println("3 - ADMIN Interface");
      System.out.println("0 - Exit");
      System.out.println("Enter Selection:");
      try {
        int userInput = Integer.valueOf(br.readLine());
        // figure out where to go
        exit = selector(userInput);
      } catch (Exception e) {
        System.out.println("Please enter a valid input.");
      }
    }
  }
  
  private boolean selector(int userInput) throws SQLException, IOException,
      ConnectionFailedException, IllegalAmountException, InsufficientFundsException,
      DatabaseInsertException {
    if (userInput == 1) {
      TellerInterfaceView terminalView = new TellerInterfaceView();
      terminalView.mainMenu();
    } else if (userInput == 2) {
      ATMInterfaceView atmView = new ATMInterfaceView();
      atmView.mainMenu();
    } else if (userInput == 3) {
      AdminTerminalInterfaceView adminView = new AdminTerminalInterfaceView();
      adminView.mainMenu();
    } else if (userInput == 0) {
      System.out.println("Exiting...");
      return true;
    } else {
      System.out.println("Please try again:");
    }
    return false;
  }
  
  
}
