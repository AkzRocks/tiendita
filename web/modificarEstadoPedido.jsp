<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="modelo.Pedido" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modificar Estado del Pedido</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        body {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            min-height: 100vh;
            padding: 40px 0;
        }
        .card {
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="card">
            <div class="card-header bg-success text-white">
                <h2 class="mb-0"><i class="bi bi-pencil-square"></i> Modificar Estado del Pedido</h2>
            </div>
            <div class="card-body">
                <%
                    Pedido pedido = (Pedido) request.getAttribute("pedido");
                    
                    if (pedido == null) {
                %>
                <div class="alert alert-danger">
                    No se pudo cargar el pedido. <a href="Pedidos" class="alert-link">Volver a la lista</a>
                </div>
                <%
                        return;
                    }
                %>
                
                <form action="ModificarPedido" method="post">
                    <input type="hidden" name="id" value="<%= pedido.getId() %>">
                    
                    <div class="mb-3">
                        <label class="form-label">Pedido ID:</label>
                        <input type="text" class="form-control" value="<%= pedido.getId() %>" readonly>
                    </div>
                    
                    <div class="mb-3">
                        <label class="form-label">Total:</label>
                        <input type="text" class="form-control" value="$<%= String.format("%.2f", pedido.getTotal()) %>" readonly>
                    </div>
                    
                    <div class="mb-3">
                        <label for="estado" class="form-label">Estado</label>
                        <select class="form-select" id="estado" name="estado" required>
                            <option value="Pendiente" <%= pedido.getEstado().equals("Pendiente") ? "selected" : "" %>>Pendiente</option>
                            <option value="Procesando" <%= pedido.getEstado().equals("Procesando") ? "selected" : "" %>>Procesando</option>
                            <option value="Completado" <%= pedido.getEstado().equals("Completado") ? "selected" : "" %>>Completado</option>
                            <option value="Cancelado" <%= pedido.getEstado().equals("Cancelado") ? "selected" : "" %>>Cancelado</option>
                        </select>
                    </div>
                    
                    <div class="d-flex gap-2">
                        <a href="Pedidos" class="btn btn-secondary">
                            <i class="bi bi-arrow-left"></i> Cancelar
                        </a>
                        <button type="submit" class="btn btn-success">
                            <i class="bi bi-check-circle"></i> Guardar Cambios
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>