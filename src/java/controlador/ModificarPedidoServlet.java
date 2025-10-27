package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Pedido;
import modelo.Cliente;
import modelo.Producto;
import static modelo.Conexion.getConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ModificarPedido")
public class ModificarPedidoServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Pedido pedido = null;
            List<Cliente> clientes = new ArrayList<>();
            List<Producto> productos = new ArrayList<>();
            
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
                    pedido.setProductoId(rs.getInt("producto_id"));
                    pedido.setCantidad(rs.getInt("cantidad"));
                    pedido.setEstado(rs.getString("estado"));
                    pedido.setTotal(rs.getDouble("total"));
                    pedido.setFechaPedido(rs.getTimestamp("fecha_pedido"));
                }
                
                // Obtener clientes - NOMBRES CORRECTOS
                String sqlClientes = "SELECT id, nombreCliente FROM clientes";
                PreparedStatement psClientes = conn.prepareStatement(sqlClientes);
                ResultSet rsClientes = psClientes.executeQuery();
                
                while (rsClientes.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setId(rsClientes.getInt("id"));
                    cliente.setNombreCliente(rsClientes.getString("nombreCliente"));
                    clientes.add(cliente);
                }
                
                // Obtener productos - NOMBRES CORRECTOS
                String sqlProductos = "SELECT id, nombreProducto, precioProducto, stockProducto FROM productos";
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
                
                System.out.println("DEBUG - Pedido cargado: " + (pedido != null ? pedido.getId() : "null"));
                System.out.println("DEBUG - Clientes: " + clientes.size());
                System.out.println("DEBUG - Productos: " + productos.size());
            }
            
            if (pedido == null) {
                response.sendRedirect("Pedidos?error=no_encontrado");
                return;
            }
            
            request.setAttribute("pedido", pedido);
            request.setAttribute("clientes", clientes);
            request.setAttribute("productos", productos);
            request.getRequestDispatcher("modificarPedido.jsp").forward(request, response);
            
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
            int clienteId = Integer.parseInt(request.getParameter("clienteId"));
            int productoId = Integer.parseInt(request.getParameter("productoId"));
            int cantidad = Integer.parseInt(request.getParameter("cantidad"));
            String estado = request.getParameter("estado");
            
            System.out.println("DEBUG - Modificando pedido ID: " + id);
            System.out.println("DEBUG - Cliente: " + clienteId + ", Producto: " + productoId + ", Cantidad: " + cantidad);
            
            try (Connection conn = getConnection()) {
                // Obtener datos del pedido anterior
                String sqlOld = "SELECT producto_id, cantidad FROM pedidos WHERE id = ?";
                PreparedStatement psOld = conn.prepareStatement(sqlOld);
                psOld.setInt(1, id);
                ResultSet rsOld = psOld.executeQuery();
                
                int oldProductoId = 0;
                int oldCantidad = 0;
                if (rsOld.next()) {
                    oldProductoId = rsOld.getInt("producto_id");
                    oldCantidad = rsOld.getInt("cantidad");
                }
                
                System.out.println("DEBUG - Producto anterior: " + oldProductoId + ", Cantidad anterior: " + oldCantidad);
                
                // Devolver stock del producto anterior
                if (oldProductoId > 0) {
                    String sqlReturnStock = "UPDATE productos SET stockProducto = stockProducto + ? WHERE id = ?";
                    PreparedStatement psReturn = conn.prepareStatement(sqlReturnStock);
                    psReturn.setInt(1, oldCantidad);
                    psReturn.setInt(2, oldProductoId);
                    psReturn.executeUpdate();
                    System.out.println("DEBUG - Stock devuelto: " + oldCantidad + " unidades al producto " + oldProductoId);
                }
                
                // Obtener precio del nuevo producto y verificar stock
                String sqlPrecio = "SELECT precioProducto, stockProducto FROM productos WHERE id = ?";
                PreparedStatement psPrecio = conn.prepareStatement(sqlPrecio);
                psPrecio.setInt(1, productoId);
                ResultSet rs = psPrecio.executeQuery();
                
                if (rs.next()) {
                    double precio = rs.getDouble("precioProducto");
                    int stock = rs.getInt("stockProducto");
                    
                    System.out.println("DEBUG - Nuevo producto - Precio: " + precio + ", Stock disponible: " + stock);
                    
                    if (stock < cantidad) {
                        System.out.println("ERROR - Stock insuficiente");
                        response.sendRedirect("Pedidos?error=sin_stock");
                        return;
                    }
                    
                    double total = precio * cantidad;
                    
                    // Actualizar pedido
                    String sqlUpdate = "UPDATE pedidos SET cliente_id = ?, producto_id = ?, cantidad = ?, estado = ?, total = ? WHERE id = ?";
                    PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
                    psUpdate.setInt(1, clienteId);
                    psUpdate.setInt(2, productoId);
                    psUpdate.setInt(3, cantidad);
                    psUpdate.setString(4, estado);
                    psUpdate.setDouble(5, total);
                    psUpdate.setInt(6, id);
                    int rowsUpdated = psUpdate.executeUpdate();
                    
                    System.out.println("DEBUG - Filas actualizadas: " + rowsUpdated);
                    
                    // Descontar nuevo stock
                    String sqlUpdateStock = "UPDATE productos SET stockProducto = stockProducto - ? WHERE id = ?";
                    PreparedStatement psStock = conn.prepareStatement(sqlUpdateStock);
                    psStock.setInt(1, cantidad);
                    psStock.setInt(2, productoId);
                    psStock.executeUpdate();
                    
                    System.out.println("DEBUG - Stock descontado: " + cantidad + " unidades del producto " + productoId);
                    
                    response.sendRedirect("Pedidos?mensaje=modificado");
                } else {
                    System.out.println("ERROR - Producto no encontrado");
                    response.sendRedirect("Pedidos?error=producto_no_encontrado");
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
        }
    }
}