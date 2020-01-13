package com.example.rest;

import java.sql.*;

public class DBUtil {
	
	 	private static String url = "jdbc:mysql://140.112.12.252:3306/hotelsystem?serverTimezone=UTC";    
	    private static String driverName = "com.mysql.cj.jdbc.Driver";   
	    private static String username = "OOAD";   
	    private static String password = "caece";
    	private static Connection con;

	    public static Connection getConnection() {
	        try {
	            Class.forName(driverName);
	            
	            try {
	                con = DriverManager.getConnection(url, username, password);
	            } catch (SQLException ex) {
	                
	                System.out.println("Failed to create the database connection."); 
	            }
	        } catch (ClassNotFoundException ex) {
	            // log an exception. for example:
	            System.out.println("Driver not found."); 
	        }
	        return con;
	    }
	    
}
