package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

public class LoginServlet extends HttpServlet {

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/tu_bd";
        String user = "tu_usuario";
        String password = "tu_password";
        return DriverManager.getConnection(url, user, password);
    }

    private String obtenerRol(String username, String password) {
        String sql = "SELECT rol FROM usuarios WHERE username=? AND password=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("rol");
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rol = obtenerRol(username, password);
        if (rol == null) {
            request.setAttribute("errorMessage", "Usuario o contraseña incorrectos");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.getSession().setAttribute("rol", rol);
            request.getSession().setAttribute("username", username);
            switch (rol) {
                case "administrador": response.sendRedirect("adminPanel.jsp"); break;
                case "vendedor": response.sendRedirect("vendedorPanel.jsp"); break;
                case "cliente": response.sendRedirect("clientePanel.jsp"); break;
                default: response.sendRedirect("login.jsp"); break;
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { response.sendRedirect("login.jsp"); }
}
