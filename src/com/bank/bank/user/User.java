package com.bank.bank.user;

import com.bank.bank.accounts.Account;
import com.bank.database.DatabaseInsertException;
import java.sql.SQLException;

import com.bank.databasehelper.DatabaseSelectHelper;
import com.bank.security.PasswordHelpers;
import java.util.List;

public interface User {

  /**
   * Gets the ID of the user.
   */
  public int getId();

  /**
   * Sets the ID of the user.
   *
   * @param id the ID to be set.
   */
  public void setId(int id);

  /**
   * Gets the name of the user.
   */
  public String getName();

  /**
   * Sets the name of the user.
   *
   * @param name the name to be set.
   */
  public void setName(String name);

  /**
   * Gets the age of the user.
   */
  public int getAge();

  /**
   * Sets the age of the user.
   *
   * @param age the age to be set.
   */
  public void setAge(int age);

  /**
   * Gets the role ID of the user.
   */
  public int getRoleId();

  /**
   * Checks whether password inputed is authorized or not.
   *
   * @param password the password entered.
   * @throws SQLException if the connection is unsuccessful.
   */
  
  public String getAddress();
  
  public List<Account> getAccounts() throws SQLException;
  
  public void addAccount(Account account) throws SQLException;
  
  public void setAccounts(List<Account> account) throws SQLException;
  
  public boolean authenticate(String password) throws SQLException;
  
  public void updateAccounts() throws SQLException;
}
