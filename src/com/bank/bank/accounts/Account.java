package com.bank.bank.accounts;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface Account {
  
  /**
   * Gets the id of the account.
   *
   * @return the id of the account.
   */
  public int getId();
  
  /**
   * Gets the name of the account.
   *
   * @return the name of the account.
   */
  public String getName();
  
  /**
   * Gets the balance of the account.
   *
   * @return the balance of the account.
   */
  public BigDecimal getBalance();
  
  /**
   * Sets the balance of the account.
   *
   * @param balance the balance to be set.
   */
  public void setBalance(BigDecimal balance);
  
  /**
   * Gets the type of the account.
   *
   * @return the type of the account.
   */
  public int getType() throws SQLException;
  
  /**
   * Finds and sets interest rate.
   *
   * @throws SQLException if connection to database was unsuccessful.
   */
  public void findAndSetInterestRate() throws SQLException;
  
  /**
   * Adds interest and change balance.
   */
  public void addInterest() throws SQLException;

}
