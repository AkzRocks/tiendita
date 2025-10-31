package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Pedido;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static modelo.Conexion.getConnection;

@WebServlet("/VerDetallePedido")
public class VerDetallePedidoServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int pedidoId = Integer.parseInt(request.getParameter("id"));
            
            Pedido pedido = null;
            List<Map<String, Object>> detalles = new ArrayList<>();
            
            try (Connection conn = getConnection()) {
                
                // Obtener información del pedido
                String sqlPedido = "SELECT p.*, c.nombreCliente, c.emailCliente, c.telefonoCliente " +
                                  "FROM pedidos p " +
                                  "JOIN clientes c ON p.cliente_id = c.id " +
                                  "WHERE p.id = ?";
                PreparedStatement psPedido = conn.prepareStatement(sqlPedido);
                psPedido.setInt(1, pedidoId);
                ResultSet rsPedido = psPedido.executeQuery();
                
                if (rsPedido.next()) {
                    pedido = new Pedido();
                    pedido.setId(rsPedido.getInt("id"));
                    pedido.setClienteId(rsPedido.getInt("cliente_id"));
                    pedido.setFechaPedido(rsPedido.getTimestamp("fecha_pedido"));
                    pedido.setEstado(rsPedido.getString("estado"));
                    pedido.setTotal(rsPedido.getDouble("total"));
                    pedido.setNombreCliente(rsPedido.getString("nombreCliente"));
                }
                
                // Obtener detalles del pedido
                String sqlDetalle = "SELECT pd.*, pr.nombreProducto " +
                                   "FROM pedido_detalle pd " +
                                   "JOIN productos pr ON pd.producto_id = pr.id " +
                                   "WHERE pd.pedido_id = ?";
                PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle);
                psDetalle.setInt(1, pedidoId);
                ResultSet rsDetalle = psDetalle.executeQuery();
                
                while (rsDetalle.next()) {
                    Map<String, Object> detalle = new HashMap<>();
                    detalle.put("id", rsDetalle.getInt("id"));
                    detalle.put("productoId", rsDetalle.getInt("producto_id"));
                    detalle.put("nombreProducto", rsDetalle.getString("nombreProducto"));
                    detalle.put("cantidad", rsDetalle.getInt("cantidad"));
                    detalle.put("precio", rsDetalle.getDouble("precio"));
                    detalle.put("subtotal", rsDetalle.getDouble("subtotal"));
                    detalles.add(detalle);
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("error", "sql");
            }
            
            request.setAttribute("pedido", pedido);
            request.setAttribute("detalles", detalles);
            request.getRequestDispatcher("verDetallePedido.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("Pedidos?error=id_invalido");
        }
    }
}