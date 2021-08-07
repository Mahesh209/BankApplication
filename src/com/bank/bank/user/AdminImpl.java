package com.bank.bank.user;


public class AdminImpl extends UserImpl implements Admin {

  private int id;
  private String name;
  private int age;
  private String address;
  private int roleId;
  private boolean authenticated;

  /**
   * Constructor for Admin with authentication.
   *
   * @param id the id of the user.
   * @param name the name of the admin.
   * @param age the age of the admin.
   * @param address the address of the admin.
   * @param roleId the role ID of the admin.
   */
  public AdminImpl(int id, String name, int age, String address, int roleId) {
    this.setId(id);
    this.setName(name);
    this.setAge(age);
    this.setAddress(address);
    this.setRoleId(roleId);
  }

  /**
   * Constructor for Admin with authentication.
   *
   * @param id the id of the user.
   * @param name the name of the admin.
   * @param age the age of the admin.
   * @param address the address of the admin.
   * @param roleId the role ID of the admin.
   * @param authenticated if the authentication is successful.
   */
  public AdminImpl(int id, String name, int age, String address, int roleId, boolean authenticated) {
    this.setId(id);
    this.setName(name);
    this.setAge(age);
    this.setAddress(address);
    this.setRoleId(roleId);
    this.authenticated = authenticated;
  }
}
