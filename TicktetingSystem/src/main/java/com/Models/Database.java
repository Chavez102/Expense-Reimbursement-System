package com.Models;
 
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


import org.eclipse.jetty.server.session.DatabaseAdaptor;
import org.eclipse.jetty.server.session.DefaultSessionCache;
import org.eclipse.jetty.server.session.FileSessionDataStore;
import org.eclipse.jetty.server.session.JDBCSessionDataStoreFactory;
import org.eclipse.jetty.server.session.SessionCache;
import org.eclipse.jetty.server.session.SessionHandler;

import com.Revature.util.ConnectionFactory;




public class Database implements Serializable{
  PreparedStatement stmt = null;  
  ResultSet set = null;
  
  public Database() {}
  
  public void insertTicket(Employee employee,Ticket ticket){
     
    
    try (Connection conn = ConnectionFactory.getConnection()){

       final String str=  "INSERT INTO Tickets (Status, Amount, Description,employeeusername) "
                       + "VALUES (?,?,?,?)";
       
       stmt = conn.prepareStatement(str);
       stmt.setString(1, ticket.getStatus() );
       stmt.setDouble(2,ticket.getAmount() );
       stmt.setString(3,ticket.getDescription() );
       stmt.setString(4, employee.getUserName() );
        
       stmt.execute(); 
      
    } catch ( Exception e ) {
       System.err.println( e.getClass().getName()+": "+ e.getMessage() );
       
    }finally {
      try {
        stmt.close();
      } catch (SQLException e) { 
        e.printStackTrace();
      }
    }
  }
  
  public void insertUser(User user){
   
    PreparedStatement stmt = null; 
    try(Connection c = ConnectionFactory.getConnection())  { 
      
       System.out.println("Opened database successfully");
 
         
       final String str=  "INSERT INTO Users (UserName,passsword,role) VALUES(?,?,?)";

      stmt = c.prepareStatement(str);
      stmt.setString(1, user.getUserName() );
      stmt.setString(2, user.getPassword());
      stmt.setString(3, user.getRole());
      
      stmt.execute();   
      
   }catch(SQLException e) {
       e.printStackTrace();
   } finally { 
       try {
         stmt.close();  
       } catch (SQLException e1) { 
        e1.printStackTrace();
       }
  }
    
   
 }
  
  
  
  public void insertEmployee(Employee e) {
      
      try(Connection c = ConnectionFactory.getConnection())  
      {  
  
        final String str=  "INSERT INTO Employees (UserName) VALUES(?)";
  
        stmt = c.prepareStatement(str);
        stmt.setString(1, e.getUserName() ); 
        
        stmt.execute();   
      
     }catch(SQLException ex) {
         ex.printStackTrace();
     } finally { 
         try {
           stmt.close();  
         } catch (SQLException e1) { 
          e1.printStackTrace();
         }
    }
  }
  
 
  public boolean isUserNameValid(String username) { 
		Connection conn = null;
		try { 
			// Make Connection
			conn = ConnectionFactory.getConnection();
			
			final String str=  "SELECT * FROM users where username = ?" ;

            stmt = conn.prepareStatement(str);
            stmt.setString(1, username );
           
			set = stmt.executeQuery();
 
			if ( set.next() ) {
			  return true;
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {  
          e.printStackTrace();
        } 
		finally {

			try {
				conn.close();
				set.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return false;
	}
  
  public boolean isEmployee(String username) { 
    Connection conn = null;
    try { 
        // Make Connection
        conn = ConnectionFactory.getConnection();
        
        final String str=  "SELECT * FROM Employees WHERE username = ?" ;

        stmt = conn.prepareStatement(str);
        stmt.setString(1, username );
       
        set = stmt.executeQuery();

        if ( set.next() ) {
          return true;
        }
    } 
    catch (SQLException e) {
        e.printStackTrace();
    } catch (Exception e) {  
      e.printStackTrace();
    } 
    finally {

        try {
            conn.close();
            set.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return false;
}
  
  
  public boolean isPasswordValid(String username,String inputPassword) {
    
    ResultSet set = null;
    PreparedStatement stmt = null;
    Connection conn = null;

    try { 

        conn = ConnectionFactory.getConnection();
        
        final String str=  "SELECT * FROM users where username = ?" ;

        stmt = conn.prepareStatement(str);
        stmt.setString(1, username );
       
        set = stmt.executeQuery();

        if ( set.next() ) {
          String realPassword= set.getString(2);
          
          if ( inputPassword.equals(realPassword))
            return true;
        }
    } 
    catch (SQLException e) {
        e.printStackTrace();
    } catch (Exception e) {  
      e.printStackTrace();
    } 
    finally {

        try {
            conn.close();
            set.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    return false;
  }
  
  

  

  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  public static SessionHandler fileSessionHandler() {
    System.out.println("Gets Here");
    SessionHandler sessionHandler = new SessionHandler();
    SessionCache sessionCache = new DefaultSessionCache(sessionHandler);
    sessionCache.setSessionDataStore(fileSessionDataStore());
    sessionHandler.setSessionCache(sessionCache);
    sessionHandler.setHttpOnly(true);
    // make additional changes to your SessionHandler here
    System.out.println("MY sesssion :"+sessionHandler);
    return sessionHandler;
}

  private static FileSessionDataStore fileSessionDataStore() {
    FileSessionDataStore fileSessionDataStore = new FileSessionDataStore();
    File baseDir = new File(System.getProperty("java.io.tmpdir"));
    File storeDir = new File(baseDir, "javalin-session-store");
    storeDir.mkdir();
    fileSessionDataStore.setStoreDir(storeDir);
    return fileSessionDataStore;
}


}
