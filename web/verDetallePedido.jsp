<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="modelo.Pedido" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalle del Pedido</title>
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
        .info-section {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 20px;
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
            <div class="card-header bg-success text-white">
                <h2 class="mb-0"><i class="bi bi-receipt"></i> Detalle del Pedido #<%= request.getAttribute("pedido") != null ? ((Pedido)request.getAttribute("pedido")).getId() : "" %></h2>
            </div>
            <div class="card-body">
                <%
                    Pedido pedido = (Pedido) request.getAttribute("pedido");
                    List<Map<String, Object>> detalles = (List<Map<String, Object>>) request.getAttribute("detalles");
                    
                    if (pedido == null) {
                %>
                <div class="alert alert-danger">
                    No se pudo cargar el pedido. <a href="Pedidos" class="alert-link">Volver a la lista</a>
                </div>
                <%
                        return;
                    }
                    
                    String badgeClass = "badge-pendiente";
                    if (pedido.getEstado().equals("Procesando")) badgeClass = "badge-procesando";
                    else if (pedido.getEstado().equals("Completado")) badgeClass = "badge-completado";
                    else if (pedido.getEstado().equals("Cancelado")) badgeClass = "badge-cancelado";
                %>
                
                <!-- Información del Pedido -->
                <div class="info-section">
                    <div class="row">
                        <div class="col-md-6">
                            <p><strong>Cliente:</strong> <%= pedido.getNombreCliente() %></p>
                            <p><strong>Fecha:</strong> <%= new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(pedido.getFechaPedido()) %></p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>Estado:</strong> <span class="badge <%= badgeClass %>"><%= pedido.getEstado() %></span></p>
                            <p><strong>Total:</strong> <span class="text-success fs-4">$<%= String.format("%.2f", pedido.getTotal()) %></span></p>
                        </div>
                    </div>
                </div>
                
                <!-- Tabla de Productos -->
                <h5 class="mb-3">Productos del Pedido</h5>
                <div class="table-responsive">
                    <table class="table table-striped table-bordered">
                        <thead class="table-dark">
                            <tr>
                                <th>#</th>
                                <th>Producto</th>
                                <th>Precio Unit.</th>
                                <th>Cantidad</th>
                                <th>Subtotal</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (detalles != null && !detalles.isEmpty()) {
                                    int contador = 1;
                                    double totalGeneral = 0;
                                    for (Map<String, Object> detalle : detalles) {
                                        double subtotal = (Double) detalle.get("subtotal");
                                        totalGeneral += subtotal;
                            %>
                            <tr>
                                <td><%= contador++ %></td>
                                <td><%= detalle.get("nombreProducto") %></td>
                                <td>$<%= String.format("%.2f", (Double)detalle.get("precio")) %></td>
                                <td><%= detalle.get("cantidad") %></td>
                                <td>$<%= String.format("%.2f", subtotal) %></td>
                            </tr>
                            <%
                                    }
                            %>
                            <tr class="table-success">
                                <td colspan="4" class="text-end"><strong>TOTAL:</strong></td>
                                <td><strong>$<%= String.format("%.2f", totalGeneral) %></strong></td>
                            </tr>
                            <%
                                } else {
                            %>
                            <tr>
                                <td colspan="5" class="text-center text-muted">No hay productos en este pedido</td>
                            </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                </div>
                
                <!-- Botones de Acción -->
                <div class="mt-4">
                    <a href="Pedidos" class="btn btn-secondary">
                        <i class="bi bi-arrow-left"></i> Volver a la Lista
                    </a>
                    <button onclick="window.print()" class="btn btn-info">
                        <i class="bi bi-printer"></i> Imprimir
                    </button>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>