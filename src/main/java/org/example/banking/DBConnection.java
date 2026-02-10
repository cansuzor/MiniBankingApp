package org.example.banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection con;

    public static Connection getConnection() {
        if (con != null) {
            return con; // return existing connection if already created
        }

        try {
            String mysqlJDBCDriver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/BANK";
            String user = "root";
            String pass = "";

            // Load the driver
           Class.forName(mysqlJDBCDriver);

            // Create connection
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("✅ Connected to BANK database!");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Connection failed!");
            e.printStackTrace();
        }

        return con;
    }
}
