package com.Revature;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.eclipse.jetty.server.session.Session;

import com.Models.Database;
import com.Models.Employee;
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
		  
		    String serverResponse=null;
		  
  		    User user= ctx.bodyAsClass(User.class);
            
            if (database.isUserNameValid(user.getUserName()) ) {
              System.out.println( "username already Exists" );
              ctx.status(400);
              serverResponse= "username already Exists";
              ctx.result(serverResponse);
             return;
              
            }
            
            database.insertUser(user);
            
            Employee e = new Employee(user.getUserName(),user.getPassword());
           
            database.insertEmployee(e);
            
            
		});
		
		
		
		app.get("/LogIn", (Context ctx) ->{  
		  String serverResponse="username or password is Incorrect";
          
          User user= ctx.bodyAsClass(User.class);
          
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
          
          HttpSession s = ctx.req().getSession(); 
          
          
          ctx.sessionAttribute("username_key", user.getUserName());
          
          if (database.isEmployee(user.getUserName())) {
            Employee e = new Employee(user.getUserName(),user.getPassword());
            ctx.sessionAttribute("role_key",e);
            System.out.println(" Logged in as a Employee User ");
          } 
          System.out.println("httpSession ID: "+s.getId()); 
        
      }); 


		
		app.post("/ticketSubmimssion", (Context ctx) ->{
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
          
          
          
          database.insertTicket(e,ticket);
          
          serverResponse = "Successfully inserted ";
          ctx.result(serverResponse); 
          System.out.println(serverResponse);
           
          HttpSession s = ctx.req().getSession();
          //httpSession 
          System.out.println("httpSession ID: "+s.getId());
        });
		
		
		
		
		
		  app.get("/LogOut", (Context ctx) ->{  
	          User user= ctx.sessionAttribute("obj_key") ;
	          
	          System.out.println("BYE : "+ user.getUserName() ); 
	          ctx.req().getSession().invalidate();;
	          //httpSession
	          System.out.println("LOGGED OUT ");  
	      });
		
		
	}

}
