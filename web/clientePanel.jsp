<%@page import="javax.servlet.http.HttpSession"%>
<%
    HttpSession session = request.getSession(false);
    String rol = (session != null) ? (String) session.getAttribute("rol") : null;
    if (rol == null || !rol.equals("cliente")) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<html>
    <head><title>Panel Cliente</title></head>
    <body>
        <h1>Bienvenido Cliente</h1>
        <button>Ver Precios</button>
        <button>Agregar al Carrito</button>
    </body>
</html>
