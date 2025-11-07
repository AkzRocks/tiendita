<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="modelo.Usuario" %>
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
    <title>Nuevo Usuario</title>
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
                    <div class="card-header bg-success text-white">
                        <h3 class="mb-0"><i class="bi bi-person-plus-fill"></i> Nuevo Usuario</h3>
                    </div>
                    <div class="card-body">
                        <%
                            String error = request.getParameter("error");
                            if (error != null) {
                                String textoError = "";
                                if (error.equals("campos_vacios")) textoError = "Por favor complete todos los campos";
                                else if (error.equals("usuario_duplicado")) textoError = "El usuario o email ya existe";
                                else if (error.equals("sql")) textoError = "Error en la base de datos";
                                else if (error.equals("no_guardado")) textoError = "No se pudo guardar el usuario";
                        %>
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="bi bi-exclamation-triangle-fill"></i> <%= textoError %>
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                        <%
                            }
                        %>
                        
                        <form action="GuardarUsuario" method="post">
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="username" class="form-label">
                                        <i class="bi bi-person"></i> Username *
                                    </label>
                                    <input type="text" class="form-control" id="username" 
                                           name="username" placeholder="usuario123" required>
                                    <small class="text-muted">Nombre de usuario único para login</small>
                                </div>
                                
                                <div class="col-md-6 mb-3">
                                    <label for="password" class="form-label">
                                        <i class="bi bi-key"></i> Contraseña *
                                    </label>
                                    <input type="password" class="form-control" id="password" 
                                           name="password" placeholder="********" required>
                                    <small class="text-muted">Mínimo 6 caracteres</small>
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="nombreCompleto" class="form-label">
                                    <i class="bi bi-person-badge"></i> Nombre Completo *
                                </label>
                                <input type="text" class="form-control" id="nombreCompleto" 
                                       name="nombreCompleto" placeholder="Juan Pérez García" required>
                            </div>
                            
                            <div class="mb-3">
                                <label for="email" class="form-label">
                                    <i class="bi bi-envelope"></i> Email *
                                </label>
                                <input type="email" class="form-control" id="email" 
                                       name="email" placeholder="usuario@email.com" required>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="rol" class="form-label">
                                        <i class="bi bi-shield-check"></i> Rol *
                                    </label>
                                    <select class="form-select" id="rol" name="rol" required>
                                        <option value="empleado" selected>Empleado</option>
                                        <option value="admin">Administrador</option>
                                    </select>
                                </div>
                                
                                <div class="col-md-6 mb-3">
                                    <label class="form-label d-block">
                                        <i class="bi bi-toggle-on"></i> Estado
                                    </label>
                                    <div class="form-check form-switch">
                                        <input class="form-check-input" type="checkbox" 
                                               id="activo" name="activo" checked>
                                        <label class="form-check-label" for="activo">
                                            Usuario Activo
                                        </label>
                                    </div>
                                </div>
                            </div>
                            
                            <hr>
                            
                            <div class="alert alert-info">
                                <i class="bi bi-info-circle-fill"></i> 
                                <strong>Diferencias entre roles:</strong>
                                <ul class="mb-0 mt-2">
                                    <li><strong>Administrador:</strong> Acceso completo al sistema, puede gestionar usuarios</li>
                                    <li><strong>Empleado:</strong> Puede gestionar clientes y productos</li>
                                </ul>
                            </div>
                            
                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="Usuarios" class="btn btn-secondary me-md-2">
                                    <i class="bi bi-x-circle"></i> Cancelar
                                </a>
                                <button type="submit" class="btn btn-success">
                                    <i class="bi bi-save"></i> Guardar Usuario
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