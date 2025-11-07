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
import java.sql.SQLException;
import modelo.Conexion;
import modelo.Usuario;

@WebServlet("/GuardarUsuario")
public class GuardarUsuarioServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Verificar que sea administrador
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp?error=sesion_requerida");
            return;
        }
        
        Usuario usuarioActual = (Usuario) session.getAttribute("usuario");
        if (!usuarioActual.isAdmin()) {
            response.sendRedirect("dashboard.jsp?error=permiso_denegado");
            return;
        }
        
        // Obtener datos del formulario
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nombreCompleto = request.getParameter("nombreCompleto");
        String email = request.getParameter("email");
        String rol = request.getParameter("rol");
        String activoStr = request.getParameter("activo");
        boolean activo = activoStr != null && activoStr.equals("on");
        
        // Validación básica
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            nombreCompleto == null || nombreCompleto.trim().isEmpty() ||
            email == null || email.trim().isEmpty()) {
            
            response.sendRedirect("nuevoUsuario.jsp?error=campos_vacios");
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = Conexion.getConnection();
            String sql = "INSERT INTO usuarios (username, password, nombreCompleto, email, rol, activo) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, nombreCompleto);
            pstmt.setString(4, email);
            pstmt.setString(5, rol);
            pstmt.setBoolean(6, activo);
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                response.sendRedirect("Usuarios?mensaje=usuario_creado");
            } else {
                response.sendRedirect("nuevoUsuario.jsp?error=no_guardado");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            // Verificar si es error de duplicado (username o email)
            if (e.getMessage().contains("Duplicate entry")) {
                response.sendRedirect("nuevoUsuario.jsp?error=usuario_duplicado");
            } else {
                response.sendRedirect("nuevoUsuario.jsp?error=sql");
            }
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}