package modelo;
import java.sql.*;

public class Conexion {
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/tiendapcdos?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String password = "12345";

            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅Base de datos conectada");
            return conn;

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("❌Error al conectar: " + e.getMessage());
            return null;
        }
    }
}

