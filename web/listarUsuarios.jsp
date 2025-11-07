<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Usuario" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    // Verificar sesión y rol de administrador
    Usuario usuarioActual = (Usuario) session.getAttribute("usuario");
    if (usuarioActual == null || !usuarioActual.isAdmin()) {
        response.sendRedirect("dashboard.jsp?error=permiso_denegado");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Usuarios</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">
    <nav class="navbar navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="dashboard.jsp">
                <i class="bi bi-pc-display"></i> Tienda PC
            </a>
            <div class="d-flex">
                <span class="navbar-text text-white me-3">
                    <i class="bi bi-person-circle"></i> <%= usuarioActual.getNombreCompleto() %>
                </span>
                <a href="Logout" class="btn btn-outline-light btn-sm">
                    <i class="bi bi-box-arrow-right"></i> Salir
                </a>
            </div>
        </div>
    </nav>
    
    <div class="container mt-5">
        <div class="card">
            <div class="card-header bg-dark text-white d-flex justify-content-between align-items-center">
                <h2 class="mb-0"><i class="bi bi-people-fill"></i> Gestión de Usuarios</h2>
                <div>
                    <a href="dashboard.jsp" class="btn btn-light btn-sm me-2">
                        <i class="bi bi-arrow-left"></i> Volver
                    </a>
                    <a href="nuevoUsuario.jsp" class="btn btn-success btn-sm">
                        <i class="bi bi-person-plus-fill"></i> Nuevo Usuario
                    </a>
                </div>
            </div>
            <div class="card-body">
                <%
                    String mensaje = request.getParameter("mensaje");
                    String error = request.getParameter("error");
                    
                    if (mensaje != null) {
                        String textoMensaje = "";
                        if (mensaje.equals("usuario_creado")) textoMensaje = "Usuario creado exitosamente";
                        else if (mensaje.equals("usuario_modificado")) textoMensaje = "Usuario modificado exitosamente";
                        else if (mensaje.equals("usuario_eliminado")) textoMensaje = "Usuario eliminado exitosamente";
                %>
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="bi bi-check-circle-fill"></i> <%= textoMensaje %>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <%
                    }
                    
                    if (error != null) {
                        String textoError = "";
                        if (error.equals("sql")) textoError = "Error en la base de datos";
                        else if (error.equals("no_encontrado")) textoError = "Usuario no encontrado";
                        else if (error.equals("id_invalido")) textoError = "ID inválido";
                        else if (error.equals("no_puede_eliminar_propio")) textoError = "No puedes eliminar tu propio usuario";
                %>
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="bi bi-exclamation-triangle-fill"></i> <%= textoError %>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <%
                    }
                %>
                
                <div class="table-responsive">
                    <table class="table table-striped table-hover">
                        <thead class="table-dark">
                            <tr>
                                <th>ID</th>
                                <th>Username</th>
                                <th>Nombre Completo</th>
                                <th>Email</th>
                                <th>Rol</th>
                                <th>Estado</th>
                                <th>Último Acceso</th>
                                <th class="text-center">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                
                                if (usuarios != null && !usuarios.isEmpty()) {
                                    for (Usuario usuario : usuarios) {
                            %>
                            <tr>
                                <td><%= usuario.getId() %></td>
                                <td><strong><%= usuario.getUsername() %></strong></td>
                                <td><%= usuario.getNombreCompleto() %></td>
                                <td><%= usuario.getEmail() %></td>
                                <td>
                                    <% if (usuario.getRol().equals("admin")) { %>
                                        <span class="badge bg-danger">
                                            <i class="bi bi-shield-fill-check"></i> Admin
                                        </span>
                                    <% } else { %>
                                        <span class="badge bg-info">
                                            <i class="bi bi-person-badge"></i> Empleado
                                        </span>
                                    <% } %>
                                </td>
                                <td>
                                    <% if (usuario.isActivo()) { %>
                                        <span class="badge bg-success">
                                            <i class="bi bi-check-circle"></i> Activo
                                        </span>
                                    <% } else { %>
                                        <span class="badge bg-secondary">
                                            <i class="bi bi-x-circle"></i> Inactivo
                                        </span>
                                    <% } %>
                                </td>
                                <td>
                                    <%= usuario.getUltimoAcceso() != null ? 
                                        sdf.format(usuario.getUltimoAcceso()) : 
                                        "<small class='text-muted'>Nunca</small>" %>
                                </td>
                                <td class="text-center">
                                    <a href="ModificarUsuario?id=<%= usuario.getId() %>" 
                                       class="btn btn-sm btn-warning" 
                                       title="Editar">
                                        <i class="bi bi-pencil-square"></i>
                                    </a>
                                    <% if (usuario.getId() != usuarioActual.getId()) { %>
                                    <a href="EliminarUsuario?id=<%= usuario.getId() %>" 
                                       class="btn btn-sm btn-danger" 
                                       title="Eliminar"
                                       onclick="return confirm('¿Estás seguro de eliminar este usuario?')">
                                        <i class="bi bi-trash"></i>
                                    </a>
                                    <% } else { %>
                                    <button class="btn btn-sm btn-secondary" disabled title="No puedes eliminarte a ti mismo">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                    <% } %>
                                </td>
                            </tr>
                            <%
                                    }
                                } else {
                            %>
                            <tr>
                                <td colspan="8" class="text-center text-muted">
                                    No hay usuarios registrados
                                </td>
                            </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>