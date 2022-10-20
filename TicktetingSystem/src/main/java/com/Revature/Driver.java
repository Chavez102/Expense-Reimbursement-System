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
	    
		Javalin app = Javalin.create().start(8000); 
		 
	//Permissions for all Users	 
		app.post("/SignUp", (Context ctx) ->{ 
		    signUp(ctx,database);
		}); 
		 
	//Permissions for both Employees and Managers
		app.post("/LogIn", (Context ctx) ->{  
		  logIn(ctx,database);
        });  
		
		app.post("/LogOut", (Context ctx) ->{  
          logOut(ctx,database); 
        });
		
		app.get("/ViewPendingTickets", (Context ctx) ->{  
          viewPendingTickets(ctx,database); 
        });
		
		app.get("/ViewProcessedTickets", (Context ctx) ->{  
          viewProcessedTickets(ctx,database); 
		});

		
	//Permissions for Employees
		app.post("/ticketSubmimssion", (Context ctx) ->{
		  submitTicket(ctx,database); 
        });  
		
		
	//Permissions for Managers 
		app.put("/ProcessTicket", (Context ctx) ->{  
          processTicket(ctx,database); 
      }); 
		
		app.put("/ChangeRole", (Context ctx) ->{  
          changeRole(ctx,database); 
      }); 
	}
    
     

    //Permissions for all Users 
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

    }

    //Permissions for both Employees and Managers
	public static void logIn(Context ctx,Database database) {
	  
	  String serverResponse=null;
      
      User user= ctx.bodyAsClass(User.class);
      
      serverResponse = "username or password is Incorrect";
      if (!database.isUserNameValid(user.getUserName()) ) { 
        ctx.status(404); 
        ctx.result(serverResponse);
        return;
        
      }
      
      if(!database.isPasswordValid(user.getUserName(), user.getPassword())) { 
        ctx.result(serverResponse);
        return;
      } 
      
      //create Session
      HttpSession s = ctx.req().getSession(); 
      
      
      ctx.sessionAttribute("username_key", user.getUserName());
      
      if (database.isEmployee(user.getUserName())) {
        Employee e = new Employee(user.getUserName(),user.getPassword());
        ctx.sessionAttribute("role_key",e); 
        ctx.result("Welcome Employee "+ e.getUserName());
      }
      else if (database.isManager(user.getUserName())) {
        Manager manager = new Manager(user.getUserName(),user.getPassword());
        ctx.sessionAttribute("role_key",manager); 
        ctx.result("Welcome Manager "+ manager.getUserName());
        
      }  
//      System.out.println("httpSession ID: "+s.getId()); 
	  
	}

	public static void logOut(Context ctx,Database database) {
      String username= ctx.sessionAttribute("username_key") ;
      
      String serverResponse ="BYE : "+ username ; 
      ctx.req().getSession(false).invalidate();;
      
      
       
      ctx.result(serverResponse);
	}
	
	private static void viewPendingTickets(Context ctx, Database database) { 
      String userName= ctx.sessionAttribute("username_key") ;
      String serverResponse="";
      
      //check if logged in
      if ( ! ctx.req().isRequestedSessionIdValid() ) {
           serverResponse = "You are not logged in"; 
           ctx.result(serverResponse);
           return ;
      }
       
      if (userHasManagerPermissions(ctx,database,userName)) {
        List<Ticket> ticketList= database.executeFindTicketsStatemetn("SELECT * FROM PendingTickets");
        
        ctx.result(ticketList.toString());  
      }else if (userHasEmployeePermissions(ctx,database,userName)) {
        List<Ticket> ticketList= database.executeFindTicketsStatemetn("SELECT * FROM pendingtickets WHERE employeeusername = ?",userName);
         
        ctx.result(ticketList.toString());  
      } 
    }
	
	private static void viewProcessedTickets(Context ctx, Database database) {
	      System.out.println("my username is: "+ ctx.sessionAttribute("username_key"));
	      String userName= ctx.sessionAttribute("username_key") ;
	       
	       
	      if ( userHasManagerPermissions(ctx,database,userName)) {
	        List<Ticket> ticketList= database.executeFindTicketsStatemetn("SELECT * FROM ProcessedTickets"); 
	        System.out.println(ticketList); 
	        ctx.result(ticketList.toString());  
	      }else if (userHasEmployeePermissions(ctx,database,userName)) {
	        List<Ticket> ticketList= database.executeFindTicketsStatemetn("SELECT * FROM processedtickets WHERE employeeusername = ?",userName); 
	        ctx.result(ticketList.toString()); 
	      }
	      
	      
	    }
	
	//Permissions for Employees
	public static void submitTicket(Context ctx,Database database) {
      System.out.println("my username is: "+ ctx.sessionAttribute("username_key"));
      String userName= ctx.sessionAttribute("username_key") ;
      
      String serverResponse="";
      if (! userHasEmployeePermissions(ctx,database,userName)) {
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
	  
	//Permissions for Managers 
	private static void processTicket(Context ctx, Database database) {
	  
      String userName= ctx.sessionAttribute("username_key") ;
      
      String response =null;
       
      if (! userHasManagerPermissions(ctx,database,userName)) {
        return ;
      } 
      
      Ticket ticket= ctx.bodyAsClass(Ticket.class); 
      
      String newStatus= ticket.getStatus();
      
      if ( !(newStatus.equalsIgnoreCase("approved" ) || newStatus.equalsIgnoreCase("denied"))) {
        response = "status must be 'approved'or 'denied' ";
        ctx.result(response);
        return;
      }
      
      
      
      int ticketId = ticket.getId();
      
      ticket= database.getPendingTicket(ticketId);
      if (ticket == null) {
        response = "Ticket Id does not exits in Pending Tickets"; 
        ctx.json(response);
        return;
      }
      ticket.setStatus(newStatus);
      
      //remove ticket from PendingTickets table
      database.deletePendingTicket(ticket.getId()); 
      
      //add ticket to ProcessedTickets
      database.insertProcessedTicket(ticket.getEmployeeUserName(), ticket); 
      
      response = "Ticket Processed";
      ctx.json(response);
        
	}


    private static void changeRole(Context ctx, Database database) {
      User user= ctx.bodyAsClass(User.class);
      String newRole = user.getRole();
      String response="updated employee";
      
      if (! userHasEmployeePermissions(ctx,database,user.getUserName())) {
        return;
      }
      
      if ( ! ( newRole.equalsIgnoreCase("manager")|| newRole.equalsIgnoreCase("employee") ) ) {
        response = "role should be 'manager' or 'employee'";
        ctx.result(response);
      }
      
      
      if (!database.isUserNameValid(user.getUserName()) ) { 
        ctx.status(404); 
        response = "username doesnt exist";
        ctx.result(response);
        return;
        
      }
      
      
      if(database.isEmployee(user.getUserName())) {
        if(newRole.equalsIgnoreCase("employee")) {
          response = user.getUserName()+"is already an "+ newRole;
          ctx.result(response);
          return;
        }
        
        database.UpdateEmployee(user.getUserName());
        ctx.result(response);
      }else if(database.isManager(user.getUserName())) {
        if(newRole.equalsIgnoreCase("manager")) {
          response = user.getUserName()+"is already a "+ newRole;
          ctx.result(response);
          return;
        }
        
        database.UpdateManager(user.getUserName());
      }
    
    }

	
	//Login Verification Process
	private static boolean userHasManagerPermissions(Context ctx,Database database, String userName) {
	  String serverResponse="";
	  //check if logged in
      if ( ! ctx.req().isRequestedSessionIdValid() ) {
           serverResponse = "You are not logged in"; 
           ctx.result(serverResponse);
           return false;
      }
      
      //check if user is manager
      if (! database.isManager(userName)) {
        serverResponse = "You are not a Manager"; 
        ctx.result(serverResponse);
        return false;
      }
      return true;
	  
	}

	
	private static boolean userHasEmployeePermissions(Context ctx,Database database, String userName) {
      String serverResponse="";
      //check if logged in
      if ( ! ctx.req().isRequestedSessionIdValid() ) {
           serverResponse = "You are not logged in"; 
           ctx.result(serverResponse);
           return false;
      }
      
      //check if user is Employee
      if (! database.isEmployee(userName)) {
        serverResponse = "You are not an Employee"; 
        ctx.result(serverResponse);
        return false;
      }
      return true;
      
    }
}
