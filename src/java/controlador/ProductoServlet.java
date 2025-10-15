package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.*;
import modelo.Conexion;
import modelo.Producto;


@WebServlet("/Productos")
public class ProductoServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Producto> productos = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM productos");
            
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombreProducto(rs.getString("nombreProducto"));
                producto.setPrecioProducto(rs.getDouble("precioProducto"));
                producto.setStockProducto(rs.getInt("stockProducto"));
                productos.add(producto);
            }
            
            request.setAttribute("productos", productos);
            
        } catch (SQLException e) {
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
            }
        }
        
        request.getRequestDispatcher("listarProductos.jsp").forward(request, response);
    }
}