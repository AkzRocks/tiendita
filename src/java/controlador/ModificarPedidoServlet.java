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
                }
                
                // Obtener clientes
                String sqlClientes = "SELECT * FROM clientes";
                PreparedStatement psClientes = conn.prepareStatement(sqlClientes);
                ResultSet rsClientes = psClientes.executeQuery();
                
                while (rsClientes.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setId(rsClientes.getInt("id"));
                    cliente.setNombreCliente(rsClientes.getString("nombre"));
                    clientes.add(cliente);
                }
                
                // Obtener productos
                String sqlProductos = "SELECT * FROM productos";
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
            }
            
            request.setAttribute("pedido", pedido);
            request.setAttribute("clientes", clientes);
            request.setAttribute("productos", productos);
            request.getRequestDispatcher("modificarPedido.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("Pedidos?error=id_invalido");
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
            
            try (Connection conn = getConnection()) {
                // Obtener cantidad anterior del pedido
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
                
                // Devolver stock anterior
                String sqlReturnStock = "UPDATE productos SET stockProducto = stockProducto + ? WHERE id = ?";
                PreparedStatement psReturn = conn.prepareStatement(sqlReturnStock);
                psReturn.setInt(1, oldCantidad);
                psReturn.setInt(2, oldProductoId);
                psReturn.executeUpdate();
                
                // Obtener nuevo precio y verificar stock
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
                    
                    // Actualizar pedido
                    String sqlUpdate = "UPDATE pedidos SET cliente_id = ?, producto_id = ?, cantidad = ?, estado = ?, total = ? WHERE id = ?";
                    PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
                    psUpdate.setInt(1, clienteId);
                    psUpdate.setInt(2, productoId);
                    psUpdate.setInt(3, cantidad);
                    psUpdate.setString(4, estado);
                    psUpdate.setDouble(5, total);
                    psUpdate.setInt(6, id);
                    psUpdate.executeUpdate();
                    
                    // Descontar nuevo stock
                    String sqlUpdateStock = "UPDATE productos SET stockProducto = stockProducto - ? WHERE id = ?";
                    PreparedStatement psStock = conn.prepareStatement(sqlUpdateStock);
                    psStock.setInt(1, cantidad);
                    psStock.setInt(2, productoId);
                    psStock.executeUpdate();
                    
                    response.sendRedirect("Pedidos?mensaje=modificado");
                } else {
                    response.sendRedirect("Pedidos?error=producto_no_encontrado");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("Pedidos?error=sql");
        }
    }
}