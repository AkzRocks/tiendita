<%-- 
    Document   : guardar
    Created on : 14 oct 2025, 2:29:37
    Author     : Jean
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Recibir datos del formulario
    String nombre = request.getParameter("nombre");
    String email = request.getParameter("email");
    String telefono = request.getParameter("telefono");
    String direccion = request.getParameter("direccion");
    
    // Guardar en sesión (solución temporal)
    java.util.List<String> clientes = (java.util.List<String>) session.getAttribute("clientes");
    if (clientes == null) {
        clientes = new java.util.ArrayList<>();
    }
    
    // Agregar nuevo cliente
    String nuevoCliente = nombre + "|" + email + "|" + telefono + "|" + direccion;
    clientes.add(nuevoCliente);
    session.setAttribute("clientes", clientes);
%>

<!DOCTYPE html>
<html>
<head>
    <title>Guardando Cliente</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <div class="alert alert-success text-center">
            <h4>Cliente guardado exitosamente</h4>
            <p>Redirigiendo a la lista de clientes...</p>
        </div>
    </div>
    
    <script>
        setTimeout(function() {
            window.location.href = "listar.jsp";
        }, 2000);
    </script>
</body>
</html>