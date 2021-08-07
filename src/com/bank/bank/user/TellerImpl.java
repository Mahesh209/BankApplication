package com.bank.bank.user;


public class TellerImpl extends UserImpl implements Teller {

  private int id;
  private String name;
  private int age;
  private String address;
  private int roleId;
  private boolean authenticated;

  /**
   * Constructor for Teller without authentication.
   *
   * @param id the id of the user.
   * @param name the name of the Teller.
   * @param age the age of the Teller.
   * @param address the address of the Teller.
   * @param roleId the role ID of the teller.
   */
  public TellerImpl(int id, String name, int age, String address, int roleId) {
    this.setId(id);
    this.setName(name);
    this.setAge(age);
    this.setAddress(address);
    this.setRoleId(roleId);
  }

  /**
   * Constructor for Teller with authentication.
   *
   * @param id the id of the user.
   * @param name the name of the Teller.
   * @param age the age of the Teller.
   * @param address the address of the Teller.
   * @param roleId the role ID of the teller.
   * @param authenticated if the authentication is successful.
   */
  public TellerImpl(int id, String name, int age, String address, int roleId, boolean authenticated) {
    this.setId(id);
    this.setName(name);
    this.setAge(age);
    this.setAddress(address);
    this.setRoleId(roleId);
    this.authenticated = authenticated;
  }
}
