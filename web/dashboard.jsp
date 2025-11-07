<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="modelo.Usuario" %>
<%
    // Verificar sesión
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("login.jsp?error=sesion_requerida");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Tienda PC</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .sidebar {
            min-height: 100vh;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        .sidebar .nav-link {
            color: rgba(255,255,255,0.8);
            padding: 12px 20px;
            margin: 5px 0;
            border-radius: 8px;
            transition: all 0.3s;
        }
        .sidebar .nav-link:hover, .sidebar .nav-link.active {
            background-color: rgba(255,255,255,0.2);
            color: white;
        }
        .card-stat {
            border-radius: 15px;
            border: none;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            transition: transform 0.3s;
        }
        .card-stat:hover {
            transform: translateY(-5px);
        }
        .user-info {
            background-color: rgba(255,255,255,0.1);
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <div class="col-md-2 sidebar p-0">
                <div class="p-3">
                    <div class="text-center mb-4 mt-3">
                        <i class="bi bi-pc-display" style="font-size: 50px;"></i>
                        <h4>Tienda PC</h4>
                    </div>
                    
                    <!-- Información del usuario -->
                    <div class="user-info text-center">
                        <i class="bi bi-person-circle" style="font-size: 60px;"></i>
                        <h6 class="mt-2 mb-0"><%= usuario.getNombreCompleto() %></h6>
                        <small><%= usuario.getUsername() %></small>
                        <br>
                        <span class="badge bg-light text-dark mt-2">
                            <%= usuario.getRol().toUpperCase() %>
                        </span>
                    </div>
                    
                    <!-- Menú de navegación -->
                    <nav class="nav flex-column">
                        <a class="nav-link active" href="dashboard.jsp">
                            <i class="bi bi-speedometer2"></i> Dashboard
                        </a>
                        <a class="nav-link" href="Clientes">
                            <i class="bi bi-people-fill"></i> Clientes
                        </a>
                        <a class="nav-link" href="Productos">
                            <i class="bi bi-box-seam"></i> Productos
                        </a>
                        
                        <% if (usuario.isAdmin()) { %>
                        <hr style="border-color: rgba(255,255,255,0.3);">
                        <a class="nav-link" href="Usuarios">
                            <i class="bi bi-person-gear"></i> Gestión Usuarios
                        </a>
                        <% } %>
                        
                        <hr style="border-color: rgba(255,255,255,0.3);">
                        
                        <a class="nav-link" href="Logout">
                            <i class="bi bi-box-arrow-right"></i> Cerrar Sesión
                        </a>
                    </nav>
                </div>
            </div>
            
            <!-- Contenido principal -->
            <div class="col-md-10 p-4">
                <div class="mb-4">
                    <h2>Bienvenido, <%= usuario.getNombreCompleto() %>!</h2>
                    <p class="text-muted">Panel de control - Sistema de Gestión</p>
                </div>
                
                <!-- Tarjetas estadísticas -->
                <div class="row mb-4">
                    <div class="col-md-3">
                        <div class="card card-stat text-white bg-primary">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h6 class="card-title">Total Clientes</h6>
                                        <h2 class="mb-0">    </h2>
                                    </div>
                                    <i class="bi bi-people-fill" style="font-size: 40px; opacity: 0.5;"></i>
                                </div>
                            </div>
                            <div class="card-footer bg-transparent border-0">
                                <a href="Clientes" class="text-white text-decoration-none">
                                    Ver detalles <i class="bi bi-arrow-right"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-3">
                        <div class="card card-stat text-white bg-success">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h6 class="card-title">Total Productos</h6>
                                        <h2 class="mb-0">--</h2>
                                    </div>
                                    <i class="bi bi-box-seam" style="font-size: 40px; opacity: 0.5;"></i>
                                </div>
                            </div>
                            <div class="card-footer bg-transparent border-0">
                                <a href="Productos" class="text-white text-decoration-none">
                                    Ver detalles <i class="bi bi-arrow-right"></i>
                                </a>
                            </div>
                        </div>
                    </div>
          
                    
                    <div class="col-md-3">
                        <div class="card card-stat text-white bg-info">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h6 class="card-title">Valor Inventario</h6>
                                        <h2 class="mb-0">$--</h2>
                                    </div>
                                    <i class="bi bi-currency-dollar" style="font-size: 40px; opacity: 0.5;"></i>
                                </div>
                            </div>
                            <div class="card-footer bg-transparent border-0">
                                <span class="text-white">Total estimado</span>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Accesos rápidos -->
                <div class="row">
                    <div class="col-md-12">
                        <div class="card">
                            <div class="card-header bg-white">
                                <h5 class="mb-0"><i class="bi bi-lightning-fill"></i> Accesos Rápidos</h5>
                            </div>
                            <div class="card-body">
                                <div class="row text-center">
                                    <div class="col-md-3 mb-3">
                                        <a href="nuevoCliente.jsp" class="btn btn-outline-primary btn-lg w-100">
                                            <i class="bi bi-person-plus-fill d-block mb-2" style="font-size: 40px;"></i>
                                            Nuevo Cliente
                                        </a>
                                    </div>
                                    <div class="col-md-3 mb-3">
                                        <a href="nuevoProducto.jsp" class="btn btn-outline-success btn-lg w-100">
                                            <i class="bi bi-plus-square-fill d-block mb-2" style="font-size: 40px;"></i>
                                            Nuevo Producto
                                        </a>
                                    </div>
                                    <div class="col-md-3 mb-3">
                                        <a href="Clientes" class="btn btn-outline-info btn-lg w-100">
                                            <i class="bi bi-list-ul d-block mb-2" style="font-size: 40px;"></i>
                                            Ver Clientes
                                        </a>
                                    </div>
                                    <div class="col-md-3 mb-3">
                                        <a href="Productos" class="btn btn-outline-warning btn-lg w-100">
                                            <i class="bi bi-grid-fill d-block mb-2" style="font-size: 40px;"></i>
                                            Ver Productos
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>