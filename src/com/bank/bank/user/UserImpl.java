package com.bank.bank.user;

import com.bank.bank.accounts.Account;
import com.bank.database.DatabaseInsertException;
import com.bank.databasehelper.DatabaseSelectHelper;
import com.bank.security.PasswordHelpers;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class UserImpl {

  private int id;
  private String name;
  private int age;
  private String address;
  private int roleId;
  private boolean authenticated;
  private List<Account> accounts = new ArrayList<>();

  /**
   * Gets the ID of the user.
   *
   * @return the ID of the user.
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the ID of the user.
   *
   * @param id the ID to be set.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Gets the name of the user.
   *
   * @return the name of the user.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the user.
   *
   * @param name the name to be set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the age of the user.
   *
   * @return the name of the user.
   */
  public int getAge() {
    return age;
  }

  /**
   * Sets the age of the user.
   *
   * @param age the age to be set.
   */
  public void setAge(int age) {
    this.age = age;
  }

  /**
   * Gets the role ID of the user.
   *
   * @return the role ID of the user.
   */
  public int getRoleId() {
    return roleId;
  }
  
  public void setAddress(String address) {
      this.address = address;
  }
  
  public void setRoleId(int roleId){
      this.roleId = roleId;
  }
  
  public String getAddress(){
        return this.address;
  }
  
  public List<Account> getAccounts(){
      return this.accounts;
  }
  
  public void setAccounts(List<Account> account) throws SQLException{
      for(int i : DatabaseSelectHelper.getAccountIds(id)){
          this.addAccount(DatabaseSelectHelper.getAccountDetails(i));
      }
  }
  
  /**
   * Adds an account
   * 
   * @param account the account to be added.
   */
  public void addAccount(Account account) throws SQLException {
    this.accounts.add(account);
  }
  
  /**
   * Checks whether password inputted is authorized or not.
   *
   * @param password the password entered.
   * @return whether the password matches the one in the database or not.
   * @throws SQLException if the connection is unsuccessful.
   */
 
  public final boolean authenticate(String password) throws SQLException {
    String dataBasePassword = DatabaseSelectHelper.getPassword(this.getId());
    this.authenticated = PasswordHelpers.comparePassword(dataBasePassword, password);
    return this.authenticated;
  }
  
  public void updateAccounts() throws SQLException {
     this.accounts.clear();
     this.setAccounts(this.accounts);
  }

}
