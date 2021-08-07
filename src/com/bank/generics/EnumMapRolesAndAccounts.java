package com.bank.generics;

import com.bank.databasehelper.DatabaseSelectHelper;

import java.sql.SQLException;
import java.util.EnumMap;

public class EnumMapRolesAndAccounts {
  public EnumMap<Roles, Integer> roleIds = new EnumMap<Roles, Integer>(Roles.class);
  public EnumMap<AccountTypes, Integer> accountTypes = new EnumMap<AccountTypes, Integer>(AccountTypes.class);
  
  /**
   * Fill in the enumMap for both fields (account and roles).
   * @throws SQLException if there is an error when getting info from database.
   */
  public void createEnumMap() throws SQLException {
    // Create objects of the factory, so we can use their methods
    RolesEnumMapFactory rolesEnumMapFactory = new RolesEnumMapFactory();
    AccountTypesEnumMapFactory accountEnumMapFactory = new AccountTypesEnumMapFactory();
    
    // Using the respective factory methods to create a corresponding EnumMap
    // from whats in the database.
    this.roleIds = rolesEnumMapFactory.getMap(DatabaseSelectHelper.getRoles(), roleIds);
    this.accountTypes = accountEnumMapFactory.getEnumMap(
        DatabaseSelectHelper.getAccountTypesIds(),accountTypes);
  
  }
  
  /**
   * Update The current EnumMaps for both roleIds and accountTypes.
   * @throws SQLException if receiving info from database fails.
   */
  public void updateEnumMap() throws SQLException {
    this.createEnumMap();
  }

}