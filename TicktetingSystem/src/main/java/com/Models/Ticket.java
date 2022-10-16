package com.Models;

public class Ticket {
  private String status;
  private String description;
  private double amount;
  private String employeeUserName;
  private int id;
 

  public Ticket() {
    this.setPendingStatus();
  }

  public Ticket(int id, String status) { 
    this.id = id;
    this.status= status;
  }

  public Ticket(String status, String description, double amount) {
    this.setPendingStatus();
    this.description= description;
    this.amount = amount;
  }

  public Ticket(String status, String description, double amount, int id,String employeeUserName) {
    super();
    this.status = status;
    this.description = description;
    this.amount = amount;
    this.setEmployeeUserName(employeeUserName);
    this.id = id;
  }

  public Ticket(String status, String description, double amount, int id) {
    super();
    this.status = status;
    this.description = description;
    this.amount = amount;
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public double getAmount() {
    return amount;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setPendingStatus() {
    this.status = "Pending";
  }

  public void setApprovedStatus() {
    this.status = "Approved";
  }

  public void setDeniedStatus() {
    this.status = "Denied";
  }

 
  @Override
  public String toString() {
    return "\nTicket [ id="+id+", status=" + status + ", description=" + description + ", amount=" + amount +"]";
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEmployeeUserName() {
    return employeeUserName;
  }

  public void setEmployeeUserName(String employeeUserName) {
    this.employeeUserName = employeeUserName;
  }




}
