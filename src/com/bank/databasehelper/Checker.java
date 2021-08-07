package com.bank.databasehelper;

import com.bank.generics.AccountTypes;
import com.bank.generics.Roles;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class Checker {
    
  /**
   * Checks whether the desired account type is valid or not.
   *
   * @param id the integer representation of the account type.
   * @return whether the account type is valid or not.
   */
  static boolean checkValidAccountsType(int id) throws SQLException {
    
    boolean restrictedsavings = AccountTypes.RESTRICTEDSAVING.ordinal() == id;
    // account type ids are greater than 0
    if (id <= DatabaseSelectHelper.getAccountTypesIds().size() && id >= 1) {
      return true;
    }
    return false;
  }

  /**
   * Checks whether the desired role ID is valid or not.
   *
   * @param roleId the role ID.
   * @return whether the role ID is valid or not.
   */
  static boolean checkValidRolesId(int roleId) throws SQLException {
      List <Integer> ints = DatabaseSelectHelper.getRoles();

    
    // valid role id
    if (roleId <= ints.size() && roleId >= 1) {
      return true;
    }
    return false;
  }

  /**
   * Checks whether the desired user account user ID is valid or not.
   *
   * @param userId the user ID of the user account.
   * @return whether the user account user ID is valid or not.
   */
  static boolean checkValidUserAccountUserId(int userId) {
    boolean result = true;
    // user ids start from 1 and increment
    if (userId < 1) {
      result = false;
    }
    return result;
  }

  /**
   * Checks whether the desired user account account ID is valid or not.
   *
   * @param accountId the account ID of the user account.
   * @return whether the user account account ID is valid or not.
   */
  static boolean checkValidUserAccountAccountId(int accountId) {
    boolean result = true;
    // account ids start from 1 and increment
    if (accountId < 1) {
      result = false;
    }
    return result;
  }
  
  /**
   * Checks whether the desired account ID is valid or not.
   *
   * @param id the id of the account.
   * @return whether it is valid or not.
   */
  static boolean checkValidAccountsId(int id) {
    boolean result = true;
    // account ids start from 1 and increment
    if (id < 1) {
      result = false;
    }
    return result;
  }

  /**
   * Checks whether the desired account name is valid or not.
   *
   * @param name the desired name of the account.
   * @return whether the name is valid or not.
   */
  static boolean checkValidAccountsName(String name) {
    boolean result = true;
    // account names must not be null or empty
    if (name == null || name.isEmpty()) {
      result = false;
    }
    return result;
  }

  /**
   * Checks whether the desired account balance is valid or not.
   *
   * @param balance the BigDecimal representation of the balance.
   * @return whether the account balance is valid or not.
   */
  static boolean checkValidAccountsBalance(BigDecimal balance) {
    boolean result = true;
    // balance must be non negative
    if (balance.doubleValue() < 0) {
      result = false;
    }
    return result;

  }

  /**
   * Checks whether the desired account type ID is valid or not.
   *
   * @param typeId the type ID of the account.
   * @return whether the account type ID is valid or not.
   */
  static boolean checkValidAccountTypesId(int typeId) {
    boolean result = true;
    // account type ids are between 1 and 3
    if (typeId < 1 || typeId > AccountTypes.values().length) {
      result = false;
    }
    return result;
  }

  /**
   * Checks whether the desired account type name is valid or not.
   *
   * @param type the name type of the account.
   * @return whether the account type name is valid or not.
   */
  static boolean checkValidAccountTypesName(String type) {
    AccountTypes chequing = AccountTypes.values()[0];
    AccountTypes saving = AccountTypes.values()[1];
    AccountTypes tfsa = AccountTypes.values()[2];
    AccountTypes restricted = AccountTypes.values()[3];
    // account types name must equal one from the enumerator
    boolean result = (type.equalsIgnoreCase(restricted.name())
        || type.equalsIgnoreCase(chequing.name())
        || type.equalsIgnoreCase(saving.name())
        || type.equalsIgnoreCase(tfsa.name()));
    return result;
  }

  /**
   * Checks whether the desired account type interest rate is valid or not.
   *
   * @param interestRate the interest rate.
   * @return whether the account type interest rate is valid or not.
   */
  static boolean checkValidAccountTypesInterestRate(BigDecimal interestRate) {
    boolean result = true;
    // interest rate must be bigger than 0 and smaller equal to 1
    if (interestRate.doubleValue() < 0 || interestRate.doubleValue() >= 1) {
      result = false;
    }
    return result;
  }

  /**
   * Checks whether the desired role name is valid or not.
   *
   * @param role the role name.
   * @return whether the role name is valid or not.
   */
  static boolean checkValidRolesName(String role) {
    Roles admin = Roles.values()[0];
    Roles teller = Roles.values()[1];
    Roles customer = Roles.values()[2];
    // role names must be from the enumerator
    boolean result = (role.equalsIgnoreCase(admin.name()) || role.equalsIgnoreCase(teller.name())
        || role.equalsIgnoreCase(customer.name()));
    return result;
  }

  /**
   * Checks whether the desired user ID is valid or not.
   *
   * @param id the id of the user.
   * @return whether the user ID is valid or not.
   */
  static boolean checkValidUsersId(int id) {
    boolean result = true;
    // users id must start from 1 and increment
    if (id < 1) {
      result = false;
    }
    return result;
  }

  /**
   * Checks whether the desired user name is valid or not.
   *
   * @param name the user name.
   * @return whether the user name is valid or not.
   */
  static boolean checkValidUsersName(String name) {
    boolean result = true;
    // user name must not be null or empty
    if (name == null || name.isEmpty()) {
      result = false;
    }
    return result;
  }

  /**
   * Checks whether the desired age is valid or not.
   *
   * @param age the age or the user.
   * @return whether the age is valid or not.
   */
  static boolean checkValidUsersAge(int age) {
    boolean result = true;
    // user age must be greater than 0
    if (age <= 0) {
      result = false;
    }
    return result;
  }

  /**
   * Checks whether the desired address is valid or not.
   *
   * @param address the address of the user.
   * @return whether the address is valid or not.
   */
  static boolean checkValidUsersAddress(String address) {
    boolean result = true;
    // 100 character limit, address must not be null or empty
    if (address.length() > 100 || address == null || address.isEmpty()) {
      result = false;
    }
    return result;
  }



  /**
   * Checks whether the desired user account account ID is valid or not.
   *
   * @param accountId the account ID of the user account.
   * @param userId the userId which owns the accounts.
   * @return whether the user account account ID is valid or not.
   */
  static boolean checkValidUserAccountAccountId(int accountId, int userId)
      throws SQLException {
    boolean result = true;
    // account ids must start from 1 and increment
    if (accountId < 1) {
      result = false;
    }
    return result;
  }

  /**
   * Checks whether the desired role ID is valid or not.
   *
   * @param roleId the role ID of the user.
   * @return whether the role ID is valid or not.
   */
  static boolean checkValidUsersRoleId(int roleId) {
    boolean result = true;
    // role ids are above 0
    if (roleId < 1) {
      result = false;
    }
    return result;
  }
  
  /**
   * Checks if the message adheres to the rule of being
   * 512 characters or less.
   * @param message the message being checked.
   * @return boolean if the message adheres to the rules.
   */
  static boolean checkValidMessage(String message) {
    if (message.length() > 512) {
      return false;
    } else {
      return true;
    }
  }
  

}
