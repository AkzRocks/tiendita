<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Login - Mi Tienda</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light">
        <div class="container" style="max-width:420px;margin-top:80px;">
            <div class="card">
                <div class="card-body">
                    <h3 class="card-title text-center">Iniciar sesión</h3>
                    <form method="post" action="Login">
                        <div class="mb-3">
                            <label for="username" class="form-label">Usuario</label>
                            <input id="username" name="username" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">Contraseña</label>
                            <input id="password" type="password" name="password" class="form-control" required>
                        </div>
                        <button class="btn btn-primary w-100">Entrar</button>
                    </form>
                    <div class="mt-3 text-center">
                        <a href="index.html">Volver</a>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>

