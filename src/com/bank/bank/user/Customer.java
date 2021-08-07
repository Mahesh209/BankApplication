package com.bank.bank.user;

import java.sql.SQLException;
import java.util.List;

import com.Messaging.CustomerMessagingCentre;
import com.bank.bank.accounts.Account;
import com.bank.database.DatabaseInsertException;

public interface Customer extends User {

  public CustomerMessagingCentre getMessagingCentre();
  
  public boolean possessAccount(int accountId);

  
}
