package modelo;

import java.sql.Timestamp;

public class Pedido {
    private int id;
    private int clienteId;
    private int productoId;
    private int cantidad;
    private Timestamp fechaPedido;
    private String estado;
    private double total;
    
    // Campos adicionales para mostrar
    private String nombreCliente;
    private String nombreProducto;
    private double precioProducto;
    
    // Constructor vacío
    public Pedido() {}
    
    // Constructor completo
    public Pedido(int id, int clienteId, int productoId, int cantidad, 
                  Timestamp fechaPedido, String estado, double total) {
        this.id = id;
        this.clienteId = clienteId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
        this.total = total;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }
    
    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    
    public Timestamp getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(Timestamp fechaPedido) { this.fechaPedido = fechaPedido; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    
    public double getPrecioProducto() { return precioProducto; }
    public void setPrecioProducto(double precioProducto) { this.precioProducto = precioProducto; }
}