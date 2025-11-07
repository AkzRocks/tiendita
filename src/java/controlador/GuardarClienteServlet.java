package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;
import modelo.Conexion;

@WebServlet("/GuardarCliente")
public class GuardarClienteServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nombreCliente = request.getParameter("nombreCliente");
        String emailCliente = request.getParameter("emailCliente");
        String telefonoCliente = request.getParameter("telefonoCliente");
        String dniRuc = request.getParameter("dniRuc");
        String direccion = request.getParameter("direccion");
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = Conexion.getConnection();
            String sql = "INSERT INTO clientes (nombreCliente, emailCliente, telefonoCliente, dniRuc, direccion) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombreCliente);
            pstmt.setString(2, emailCliente);
            pstmt.setInt(3, Integer.parseInt(telefonoCliente));
            pstmt.setString(4, dniRuc);
            pstmt.setString(5, direccion);
            pstmt.executeUpdate();
            
            response.sendRedirect("Clientes");
            
        } catch (IOException | NumberFormatException | SQLException e) {
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }
}