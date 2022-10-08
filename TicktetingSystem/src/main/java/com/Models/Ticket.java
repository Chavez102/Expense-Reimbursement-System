package com.Models;

public class Ticket {
	private String status;
	
	public Ticket() {
		this.status = "Pending";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status=status;
	}
	
	public void setPending() {
		this.status = "Pending";
	}
	
	public void setApproved() {
		this.status = "Approved";
	}
	
	public void setDenied() {
		this.status = "Denied";
	}
	
	
	
}
