package com.bank.bank.accounts;

import com.bank.databasehelper.DatabaseSelectHelper;
import com.bank.generics.AccountTypes;
import com.bank.generics.EnumMapRolesAndAccounts;

import java.math.BigDecimal;
import java.sql.SQLException;

public class TFSAImpl extends SavingsAccountImpl implements TFSA {

  private int id;
  private String name;
  private BigDecimal balance;
  private int type;
  private BigDecimal interestRate;

  /**
   * Constructor for TFSA.
   *
   * @param id the id of the account.
   * @param name the name of the account.
   * @param balance the balance of the account.
   */
  public TFSAImpl(int id, String name, BigDecimal balance) throws SQLException {
    super(id, name, balance);
    EnumMapRolesAndAccounts map = new EnumMapRolesAndAccounts();
    map.createEnumMap();
    this.setType(map.accountTypes.get(AccountTypes.TFSA));
  }
}
