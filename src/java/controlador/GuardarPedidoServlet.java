package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import modelo.Conexion;
import modelo.Producto;

@WebServlet("/GuardarPedidoServlet")
public class GuardarPedidoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Obtener la sesión y verificar usuario
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("rol") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String rol = (String) session.getAttribute("rol");
        Integer clienteId = (Integer) session.getAttribute("clienteId");

        if (clienteId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Obtener los productos seleccionados
        String[] productosIds = request.getParameterValues("productoId");
        String[] cantidades = request.getParameterValues("cantidad");

        if (productosIds == null || cantidades == null || productosIds.length == 0) {
            request.setAttribute("mensajeError", "No se seleccionaron productos para el pedido.");
            request.getRequestDispatcher("carrito.jsp").forward(request, response);
            return;
        }

        Connection conn = null;
        PreparedStatement psPedido = null;
        PreparedStatement psDetalle = null;
        PreparedStatement psActualizarStock = null;
        ResultSet rs = null;

        try {
            conn = Conexion.getConnection();
            conn.setAutoCommit(false); // Inicia transacción

            // 1️⃣ Insertar en la tabla pedidos
            String sqlPedido = "INSERT INTO pedidos (cliente_id, fecha_pedido, estado, total) VALUES (?, NOW(), ?, ?)";
            psPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            psPedido.setInt(1, clienteId);
            psPedido.setString(2, "Pendiente");
            psPedido.setDouble(3, 0.0); // Total inicial (se actualizará luego)
            psPedido.executeUpdate();

            rs = psPedido.getGeneratedKeys();
            int pedidoId = 0;
            if (rs.next()) {
                pedidoId = rs.getInt(1);
            }

            // 2️⃣ Insertar los detalles del pedido
            String sqlDetalle = "INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
            psDetalle = conn.prepareStatement(sqlDetalle);

            String sqlActualizarStock = "UPDATE productos SET stockProducto = stockProducto - ? WHERE id = ?";
            psActualizarStock = conn.prepareStatement(sqlActualizarStock);

            double totalPedido = 0.0;

            for (int i = 0; i < productosIds.length; i++) {
                int productoId = Integer.parseInt(productosIds[i]);
                int cantidad = Integer.parseInt(cantidades[i]);

                // Obtener precio actual del producto
                double precio = obtenerPrecioProducto(conn, productoId);
                double subtotal = precio * cantidad;
                totalPedido += subtotal;

                // Insertar detalle
                psDetalle.setInt(1, pedidoId);
                psDetalle.setInt(2, productoId);
                psDetalle.setInt(3, cantidad);
                psDetalle.setDouble(4, precio);
                psDetalle.setDouble(5, subtotal);
                psDetalle.addBatch();

                // Actualizar stock
                psActualizarStock.setInt(1, cantidad);
                psActualizarStock.setInt(2, productoId);
                psActualizarStock.addBatch();
            }

            psDetalle.executeBatch();
            psActualizarStock.executeBatch();

            // 3️⃣ Actualizar el total del pedido
            String sqlActualizarTotal = "UPDATE pedidos SET total = ? WHERE id = ?";
            try (PreparedStatement psUpdateTotal = conn.prepareStatement(sqlActualizarTotal)) {
                psUpdateTotal.setDouble(1, totalPedido);
                psUpdateTotal.setInt(2, pedidoId);
                psUpdateTotal.executeUpdate();
            }

            conn.commit();

            // 4️⃣ Redirigir según el rol
            if (rol.equals("cliente")) {
                response.sendRedirect("confirmacionPedido.jsp?pedidoId=" + pedidoId);
            } else if (rol.equals("vendedor")) {
                response.sendRedirect("panelVendedor.jsp?msg=pedidoRegistrado");
            } else if (rol.equals("administrador")) {
                response.sendRedirect("panelAdmin.jsp?msg=pedidoRegistrado");
            } else {
                response.sendRedirect("index.jsp");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            request.setAttribute("mensajeError", "Error al guardar el pedido: " + e.getMessage());
            request.getRequestDispatcher("carrito.jsp").forward(request, response);
        } finally {
            try {
                if (rs != null) rs.close();
                if (psPedido != null) psPedido.close();
                if (psDetalle != null) psDetalle.close();
                if (psActualizarStock != null) psActualizarStock.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método auxiliar para obtener el precio actual del producto
    private double obtenerPrecioProducto(Connection conn, int productoId) throws SQLException {
        String sql = "SELECT precioProducto FROM productos WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("precioProducto");
                }
            }
        }
        return 0.0;
    }
}
