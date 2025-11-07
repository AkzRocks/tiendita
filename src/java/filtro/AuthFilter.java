package filtro;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebFilter("/*")
public class AuthFilter implements Filter {

    private static final Map<String, List<String>> permitMap = new HashMap<>();
    static {
    
        permitMap.put("/index.html", List.of("ANONYMOUS", "CLIENTE","VENDEDOR","ADMIN"));
        permitMap.put("/login.jsp", List.of("ANONYMOUS"));
        permitMap.put("/Login", List.of("ANONYMOUS"));
        permitMap.put("/Logout", List.of("CLIENTE","VENDEDOR","ADMIN"));

        permitMap.put("/Productos", List.of("ADMIN","VENDEDOR"));
        permitMap.put("/GuardarProducto", List.of("ADMIN","VENDEDOR"));
        permitMap.put("/ModificarProducto", List.of("ADMIN","VENDEDOR"));
        permitMap.put("/EliminarProducto", List.of("ADMIN"));

        permitMap.put("/Clientes", List.of("ADMIN","VENDEDOR"));
        permitMap.put("/GuardarCliente", List.of("ADMIN","VENDEDOR"));
        permitMap.put("/ModificarCliente", List.of("ADMIN","VENDEDOR"));
        permitMap.put("/EliminarCliente", List.of("ADMIN"));

        permitMap.put("/Pedidos", List.of("ADMIN","VENDEDOR","CLIENTE"));
        permitMap.put("/GuardarPedido", List.of("ADMIN","VENDEDOR","CLIENTE"));
        permitMap.put("/ModificarPedido", List.of("ADMIN","VENDEDOR"));
        permitMap.put("/EliminarPedido", List.of("ADMIN","VENDEDOR"));

        permitMap.put("/VerDetallePedido", List.of("ADMIN","VENDEDOR","CLIENTE"));
    }

    private boolean isPublic(String path) {
        // permitir recursos estáticos (js, css, images)
        return path.startsWith("/resources/") || path.endsWith(".css") || path.endsWith(".js") || path.endsWith(".png") || path.endsWith(".jpg");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getServletPath();

        if (isPublic(path) || path.equals("/login.jsp") || path.equals("/Login") || path.equals("/index.html")) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = request.getSession(false);
        String role = null;
        if (session != null) role = (String) session.getAttribute("role");

        if (role == null) {
            // si la ruta permite ANONYMOUS, dejar pasar; si no, redirigir al login
            List<String> allowed = permitMap.get(path);
            if (allowed != null && allowed.contains("ANONYMOUS")) {
                chain.doFilter(req, res);
            } else {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
            return;
        }

        List<String> allowed = permitMap.get(path);
        if (allowed != null) {
            if (allowed.contains(role)) {
                chain.doFilter(req, res);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No autorizado para esta acción");
            }
        } else {
            chain.doFilter(req, res);
        }
    }
}
