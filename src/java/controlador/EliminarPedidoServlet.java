package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import static modelo.Conexion.getConnection;

@WebServlet("/EliminarPedido")
public class EliminarPedidoServlet extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            
            try (Connection conn = getConnection()) {
                // Obtener datos del pedido antes de eliminar
                String sqlSelect = "SELECT producto_id, cantidad FROM pedidos WHERE id = ?";
                PreparedStatement psSelect = conn.prepareStatement(sqlSelect);
                psSelect.setInt(1, id);
                ResultSet rs = psSelect.executeQuery();
                
                if (rs.next()) {
                    int productoId = rs.getInt("producto_id");
                    int cantidad = rs.getInt("cantidad");
                    
                    // Devolver stock
                    String sqlStock = "UPDATE productos SET stockProducto = stockProducto + ? WHERE id = ?";
                    PreparedStatement psStock = conn.prepareStatement(sqlStock);
                    psStock.setInt(1, cantidad);
                    psStock.setInt(2, productoId);
                    psStock.executeUpdate();
                    
                    // Eliminar pedido
                    String sqlDelete = "DELETE FROM pedidos WHERE id = ?";
                    PreparedStatement psDelete = conn.prepareStatement(sqlDelete);
                    psDelete.setInt(1, id);
                    psDelete.executeUpdate();
                    
                    response.sendRedirect("Pedidos?mensaje=eliminado");
                } else {
                    response.sendRedirect("Pedidos?error=no_encontrado");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("Pedidos?error=sql");
        }
    }
}