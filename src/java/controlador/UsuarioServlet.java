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
import java.util.ArrayList;
import java.util.List;
import modelo.Conexion;
import modelo.Usuario;

@WebServlet("/Usuarios")
public class UsuarioServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
        
        List<Usuario> usuarios = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConnection();
            String sql = "SELECT * FROM usuarios ORDER BY id DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setUsername(rs.getString("username"));
                usuario.setNombreCompleto(rs.getString("nombreCompleto"));
                usuario.setEmail(rs.getString("email"));
                usuario.setRol(rs.getString("rol"));
                usuario.setActivo(rs.getBoolean("activo"));
                usuario.setFechaCreacion(rs.getTimestamp("fechaCreacion"));
                usuario.setUltimoAcceso(rs.getTimestamp("ultimoAcceso"));
                
                usuarios.add(usuario);
            }
            
            request.setAttribute("usuarios", usuarios);
            request.getRequestDispatcher("listarUsuarios.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("dashboard.jsp?error=sql");
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
}