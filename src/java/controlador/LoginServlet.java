package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.Conexion;
import modelo.Usuario;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Mostrar el formulario de login
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Validación básica
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Por favor complete todos los campos");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConnection();
            
            // Consulta para verificar credenciales
            String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ? AND activo = TRUE";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // Login exitoso
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setUsername(rs.getString("username"));
                usuario.setNombreCompleto(rs.getString("nombreCompleto"));
                usuario.setEmail(rs.getString("email"));
                usuario.setRol(rs.getString("rol"));
                usuario.setActivo(rs.getBoolean("activo"));
                
                // Actualizar último acceso
                actualizarUltimoAcceso(conn, usuario.getId());
                
                // Crear sesión
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                session.setAttribute("username", usuario.getUsername());
                session.setAttribute("nombreCompleto", usuario.getNombreCompleto());
                session.setAttribute("rol", usuario.getRol());
                session.setMaxInactiveInterval(30 * 60); // 30 minutos
                
                // Redirigir al dashboard
                response.sendRedirect("dashboard.jsp");
                
            } else {
                // Login fallido
                request.setAttribute("error", "Usuario o contraseña incorrectos");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en la base de datos");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void actualizarUltimoAcceso(Connection conn, int userId) {
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE usuarios SET ultimoAcceso = NOW() WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}