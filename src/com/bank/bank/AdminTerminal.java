package com.bank.bank;

import com.Messaging.Message;
import com.Messaging.MessageImpl;
import com.bank.bank.user.Admin;
import com.bank.bank.user.AdminImpl;
import com.bank.bank.user.Customer;
import com.bank.bank.user.CustomerImpl;
import com.bank.bank.user.User;
import com.bank.database.DatabaseInsertException;
import com.bank.databasehelper.DatabaseInsertHelper;
import com.bank.databasehelper.DatabaseSelectHelper;
import com.bank.exceptions.ConnectionFailedException;
import com.bank.generics.EnumMapRolesAndAccounts;
import com.bank.generics.Roles;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminTerminal {

  private boolean currentAdminAuthenticated;
  private Admin currentAdmin;

  /**
   * Constructor for AdminTerminal.
   *
   * @param adminId the admin's ID
   * @param password the admin's password
   * @throws SQLException if the connection fails.
   * @throws DatabaseInsertException if inserting account fails.
   */
  public AdminTerminal(int adminId, String password) throws SQLException, 
      DatabaseInsertException {
    this.currentAdmin = (Admin) DatabaseSelectHelper.getUserDetails(adminId);
    this.currentAdminAuthenticated = this.currentAdmin.authenticate(password);
  }

  /**
   * Creates an admin account.
   *
   * @param name the name of the account
   * @param address the address of the account
   * @param age the age of the account
   * @param password the password of the account
   * @return the ID of the account.
   * @throws SQLException if the connection fails.
   * @throws ConnectionFailedException if not authenticated.
   * @throws DatabaseInsertException if database insertion failed
   */
  public int adminAccountCreate(String name, String address, int age, String password) 
      throws SQLException, ConnectionFailedException, DatabaseInsertException {
    if (currentAdminAuthenticated) {
      // get the role ID of admin
      EnumMapRolesAndAccounts map = new EnumMapRolesAndAccounts();
      map.createEnumMap();
      int roleId = map.roleIds.get(Roles.ADMIN);
      // make a new admin
      int id = DatabaseInsertHelper.insertNewUser(name, age, address, roleId, password);
     
      return id;
    }
    throw new ConnectionFailedException();
  }
  
  /**
   * Creates a teller account.
   *
   * @param name the name of the account
   * @param address the address of the account
   * @param age the age of the account
   * @param password the password of the account
   * @return the ID of the account.
   * @throws SQLException if the connection fails.
   * @throws ConnectionFailedException if not authenticated.
   * @throws DatabaseInsertException if insertion failed
   */
  public int tellerAccountCreate(String name, String address, int age, String password) 
      throws SQLException, ConnectionFailedException, DatabaseInsertException {
    if (currentAdminAuthenticated) {
      // get the role ID of admin
      EnumMapRolesAndAccounts map = new EnumMapRolesAndAccounts();
      map.createEnumMap();
      int roleId = map.roleIds.get(Roles.TELLER);
      // make a new teller
      int id = DatabaseInsertHelper.insertNewUser(name, age, address, roleId, password);
      return id;
    }
    throw new ConnectionFailedException();
  }

  /**
   * Authenticates admin account.
   *
   * @param adminId the id for this admin
   * @param password the password for this admin
   * @return whether authentication is successful or not.
   * @throws SQLException if the connection fails.
   * @throws DatabaseInsertException if inserting account fails.
   */
  public boolean adminAccountAuthenticate(int adminId, String password) throws
          DatabaseInsertException, SQLException {
    this.currentAdmin = (Admin) DatabaseSelectHelper.getUserDetails(adminId);
    this.currentAdminAuthenticated = this.currentAdmin.authenticate(password);
    return this.currentAdminAuthenticated;
  }

  /**
   * Views all admins.
   *
   * @return the list of all admins.
   * @throws SQLException if connection is unsuccessful.
   */
  public List<Integer> viewAllAdmins() throws SQLException, DatabaseInsertException {
    if (currentAdminAuthenticated) {
      EnumMapRolesAndAccounts map = new EnumMapRolesAndAccounts();
      map.createEnumMap();
      List<Integer> adminIds = new ArrayList<Integer>();
      int index = 1;
      while (DatabaseSelectHelper.getUserDetails(index) != null) {
        if (DatabaseSelectHelper.getUserRole(index) == map.roleIds.get(Roles.ADMIN)) {
          adminIds.add(index);
        }
        index++;
      }
      return adminIds;
          
    }
    return null;
  }
  
  /**
   * Views all tellers.
   *
   * @return the list of all tellers.
   * @throws SQLException if connection is unsuccessful.
   */
  public List<Integer> viewAllTellers() throws SQLException, DatabaseInsertException {
    if (currentAdminAuthenticated) {
      EnumMapRolesAndAccounts map = new EnumMapRolesAndAccounts();
      map.createEnumMap();
      List<Integer> tellerIds = new ArrayList<Integer>();
      int index = 1;
      while (DatabaseSelectHelper.getUserDetails(index) != null) {
        if (DatabaseSelectHelper.getUserRole(index) == map.roleIds.get(Roles.TELLER)) {
          tellerIds.add(index);
        }
        index++;
      }
      return tellerIds;
          
    }
    return null;
  }
  
  /**
   * Views all customers.
   *
   * @return the list of all customers.
   * @throws SQLException if connection is unsuccessful.
   */
  public List<Integer> viewAllCustomers() throws SQLException, DatabaseInsertException {
    if (currentAdminAuthenticated) {
      EnumMapRolesAndAccounts map = new EnumMapRolesAndAccounts();
      map.createEnumMap();
      List<Integer> customerIds = new ArrayList<Integer>();
      int index = 1;
      while (DatabaseSelectHelper.getUserDetails(index) != null) {
        if (DatabaseSelectHelper.getUserRole(index) == map.roleIds.get(Roles.CUSTOMER)) {
          customerIds.add(index);
        }
        index++;
      }
      return customerIds;
        
    }
    return null;
  }
  
  public Admin getCurrentAdmin() {
    return this.currentAdmin;
  }
}