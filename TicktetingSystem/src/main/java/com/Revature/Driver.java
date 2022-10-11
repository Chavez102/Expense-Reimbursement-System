package com.Revature;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import com.Models.Database;
import com.Models.Employee;
import com.Models.Ticket;
import com.Models.User;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class Driver {

	public static void main(String[] args) { 
		
	    System.out.println("Start"); 
	    Database database= new Database();
	    
		Javalin app = Javalin.create( config-> {
		  config.jetty.sessionHandler(()->Database.fileSessionHandler());
		  config.plugins.enableRouteOverview("/");
		   
		}).start(8000); 
		
	
		 
		app.get("/LandPage", (Context ctx) ->{ 
		    ctx.sessionAttribute("myKey","my Value"); 
		    Employee e = new Employee("s1","p1");
		    ctx.sessionAttribute("obj", e);
			System.out.println("log in start");
			
			String myresponse="Session Value :" + ctx.sessionAttribute("myKey");
			
			String j= ((Employee) ctx.sessionAttribute("obj")).getUserName();
			
			myresponse= myresponse + "\n employeename :" + j;
			ctx.result(myresponse);
			
			User user= ctx.bodyAsClass(User.class);
			
			if (!database.insertUser(user) ) {
			  System.out.println( "bad request" );
			  ctx.status(400);
			}
			
		});
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		app.post("/ticketSubmimssion", (Context ctx) ->{

          System.out.println("ticket submission start"); 
          Ticket ticket= ctx.bodyAsClass(Ticket.class); 
          
          Employee e = new Employee(ticket.getEmployeeUserName(),"p1");
          
          database.insertTicket(e,ticket);
          ctx.result("Successfully inserted ");
          
          System.out.println("Successfully inserted ");
        });
		
		
		
		
	}

}
