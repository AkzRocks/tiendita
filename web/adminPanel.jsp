<%@page import="javax.servlet.http.HttpSession"%>
<%
    HttpSession session = request.getSession(false);
    String rol = (session != null) ? (String) session.getAttribute("rol") : null;
    if (rol == null || !rol.equals("administrador")) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<html>
    <head><title>Panel Administrador</title></head>
    <body>
        <h1>Bienvenido Administrador</h1>
        <button>Agregar Cliente</button>
        <button>Modificar Producto</button>
        <button>Eliminar Vendedor</button>
    </body>
</html>
