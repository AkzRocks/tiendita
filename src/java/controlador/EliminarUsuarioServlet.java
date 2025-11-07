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

@WebServlet("/EliminarUsuario")
public class EliminarUsuarioServlet extends HttpServlet {
    
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
        
        String idUsuario = request.getParameter("id");
        
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            response.sendRedirect("Usuarios?error=id_requerido");
            return;
        }
        
        // Prevenir que el usuario se elimine a sí mismo
        if (Integer.parseInt(idUsuario) == usuarioActual.getId()) {
            response.sendRedirect("Usuarios?error=no_puede_eliminar_propio");
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = Conexion.getConnection();
            String sql = "DELETE FROM usuarios WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(idUsuario));
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                response.sendRedirect("Usuarios?mensaje=usuario_eliminado");
            } else {
                response.sendRedirect("Usuarios?error=no_encontrado");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("Usuarios?error=sql");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}