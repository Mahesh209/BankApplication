package com.bank.bank.accounts;

import com.bank.databasehelper.DatabaseSelectHelper;
import com.bank.generics.AccountTypes;
import com.bank.generics.EnumMapRolesAndAccounts;

import java.math.BigDecimal;
import java.sql.SQLException;

public class SavingsAccountImpl extends AccountImpl implements SavingsAccount {

  private int id;
  private String name;
  private BigDecimal balance;
  private int type;
  private BigDecimal interestRate;

  /**
   * Constructor for SavingsAccount.
   *
   * @param id the id of the account.
   * @param name the name of the account.
   * @param balance the balance of the account.
   */
  public SavingsAccountImpl(int id, String name, BigDecimal balance) throws SQLException {
    this.setId(id);
    this.setName(name);
    this.setBalance(balance);
    EnumMapRolesAndAccounts map = new EnumMapRolesAndAccounts();
    map.createEnumMap();
    this.setType(map.accountTypes.get(AccountTypes.SAVINGS));
  }
}
