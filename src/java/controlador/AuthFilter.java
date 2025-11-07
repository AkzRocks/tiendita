package controlador;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialización del filtro
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        
        // Páginas públicas que no requieren autenticación
        boolean isPublicPage = uri.endsWith("login.jsp") || 
                               uri.endsWith("Login") ||
                               uri.contains("/css/") ||
                               uri.contains("/js/") ||
                               uri.contains("/images/");
        
        // Verificar si el usuario está autenticado
        HttpSession session = httpRequest.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("usuario") != null);
        
        if (isPublicPage || isLoggedIn) {
            // Permitir acceso
            chain.doFilter(request, response);
        } else {
            // Redirigir al login
            httpResponse.sendRedirect(contextPath + "/login.jsp?error=sesion_requerida");
        }
    }
    
    @Override
    public void destroy() {
        // Limpieza del filtro
    }
}