package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import static modelo.Conexion.getConnection;

@WebServlet("/GuardarPedido")
public class GuardarPedidoServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int clienteId = Integer.parseInt(request.getParameter("clienteId"));
            double total = Double.parseDouble(request.getParameter("total"));
            
            // Obtener arrays de productos
            String[] productoIds = request.getParameterValues("productoId[]");
            String[] cantidades = request.getParameterValues("cantidad[]");
            String[] precios = request.getParameterValues("precio[]");
            String[] subtotales = request.getParameterValues("subtotal[]");
            
            System.out.println("DEBUG - Cliente ID: " + clienteId);
            System.out.println("DEBUG - Total: " + total);
            System.out.println("DEBUG - Número de productos: " + (productoIds != null ? productoIds.length : 0));
            
            if (productoIds == null || productoIds.length == 0) {
                response.sendRedirect("Pedidos?error=sin_productos");
                return;
            }
            
            try (Connection conn = getConnection()) {
                conn.setAutoCommit(false);
                
                try {
                    // 1. Insertar pedido principal
                    String sqlPedido = "INSERT INTO pedidos (cliente_id, fecha_pedido, estado, total) VALUES (?, NOW(), 'Pendiente', ?)";
                    PreparedStatement psPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
                    psPedido.setInt(1, clienteId);
                    psPedido.setDouble(2, total);
                    psPedido.executeUpdate();
                    
                    // Obtener el ID del pedido generado
                    ResultSet rs = psPedido.getGeneratedKeys();
                    int pedidoId = 0;
                    if (rs.next()) {
                        pedidoId = rs.getInt(1);
                    }
                    
                    System.out.println("DEBUG - Pedido creado con ID: " + pedidoId);
                    
                    // 2. Insertar cada producto en pedido_detalle y actualizar stock
                    for (int i = 0; i < productoIds.length; i++) {
                        if (productoIds[i] != null && !productoIds[i].isEmpty()) {
                            int productoId = Integer.parseInt(productoIds[i]);
                            int cantidad = Integer.parseInt(cantidades[i]);
                            double precio = Double.parseDouble(precios[i]);
                            double subtotal = Double.parseDouble(subtotales[i]);
                            
                            System.out.println("DEBUG - Producto " + (i+1) + ": ID=" + productoId + ", Cant=" + cantidad + ", Precio=" + precio);
                            
                            // Verificar stock disponible
                            String sqlStock = "SELECT stockProducto FROM productos WHERE id = ?";
                            PreparedStatement psStock = conn.prepareStatement(sqlStock);
                            psStock.setInt(1, productoId);
                            ResultSet rsStock = psStock.executeQuery();
                            
                            if (rsStock.next()) {
                                int stockDisponible = rsStock.getInt("stockProducto");
                                if (stockDisponible < cantidad) {
                                    conn.rollback();
                                    response.sendRedirect("Pedidos?error=sin_stock");
                                    return;
                                }
                            }
                            
                            // Insertar detalle del pedido
                            String sqlDetalle = "INSERT INTO pedido_detalle (pedido_id, producto_id, cantidad, precio, subtotal) VALUES (?, ?, ?, ?, ?)";
                            PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle);
                            psDetalle.setInt(1, pedidoId);
                            psDetalle.setInt(2, productoId);
                            psDetalle.setInt(3, cantidad);
                            psDetalle.setDouble(4, precio);
                            psDetalle.setDouble(5, subtotal);
                            psDetalle.executeUpdate();
                            
                            // Actualizar stock del producto
                            String sqlUpdateStock = "UPDATE productos SET stockProducto = stockProducto - ? WHERE id = ?";
                            PreparedStatement psUpdateStock = conn.prepareStatement(sqlUpdateStock);
                            psUpdateStock.setInt(1, cantidad);
                            psUpdateStock.setInt(2, productoId);
                            psUpdateStock.executeUpdate();
                            
                            System.out.println("DEBUG - Stock actualizado para producto " + productoId);
                        }
                    }
                    
                    conn.commit();
                    System.out.println("DEBUG - Pedido guardado exitosamente");
                    response.sendRedirect("Pedidos?mensaje=creado");
                    
                } catch (Exception e) {
                    conn.rollback();
                    e.printStackTrace();
                    System.out.println("ERROR - Rollback ejecutado: " + e.getMessage());
                    throw e;
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("ERROR - Formato de número inválido: " + e.getMessage());
            response.sendRedirect("Pedidos?error=datos_invalidos");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERROR SQL: " + e.getMessage());
            response.sendRedirect("Pedidos?error=sql");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR general: " + e.getMessage());
            response.sendRedirect("Pedidos?error=general");
        }
    }
}