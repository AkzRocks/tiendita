package modelo;
import java.sql.*;

public class Conexion {
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/tiendapcdos";
            String user = "root";
            String password = System.getenv("MYSQL_PASSWORD"); // Variable de entorno

            if (password == null) {
                System.out.println("⚠️ La variable de entorno MYSQL_PASSWORD no está definida.");
                return null;
            }

            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Base de datos conectada");
            return conn;

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("❌ Error al conectar: " + e.getMessage());
            return null;
        }
    }
}
