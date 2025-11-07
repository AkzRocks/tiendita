<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Pedido" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista de Pedidos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        body {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            min-height: 100vh;
            padding: 20px 0;
        }
        .card {
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
        }
        .badge-pendiente { background-color: #ffc107; }
        .badge-procesando { background-color: #0dcaf0; }
        .badge-completado { background-color: #198754; }
        .badge-cancelado { background-color: #dc3545; }
    </style>
</head>
<body>
    <div class="container mt-4">
        <div class="card">
            <div class="card-header bg-success text-white d-flex justify-content-between align-items-center">
                <div>
                    <a href="index.html" class="btn btn-light me-2">
                        <i class="bi bi-house"></i> Inicio
                    </a>
                    <h2 class="mb-0 d-inline">Lista de Pedidos</h2>
                </div>
                <a href="Pedidos?action=nuevo" class="btn btn-light">
                    <i class="bi bi-plus-circle"></i> Nuevo Pedido
                </a>
            </div>
            <div class="card-body">
                <%
                    String mensaje = request.getParameter("mensaje");
                    String error = request.getParameter("error");
                    
                    if (mensaje != null) {
                        String textoMensaje = "";
                        if (mensaje.equals("creado")) {
                            textoMensaje = "Pedido creado exitosamente";
                        } else if (mensaje.equals("modificado")) {
                            textoMensaje = "Pedido modificado exitosamente";
                        } else if (mensaje.equals("eliminado")) {
                            textoMensaje = "Pedido eliminado exitosamente";
                        }
                %>
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <%= textoMensaje %>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <%
                    }
                    
                    if (error != null) {
                        String textoError = "";
                        if (error.equals("sql")) {
                            textoError = "Error en la base de datos";
                        } else if (error.equals("sin_stock")) {
                            textoError = "Stock insuficiente para este pedido";
                        } else if (error.equals("no_encontrado")) {
                            textoError = "Pedido no encontrado";
                        } else if (error.equals("sin_productos")) {
                            textoError = "Debe agregar al menos un producto";
                        }
                %>
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <%= textoError %>
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
                                <th>Cliente</th>
                                <th>DNI/RUC</th>
                                <th>Total</th>
                                <th>Fecha</th>
                                <th>Estado</th>
                                <th class="text-center">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<Pedido> pedidos = (List<Pedido>) request.getAttribute("pedidos");
                                if (pedidos != null && !pedidos.isEmpty()) {
                                    for (Pedido pedido : pedidos) {
                                        String badgeClass = "badge-pendiente";
                                        if (pedido.getEstado().equals("Procesando")) badgeClass = "badge-procesando";
                                        else if (pedido.getEstado().equals("Completado")) badgeClass = "badge-completado";
                                        else if (pedido.getEstado().equals("Cancelado")) badgeClass = "badge-cancelado";
                            %>
                            <tr>
                                <td><%= pedido.getId() %></td>
                                <td><%= pedido.getNombreCliente() != null ? pedido.getNombreCliente() : "N/A" %></td>
                                <td><%= pedido.getDniRuc() != null ? pedido.getDniRuc() : "-" %></td>
                                <td>$<%= String.format("%.2f", pedido.getTotal()) %></td>
                                <td><%= new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(pedido.getFechaPedido()) %></td>
                                <td><span class="badge <%= badgeClass %>"><%= pedido.getEstado() %></span></td>
                                <td class="text-center">
                                    
                                    <a href="VerDetallePedido?id=<%= pedido.getId() %>" 
                                       class="btn btn-sm btn-info" 
                                       title="Ver Detalle">
                                        <i class="bi bi-eye"></i> Ver
                                    </a>
                                    <a href="EditarPedido?id=<%= pedido.getId() %>" 
                                        class="btn btn-sm btn-primary" 
                                        title="Editar Pedido">
                                         <i class="bi bi-pencil"></i> Editar
                                    </a>
                                    <a href="ModificarPedido?id=<%= pedido.getId() %>" 
                                        class="btn btn-sm btn-warning" 
                                        title="Cambiar Estado">
                                         <i class="bi bi-pencil-square"></i> Estado
                                    </a>
                                    <a href="EliminarPedido?id=<%= pedido.getId() %>" 
                                       class="btn btn-sm btn-danger" 
                                       title="Eliminar"
                                       onclick="return confirm('¿Estás seguro de eliminar este pedido?')">
                                        <i class="bi bi-trash"></i> Eliminar
                                    </a>
                                </td>
                            </tr>
                            <%
                                    }
                                } else {
                            %>
                            <tr>
                                <td colspan="6" class="text-center text-muted">
                                    No hay pedidos registrados
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