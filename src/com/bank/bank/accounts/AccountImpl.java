package com.bank.bank.accounts;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.bank.databasehelper.DatabaseSelectHelper;
import java.math.RoundingMode;

public abstract class AccountImpl implements Account {

  private int id;
  private String name;
  private BigDecimal balance;
  private int type;
  private BigDecimal interestRate;

  /**
   * Gets the id of the account.
   *
   * @return the id of the account.
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the id of the account.
   *
   * @param id the id to be set.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Gets the name of the account.
   *
   * @return the name of the account.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the account.
   *
   * @param name the name to be set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the balance of the account.
   *
   * @return the balance of the account.
   */
  public BigDecimal getBalance() {
    return balance;
  }

  /**
   * Sets the balance of the account.
   *
   * @param balance the balance to be set.
   */
  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  /**
   * Gets the type of the account.
   *
   * @return the type of the account.
   */
  public int getType() {
    return type;
  }
  
  public void setType(int type) throws SQLException {
      this.type = type;
  }
  
  /**
   * Finds and sets interest rate.
   *
   * @throws SQLException if connection to database was unsuccessful.
   */
  public void findAndSetInterestRate() throws SQLException {
    this.interestRate =
        DatabaseSelectHelper.getInterestRate(DatabaseSelectHelper.getAccountType(this.id));
  }
  
  /**
   * Adds interest and change balance..a
   */
  public void addInterest() throws SQLException {
      this.findAndSetInterestRate();
      BigDecimal interestRate = this.interestRate.add(new BigDecimal("1"));
        BigDecimal oldBalance = DatabaseSelectHelper.getBalance(this.getId());
        BigDecimal newBalance = oldBalance.multiply(interestRate);
        newBalance = newBalance.setScale(2, RoundingMode.HALF_UP);

    
    this.setBalance(newBalance);
  }

}
