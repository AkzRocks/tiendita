<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="modelo.Pedido" %>
<%@ page import="modelo.Cliente" %>
<%@ page import="modelo.Producto" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modificar Pedido</title>
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
                <h2 class="mb-0"><i class="bi bi-pencil-square"></i> Modificar Pedido</h2>
            </div>
            <div class="card-body">
                <%
                    Pedido pedido = (Pedido) request.getAttribute("pedido");
                    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
                    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
                    if (pedido == null) {
                    response.sendRedirect("Pedidos?error=no_encontrado");
                    return;
                }
            %>
            
            <form action="ModificarPedido" method="post">
                <input type="hidden" name="id" value="<%= pedido.getId() %>">
                
                <div class="mb-3">
                    <label for="clienteId" class="form-label">Cliente</label>
                    <select class="form-select" id="clienteId" name="clienteId" required>
                        <%
                            if (clientes != null) {
                                for (Cliente cliente : clientes) {
                        %>
                        <option value="<%= cliente.getId() %>" 
                                <%= cliente.getId() == pedido.getClienteId() ? "selected" : "" %>>
                            <%= cliente.getNombreCliente() %>
                        </option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>
                
                <div class="mb-3">
                    <label for="productoId" class="form-label">Producto</label>
                    <select class="form-select" id="productoId" name="productoId" required onchange="updatePrice()">
                        <%
                            if (productos != null) {
                                for (Producto producto : productos) {
                        %>
                        <option value="<%= producto.getId() %>" 
                                <%= producto.getId() == pedido.getProductoId() ? "selected" : "" %>
                                data-precio="<%= producto.getPrecioProducto() %>"
                                data-stock="<%= producto.getStockProducto() %>">
                            <%= producto.getNombreProducto() %> - Stock: <%= producto.getStockProducto() %>
                        </option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>
                
                <div class="mb-3">
                    <label for="cantidad" class="form-label">Cantidad</label>
                    <input type="number" class="form-control" id="cantidad" name="cantidad" 
                           value="<%= pedido.getCantidad() %>" min="1" required onchange="updateTotal()">
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
                
                <div class="mb-3">
                    <label class="form-label">Total Estimado</label>
                    <input type="text" class="form-control" id="totalEstimado" 
                           value="$<%= String.format("%.2f", pedido.getTotal()) %>" readonly>
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
<script>
    function updatePrice() {
        updateTotal();
    }
    
    function updateTotal() {
        const select = document.getElementById('productoId');
        const cantidad = document.getElementById('cantidad').value;
        const totalField = document.getElementById('totalEstimado');
        
        if (select.selectedIndex >= 0 && cantidad > 0) {
            const option = select.options[select.selectedIndex];
            const precio = parseFloat(option.getAttribute('data-precio'));
            const stock = parseInt(option.getAttribute('data-stock'));
            
            if (parseInt(cantidad) > stock) {
                alert('La cantidad supera el stock disponible (' + stock + ')');
                document.getElementById('cantidad').value = stock;
                return;
            }
            
            const total = precio * cantidad;
            totalField.value = '$' + total.toFixed(2);
        }
    }
    
    // Calcular total inicial al cargar la página
    window.onload = function() {
        updateTotal();
    };
</script>