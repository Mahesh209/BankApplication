package com.bank.bank.user;

import com.Messaging.CustomerMessagingCentre;
import com.Messaging.Message;
import com.bank.bank.accounts.Account;
import com.bank.database.DatabaseInsertException;
import com.bank.databasehelper.DatabaseInsertHelper;
import com.bank.databasehelper.DatabaseSelectHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerImpl extends UserImpl implements Customer {

  private int id;
  private String name;
  private int age;
  private String address;
  private int roleId;
  private boolean authenticated;
  private CustomerMessagingCentre custMsgCentre = new CustomerMessagingCentre();
  

  /**
   * Constructor for Customer without authentica
10 - Update user info
11 - Exittion.
   *
   * @param id the id of the user.
   * @param name the name of the customer.
   * @param age the age of the customer.
   * @param address the address of the customer.
   * @param roleId the role ID of the customer.
   */
  public CustomerImpl(int id, String name, int age, String address, int roleId)
      throws DatabaseInsertException, SQLException {
    this.setId(id);
    this.setName(name);
    this.setAge(age);
    this.setAddress(address);
    this.setRoleId(roleId);
    List<Account> accounts = new ArrayList<>();
    this.setMessages();
    this.setAccounts(accounts);
  }

  /**
   * Constructor for Customer with authentication.
   *
   * @param id the id of the user.
   * @param name the name of the customer.
   * @param age the age of the customer.
   * @param address the address of the customer.
   * @param roleId the role ID of the customer.
   * @param authenticated if the authentication is successful.
   */
  public CustomerImpl(
      int id, String name, int age, String address, int roleId, boolean authenticated)
          throws DatabaseInsertException, SQLException {
    //this.setMessages();
    this.setId(id);
    this.setName(name);
    this.setAge(age);
    this.setAddress(address);
    this.setRoleId(roleId);
    List<Account> accounts = new ArrayList<>();
    this.setAccounts(accounts);
    this.authenticated = authenticated;
    this.setAccounts(accounts);
  }

  public boolean possessAccount(int accountId){
      List<Integer> accountIds = new ArrayList<>();
    for(Account e : this.getAccounts()){
        accountIds.add(e.getId());
    }
    if(accountIds.contains(accountId)){
        return true;
    }
    return false;
  }
  
  private void setMessages() throws SQLException {
    List<Message> allMsgs = DatabaseSelectHelper.getAllMessages(this.getId());
    try {
      for (Message message : allMsgs) {
        custMsgCentre.addMessage(message);
      }
    } catch (NullPointerException e) {}
  }
  
  public CustomerMessagingCentre getMessagingCentre() {
    return this.custMsgCentre;
  }
}
