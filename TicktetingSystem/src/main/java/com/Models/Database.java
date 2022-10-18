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
  
  public void executeStatement(String str, String...args) {
    
    try(Connection c = ConnectionFactory.getConnection())  
    {   
      stmt = c.prepareStatement(str);
      
      int i=1;
      for (String arg:args) {  
        double d;
        try
        {
          d =Double.parseDouble(arg);
          stmt.setDouble(i, d ); 
        }
        catch(NumberFormatException e)
        {
          //arg not double
          stmt.setString(i, arg ); 
        } 
        i++;
      } 
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
  
  
  
  public List<User> executeFindUsersStatemetn(String str,String...args) {
    
    List<User> userList = new ArrayList<>();
    ResultSet set = null; 

    try(Connection c = ConnectionFactory.getConnection())  
    {   
      stmt = c.prepareStatement(str);
      
      int i=1;
      for (String arg:args) {   
        double d;
        try
        {
          d =Double.parseDouble(arg);
          stmt.setDouble(i, d ); 
        }
        catch(NumberFormatException e)
        {
          //arg not double
          stmt.setString(i, arg ); 
        } 
        i++;
      } 
      set = stmt.executeQuery();
      
      while (set.next()) {
        userList.add(
                new User(set.getString(1), set.getString(2) )
         );
      }
    
   }catch(SQLException ex) {
       System.out.println("error 12");
       ex.printStackTrace();
   } finally { 
       try {
         stmt.close();  
         set.close();
       } catch (SQLException e1) { 
        e1.printStackTrace();
       }
   }
   return userList;
    
  }
  
  public List<Employee> executeFindEmployeesStatemetn(String str,String...args) {
    
    List<Employee> employeeList = new ArrayList<>();
    ResultSet set = null; 

    try(Connection c = ConnectionFactory.getConnection())  
    {   
      stmt = c.prepareStatement(str);
      
      int i=1;
      for (String arg:args) {   
        double d;
        try
        {
          d =Double.parseDouble(arg);
          stmt.setDouble(i, d ); 
        }
        catch(NumberFormatException e)
        {
          //arg not double
          stmt.setString(i, arg ); 
        } 
        i++;
      } 
      set = stmt.executeQuery();
      
      while (set.next()) {
        employeeList.add(
                new Employee( set.getString(1) ) 
         );
      }
    
   }catch(SQLException ex) {
       System.out.println("error 12");
       ex.printStackTrace();
   } finally { 
       try {
         stmt.close();  
         set.close();
       } catch (SQLException e1) { 
        e1.printStackTrace();
       }
   }
   return employeeList;
    
  }
  
  public List<Manager> executeFindManagerssStatemetn(String str,String...args) {
    
    List<Manager> managerList = new ArrayList<>();
    ResultSet set = null; 

    try(Connection c = ConnectionFactory.getConnection())  
    {   
      stmt = c.prepareStatement(str);
      
      int i=1;
      for (String arg:args) {   
        double d;
        try
        {
          d =Double.parseDouble(arg);
          stmt.setDouble(i, d ); 
        }
        catch(NumberFormatException e)
        {
          //arg not double
          stmt.setString(i, arg ); 
        } 
        i++;
      } 
      set = stmt.executeQuery();
      
      while (set.next()) {
        managerList.add(
                new Manager( set.getString(1) ) 
         );
      }
    
   }catch(SQLException ex) {
       System.out.println("error 12");
       ex.printStackTrace();
   } finally { 
       try {
         stmt.close();  
         set.close();
       } catch (SQLException e1) { 
        e1.printStackTrace();
       }
   }
   return managerList;
    
  }
  

  public List<Ticket> executeFindTicketsStatemetn(String str,String...args) {
    
    List<Ticket> ticketList = new ArrayList<>();
    ResultSet set = null; 

    try(Connection c = ConnectionFactory.getConnection())  
    {   
      stmt = c.prepareStatement(str);
      
      int i=1;
      for (String arg:args) {   
        double d;
        try
        {
          d =Double.parseDouble(arg);
          stmt.setDouble(i, d ); 
        }
        catch(NumberFormatException e)
        {
          //arg not double
          stmt.setString(i, arg ); 
        } 
        i++;
      } 
      set = stmt.executeQuery();
      
      while (set.next()) {
        ticketList.add( new Ticket( 
                          set.getString(3),               //status
                          set.getString(5),               //description
                          set.getDouble(4),               //amount
                          set.getInt(1),                   //Ticket id
                          set.getString(2)                 //Employee username
                       ) 
         );
      }
    
   }catch(SQLException ex) {
       System.out.println("error 12");
       ex.printStackTrace();
   } finally { 
       try {
         stmt.close();  
         set.close();
       } catch (SQLException e1) { 
        e1.printStackTrace();
       }
   }
   return ticketList ; 
  }
   
  
  public void deletePendingTicket(int ticketId){ 
    
    executeStatement("DELETE FROM PendingTickets WHERE TicketId=?", Integer.toString(ticketId));

  }
  
  public  Ticket getPendingTicket(int ticketId){ 
   
    List<Ticket> ticketList= executeFindTicketsStatemetn("SELECT * FROM PendingTickets WHERE ticketid=?",Integer.toString(ticketId));
    return ticketList.get(0);

  }
  
  public void insertPendingTicket(Employee employee,Ticket ticket){ 
    
    String str=  "INSERT INTO PendingTickets (Status, Amount, Description,employeeusername) VALUES (?,?,?,?)";
    executeStatement(str,
        ticket.getStatus(),
        Double.toString( ticket.getAmount() ),
        ticket.getDescription(),
        employee.getUserName()
        ); 

  }
  
  public void insertProcessedTicket(String employeeUsername,Ticket ticket){ 
    
    String str=  "INSERT INTO ProcessedTickets (TicketId, Status, Amount, Description,employeeusername) VALUES(?,?,?,?,?)";
    executeStatement(str,
        Integer.toString(ticket.getId()),
        ticket.getStatus(),
        Double.toString( ticket.getAmount() ),
        ticket.getDescription(),
        employeeUsername
        ); 
  }
  
  public void insertUser(User user){
    String str=  "INSERT INTO Users (UserName,passsword,role) VALUES(?,?,?)";
    executeStatement(str,
        user.getUserName() ,
        user.getPassword(),
        user.getRole()
        ); 
 }
  
  
  public void insertEmployee(Employee e) {
    String str=  "INSERT INTO Employees (UserName) VALUES(?)";
    executeStatement(str, e.getUserName() );  
  }
  
  public void insertManager(Manager m) {
    
    String str=  "INSERT INTO Managers (UserName) VALUES(?)";
    executeStatement(str,  m.getUserName()  );   
  }
  
  
 
  public boolean isUserNameValid(String username) { 
    
    List<User> users= executeFindUsersStatemetn("SELECT * FROM users WHERE username = ? "
                                            ,username);
    
    return !users.isEmpty() ; 
  }
  
  public boolean isEmployee(String username) { 
    
    List<Employee> users= executeFindEmployeesStatemetn("SELECT * FROM employees WHERE username = ? "
        ,username);

    return !users.isEmpty() ; 
 
}
  
  public boolean isManager(String username) { 
    
    List<Manager> managers= executeFindManagerssStatemetn("SELECT * FROM managers WHERE username = ? "
        ,username);

    return !managers.isEmpty() ; 
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
