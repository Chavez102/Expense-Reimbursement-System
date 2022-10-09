package com.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {
  public Database() {}
  
  public void insertTicket(Ticket ticket){
    Connection c = null;
    Statement stmt = null;
    try {

       c = DriverManager
          .getConnection("jdbc:postgresql://localhost:5432/mydatabase",
          "postgres", "password");
       System.out.println("Opened database successfully");

       stmt = c.createStatement();
       String sql = "INSERT INTO Tickets (Status, Amount, Description) "
                   +"VALUES('" + ticket.getStatus()+"'"
                   + ", '"+Double.toString(ticket.getAmount())+"'"
                   + ", '"+ticket.getDescription() + "'"+
                   ");";
       stmt.executeUpdate(sql);
       stmt.close();
       c.close();
    } catch ( Exception e ) {
       System.err.println( e.getClass().getName()+": "+ e.getMessage() );
       System.exit(0);
    }
  }
  

}
