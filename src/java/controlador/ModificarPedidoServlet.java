package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Pedido;
import java.io.IOException;
import java.sql.*;
import static modelo.Conexion.getConnection;

@WebServlet("/ModificarPedido")
public class ModificarPedidoServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Pedido pedido = null;
            
            try (Connection conn = getConnection()) {
                // Obtener pedido
                String sql = "SELECT * FROM pedidos WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    pedido = new Pedido();
                    pedido.setId(rs.getInt("id"));
                    pedido.setClienteId(rs.getInt("cliente_id"));
                    pedido.setEstado(rs.getString("estado"));
                    pedido.setTotal(rs.getDouble("total"));
                    pedido.setFechaPedido(rs.getTimestamp("fecha_pedido"));
                }
                
                System.out.println("DEBUG - Pedido cargado: " + (pedido != null ? pedido.getId() : "null"));
            }
            
            if (pedido == null) {
                response.sendRedirect("Pedidos?error=no_encontrado");
                return;
            }
            
            request.setAttribute("pedido", pedido);
            request.getRequestDispatcher("modificarEstadoPedido.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("Pedidos?error=id_invalido");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERROR SQL: " + e.getMessage());
            response.sendRedirect("Pedidos?error=sql");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String estado = request.getParameter("estado");
            
            System.out.println("DEBUG - Modificando estado del pedido ID: " + id + " a " + estado);
            
            try (Connection conn = getConnection()) {
                // Actualizar solo el estado del pedido
                String sqlUpdate = "UPDATE pedidos SET estado = ? WHERE id = ?";
                PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
                psUpdate.setString(1, estado);
                psUpdate.setInt(2, id);
                int rowsUpdated = psUpdate.executeUpdate();
                
                System.out.println("DEBUG - Filas actualizadas: " + rowsUpdated);
                
                response.sendRedirect("Pedidos?mensaje=modificado");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("ERROR - Formato de número inválido: " + e.getMessage());
            response.sendRedirect("Pedidos?error=datos_invalidos");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERROR SQL: " + e.getMessage());
            response.sendRedirect("Pedidos?error=sql");
        }
    }
}