package com.Models;

public class Employee extends User{
	
	public Employee(String username, String password ) {
		super(username,password);
	}
	
	public void requestReimbursement() {
		
		System.out.println("Reimburstment start");
		
	}
}
