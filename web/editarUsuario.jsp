<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="modelo.Usuario" %>
<%
    // Verificar sesión y rol de administrador
    Usuario usuarioActual = (Usuario) session.getAttribute("usuario");
    if (usuarioActual == null || !usuarioActual.isAdmin()) {
        response.sendRedirect("dashboard.jsp?error=permiso_denegado");
        return;
    }
    
    Usuario usuario = (Usuario) request.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("Usuarios?error=no_encontrado");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Usuario</title>
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
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header bg-warning text-dark">
                        <h3 class="mb-0"><i class="bi bi-pencil-square"></i> Editar Usuario</h3>
                    </div>
                    <div class="card-body">
                        <form action="ModificarUsuario" method="post">
                            <input type="hidden" name="id" value="<%= usuario.getId() %>">
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="username" class="form-label">
                                        <i class="bi bi-person"></i> Username *
                                    </label>
                                    <input type="text" class="form-control" id="username" 
                                           name="username" value="<%= usuario.getUsername() %>" required>
                                </div>
                                
                                <div class="col-md-6 mb-3">
                                    <label for="password" class="form-label">
                                        <i class="bi bi-key"></i> Nueva Contraseña
                                    </label>
                                    <input type="password" class="form-control" id="password" 
                                           name="password" placeholder="Dejar vacío para no cambiar">
                                    <small class="text-muted">Solo completa si quieres cambiar la contraseña</small>
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="nombreCompleto" class="form-label">
                                    <i class="bi bi-person-badge"></i> Nombre Completo *
                                </label>
                                <input type="text" class="form-control" id="nombreCompleto" 
                                       name="nombreCompleto" value="<%= usuario.getNombreCompleto() %>" required>
                            </div>
                            
                            <div class="mb-3">
                                <label for="email" class="form-label">
                                    <i class="bi bi-envelope"></i> Email *
                                </label>
                                <input type="email" class="form-control" id="email" 
                                       name="email" value="<%= usuario.getEmail() %>" required>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="rol" class="form-label">
                                        <i class="bi bi-shield-check"></i> Rol *
                                    </label>
                                    <select class="form-select" id="rol" name="rol" required>
                                        <option value="empleado" <%= usuario.getRol().equals("empleado") ? "selected" : "" %>>Empleado</option>
                                        <option value="admin" <%= usuario.getRol().equals("admin") ? "selected" : "" %>>Administrador</option>
                                    </select>
                                </div>
                                
                                <div class="col-md-6 mb-3">
                                    <label class="form-label d-block">
                                        <i class="bi bi-toggle-on"></i> Estado
                                    </label>
                                    <div class="form-check form-switch">
                                        <input class="form-check-input" type="checkbox" 
                                               id="activo" name="activo" <%= usuario.isActivo() ? "checked" : "" %>>
                                        <label class="form-check-label" for="activo">
                                            Usuario Activo
                                        </label>
                                    </div>
                                </div>
                            </div>
                            
                            <% if (usuario.getId() == usuarioActual.getId()) { %>
                            <div class="alert alert-warning">
                                <i class="bi bi-exclamation-triangle-fill"></i> 
                                <strong>Atención:</strong> Estás editando tu propio usuario. 
                                Ten cuidado al cambiar tu rol o desactivarte.
                            </div>
                            <% } %>
                            
                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="Usuarios" class="btn btn-secondary me-md-2">
                                    <i class="bi bi-x-circle"></i> Cancelar
                                </a>
                                <button type="submit" class="btn btn-warning text-dark">
                                    <i class="bi bi-save"></i> Guardar Cambios
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>