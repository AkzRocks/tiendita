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
import modelo.Producto;

@WebServlet("/ModificarProducto")
public class ModificarProductoServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idProducto = request.getParameter("id");
        
        if (idProducto == null || idProducto.trim().isEmpty()) {
            response.sendRedirect("Productos?error=id_requerido");
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConnection();
            String sql = "SELECT * FROM productos WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(idProducto));
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombreProducto(rs.getString("nombreProducto"));
                producto.setPrecioProducto(rs.getDouble("precioProducto"));
                producto.setStockProducto(rs.getInt("stockProducto"));
                
                request.setAttribute("producto", producto);
                request.getRequestDispatcher("editarProducto.jsp").forward(request, response);
            } else {
                response.sendRedirect("Productos?error=no_encontrado");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("Productos?error=sql");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("Productos?error=id_invalido");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idProducto = request.getParameter("id");
        String nombreProducto = request.getParameter("nombreProducto");
        String precioProducto = request.getParameter("precioProducto");
        String stockProducto = request.getParameter("stockProducto");
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = Conexion.getConnection();
            String sql = "UPDATE productos SET nombreProducto = ?, precioProducto = ?, stockProducto = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombreProducto);
            pstmt.setDouble(2, Double.parseDouble(precioProducto));
            pstmt.setInt(3, Integer.parseInt(stockProducto));
            pstmt.setInt(4, Integer.parseInt(idProducto));
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                response.sendRedirect("Productos?mensaje=modificado");
            } else {
                response.sendRedirect("Productos?error=no_modificado");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("Productos?error=sql");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}