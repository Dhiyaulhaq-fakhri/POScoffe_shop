/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Lenovo
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class koneksi {

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/db_coffeeshop";
        String user = "root";
        String pass = "";

        return DriverManager.getConnection(url, user, pass);
    }
}

