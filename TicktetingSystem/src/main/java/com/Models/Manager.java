package com.Models;

import java.io.Serializable;
import java.util.List;

public class Manager extends User {
	
	public Manager(String username, String password ) {
	  super(username,password);
	}
	
	public Manager(String username ) {
	      super(username,"");
	}
	
	public List<Ticket> viewPendingTickets() {
		
		System.out.println("Reimburstment start");
		
		return null;
		
	}
	
	public String viewTicket(Ticket ticket) {
		return ticket.toString();
	}
	
	public void approveTicket(Ticket ticket) {
		ticket.setApprovedStatus();
	}
	
	public void denyTicket(Ticket ticket) {
		ticket.setDeniedStatus();
	}
	
	
	
}
