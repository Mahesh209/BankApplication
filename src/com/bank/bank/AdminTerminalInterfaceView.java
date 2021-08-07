package com.bank.bank;

import com.Messaging.Message;
import com.bank.bank.accounts.Account;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminTerminalInterfaceView {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    EnumMapRolesAndAccounts map = new EnumMapRolesAndAccounts();

    /**
     * Main menu for the admin, allowing admin to access all other admin
     * functions.
     *
     * @throws SQLException if the connection fails
     * @throws ConnectionFailedException if not authenticated
     * @throws DatabaseInsertException if insert fails
     * @throws IOException if IO error occurs
     * @throws IllegalAmountException if amount is too much or too little
     * @throws InsufficientFundsException if not enough funds
     */
    public void mainMenu() throws SQLException, ConnectionFailedException,
            DatabaseInsertException, IOException, IllegalAmountException,
            InsufficientFundsException {
        map.createEnumMap();
        System.out.println("*ADMIN MODE*");
        int userId = -1;
        boolean adminAuthenticated = false;
        try {
          while (!adminAuthenticated) {
              // prompt the user with login instructions for admin
              System.out.println("Please enter your user ID:");
              userId = Integer.valueOf(br.readLine());
              // verify that the account is of type admin
              if (DatabaseSelectHelper.getUserRole(userId) == map.roleIds.get(Roles.ADMIN)) {
                  System.out.println("Please enter your password:");
                  String password = br.readLine();
                  if (DatabaseSelectHelper.getUserDetails(userId).authenticate(password)) {
                      System.out.println("*AUTHENTICATION SUCCESS*");
                      // authentication success, prompt admin
                      AdminTerminal terminal = new AdminTerminal(userId, password);
                      terminal.adminAccountAuthenticate(userId, password);
                      boolean exitAdminTerminal = false;
                      int prompt = 0;
                      // loop through admin terminal until exit condition
                      while (!exitAdminTerminal) {
                          // prompt the user for a selection choice
                          try{
                          System.out.println("Please select an option:");
                          System.out.println("1 - Make new admin account");
                          System.out.println("2 - View all admin accounts");
                          System.out.println("3 - View all teller accounts");
                          System.out.println("4 - View all customer accounts");
                          System.out.println("5 - Make new teller account");
                          System.out.println("6 - View money owed.");
                          System.out.println("7 - View total money held by the bank");
                          System.out.println("8 - Promote teller to admin");
                          System.out.println("9 - Message Centre");
                          System.out.println("10 - Exit");
                          System.out.println("Your selection:");
                          
                          prompt = Integer.valueOf(br.readLine());
                          exitAdminTerminal = selector(prompt, terminal, 1);
                          }
                          catch(Exception e){
                              System.out.println("Please enter a valid input");
                          }
                      }
                      adminAuthenticated = exitAdminTerminal;
                  } else {
                      System.out.println("Authentication error, try again...");
                  }
              } else {
                  System.out.println("Not an admin account, try again");
              }
          }
        } catch (Exception e) {
          System.out.println("Invalid ID"); 
        }

    }

    /**
     * Method to determine what action to perform, based on user input.
     *
     * @param prompt the value of the user input
     * @param terminal this terminal
     * @return true
     * @throws SQLException if the connection fails
     * @throws IOException if IO fails
     * @throws ConnectionFailedException if not authenticated
     * @throws IllegalAmountException if illegal amount
     * @throws InsufficientFundsException if not enough funds in account
     * @throws DatabaseInsertException if insert fails
     */
    private boolean selector(int prompt, AdminTerminal terminal, int menu) throws SQLException, IOException,
            ConnectionFailedException, IllegalAmountException, InsufficientFundsException,
            DatabaseInsertException {
        if (menu == 1) {
            if (prompt == 1) {
                createAccount(terminal);
            } else if (prompt == 2) {
                viewAdmin(terminal);
            } else if (prompt == 3) {
                viewTeller(terminal);
            } else if (prompt == 4) {
                viewCustomer(terminal);
            } else if (prompt == 5) {
                createTellerAccount(terminal);
            } else if (prompt == 6) {
                findTotalAmountOwed(terminal);
            } else if (prompt == 7) {
                totalMoney(terminal);
            } else if (prompt == 8) {
                promoteTeller(terminal);
            } else if (prompt == 9) {
                messaging(terminal);
            } else if (prompt == 10) {
                return true;
            } else {
                System.out.println("Invalid selection");
            }
            return false;
        } else {
            if (prompt == 1) {
                viewInbox(terminal);
            } else if (prompt == 2) {
                sendMessage(terminal);
            } else if (prompt == 3) {
                viewOtherMessages(terminal);
            } else if (prompt == 4) {
                return true;
            } else {
                System.out.println("Invalid selection");
            }
            return false;
        }
    }

    private void promoteTeller(AdminTerminal terminal) throws IOException, SQLException, DatabaseInsertException {
        System.out.println("Please enter the id of the Teller you wish to promote:");
        int id = Integer.valueOf(br.readLine());
        DatabaseUpdateHelper.updateUserRole(map.roleIds.get(Roles.ADMIN), id);
        DatabaseInsertHelper.insertMessage(id, "You have been promoted to Admin");
        System.out.println("The teller " + id + " has been promoted.");
    }

    private void totalMoney(AdminTerminal terminal) throws SQLException, DatabaseInsertException {
        List<Integer> customerIds = terminal.viewAllCustomers();
        BigDecimal amount = new BigDecimal("0");
        EnumMapRolesAndAccounts map = new EnumMapRolesAndAccounts();
        map.createEnumMap();
        for (Integer i : customerIds) {
            Account curr = DatabaseSelectHelper.getAccountDetails(i);
            if (curr.getType() == map.accountTypes.get(AccountTypes.BALANCEOWING)) {
                amount.subtract(curr.getBalance());
            } else {
                amount.add(curr.getBalance());
            }
        }
        System.out.println("The total amount of money held is " + amount.toString());
    }

    /**
     * The total amount owed by Balance Owing accounts.
     *
     * @param terminal The current terminal being opperated by the Admin.
     * @throws SQLException
     * @throws DatabaseInsertException
     */
    private void findTotalAmountOwed(AdminTerminal terminal) throws SQLException, DatabaseInsertException {
        List<Integer> customerIds = terminal.viewAllCustomers();
        BigDecimal amount = new BigDecimal("0");
        EnumMapRolesAndAccounts map = new EnumMapRolesAndAccounts();
        map.createEnumMap();
        for (Integer i : customerIds) {
            Account curr = DatabaseSelectHelper.getAccountDetails(i);
            if (curr.getType() == map.accountTypes.get(AccountTypes.BALANCEOWING)) {
                amount.add(curr.getBalance());
            }
        }
        System.out.println("The total amount of money owed is " + amount.toString());
    }

    /**
     * Method to create an admin account.
     *
     * @param terminal this terminal
     * @throws IOException if IO fails
     * @throws SQLException if connection fails
     * @throws ConnectionFailedException if not authenticated
     * @throws InsufficientFundsException if not enough funds
     * @throws IllegalAmountException if amount of funds is illegal
     * @throws DatabaseInsertException if insert fails
     */
    public void createAccount(AdminTerminal terminal) throws IOException, SQLException,
            ConnectionFailedException, InsufficientFundsException, IllegalAmountException,
            DatabaseInsertException {
        System.out.println("*CREATING ADMINISTRATOR ACCOUNT*");
        // prompt user for name, age, address, and password
        System.out.println("Please enter the name:");
        String name = br.readLine();
        System.out.println("Please enter the age:");
        int age = Integer.valueOf(br.readLine());
        System.out.println("Please enter the address:");
        String address = br.readLine();
        System.out.println("Set a new password:");
        String password = br.readLine();
        // get admin role ID
        int userId = terminal.adminAccountCreate(name, address, age, password);
        System.out.println("Created admin account with the following details:");
        System.out.println("Name - " + name);
        System.out.println("Age - " + age);
        System.out.println("Address - " + address);
        System.out.println("User ID - " + userId);
        DatabaseInsertHelper.insertMessage(userId, "Welcome to your account!");
    }

    /**
     * Method to print admins info in database.
     *
     * @param terminal this terminal
     * @throws SQLException if connection fails
     * @throws DatabaseInsertException if insert fails
     */
    public void viewAdmin(AdminTerminal terminal) throws SQLException, DatabaseInsertException {
        List<Integer> admins = terminal.viewAllAdmins();
        for (Integer e : admins) {
            System.out.println("AdminID: " + e.toString() + ", Name: "
                    + DatabaseSelectHelper.getUserDetails(e.intValue()).getName());
        }
        System.out.println("A total of " + admins.size() + " admin accounts.");
        System.out.println("Exiting...");
    }

    /**
     * Method to print teller information in database.
     *
     * @param terminal this terminal
     * @throws SQLException if connection fails
     * @throws DatabaseInsertException if insert fails
     */
    public void viewTeller(AdminTerminal terminal) throws SQLException, DatabaseInsertException {
        List<Integer> tellers = terminal.viewAllTellers();
        for (Integer e : tellers) {
            System.out.println("TellerID: " + e.toString() + ", Name: "
                    + DatabaseSelectHelper.getUserDetails(e.intValue()).getName());
        }
        System.out.println("A total of " + tellers.size() + " teller accounts.");
        System.out.println("Exiting...");
    }

    /**
     * Method to print customer information in database.
     *
     * @param terminal this terminal
     * @throws SQLException if connection fails
     * @throws DatabaseInsertException if insert fails
     */
    public void viewCustomer(AdminTerminal terminal) throws SQLException, DatabaseInsertException {
        List<Integer> customers = terminal.viewAllCustomers();
        for (Integer e : customers) {
            System.out.println("CustomerID: " + e.toString() + ", Name: "
                    + DatabaseSelectHelper.getUserDetails(e.intValue()).getName());
        }
        System.out.println("A total of " + customers.size() + " customer accounts.");
        System.out.println("Exiting...");
    }

    /**
     * Method to create a new teller.
     *
     * @param terminal this terminal
     * @throws IOException if IO fails
     * @throws SQLException if connection fails
     * @throws ConnectionFailedException if not authenticated
     * @throws DatabaseInsertException if insert fails
     */
    public void createTellerAccount(AdminTerminal terminal) throws IOException, SQLException,
            ConnectionFailedException, DatabaseInsertException {
        System.out.println("*CREATING TELLER ACCOUNT*");
        // prompt user for name, age, address, and password
        System.out.println("Please enter the name:");
        String name = br.readLine();
        System.out.println("Please enter the age:");
        int age = Integer.valueOf(br.readLine());
        System.out.println("Please enter the address:");
        String address = br.readLine();
        System.out.println("Set a new password:");
        String password = br.readLine();
        // get teller role ID 
        int userId = terminal.tellerAccountCreate(name, address, age, password);
        System.out.println("Created admin account with the following details:");
        System.out.println("Name - " + name);
        System.out.println("Age - " + age);
        System.out.println("Address - " + address);
        System.out.println("User ID - " + userId);
        DatabaseInsertHelper.insertMessage(userId, "Welcome to your account!");
    }

    public void messaging(AdminTerminal terminal) throws NumberFormatException,
            IOException, SQLException, ConnectionFailedException,
            IllegalAmountException, InsufficientFundsException, DatabaseInsertException {
        
        boolean exit = false;
        while (!exit) {
            try{
            System.out.println("*MESSAGING CENTRE*");
            System.out.println("Please select an option:");
            System.out.println("1 - View inbox");
            System.out.println("2 - Create new message");
            System.out.println("3 - View customer or teller inbox");
            System.out.println("4 - Exit");
            
            int prompt = Integer.valueOf(br.readLine());
            exit = selector(prompt, terminal, 2);
            }
            catch(Exception e){
                System.out.println("Please enter a valid input");
            }
        }
    }

    public void viewInbox(AdminTerminal terminal) throws SQLException, IOException {
        System.out.println("Your Inbox:");
        List<Integer> messageIds = new ArrayList<>();
        List<Message> messages = DatabaseSelectHelper.getAllMessages(terminal.getCurrentAdmin().getId());
        for (Message message : messages) {
            System.out.println(message);
            messageIds.add(message.getId());
        }
        System.out.println("Please enter in the ID of the message you want to view");
        try{
        int messageId = Integer.valueOf(br.readLine());
        
        if (messageIds.contains(messageId)) {
            Message thisMessage = null;
            for (Message message : messages) {
                if(message.getId() == messageId){
                    thisMessage = message;
                    
                }
            }
            System.out.println("--BEGIN--");
            System.out.println(DatabaseSelectHelper.getSpecificMessage(messageId));
            System.out.println("--END--");
            
            thisMessage.toggleViewed();
            DatabaseUpdateHelper.updateUserMessageState(messageId);
             
        }
        }catch(Exception e){
            System.out.println("Please enter a valid input");
        }

    }

    public void sendMessage(AdminTerminal terminal) throws NumberFormatException, IOException, DatabaseInsertException, SQLException {
        System.out.println("Creating message");
        System.out.println("Please enter the id of the user you want to message:");
        int id = Integer.valueOf(br.readLine());
        System.out.println("Enter your message:");
        String message = br.readLine();
        DatabaseInsertHelper.insertMessage(id, message);
        System.out.println("Your message has been sent.");
    }

    public void viewOtherMessages(AdminTerminal terminal) throws NumberFormatException,
            IOException, SQLException {
        System.out.print("Enter a user id:");
        int userId = Integer.valueOf(br.readLine());

        map.createEnumMap();
        if (DatabaseSelectHelper.getUserRole(userId) == map.roleIds.get(Roles.TELLER) || DatabaseSelectHelper.getUserRole(userId) == map.roleIds.get(Roles.TELLER)) {
            List<Message> messages = DatabaseSelectHelper.getAllMessages(userId);
            List<Integer> messageIds = new ArrayList<>();
            for (Message message : messages) {
                System.out.println(message);
                messageIds.add(message.getId());
            }
            System.out.println("Please enter in the ID of the message you want to view");
            try{
            int messageId = Integer.valueOf(br.readLine());

            if (messageIds.contains(messageId)) {
                Message thisMessage = null;
            for (Message message : messages) {
                if(message.getId() == messageId){
                    thisMessage = message;
                    
                }
            }
            System.out.println("--BEGIN--");
            System.out.println(DatabaseSelectHelper.getSpecificMessage(messageId));
            System.out.println("--END--");
            if(!thisMessage.getViewed()){
                System.out.println("Toggle view status?");
                System.out.println("1 - Toggle");
                System.out.println("2 - Don't toggle");
                try{
                int changeViewed = Integer.valueOf(br.readLine());
                if(changeViewed == 1){
                    thisMessage.toggleViewed();
                    DatabaseUpdateHelper.updateUserMessageState(messageId);
                    System.out.println("Exiting...");
                }
                else if(changeViewed == 2){
                    System.out.println("Exiting...");
                }
                else{
                    throw new IOException("Please enter a valid input.");
                }
                } catch(Exception e){
                    System.out.println("Please enter a valid input.");
                }
            }else{
                System.out.println("Exiting...");
            }
            }
            } catch(Exception e){
                 System.out.println("Invalid ID");
            }
            

        
        } else {
            System.out.println("Not a customer or teller account");
        }
    }
}
