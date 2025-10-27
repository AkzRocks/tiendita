package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Pedido;
import modelo.Cliente;
import modelo.Producto;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static modelo.Conexion.getConnection;

@WebServlet("/Pedidos")
public class PedidosServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<Pedido> pedidos = new ArrayList<>();
        List<Cliente> clientes = new ArrayList<>();
        List<Producto> productos = new ArrayList<>();
        
        try (Connection conn = getConnection()) {
            // Obtener pedidos con información relacionada
            String sqlPedidos = "SELECT p.*, c.nombre as nombre_cliente, " +
                               "pr.nombreProducto as nombre_producto, pr.precioProducto " +
                               "FROM pedidos p " +
                               "JOIN clientes c ON p.cliente_id = c.id " +
                               "JOIN productos pr ON p.producto_id = pr.id " +
                               "ORDER BY p.fecha_pedido DESC";
            
            PreparedStatement psPedidos = conn.prepareStatement(sqlPedidos);
            ResultSet rsPedidos = psPedidos.executeQuery();
            
            while (rsPedidos.next()) {
                Pedido pedido = new Pedido();
                pedido.setId(rsPedidos.getInt("id"));
                pedido.setClienteId(rsPedidos.getInt("cliente_id"));
                pedido.setProductoId(rsPedidos.getInt("producto_id"));
                pedido.setCantidad(rsPedidos.getInt("cantidad"));
                pedido.setFechaPedido(rsPedidos.getTimestamp("fecha_pedido"));
                pedido.setEstado(rsPedidos.getString("estado"));
                pedido.setTotal(rsPedidos.getDouble("total"));
                pedido.setNombreCliente(rsPedidos.getString("nombre_cliente"));
                pedido.setNombreProducto(rsPedidos.getString("nombre_producto"));
                pedido.setPrecioProducto(rsPedidos.getDouble("precioProducto"));
                pedidos.add(pedido);
            }
            
            // Obtener clientes para el formulario
            String sqlClientes = "SELECT * FROM clientes";
            PreparedStatement psClientes = conn.prepareStatement(sqlClientes);
            ResultSet rsClientes = psClientes.executeQuery();
            
            while (rsClientes.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rsClientes.getInt("id"));
                cliente.setNombreCliente(rsClientes.getString("nombre"));
                clientes.add(cliente);
            }
            
            // Obtener productos para el formulario
            String sqlProductos = "SELECT * FROM productos WHERE stockProducto > 0";
            PreparedStatement psProductos = conn.prepareStatement(sqlProductos);
            ResultSet rsProductos = psProductos.executeQuery();
            
            while (rsProductos.next()) {
                Producto producto = new Producto();
                producto.setId(rsProductos.getInt("id"));
                producto.setNombreProducto(rsProductos.getString("nombreProducto"));
                producto.setPrecioProducto(rsProductos.getDouble("precioProducto"));
                producto.setStockProducto(rsProductos.getInt("stockProducto"));
                productos.add(producto);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "sql");
        }
        
        request.setAttribute("pedidos", pedidos);
        request.setAttribute("clientes", clientes);
        request.setAttribute("productos", productos);
        request.getRequestDispatcher("listarPedidos.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int clienteId = Integer.parseInt(request.getParameter("clienteId"));
            int productoId = Integer.parseInt(request.getParameter("productoId"));
            int cantidad = Integer.parseInt(request.getParameter("cantidad"));
            
            try (Connection conn = getConnection()) {
                // Obtener precio del producto
                String sqlPrecio = "SELECT precioProducto, stockProducto FROM productos WHERE id = ?";
                PreparedStatement psPrecio = conn.prepareStatement(sqlPrecio);
                psPrecio.setInt(1, productoId);
                ResultSet rs = psPrecio.executeQuery();
                
                if (rs.next()) {
                    double precio = rs.getDouble("precioProducto");
                    int stock = rs.getInt("stockProducto");
                    
                    if (stock < cantidad) {
                        response.sendRedirect("Pedidos?error=sin_stock");
                        return;
                    }
                    
                    double total = precio * cantidad;
                    
                    // Insertar pedido
                    String sqlInsert = "INSERT INTO pedidos (cliente_id, producto_id, cantidad, total) VALUES (?, ?, ?, ?)";
                    PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
                    psInsert.setInt(1, clienteId);
                    psInsert.setInt(2, productoId);
                    psInsert.setInt(3, cantidad);
                    psInsert.setDouble(4, total);
                    psInsert.executeUpdate();
                    
                    // Actualizar stock
                    String sqlUpdateStock = "UPDATE productos SET stockProducto = stockProducto - ? WHERE id = ?";
                    PreparedStatement psStock = conn.prepareStatement(sqlUpdateStock);
                    psStock.setInt(1, cantidad);
                    psStock.setInt(2, productoId);
                    psStock.executeUpdate();
                    
                    response.sendRedirect("Pedidos?mensaje=creado");
                } else {
                    response.sendRedirect("Pedidos?error=producto_no_encontrado");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("Pedidos?error=sql");
        }
    }
}