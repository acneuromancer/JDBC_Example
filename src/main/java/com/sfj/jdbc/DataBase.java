package com.sfj.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author corto
 */
public class DataBase {

    final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    final String URL = "jdbc:derby:sampleDB;create=true";
    final String USERNAME = "";
    final String PASSWORD = "";

    // Building the connection ("bridge").
    Connection connection = null;
    Statement statement = null;
    DatabaseMetaData dbmd = null;

    public DataBase() {
        // Try to make alive/animate/vivify the bridge.
        try {
            connection = DriverManager.getConnection(URL);
            System.out.println("Bridge established.");
        } catch (SQLException ex) {
            System.out.println("A problem has occured while trying to build the connection ('Bridge')");
            System.out.println("" + ex);
        }

        // If it is alive, create a van to load.
        if (connection != null) {
            try {
                statement = connection.createStatement();
                System.out.println("The statement has been created successfully.");
            } catch (SQLException ex) {
                System.out.println("A problem has occured while trying to create the statement (the van to load).");
                System.out.println("" + ex);
            }
        }

        // Check whether the database is empty and the data table exists.
        try {
            dbmd = connection.getMetaData();
            System.out.println("Metadata fetched successfully.");
        } catch (SQLException ex) {
            System.out.println("Something is wrong with the database or data table.");
            System.out.println("" + ex);
        }

        try {
            ResultSet resultSet = dbmd.getTables(null, "APP", "USERS", null);
            if (!resultSet.next()) {
                statement.execute("create table users(name varchar(20), age int)");
            }
            System.out.println("Table has been created.");
        } catch (SQLException ex) {
            System.out.println("Something is wrong with the database or data table.");
            System.out.println("" + ex);
        }
    }
    
    public void showUsersMeta() {
        final String SQL = "select * from users";
        ResultSet resultSet = null;
        ResultSetMetaData rsmd = null;
        
        try {
            resultSet = statement.executeQuery(SQL);
            rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(rsmd.getColumnName(i) + " | ");
            }
            System.out.println();
        } catch (SQLException ex) {
            System.out.println("" + ex);
        }
    }
    
    public void addUser(String name, int age) {
        try {
            // final String SQL = "insert into users values ('" + name + "'," +  age + ")";
            // statement.execute(SQL);
            final String SQL = "insert into users values(?, ?)";
            PreparedStatement preparedStatement  = connection.prepareStatement(SQL);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.execute();
            System.out.println("Inserted successfully.");
        } catch (SQLException ex) {
           System.out.println("\nA problem occured while adding a user.");
           System.out.println("" + ex);
        }
    }
    
    public void showAllUsers() {
        final String SQL = "select * from users";
        
        try {
            ResultSet resultSet = statement.executeQuery(SQL);
            while(resultSet.next()) {
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                System.out.println(name + " | " + age);
            }            
        } catch (SQLException ex) {
            System.out.println("\nA problem occured while reading users.");
            System.out.println("" + ex);
        }
    }
    
    public List<User> getAllUsers() {
        final String SQL = "select * from users";
        List<User> users = null;
        
        try {
            ResultSet resultSet = statement.executeQuery(SQL);
            users = new ArrayList<>();
            while(resultSet.next()) {
                User actualUser = new User(resultSet.getString("name"), resultSet.getInt("age"));
                users.add(actualUser);                
            }
        } catch (SQLException ex) {
            System.out.println("A problem occured while reading out users.");
            System.out.println("" + ex);
        }
        
         return users;
    }
    
}
