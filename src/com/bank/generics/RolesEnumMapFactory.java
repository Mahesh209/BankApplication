package com.bank.generics;

import java.sql.SQLException;
import java.util.EnumMap;
import java.util.List;

import com.bank.databasehelper.DatabaseSelectHelper;

public class RolesEnumMapFactory {
  
  /**
   * Create a EnumMap of all the corresponding role value in the database
   * and the role in the enumerator.
   * @param roles integer list of all roles in the database.
   * @param roleId an EnumMap to hold the corresponding values.
   * @return an EnumMap.
   * @throws SQLException if receiving info from database fails.
   */
  public EnumMap<Roles, Integer> getMap(List<Integer> roles, EnumMap<Roles, Integer> roleId) throws SQLException {
    // Loop through all Roles
    
    for (int i = 1; i <= roles.size(); i++) {
      if (DatabaseSelectHelper.getRole(i).equalsIgnoreCase("ADMIN")) {
        roleId.put(Roles.ADMIN, new Integer(i));
      } else if (DatabaseSelectHelper.getRole(i).equalsIgnoreCase("TELLER")) {
        roleId.put(Roles.TELLER, new Integer(i));
      } else if (DatabaseSelectHelper.getRole(i).equalsIgnoreCase("CUSTOMER")) {
        roleId.put(Roles.CUSTOMER, new Integer(i));
      }
    }
    return roleId;
  }

}
