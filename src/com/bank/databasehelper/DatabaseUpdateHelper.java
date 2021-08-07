package com.bank.databasehelper;

import com.bank.database.DatabaseUpdater;
import com.bank.generics.AccountTypes;
import com.bank.generics.Roles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseUpdateHelper extends DatabaseUpdater {

  /**
   * Updates the role name of the user based on their ID.
   *
   * @param name the desired name.
   * @param id the ID of the user.
   * @return true if successful, false otherwise.
   * @throws SQLException if connection is unsuccessful.
   */
  public static boolean updateRoleName(String name, int id) throws SQLException {
    boolean cdt1 = Checker.checkValidRolesName(name);
    boolean cdt2 = Checker.checkValidUsersId(id);
    if (cdt1 && cdt2) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      boolean complete = DatabaseUpdater.updateRoleName(name, id, connection);
      connection.close();
      return complete;
    }
    return false;
  }

  /**
   * Updates the user name of the user based on their ID.
   *
   * @param name the desired name.
   * @param id the ID of the user.
   * @return true if successful, false otherwise.
   * @throws SQLException if connection is unsuccessful.
   */
  public static boolean updateUserName(String name, int id) throws SQLException {
    boolean cdt1 = Checker.checkValidUsersName(name);
    boolean cdt2 = Checker.checkValidUsersId(id);
    if (cdt1 && cdt2) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      boolean complete = DatabaseUpdater.updateUserName(name, id, connection);
      connection.close();
      return complete;
    }
    return false;
  }

  /**
   * Updates the age of the user based on their ID.
   *
   * @param age the desired age.
   * @param id the ID of the user.
   * @return true if successful, false otherwise.
   * @throws SQLException if connection is unsuccessful.
   */
  public static boolean updateUserAge(int age, int id) throws SQLException {
    boolean cdt1 = Checker.checkValidUsersAge(age);
    boolean cdt2 = Checker.checkValidUsersId(id);
    if (cdt1 && cdt2) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      boolean complete = DatabaseUpdater.updateUserAge(age, id, connection);
      connection.close();
      return complete;
    }
    return false;
  }

  /**
   * Updates the role of the user based on their ID.
   *
   * @param roleId the desired role in integer format.
   * @param id the ID of the user.
   * @return true if successful, false otherwise.
   * @throws SQLException if connection is unsuccessful.
   */
  public static boolean updateUserRole(int roleId, int id) throws SQLException {
    boolean cdt1 = Checker.checkValidRolesId(roleId);
    boolean cdt2 = Checker.checkValidUsersId(id);
    if (cdt1 && cdt2) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      boolean complete = DatabaseUpdater.updateUserRole(roleId, id, connection);
      connection.close();
      return complete;
    }
    return false;
  }

  /**
   * Updates the address of the user based on their ID.
   *
   * @param address the desired address.
   * @param id the ID of the user.
   * @return true if successful, false otherwise.
   * @throws SQLException if connection is unsuccessful.
   */
  public static boolean updateUserAddress(String address, int id) throws SQLException {
    boolean cdt1 = Checker.checkValidUsersAddress(address);
    boolean cdt2 = Checker.checkValidUsersId(id);
    if (cdt1 && cdt2) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      boolean complete = DatabaseUpdater.updateUserAddress(address, id, connection);
      connection.close();
      return complete;
    }
    return false;
  }

  /**
   * Updates the account name of the user based on the account ID.
   *
   * @param name the desired name.
   * @param id the ID of the account.
   * @return true if successful, false otherwise.
   * @throws SQLException if connection is unsuccessful.
   */
  public static boolean updateAccountName(String name, int id) throws SQLException {
    boolean cdt1 = Checker.checkValidAccountsName(name);
    boolean cdt2 = Checker.checkValidAccountsId(id);
    if (cdt1 && cdt2) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      boolean complete = DatabaseUpdater.updateAccountName(name, id, connection);
      connection.close();
      return complete;
    }
    return false;
  }

  /**
   * Updates the balance of the account based on the account ID.
   *
   * @param balance the desired balance.
   * @param id the ID of the account.
   * @return true if successful, false otherwise.
   * @throws SQLException if connection is unsuccessful.
   */
  public static boolean updateAccountBalance(BigDecimal balance, int id) throws SQLException {
    boolean cdt1 = Checker.checkValidAccountsBalance(balance);
    boolean cdt2 = Checker.checkValidAccountsId(id);
    balance.setScale(2, RoundingMode.HALF_UP);
    if (cdt1 && cdt2) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      boolean complete = DatabaseUpdater.updateAccountBalance(balance, id, connection);
      connection.close();
      return complete;
    }
    return false;
  }

  /**
   * Updates the account type of the account based on the account ID.
   *
   * @param typeId the integer representation of the account type.
   * @param id the ID of the account.
   * @return true if successful, false otherwise.
   * @throws SQLException if connection is unsuccessful.
   */
  public static boolean updateAccountType(int typeId, int id) throws SQLException {
    boolean cdt1 = Checker.checkValidAccountTypesId(typeId);
    boolean cdt2 = Checker.checkValidAccountsId(id);
    if (cdt1 && cdt2) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      boolean complete = DatabaseUpdater.updateAccountType(typeId, id, connection);
      connection.close();
      return complete;
    }
    return false;
  }

  /**
   * Updates the name of the account type based on the account type ID.
   *
   * @param name the desired name.
   * @param id the ID of the account type.
   * @return true if successful, false otherwise.
   * @throws SQLException if connection is unsuccessful.
   */
  public static boolean updateAccountTypeName(String name, int id) throws SQLException {
    boolean cdt1 = Checker.checkValidAccountTypesName(name);
    boolean cdt2 = Checker.checkValidAccountTypesId(id);
    if (cdt1 && cdt2) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      boolean complete = DatabaseUpdater.updateAccountTypeName(name, id, connection);
      connection.close();
      return complete;
    }
    return false;
  }

  /**
   * Updates the interest rate of the account type.
   *
   * @param interestRate the desired interest rate.
   * @param id the integer representation of the account type.
   * @return true if successful, false otherwise.
   * @throws SQLException if connection is unsuccessful.
   */
  public static boolean updateAccountTypeInterestRate(BigDecimal interestRate, int id)
      throws SQLException {
    boolean cdt1 = Checker.checkValidAccountTypesInterestRate(interestRate);
    boolean cdt2 = Checker.checkValidAccountTypesId(id);
    interestRate.setScale(2, RoundingMode.HALF_UP);
    if (cdt1 && cdt2) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      boolean complete =
          DatabaseUpdater.updateAccountTypeInterestRate(interestRate, id, connection);
      connection.close();
      return complete;
    }
    return false;
  }

  /**
   * Update the viewed status of a message.
   * @param id The id of the message.
   * @return Whether or not the message exists in the database.
   */
  public static boolean updateUserMessageState(int id) {
    boolean exists = false;
    try {
      DatabaseSelectHelper.getSpecificMessage(id);
      exists = true;
    } catch (SQLException e){}
    if (exists) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      DatabaseUpdater.updateUserMessageState(id, connection);
      return true;
    }
    return false;
    
  }

  /**
   * Update the password of a user.
   * @param password The new password.
   * @param currentCustomerId The id of the customer that gets a new password.
   * @return Whether or not the password was updated.
   */
  public static boolean updateUserPassword(String password, int currentCustomerId) {
    boolean exists = false;
    try {
      DatabaseSelectHelper.getPassword(currentCustomerId);
      exists = true;
    } catch (SQLException e){}
    if (exists) {
      Connection connection = DatabaseDriverHelper.connectOrCreateDataBase();
      DatabaseUpdater.updateUserPassword(password, currentCustomerId, connection);
      return true;
    }
    return false;
    
  }

}
