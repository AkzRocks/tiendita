package modelo;

import java.sql.Timestamp;

public class Usuario {
    private int id;
    private String username;
    private String password;
    private String nombreCompleto;
    private String email;
    private String rol;
    private boolean activo;
    private Timestamp fechaCreacion;
    private Timestamp ultimoAcceso;
    
    // Constructor vacío
    public Usuario() {
    }
    
    // Constructor para login
    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    // Constructor completo
    public Usuario(int id, String username, String password, String nombreCompleto, 
                   String email, String rol, boolean activo, Timestamp fechaCreacion, 
                   Timestamp ultimoAcceso) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.rol = rol;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
        this.ultimoAcceso = ultimoAcceso;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getRol() {
        return rol;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public Timestamp getUltimoAcceso() {
        return ultimoAcceso;
    }
    
    public void setUltimoAcceso(Timestamp ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }
    
    // Método para verificar si es administrador
    public boolean isAdmin() {
        return "admin".equals(this.rol);
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", email='" + email + '\'' +
                ", rol='" + rol + '\'' +
                ", activo=" + activo +
                '}';
    }
}