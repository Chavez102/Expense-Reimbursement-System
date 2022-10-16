package com.Revature;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import org.eclipse.jetty.server.session.Session;

import com.Models.Database;
import com.Models.Employee;
import com.Models.Manager;
import com.Models.Ticket;
import com.Models.User;

import io.javalin.Javalin;
import io.javalin.http.Context;
import jakarta.servlet.http.HttpSession;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) { 
		
	    System.out.println("Start"); 
	    Database database= new Database();
	    
		Javalin app = Javalin.create( config-> {
		  config.jetty.sessionHandler(()->Database.fileSessionHandler());
		  config.plugins.enableRouteOverview("/");
		   
		}).start(8000); 
		
	
		 
		app.get("/SignUp", (Context ctx) ->{ 
		    signUp(ctx,database);
		}); 
		
		app.get("/LogIn", (Context ctx) ->{  
		  logIn(ctx,database);
        });  

		
		app.post("/ticketSubmimssion", (Context ctx) ->{
		  submitTicket(ctx,database); 
        });
		 
		app.get("/LogOut", (Context ctx) ->{  
		    logOut(ctx,database); 
	    });
		
		app.get("/ViewPendingTickets", (Context ctx) ->{  
            viewPendingTickets(ctx,database); 
        });
		
		app.put("/ProcessTicket", (Context ctx) ->{  
          processTicket(ctx,database); 
      });
		
		
		
		
	} 

    public static void signUp(Context ctx,Database database) {
	  String serverResponse=null; 
      User user= ctx.bodyAsClass(User.class);  
      
      if (database.isUserNameValid(user.getUserName()) ) {
        serverResponse = "username already Exists";
        System.out.println( serverResponse );
        ctx.status(400); 
        ctx.result(serverResponse);
       return; 
      } 
      
      database.insertUser(user);
      
      if ( user.getRole().equals("employee") ) {

        Employee e = new Employee(user.getUserName(),user.getPassword());
        database.insertEmployee(e);
      }else if (user.getRole().equals("manager")) { 
        
        Manager e = new Manager(user.getUserName(),user.getPassword());
        database.insertManager(e);
      }else {
        serverResponse= "Role is not 'employee', 'manager'  ";
        ctx.result(serverResponse);
        return;
      }
       
      serverResponse= "User added";
      ctx.result(serverResponse);
      System.out.println(serverResponse); 

    }

	public static void logIn(Context ctx,Database database) {
	  
	  String serverResponse=null;
      
      User user= ctx.bodyAsClass(User.class);
      
      serverResponse = "username or password is Incorrect";
      if (!database.isUserNameValid(user.getUserName()) ) {
        System.out.println( serverResponse );
        ctx.status(404); 
        ctx.result(serverResponse);
        return;
        
      }
      
      if(!database.isPasswordValid(user.getUserName(), user.getPassword())) {
        System.out.println("bad password");
        ctx.result(serverResponse);
        return;
      } 
      
      //create Session
      HttpSession s = ctx.req().getSession(); 
      
      
      ctx.sessionAttribute("username_key", user.getUserName());
      
      if (database.isEmployee(user.getUserName())) {
        Employee e = new Employee(user.getUserName(),user.getPassword());
        ctx.sessionAttribute("role_key",e);
        System.out.println(" Logged in as a Employee User ");
        ctx.result("Welcome Employee "+ e.getUserName());
      }
      else if (database.isManager(user.getUserName())) {
        Manager manager = new Manager(user.getUserName(),user.getPassword());
        ctx.sessionAttribute("role_key",manager);
        System.out.println(" Logged in as a Manager User ");
        ctx.result("Welcome Manager "+ manager.getUserName());
        
      }  
      System.out.println("httpSession ID: "+s.getId()); 
	  
	}

	public static void submitTicket(Context ctx,Database database) {
      System.out.println("my username is: "+ ctx.sessionAttribute("username_key"));
      String username= ctx.sessionAttribute("username_key") ;
      
      String serverResponse="";
      //check if logged in
      if ( ! ctx.req().isRequestedSessionIdValid() ) {
           serverResponse = "You are not logged in";
           System.out.println(serverResponse);
           ctx.result(serverResponse);
           return ;
      }
      
      //check if user is Employee
      if (! database.isEmployee(username)) {
        serverResponse = "You are not an Employee";
        System.out.println(serverResponse);
        ctx.result(serverResponse);
        return ;
      }
      
       
      System.out.println("ticket submission start"); 
      Ticket ticket= ctx.bodyAsClass(Ticket.class); 
      
      
      
      Employee e = (Employee)ctx.sessionAttribute("role_key"); 
      database.insertPendingTicket(e,ticket);
      
      serverResponse = "Successfully inserted ";
      ctx.result(serverResponse); 
      System.out.println(serverResponse);
       
      HttpSession s = ctx.req().getSession();
      //httpSession 
      System.out.println("httpSession ID: "+s.getId());
	}

	public static void logOut(Context ctx,Database database) {
	     String username= ctx.sessionAttribute("username_key") ;
         
         String serverResponse ="BYE : "+ username ; 
         ctx.req().getSession().invalidate();;
         
         System.out.println(serverResponse); 
         ctx.result(serverResponse);
	}
 
	private static void viewPendingTickets(Context ctx, Database database) {
	  System.out.println("my username is: "+ ctx.sessionAttribute("username_key"));
      String username= ctx.sessionAttribute("username_key") ;
      
      String serverResponse="";
      //check if logged in
      if ( ! ctx.req().isRequestedSessionIdValid() ) {
           serverResponse = "You are not logged in";
           System.out.println(serverResponse);
           ctx.result(serverResponse);
           return ;
      }
      
      //check if user is manager
      if (! database.isManager(username)) {
        serverResponse = "You are not a Manager";
        System.out.println(serverResponse);
        ctx.result(serverResponse);
        return ;
      }
      
      List<Ticket> ticketList= database.executeFindTiccketsStatemetn("SELECT * FROM tickets");
      
      System.out.println(ticketList);
      
	  ctx.result(ticketList.toString());
	  
	  
	}


	private static void processTicket(Context ctx, Database database) {
	  System.out.println("my username is: "+ ctx.sessionAttribute("username_key"));
      String username= ctx.sessionAttribute("username_key") ;
      
      String serverResponse="";
      //check if logged in
      if ( ! ctx.req().isRequestedSessionIdValid() ) {
           serverResponse = "You are not logged in";
           System.out.println(serverResponse);
           ctx.result(serverResponse);
           return ;
      }
      
      //check if user is manager
      if (! database.isManager(username)) {
        serverResponse = "You are not a Manager";
        System.out.println(serverResponse);
        ctx.result(serverResponse);
        return ;
      }
      
      Ticket ticket= ctx.bodyAsClass(Ticket.class); 
      
      String newStatus= ticket.getStatus();
      int ticketId = ticket.getId();
      
      ticket= database.getPendingTicket(ticketId);
      ticket.setStatus(newStatus);
      
      //remove ticket from tickets table
      database.deletePendingTicket(ticket.getId()); 
      
      //add ticket to ProcessedTickets
      database.insertProcessedTicket(ticket.getEmployeeUserName(), ticket); 
       
	   
	}




}
