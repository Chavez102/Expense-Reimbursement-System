package com.Models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class Employee extends User{
    Database dataBase= new Database();
	
	public Employee(String username, String password ) {
		super(username,password);
	}
	
	public Employee(String username ) {
      super(username,"");
    }
	
	public void requestReimbursement() {
		
		System.out.println("Reimburstment start");
		
	}
	
	public String viewTicketStatus(Ticket ticket) {
		return ticket.toString();
	}
	
//	public void submitTicket(Ticket ticket) {
//	  
//	 dataBase.insertTicket(this,ticket);
//    
//	}
}
