package com.Models;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.eclipse.jetty.server.session.DatabaseAdaptor;
import org.eclipse.jetty.server.session.DefaultSessionCache;
import org.eclipse.jetty.server.session.FileSessionDataStore;
import org.eclipse.jetty.server.session.JDBCSessionDataStoreFactory;
import org.eclipse.jetty.server.session.SessionCache;
import org.eclipse.jetty.server.session.SessionHandler;

public class Database implements Serializable{
  public Database() {}
  
  public void insertTicket(Employee employee,Ticket ticket){
    Connection c = null;
    Statement stmt = null;
    try {

       c = DriverManager
          .getConnection("jdbc:postgresql://localhost:5432/mydatabase",
          "postgres", "password");
       System.out.println("Opened database successfully");

       stmt = c.createStatement();
       String sql = "INSERT INTO Tickets (Status, Amount, Description,employeeusername) "
                   +"VALUES('" + ticket.getStatus()+"'"
                   + ", '"+Double.toString(ticket.getAmount())+"'"
                   + ", '"+ticket.getDescription() + "'"
                   + ", '"+ employee.getUserName() +"'"
                   +");";
       stmt.executeUpdate(sql);
       stmt.close();
       c.close();
    } catch ( Exception e ) {
       System.err.println( e.getClass().getName()+": "+ e.getMessage() );
       System.exit(0);
    }
  }
  
  public boolean insertUser(User user){
    Connection c = null;
    Statement stmt = null;
    try {

       c = DriverManager
          .getConnection("jdbc:postgresql://localhost:5432/mydatabase",
          "postgres", "password");
       System.out.println("Opened database successfully");

       stmt = c.createStatement();
 
       String sql= "INSERT INTO Users (UserName,passsword,role) VALUES("
           + SQLquote(user.getUserName())
           + ","+SQLquote(user.getPassword())
           + ","+SQLquote(user.getRole())
           + ");";
       
       sql= "INSERT INTO Employees (UserName) VALUES("
           + SQLquote(user.getUserName()) 
           + ");";
       
       stmt.executeUpdate(sql);
       stmt.close();
       c.close();
    } catch ( Exception e ) {
        return false;
//       System.err.println( e.getClass().getName()+": "+ e.getMessage() ); 
        }
    
    return true;
  }
  
  public String SQLquote(String word) {
    return "'"+word+"'";
  }
  
  public static SessionHandler sqlSessionHandler(String driver, String url) {
    SessionHandler sessionHandler = new SessionHandler();
    SessionCache sessionCache = new DefaultSessionCache(sessionHandler);
    sessionCache.setSessionDataStore(
        jdbcDataStoreFactory(driver, url).getSessionDataStore(sessionHandler)
    );
    sessionHandler.setSessionCache(sessionCache);
    sessionHandler.setHttpOnly(true);
    // make additional changes to your SessionHandler here
    return sessionHandler;
}

  private static JDBCSessionDataStoreFactory jdbcDataStoreFactory(String driver, String url) {
    DatabaseAdaptor databaseAdaptor = new DatabaseAdaptor();
    databaseAdaptor.setDriverInfo(driver, url);
    // databaseAdaptor.setDatasource(myDataSource); // you can set data source here (for connection pooling, etc)
    JDBCSessionDataStoreFactory jdbcSessionDataStoreFactory = new JDBCSessionDataStoreFactory();
    jdbcSessionDataStoreFactory.setDatabaseAdaptor(databaseAdaptor);
    return jdbcSessionDataStoreFactory;
}
  
  public static SessionHandler fileSessionHandler() {
    SessionHandler sessionHandler = new SessionHandler();
    SessionCache sessionCache = new DefaultSessionCache(sessionHandler);
    sessionCache.setSessionDataStore(fileSessionDataStore());
    sessionHandler.setSessionCache(sessionCache);
    sessionHandler.setHttpOnly(true);
    // make additional changes to your SessionHandler here
   
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
