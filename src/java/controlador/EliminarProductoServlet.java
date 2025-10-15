package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import modelo.Conexion;

@WebServlet("/EliminarProducto")
public class EliminarProductoServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idProducto = request.getParameter("id");
        
        if (idProducto == null || idProducto.trim().isEmpty()) {
            response.sendRedirect("Productos?error=id_requerido");
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = Conexion.getConnection();
            String sql = "DELETE FROM productos WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(idProducto));
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                response.sendRedirect("Productos?mensaje=eliminado");
            } else {
                response.sendRedirect("Productos?error=no_encontrado");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("Productos?error=sql");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("Productos?error=id_invalido");
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