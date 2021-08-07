package com.bank.generics;

import java.sql.SQLException;
import java.util.EnumMap;
import java.util.List;

import com.bank.databasehelper.DatabaseSelectHelper;

public class AccountTypesEnumMapFactory {
  
  /**
   * Create an EnumMap for all corresponding AccountType ids in the database
   * and type of account in the enumerator.
   * @param accTypes list of integer of all account types.
   * @param accountTypes EnumMap to hold the account type ids and the actual account type.
   * @return an EnumMap.
   * @throws SQLException if receiving info from database fails.
   */
  public EnumMap<AccountTypes, Integer> getEnumMap(List<Integer> accTypes, EnumMap<AccountTypes, Integer> accountTypes) throws SQLException {
    for (int i = 1; i <= accTypes.size(); i++) {
      if (DatabaseSelectHelper.getAccountTypeName(i).equalsIgnoreCase("CHEQUING")) {
        accountTypes.put(AccountTypes.CHEQUING, i);
      } else if (DatabaseSelectHelper.getAccountTypeName(i).equalsIgnoreCase("SAVINGS")) {
        accountTypes.put(AccountTypes.SAVINGS, i);
      } else if (DatabaseSelectHelper.getAccountTypeName(i).equalsIgnoreCase("TFSA")) {
        accountTypes.put(AccountTypes.TFSA, i);
      } else if (DatabaseSelectHelper.getAccountTypeName(i).equalsIgnoreCase("RESTRICTEDSAVING")) {
        accountTypes.put(AccountTypes.RESTRICTEDSAVING, i);
      } else if (DatabaseSelectHelper.getAccountTypeName(i).equalsIgnoreCase("BALANCEOWING")) {
        accountTypes.put(AccountTypes.BALANCEOWING, i);
      }
    }
    return accountTypes;
  }

}
