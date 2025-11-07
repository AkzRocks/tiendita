/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.Conexion;
import modelo.Cliente;

@WebServlet("/ModificarCliente")
public class ModificarClienteServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idCliente = request.getParameter("id");
        
        if (idCliente == null || idCliente.trim().isEmpty()) {
            response.sendRedirect("Clientes?error=id_requerido");
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConnection();
            String sql = "SELECT * FROM clientes WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(idCliente));
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombreCliente(rs.getString("nombreCliente"));
                cliente.setEmailCliente(rs.getString("emailCliente"));
                cliente.setTelefonoCliente(rs.getInt("telefonoCliente"));
                try { cliente.setDniRuc(rs.getString("dniRuc")); } catch (Exception ignored) {}
                try { cliente.setDireccion(rs.getString("direccion")); } catch (Exception ignored) {}
                
                request.setAttribute("cliente", cliente);
                request.getRequestDispatcher("editarCliente.jsp").forward(request, response);
            } else {
                response.sendRedirect("Clientes?error=no_encontrado");
            }
            
        } catch (SQLException e) {
            response.sendRedirect("Clientes?error=sql");
        } catch (NumberFormatException e) {
            response.sendRedirect("Clientes?error=id_invalido");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idCliente = request.getParameter("id");
        String nombreCliente = request.getParameter("nombreCliente");
        String emailCliente = request.getParameter("emailCliente");
        String telefonoCliente = request.getParameter("telefonoCliente");
        String dniRuc = request.getParameter("dniRuc");
        String direccion = request.getParameter("direccion");
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = Conexion.getConnection();
            String sql = "UPDATE clientes SET nombreCliente = ?, emailCliente = ?, telefonoCliente = ?, dniRuc = ?, direccion = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombreCliente);
            pstmt.setString(2, emailCliente);
            pstmt.setInt(3, Integer.parseInt(telefonoCliente));
            pstmt.setString(4, dniRuc);
            pstmt.setString(5, direccion);
            pstmt.setInt(6, Integer.parseInt(idCliente));
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                response.sendRedirect("Clientes?mensaje=modificado");
            } else {
                response.sendRedirect("Clientes?error=no_modificado");
            }
            
        } catch (SQLException e) {
            response.sendRedirect("Clientes?error=sql");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
    }
}
