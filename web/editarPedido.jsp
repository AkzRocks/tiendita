<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="modelo.Pedido" %>
<%@ page import="modelo.Producto" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Pedido</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        body { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); min-height: 100vh; padding: 20px 0; }
        .card { box-shadow: 0 10px 30px rgba(0,0,0,0.2); }
        .info-section, .totals-section { background:#f8f9fa; padding: 16px; border-radius:10px; }
    </style>
</head>
<body>
<div class="container">
    <div class="card">
        <div class="card-header bg-success text-white d-flex justify-content-between align-items-center">
            <h2 class="mb-0"><i class="bi bi-pencil"></i> Editar Pedido</h2>
            <a href="Pedidos" class="btn btn-light"><i class="bi bi-arrow-left"></i> Volver</a>
        </div>
        <div class="card-body">
            <%
                Pedido pedido = (Pedido) request.getAttribute("pedido");
                List<Map<String, Object>> detalles = (List<Map<String, Object>>) request.getAttribute("detalles");
                List<Producto> productos = (List<Producto>) request.getAttribute("productos");
                if (pedido == null) {
            %>
            <div class="alert alert-danger">Pedido no encontrado.</div>
            <%
                    return;
                }
            %>

            <div class="info-section mb-3">
                <div class="row">
                    <div class="col-md-6">
                        <p class="mb-1"><strong>Pedido ID:</strong> <%= pedido.getId() %></p>
                        <p class="mb-1"><strong>Cliente:</strong> <%= pedido.getNombreCliente() %></p>
                        <p class="mb-1"><strong>DNI/RUC:</strong> <%= pedido.getDniRuc() != null ? pedido.getDniRuc() : "-" %></p>
                        <p class="mb-1"><strong>Dirección:</strong> <%= pedido.getDireccion() != null ? pedido.getDireccion() : "-" %></p>
                    </div>
                    <div class="col-md-6">
                        <p class="mb-1"><strong>Fecha:</strong> <%= new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(pedido.getFechaPedido()) %></p>
                        <p class="mb-1"><strong>Total actual:</strong> $<%= String.format("%.2f", pedido.getTotal()) %></p>
                        <div class="mb-1">
                            <label for="estado" class="form-label mb-0">Estado</label>
                            <select id="estado" name="estado" form="formEditar" class="form-select">
                                <option value="Pendiente" <%= "Pendiente".equals(pedido.getEstado())?"selected":"" %>>Pendiente</option>
                                <option value="Procesando" <%= "Procesando".equals(pedido.getEstado())?"selected":"" %>>Procesando</option>
                                <option value="Completado" <%= "Completado".equals(pedido.getEstado())?"selected":"" %>>Completado</option>
                                <option value="Cancelado" <%= "Cancelado".equals(pedido.getEstado())?"selected":"" %>>Cancelado</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

            <form id="formEditar" action="EditarPedido" method="post">
                <input type="hidden" name="id" value="<%= pedido.getId() %>">

                <h5 class="mb-2">Detalle actual</h5>
                <div class="table-responsive mb-4">
                    <table class="table table-striped table-bordered">
                        <thead class="table-dark">
                        <tr>
                            <th>#</th>
                            <th>Producto</th>
                            <th>Precio</th>
                            <th>Cantidad</th>
                            <th>Subtotal</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            if (detalles != null && !detalles.isEmpty()) {
                                int i = 1;
                                for (Map<String, Object> d : detalles) {
                        %>
                        <tr>
                            <td><%= i++ %></td>
                            <td><%= d.get("nombreProducto") %></td>
                            <td>$<%= String.format("%.2f", (Double)d.get("precio")) %></td>
                            <td><%= d.get("cantidad") %></td>
                            <td>$<%= String.format("%.2f", (Double)d.get("subtotal")) %></td>
                        </tr>
                        <%
                                }
                            } else {
                        %>
                        <tr><td colspan="5" class="text-center text-muted">Sin productos aún</td></tr>
                        <%
                            }
                        %>
                        </tbody>
                    </table>
                </div>

                <h5 class="mb-2">Agregar más productos</h5>
                <div class="table-responsive">
                    <table class="table table-bordered">
                        <thead class="table-light">
                        <tr>
                            <th style="width:40%">Producto</th>
                            <th style="width:15%">Precio</th>
                            <th style="width:15%">Cantidad</th>
                            <th style="width:20%">Subtotal</th>
                            <th style="width:10%">Acción</th>
                        </tr>
                        </thead>
                        <tbody id="tbodyNuevos"></tbody>
                    </table>
                </div>
                <button type="button" class="btn btn-primary mb-3" onclick="agregarFila()"><i class="bi bi-plus-circle"></i> Agregar Producto</button>

                <div class="totals-section mb-3">
                    <div class="row">
                        <div class="col-md-6">
                            <small class="text-muted">El total del pedido se recalculará (subtotales nuevos + existentes) con IGV 18%.</small>
                        </div>
                        <div class="col-md-6">
                            <div class="d-flex gap-2 justify-content-end">
                                <button type="submit" class="btn btn-success"><i class="bi bi-check-circle"></i> Guardar Cambios</button>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<template id="tplFila">
    <tr>
        <td>
            <select class="form-select selProducto" name="productoId[]" onchange="onProductoChange(this)">
                <option value="">Seleccione</option>
                <%
                    if (productos != null) {
                        for (Producto p : productos) {
                %>
                <option value="<%= p.getId() %>" data-precio="<%= p.getPrecioProducto() %>" data-stock="<%= p.getStockProducto() %>">
                    <%= p.getNombreProducto() %> (Stock: <%= p.getStockProducto() %>)
                </option>
                <%
                        }
                    }
                %>
            </select>
        </td>
        <td><input type="number" class="form-control inpPrecio" name="precio[]" step="0.01" readonly></td>
        <td><input type="number" class="form-control inpCantidad" name="cantidad[]" min="1" value="1" onchange="recalcular(this)"></td>
        <td><input type="number" class="form-control inpSubtotal" name="subtotal[]" step="0.01" readonly></td>
        <td class="text-center"><button type="button" class="btn btn-sm btn-outline-danger" onclick="eliminarFila(this)"><i class="bi bi-trash"></i></button></td>
    </tr>
</template>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function agregarFila() {
        const tpl = document.getElementById('tplFila');
        const clone = tpl.content.cloneNode(true);
        document.getElementById('tbodyNuevos').appendChild(clone);
    }
    function eliminarFila(btn) {
        btn.closest('tr').remove();
    }
    function onProductoChange(sel) {
        const row = sel.closest('tr');
        const opt = sel.options[sel.selectedIndex];
        if (!sel.value) { row.querySelector('.inpPrecio').value=''; row.querySelector('.inpSubtotal').value=''; return; }
        const precio = parseFloat(opt.getAttribute('data-precio')) || 0;
        row.querySelector('.inpPrecio').value = precio.toFixed(2);
        row.querySelector('.inpCantidad').max = parseInt(opt.getAttribute('data-stock')) || 1;
        recalcular(row.querySelector('.inpCantidad'));
    }
    function recalcular(inp) {
        const row = inp.closest('tr');
        const precio = parseFloat(row.querySelector('.inpPrecio').value) || 0;
        const cant = parseInt(row.querySelector('.inpCantidad').value) || 0;
        row.querySelector('.inpSubtotal').value = (precio * cant).toFixed(2);
    }
</script>
</body>
</html>
