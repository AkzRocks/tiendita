package modelo;

import java.sql.*;

public class Conexion {
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tiendapcdos", 
                "root", 
                "alejandro23"
            );
            System.out.println("Base de datos conectada");
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al conectar" + e.getMessage());
            return null;
        }
    }
}