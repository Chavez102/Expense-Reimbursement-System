package com.Revature;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import com.Models.Employee;
import com.Models.Ticket;
import com.Models.User;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	    System.out.println("Start");
	    
	    
		Javalin app = Javalin.create().start(8000); 
		System.out.println("Start");
		
		app.before((Context ctx) ->{
          System.out.println( "verifying user needs implementation");

      });
		
		
		app.get("/LandPage", (Context ctx) ->{
			System.out.println("log in start");
			ctx.result("interresting2");
			
			User user= ctx.bodyAsClass(User.class);
			
			System.out.println( user ); 
		});
		
		
		
		
		app.post("/ticketSubmimssion", (Context ctx) ->{

          System.out.println("ticket submission start");
          ctx.result("Successfully inserting ");
          
          Ticket ticket= ctx.bodyAsClass(Ticket.class); 
          
          Employee e = new Employee("u1","p1");
          
          e.submitTicket(ticket);

      });
		
		
		
		
	}

}
