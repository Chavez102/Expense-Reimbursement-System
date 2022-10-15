package com.Revature.util;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
  
  public ConnectionFactory() {}
  
  
  
  public static Connection getConnection() throws SQLException {
    Properties props = new Properties();
    try(FileReader reader = new FileReader("src/main/resources/application.properties")){
      props.load(reader);
    }catch (Exception e) {
      e.printStackTrace();
    }
    
    Connection conn = DriverManager
        .getConnection(props.getProperty("url"),
        props.getProperty("username"), 
        props.getProperty("password")
        );
    
    return conn;
    
  }
  
 
}
