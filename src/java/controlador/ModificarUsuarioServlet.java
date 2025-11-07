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

@WebServlet("/ModificarUsuario")
public class ModificarUsuarioServlet extends HttpServlet {
    
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
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConnection();
            String sql = "SELECT * FROM usuarios WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(idUsuario));
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
                usuario.setNombreCompleto(rs.getString("nombreCompleto"));
                usuario.setEmail(rs.getString("email"));
                usuario.setRol(rs.getString("rol"));
                usuario.setActivo(rs.getBoolean("activo"));
                
                request.setAttribute("usuario", usuario);
                request.getRequestDispatcher("editarUsuario.jsp").forward(request, response);
            } else {
                response.sendRedirect("Usuarios?error=no_encontrado");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("Usuarios?error=sql");
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
        
        String idUsuario = request.getParameter("id");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nombreCompleto = request.getParameter("nombreCompleto");
        String email = request.getParameter("email");
        String rol = request.getParameter("rol");
        String activoStr = request.getParameter("activo");
        boolean activo = activoStr != null && activoStr.equals("on");
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = Conexion.getConnection();
            
            // Si la contraseña está vacía, no la actualizamos
            String sql;
            if (password != null && !password.trim().isEmpty()) {
                sql = "UPDATE usuarios SET username = ?, password = ?, nombreCompleto = ?, email = ?, rol = ?, activo = ? WHERE id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.setString(3, nombreCompleto);
                pstmt.setString(4, email);
                pstmt.setString(5, rol);
                pstmt.setBoolean(6, activo);
                pstmt.setInt(7, Integer.parseInt(idUsuario));
            } else {
                sql = "UPDATE usuarios SET username = ?, nombreCompleto = ?, email = ?, rol = ?, activo = ? WHERE id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, nombreCompleto);
                pstmt.setString(3, email);
                pstmt.setString(4, rol);
                pstmt.setBoolean(5, activo);
                pstmt.setInt(6, Integer.parseInt(idUsuario));
            }
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                response.sendRedirect("Usuarios?mensaje=usuario_modificado");
            } else {
                response.sendRedirect("Usuarios?error=no_modificado");
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
}