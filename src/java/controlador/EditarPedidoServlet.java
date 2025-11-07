package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Pedido;
import modelo.Producto;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import static modelo.Conexion.getConnection;

@WebServlet("/EditarPedido")
public class EditarPedidoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int pedidoId = Integer.parseInt(request.getParameter("id"));

            Pedido pedido = null;
            List<Map<String, Object>> detalles = new ArrayList<>();
            List<Producto> productos = new ArrayList<>();

            try (Connection conn = getConnection()) {
                // Pedido con info de cliente
                String sqlPedido = "SELECT p.*, c.nombreCliente, c.dniRuc, c.direccion " +
                        "FROM pedidos p JOIN clientes c ON p.cliente_id = c.id WHERE p.id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sqlPedido)) {
                    ps.setInt(1, pedidoId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            pedido = new Pedido();
                            pedido.setId(rs.getInt("id"));
                            pedido.setClienteId(rs.getInt("cliente_id"));
                            pedido.setFechaPedido(rs.getTimestamp("fecha_pedido"));
                            pedido.setEstado(rs.getString("estado"));
                            pedido.setTotal(rs.getDouble("total"));
                            pedido.setNombreCliente(rs.getString("nombreCliente"));
                            try { pedido.setDniRuc(rs.getString("dniRuc")); } catch (Exception ignored) {}
                            try { pedido.setDireccion(rs.getString("direccion")); } catch (Exception ignored) {}
                        }
                    }
                }

                if (pedido == null) {
                    response.sendRedirect("Pedidos?error=no_encontrado");
                    return;
                }

                // Detalle actual del pedido
                String sqlDetalle = "SELECT pd.*, pr.nombreProducto " +
                        "FROM pedido_detalle pd JOIN productos pr ON pd.producto_id = pr.id " +
                        "WHERE pd.pedido_id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sqlDetalle)) {
                    ps.setInt(1, pedidoId);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            Map<String, Object> d = new HashMap<>();
                            d.put("id", rs.getInt("id"));
                            d.put("productoId", rs.getInt("producto_id"));
                            d.put("nombreProducto", rs.getString("nombreProducto"));
                            d.put("cantidad", rs.getInt("cantidad"));
                            d.put("precio", rs.getDouble("precio"));
                            d.put("subtotal", rs.getDouble("subtotal"));
                            detalles.add(d);
                        }
                    }
                }

                // Productos disponibles (con stock)
                String sqlProductos = "SELECT id, nombreProducto, precioProducto, stockProducto FROM productos WHERE stockProducto > 0";
                try (PreparedStatement ps = conn.prepareStatement(sqlProductos);
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Producto p = new Producto();
                        p.setId(rs.getInt("id"));
                        p.setNombreProducto(rs.getString("nombreProducto"));
                        p.setPrecioProducto(rs.getDouble("precioProducto"));
                        p.setStockProducto(rs.getInt("stockProducto"));
                        productos.add(p);
                    }
                }
            }

            request.setAttribute("pedido", pedido);
            request.setAttribute("detalles", detalles);
            request.setAttribute("productos", productos);
            request.getRequestDispatcher("editarPedido.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("Pedidos?error=id_invalido");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("Pedidos?error=sql");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int pedidoId = Integer.parseInt(request.getParameter("id"));
            String estado = request.getParameter("estado");

            String[] productoIds = request.getParameterValues("productoId[]");
            String[] cantidades = request.getParameterValues("cantidad[]");
            String[] precios = request.getParameterValues("precio[]");
            String[] subtotales = request.getParameterValues("subtotal[]");

            try (Connection conn = getConnection()) {
                conn.setAutoCommit(false);
                try {
                    // Insertar nuevas líneas si vinieron
                    if (productoIds != null) {
                        for (int i = 0; i < productoIds.length; i++) {
                            if (productoIds[i] == null || productoIds[i].isEmpty()) continue;
                            int productoId = Integer.parseInt(productoIds[i]);
                            int cantidad = Integer.parseInt(cantidades[i]);
                            double precio = Double.parseDouble(precios[i]);
                            double subtotal = Double.parseDouble(subtotales[i]);

                            // Verificar stock
                            try (PreparedStatement ps = conn.prepareStatement("SELECT stockProducto FROM productos WHERE id = ?")) {
                                ps.setInt(1, productoId);
                                try (ResultSet rs = ps.executeQuery()) {
                                    if (rs.next()) {
                                        int stock = rs.getInt(1);
                                        if (stock < cantidad) {
                                            conn.rollback();
                                            response.sendRedirect("EditarPedido?id=" + pedidoId + "&error=sin_stock");
                                            return;
                                        }
                                    }
                                }
                            }

                            // Insertar detalle
                            try (PreparedStatement ps = conn.prepareStatement(
                                    "INSERT INTO pedido_detalle (pedido_id, producto_id, cantidad, precio, subtotal) VALUES (?, ?, ?, ?, ?)")) {
                                ps.setInt(1, pedidoId);
                                ps.setInt(2, productoId);
                                ps.setInt(3, cantidad);
                                ps.setDouble(4, precio);
                                ps.setDouble(5, subtotal);
                                ps.executeUpdate();
                            }

                            // Actualizar stock
                            try (PreparedStatement ps = conn.prepareStatement(
                                    "UPDATE productos SET stockProducto = stockProducto - ? WHERE id = ?")) {
                                ps.setInt(1, cantidad);
                                ps.setInt(2, productoId);
                                ps.executeUpdate();
                            }
                        }
                    }

                    // Actualizar estado del pedido
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE pedidos SET estado = ? WHERE id = ?")) {
                        ps.setString(1, estado);
                        ps.setInt(2, pedidoId);
                        ps.executeUpdate();
                    }

                    // Recalcular total: sum(subtotal)*1.18
                    double sumaSubtotales = 0.0;
                    try (PreparedStatement ps = conn.prepareStatement("SELECT COALESCE(SUM(subtotal),0) FROM pedido_detalle WHERE pedido_id = ?")) {
                        ps.setInt(1, pedidoId);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) sumaSubtotales = rs.getDouble(1);
                        }
                    }
                    double total = Math.round(sumaSubtotales * 1.18 * 100.0) / 100.0;
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE pedidos SET total = ? WHERE id = ?")) {
                        ps.setDouble(1, total);
                        ps.setInt(2, pedidoId);
                        ps.executeUpdate();
                    }

                    conn.commit();
                    response.sendRedirect("Pedidos?mensaje=modificado");
                } catch (Exception ex) {
                    conn.rollback();
                    throw ex;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("Pedidos?error=general");
        }
    }
}
