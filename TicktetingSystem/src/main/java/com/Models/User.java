package com.Models;

import java.io.Serializable;

public class User implements Serializable{
	private String userName;
	private String password;
	private String role="employee";
	
	public User() {}

	public User(String userName, String password) {
		this.userName = userName;
		this.password = password; 
	}
	
	
	

  public boolean isUserValid() {
		System.out.println("Validation implementation needed!!");
		return true;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserName(String userName) {
	    this.userName = userName;
	}

  @Override
  public String toString() {
    return "User [userName=" + userName + ", password=" + password + "]";
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }


}
