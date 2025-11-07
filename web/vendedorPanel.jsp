<%@page import="javax.servlet.http.HttpSession"%>
<%
    HttpSession session = request.getSession(false);
    String rol = (session != null) ? (String) session.getAttribute("rol") : null;
    if (rol == null || !rol.equals("vendedor")) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<html>
    <head><title>Panel Vendedor</title></head>
    <body>
        <h1>Bienvenido Vendedor</h1>
        <button>Ver Stock</button>
        <button>Modificar Precios</button>
    </body>
</html>
