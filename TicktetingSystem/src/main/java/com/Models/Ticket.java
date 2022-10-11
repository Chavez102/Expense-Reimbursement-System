package com.Models;

public class Ticket {
  private String status;
  private String description;
  private double amount;
  private String employeeUserName;

  public Ticket() {
    this.setPendingStatus();
  }

  public Ticket(String status, String description, double amount) {
    this.setPendingStatus();
    this.description= description;
    this.amount = amount;
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
    return "Ticket [status=" + status + ", amount=" + amount + ", description=" + description + "]";
  }

  public String getEmployeeUserName() {
    return employeeUserName;
  }

  public void setEmployeeUserName(String employeeUserName) {
    this.employeeUserName = employeeUserName;
  }

}
