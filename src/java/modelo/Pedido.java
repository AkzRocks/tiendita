package modelo;

import java.sql.Timestamp;

public class Pedido {
    private int id;
    private int clienteId;
    private Timestamp fechaPedido;
    private String estado;
    private double total;
    
    // Campos adicionales para mostrar
    private String nombreCliente;
    private String dniRuc;
    private String direccion;
    
    public Pedido() {}
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }
    
    public Timestamp getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(Timestamp fechaPedido) { this.fechaPedido = fechaPedido; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getDniRuc() { return dniRuc; }
    public void setDniRuc(String dniRuc) { this.dniRuc = dniRuc; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}