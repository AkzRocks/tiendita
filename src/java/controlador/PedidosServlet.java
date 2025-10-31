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
        
        String action = request.getParameter("action");
        
        System.out.println("DEBUG - Action: " + action);
        
        // Si es para mostrar el formulario de nuevo pedido
        if ("nuevo".equals(action)) {
            cargarDatosFormulario(request, response);
            request.getRequestDispatcher("nuevoPedido.jsp").forward(request, response);
            return;
        }
        
        // Cargar lista de pedidos
        List<Pedido> pedidos = new ArrayList<>();
        List<Cliente> clientes = new ArrayList<>();
        List<Producto> productos = new ArrayList<>();
        
        try (Connection conn = getConnection()) {
            
            System.out.println("DEBUG - Conexión establecida");
            
            // Obtener pedidos con información relacionada
            String sqlPedidos = "SELECT p.id, p.cliente_id, p.fecha_pedido, p.estado, p.total, " +
                               "c.nombreCliente as nombre_cliente " +
                               "FROM pedidos p " +
                               "LEFT JOIN clientes c ON p.cliente_id = c.id " +
                               "ORDER BY p.fecha_pedido DESC";
            
            PreparedStatement psPedidos = conn.prepareStatement(sqlPedidos);
            ResultSet rsPedidos = psPedidos.executeQuery();
            
            while (rsPedidos.next()) {
                Pedido pedido = new Pedido();
                pedido.setId(rsPedidos.getInt("id"));
                pedido.setClienteId(rsPedidos.getInt("cliente_id"));
                pedido.setFechaPedido(rsPedidos.getTimestamp("fecha_pedido"));
                pedido.setEstado(rsPedidos.getString("estado"));
                pedido.setTotal(rsPedidos.getDouble("total"));
                pedido.setNombreCliente(rsPedidos.getString("nombre_cliente"));
                pedidos.add(pedido);
            }
            
            System.out.println("DEBUG - Pedidos cargados: " + pedidos.size());
            
            // Obtener clientes para el formulario
            String sqlClientes = "SELECT id, nombreCliente FROM clientes";
            PreparedStatement psClientes = conn.prepareStatement(sqlClientes);
            ResultSet rsClientes = psClientes.executeQuery();
            
            while (rsClientes.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rsClientes.getInt("id"));
                cliente.setNombreCliente(rsClientes.getString("nombreCliente"));
                clientes.add(cliente);
            }
            
            System.out.println("DEBUG - Clientes cargados: " + clientes.size());
            
            // Obtener productos para el formulario
            String sqlProductos = "SELECT id, nombreProducto, precioProducto, stockProducto FROM productos WHERE stockProducto > 0";
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
            
            System.out.println("DEBUG - Productos cargados: " + productos.size());
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERROR SQL: " + e.getMessage());
            request.setAttribute("error", "sql");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: " + e.getMessage());
            request.setAttribute("error", "general");
        }
        
        request.setAttribute("pedidos", pedidos);
        request.setAttribute("clientes", clientes);
        request.setAttribute("productos", productos);
        
        System.out.println("DEBUG - Redirigiendo a listarPedidos.jsp");
        
        try {
            request.getRequestDispatcher("listarPedidos.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR al cargar JSP: " + e.getMessage());
            response.getWriter().println("Error al cargar la página: " + e.getMessage());
        }
    }
    
    private void cargarDatosFormulario(HttpServletRequest request, HttpServletResponse response) {
        List<Cliente> clientes = new ArrayList<>();
        List<Producto> productos = new ArrayList<>();
        
        try (Connection conn = getConnection()) {
            // Obtener clientes
            String sqlClientes = "SELECT id, nombreCliente FROM clientes";
            PreparedStatement psClientes = conn.prepareStatement(sqlClientes);
            ResultSet rsClientes = psClientes.executeQuery();
            
            while (rsClientes.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rsClientes.getInt("id"));
                cliente.setNombreCliente(rsClientes.getString("nombreCliente"));
                clientes.add(cliente);
            }
            
            // Obtener productos con stock
            String sqlProductos = "SELECT id, nombreProducto, precioProducto, stockProducto FROM productos WHERE stockProducto > 0";
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
            
            System.out.println("DEBUG - Formulario: Clientes=" + clientes.size() + ", Productos=" + productos.size());
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("ERROR al cargar datos del formulario: " + e.getMessage());
        }
        
        request.setAttribute("clientes", clientes);
        request.setAttribute("productos", productos);
    }
}