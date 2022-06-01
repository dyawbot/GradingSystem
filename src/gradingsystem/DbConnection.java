/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gradingsystem;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author jawar
 */
public class DbConnection {
    
    static final String DB_URL = "jdbc:mysql://localhost/sgs_database";
    static final String USER = "root";
    static final String PASS = "";
    
    static final String ADMIN_URL="jdbc:mysql://localhost/sgs_administrator";
    static final String INIT_URL = "jdbc:mysql://localhost/";
    
    
    
    public static Connection getConnection()
    {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(ADMIN_URL,USER, "");
        } catch (Exception ex) {
            
            System.out.println(ex.getMessage());
            
           
        }
        return con;
    }
    
    public static Boolean getInitConnection()
    {
        Connection con = null;
        boolean ready = true;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(INIT_URL,USER, PASS);
            ready = true;
        } catch (Exception ex) {
            ready = false;
        }
        return ready;
    }
    
    public static Connection getStdConnection()
    {
         Connection con = null;

         try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            
            con = DriverManager.getConnection(DB_URL,USER, PASS);
        } catch (Exception ex) {
            
            System.out.println(ex.getMessage());
        }
        return con;
    }
    public void creatingTableForAdmin()
    {
         try(Connection conn = DriverManager.getConnection(ADMIN_URL, USER, PASS);
         Statement stmt = conn.createStatement();
      ) {		      
         String sql = "CREATE TABLE IF NOT EXISTS admin" +
                   "(id INTEGER not NULL AUTO_INCREMENT, " +
                   "username VARCHAR(255), " + 
                   "password VARCHAR(255), " +
                   " PRIMARY KEY ( id ))"; 

         stmt.executeUpdate(sql);
         System.out.println("Database created successfully...");   	  
      } catch (SQLException e) {
         e.printStackTrace();
      } 
    }
    
     public void creatingAdminDb()
            
    {
        try(Connection conn = DriverManager.getConnection(INIT_URL, USER, PASS);
         Statement stmt = conn.createStatement();
      ) {		      
         String sql = "CREATE DATABASE IF NOT EXISTS sgs_administrator";
         stmt.executeUpdate(sql);
         System.out.println("Database created successfully...");   	  
      } catch (SQLException e) {
         e.printStackTrace();
      } 
    }
     
     
    
    public void creatingDb()
            
    {
        try(Connection conn = DriverManager.getConnection(INIT_URL, USER, PASS);
         Statement stmt = conn.createStatement();
      ) {		      
         String sql = "CREATE DATABASE IF NOT EXISTS sgs_database";
         stmt.executeUpdate(sql);
         System.out.println("Database created successfully...");   	  
      } catch (Exception e) {
         e.printStackTrace();
      } 
    }
    
    
    
    public void creatingAdminDbSgs()
            
    {
        try(Connection conn = DriverManager.getConnection(INIT_URL, USER, PASS);
         Statement stmt = conn.createStatement();
      ) {		      
         String sql = "CREATE DATABASE IF NOT EXISTS sgs";
         stmt.executeUpdate(sql);
         System.out.println("Database created successfully...");   	  
      } catch (SQLException e) {
         e.printStackTrace();
      } 
    }
    
    
    public void creatingTableForStudent(String _id)
    {
         try(Connection conn = DriverManager.getConnection(DB_URL, USER, "");
         Statement stmt = conn.createStatement();
      ) {		      
         String sql = "CREATE TABLE IF NOT EXISTS tb_" + _id +
                   "(id INTEGER not NULL AUTO_INCREMENT, " +
                   "sem VARCHAR(10), " + 
                   " filipino VARCHAR(255), " + 
                   " english VARCHAR(255), " + 
                   " math VARCHAR(255), " + 
                   " science VARCHAR(255), " + 
                   " pe VARCHAR(255), " + 
                   " programming VARCHAR(255), " + 
                   " PRIMARY KEY ( id ))"; 

         stmt.executeUpdate(sql);
         System.out.println("Database created successfully...");   	  
      } catch (SQLException e) {
         e.printStackTrace();
      } 
    }
    
     public void creatingTableForAdding()
    {
         try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
         Statement stmt = conn.createStatement();
      ) {		      
         String sql = "CREATE TABLE IF NOT EXISTS tb_addingdata" +
                   "(id INT NOT NULL AUTO_INCREMENT, " +
                   " std_id VARCHAR(255), " + 
                   " std_name VARCHAR(255), " + 
                   " std_level VARCHAR(255), " + 
                   " std_curriculum VARCHAR(255), " + 
                   " std_remarks VARCHAR(255), " + 
                   " PRIMARY KEY ( id ))"; 

         stmt.executeUpdate(sql);
         System.out.println("Database created successfully...");   	  
      } catch (SQLException e) {
         e.printStackTrace();
      } 
    }
    public void insertingDbInAddingStd(String _id, String _nameOfStudent, String _level, String _curriculum)
    {
        
        try (Connection con =DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = con.createStatement();){
            String query = "INSERT INTO tb_addingdata (std_id, std_name, std_level, std_curriculum) VALUES ('"+_id + "','"+
                    _nameOfStudent + "','" + _level + "','" + _curriculum + "')";
            
            stmt.executeUpdate(query);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "We can insert the data, here is the error: " + e.getMessage());
        }
        
    }
    
    public void insertingGradesOfStudents(String _id,String _sem, String _filipino, String _english,
            String _math, String _science,String _pe, String _programming)
    {
        try (Connection con =DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = con.createStatement();){
            String query = "INSERT INTO tb_" + _id +" (sem, filipino, english, math, science,pe, programming) VALUES ('"+_sem+ "','"+_filipino+ "','"+
                    _english + "','" + _math + "','" + _science+ "','" + _pe+ "','" + _programming+ "')";
            
            stmt.executeUpdate(query);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "We can insert the data, here is the error: " + e.getMessage());
        }
    }
    
    
    
    public static Connection selectingName()
    {
        Connection con = null;
        try {
             con = DriverManager.getConnection(DB_URL,USER, PASS);
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, "Can't connect");
        }
        return con;
    }
    
}
