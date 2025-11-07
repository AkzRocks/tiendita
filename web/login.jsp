<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Tienda PC</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .login-card {
            border-radius: 15px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
        }
        .login-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 15px 15px 0 0;
            padding: 30px;
            text-align: center;
        }
        .login-icon {
            font-size: 60px;
            color: white;
            margin-bottom: 10px;
        }
        .btn-login {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            padding: 12px;
            font-weight: bold;
        }
        .btn-login:hover {
            background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-5">
                <div class="card login-card">
                    <div class="login-header">
                        <i class="bi bi-pc-display login-icon"></i>
                        <h2 class="text-white mb-0">Tienda PC</h2>
                        <p class="text-white-50 mb-0">Sistema de Gestión</p>
                    </div>
                    <div class="card-body p-4">
                        <%
                            String error = request.getParameter("error");
                            String mensaje = request.getParameter("mensaje");
                            String errorAttr = (String) request.getAttribute("error");
                            
                            if (errorAttr != null) {
                        %>
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="bi bi-exclamation-triangle-fill"></i> <%= errorAttr %>
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                        <%
                            }
                            
                            if (mensaje != null && mensaje.equals("sesion_cerrada")) {
                        %>
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            <i class="bi bi-check-circle-fill"></i> Sesión cerrada exitosamente
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                        <%
                            }
                            
                            if (error != null && error.equals("sesion_requerida")) {
                        %>
                        <div class="alert alert-warning alert-dismissible fade show" role="alert">
                            <i class="bi bi-shield-lock-fill"></i> Debe iniciar sesión para continuar
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                        <%
                            }
                        %>
                        
                        <form action="Login" method="post">
                            <div class="mb-3">
                                <label for="username" class="form-label">
                                    <i class="bi bi-person-fill"></i> Usuario
                                </label>
                                <input type="text" class="form-control" id="username" 
                                       name="username" placeholder="Ingrese su usuario" 
                                       required autofocus>
                            </div>
                            
                            <div class="mb-4">
                                <label for="password" class="form-label">
                                    <i class="bi bi-lock-fill"></i> Contraseña
                                </label>
                                <input type="password" class="form-control" id="password" 
                                       name="password" placeholder="Ingrese su contraseña" 
                                       required>
                            </div>
                            
                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary btn-login">
                                    <i class="bi bi-box-arrow-in-right"></i> Iniciar Sesión
                                </button>
                            </div>
                        </form>
                        
                        <hr class="my-4">
                        
                        <div class="text-center text-muted small">
                            <p class="mb-1"><strong>Usuarios de prueba:</strong></p>
                            <p class="mb-0">Admin: <code>admin / admin123</code></p>
                            <p class="mb-0">Empleado: <code>empleado / emp123</code></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>