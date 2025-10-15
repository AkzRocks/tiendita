package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;
import java.util.*;
import modelo.Conexion;
import modelo.Cliente;

@WebServlet("/Clientes")
public class ClienteServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Cliente> clientes = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM clientes");
            
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombreCliente(rs.getString("nombreCliente"));
                cliente.setEmailCliente(rs.getString("emailCliente"));
                cliente.setTelefonoCliente(rs.getInt("telefonoCliente"));
                clientes.add(cliente);
            }
            
            request.setAttribute("clientes", clientes);
            
        } catch (SQLException e) {
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
        
        request.getRequestDispatcher("listar.jsp").forward(request, response);
    }
}