package com.bank.bank;

import com.bank.database.DatabaseInsertException;
import com.bank.databasehelper.DatabaseInsertHelper;
import com.bank.exceptions.ConnectionFailedException;
import com.bank.exceptions.IllegalAmountException;
import com.bank.exceptions.InsufficientFundsException;
import com.bank.generics.EnumMapRolesAndAccounts;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class Bank {

  /**
   * Main banking application, allows admin creation, teller creation, tellerterminal/atm access.
   *
   * @param argv the command to select which function of the app to access.
   * @throws DatabaseInsertException an exception thrown when an error occurs with data base 
   *     insertion
   */
  public static void main(String[] argv) throws SQLException, IOException,
      ConnectionFailedException, IllegalAmountException, InsufficientFundsException, 
      DatabaseInsertException {

    /**Connection connection = DatabaseDriverExtender.connectOrCreateDataBase();
    DatabaseDriverExtender.initialize(connection);
    try {
      // define the roles and account types
      // ASSUME 1 = ADMIN, 2 = TELLER, 3 = CUSTOMER
    
      DatabaseInsertHelper.insertRole("CUSTOMER");
      DatabaseInsertHelper.insertRole("TELLER");
      DatabaseInsertHelper.insertRole("ADMIN");
      // ASSUME 1 = CHEQUING, 2 = SAVINGS, 3 = TFSA
      DatabaseInsertHelper.insertAccountType("CHEQUING", new BigDecimal("0.10"));
      DatabaseInsertHelper.insertAccountType("SAVINGS", new BigDecimal("0.10"));
      DatabaseInsertHelper.insertAccountType("TFSA", new BigDecimal("0.10"));
      DatabaseInsertHelper.insertAccountType("RESTRICTEDSAVING", new BigDecimal("0.10"));
      DatabaseInsertHelper.insertAccountType("BALANCEOWING", new BigDecimal("0.10"));
      
      connection.close();
      */
      EnumMapRolesAndAccounts enumMap = new EnumMapRolesAndAccounts();
      enumMap.createEnumMap();
     
      // check what is in argv
      BankFactory factory = new BankFactory();
      int num = Integer.valueOf(argv[0].toString());
      factory.initialWhatToDo(num);
    /**} 
    
   // catch exceptions
    catch (ConnectionFailedException e) {
      System.out.println("Failed connection!");
    } catch (IllegalAmountException e) {
      System.out.println("Negative amount entered!");
    } catch (InsufficientFundsException e) {
      System.out.println("Insufficient funds!");
    } catch (SQLException e) {
      System.out.println("Database failure!");
    } catch (NullPointerException e) {
      System.out.println("Looks like something isn't there!");
    } catch (Exception e) {
      System.out.println("Unknown oopsies :)");
    } finally {
      try {
        connection.close();
      } catch (Exception e) {
        System.out.println("Looks like it was closed already :)");
      }
    }*/
  }
}
