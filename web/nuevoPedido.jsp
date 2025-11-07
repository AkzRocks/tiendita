<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="modelo.Cliente" %>
<%@ page import="modelo.Producto" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nuevo Pedido</title>
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
        .header-section {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 20px;
        }
        .products-table {
            margin-top: 20px;
        }
        .totals-section {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            margin-top: 20px;
        }
        .btn-add-product {
            margin-top: 10px;
        }
        .product-row {
            margin-bottom: 10px;
            padding: 10px;
            border: 1px solid #dee2e6;
            border-radius: 5px;
            background: white;
        }
        .remove-btn {
            cursor: pointer;
            color: #dc3545;
        }
        .total-display {
            font-size: 1.2rem;
            font-weight: bold;
            color: #198754;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="card">
            <div class="card-header bg-success text-white">
                <h2 class="mb-0">
                    <i class="bi bi-file-earmark-plus"></i> FORMATO PARA EL REGISTRO DE PEDIDOS
                </h2>
            </div>
            <div class="card-body">
                <form id="pedidoForm" action="GuardarPedido" method="post">
                    <!-- ENCABEZADO DEL PEDIDO -->
                    <div class="header-section">
                        <div class="row">
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Cod.Cliente: <span class="text-danger">*Se digita</span></label>
                                <select class="form-select" name="clienteId" id="clienteId" required>
                                    <option value="">Seleccione un cliente</option>
                                    <%
                                        List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
                                        if (clientes != null) {
                                            for (Cliente cliente : clientes) {
                                    %>
                                    <option value="<%= cliente.getId() %>"
                                            data-nombre="<%= cliente.getNombreCliente() != null ? cliente.getNombreCliente() : "" %>"
                                            data-dni="<%= cliente.getDniRuc() != null ? cliente.getDniRuc() : "" %>"
                                            data-direccion="<%= cliente.getDireccion() != null ? cliente.getDireccion() : "" %>">
                                        <%= cliente.getId() %> - <%= cliente.getNombreCliente()%>
                                    </option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Fecha: <span class="text-muted">Del sistema</span></label>
                                <input type="text" class="form-control" value="<%= new SimpleDateFormat("dd/MM/yyyy").format(new Date()) %>" readonly>
                            </div>
                        </div>
                        <div class="row mt-3">
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Nombres:</label>
                                <input type="text" class="form-control" id="nombreCliente" readonly>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold">DNI/RUC:</label>
                                <input type="text" class="form-control" id="dniCliente" readonly>
                            </div>
                        </div>
                        <div class="row mt-3">
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Nro.Pedido: <span class="text-success">Automático</span></label>
                                <input type="text" class="form-control" value="Se genera automáticamente" readonly>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Dirección:</label>
                                <input type="text" class="form-control" id="direccionCliente" readonly>
                            </div>
                        </div>
                    </div>

                    <!-- TABLA DE PRODUCTOS -->
                    <div class="products-table">
                        <h5 class="mb-3">Productos del Pedido</h5>
                        <div class="table-responsive">
                            <table class="table table-bordered">
                                <thead class="table-dark">
                                    <tr>
                                        <th width="5%">Item</th>
                                        <th width="35%">Descripción</th>
                                        <th width="15%">Precio</th>
                                        <th width="10%">Cantidad</th>
                                        <th width="10%">IGV (18%)</th>
                                        <th width="15%">Sub Total</th>
                                        <th width="10%">Acción</th>
                                    </tr>
                                </thead>
                                <tbody id="productosBody">
                                    <!-- Los productos se agregarán aquí dinámicamente -->
                                </tbody>
                            </table>
                        </div>
                        <button type="button" class="btn btn-primary btn-add-product" onclick="agregarProducto()">
                            <i class="bi bi-plus-circle"></i> Agregar Producto
                        </button>
                    </div>

                    <!-- TOTALES -->
                    <div class="totals-section">
                        <div class="row">
                            <div class="col-md-8">
                                <div class="d-flex gap-3">
                                    <button type="submit" class="btn btn-success">
                                        <i class="bi bi-save"></i> Registrar Pedido
                                    </button>
                                    <a href="Pedidos" class="btn btn-secondary">
                                        <i class="bi bi-arrow-left"></i> Cancelar
                                    </a>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="mb-2">
                                    <label class="form-label fw-bold">SubTotal:</label>
                                    <input type="text" class="form-control total-display" id="subtotalDisplay" readonly value="$0.00">
                                    <input type="hidden" name="subtotal" id="subtotal" value="0">
                                </div>
                                <div class="mb-2">
                                    <label class="form-label fw-bold">IGV:</label>
                                    <input type="text" class="form-control total-display" id="igvDisplay" readonly value="$0.00">
                                    <input type="hidden" name="igv" id="igv" value="0">
                                </div>
                                <div class="mb-2">
                                    <label class="form-label fw-bold">Total:</label>
                                    <input type="text" class="form-control total-display bg-success text-white" id="totalDisplay" readonly value="$0.00">
                                    <input type="hidden" name="total" id="total" value="0">
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>

<template id="productoTemplate">
    <tr class="producto-row">
        <td class="text-center item-number"></td>
        <td>
            <select class="form-control producto-select" name="productoId[]" required onchange="actualizarProducto(this)">
                <option value="">Seleccione un producto</option>
                <%
                    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
                    if (productos != null) {
                        for (Producto producto : productos) {
                %>
                <option value="<%= producto.getId() %>" 
                        data-nombre="<%= producto.getNombreProducto() %>"
                        data-precio="<%= producto.getPrecioProducto() %>"
                        data-stock="<%= producto.getStockProducto() %>">
                    <%= producto.getNombreProducto() %> (Stock: <%= producto.getStockProducto() %>)
                </option>
                <%
                        }
                    }
                %>
            </select>
        </td>
        <td>
            <input type="number" class="form-control precio-input" name="precio[]" step="0.01" readonly>
        </td>
        <td>
            <input type="number" class="form-control cantidad-input" name="cantidad[]" min="1" value="1" required onchange="calcularSubtotal(this)">
        </td>
        <td>
            <input type="number" class="form-control igv-input" step="0.01" readonly>
        </td>
        <td>
            <input type="number" class="form-control subtotal-input" name="subtotal[]" step="0.01" readonly>
        </td>
        <td class="text-center">
            <button type="button" class="btn btn-sm btn-danger" onclick="eliminarProducto(this)">
                <i class="bi bi-trash"></i>
            </button>
        </td>
    </tr>
</template>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        let itemCounter = 0;

        // Cargar información del cliente al seleccionarlo
        document.getElementById('clienteId').addEventListener('change', function() {
            const select = this;
            const selectedOption = select.options[select.selectedIndex];
            
            if (select.value) {
                document.getElementById('nombreCliente').value = selectedOption.getAttribute('data-nombre') || '';
                document.getElementById('dniCliente').value = selectedOption.getAttribute('data-dni') || '';
                document.getElementById('direccionCliente').value = selectedOption.getAttribute('data-direccion') || '';
            } else {
                document.getElementById('nombreCliente').value = '';
                document.getElementById('dniCliente').value = '';
                document.getElementById('direccionCliente').value = '';
            }
        });

        function agregarProducto() {
            const template = document.getElementById('productoTemplate');
            const clone = template.content.cloneNode(true);
            
            itemCounter++;
            clone.querySelector('.item-number').textContent = itemCounter;
            
            document.getElementById('productosBody').appendChild(clone);
            renumerarItems();
        }

        function eliminarProducto(btn) {
            btn.closest('tr').remove();
            renumerarItems();
            calcularTotales();
        }

        function renumerarItems() {
            const rows = document.querySelectorAll('#productosBody tr');
            rows.forEach((row, index) => {
                row.querySelector('.item-number').textContent = index + 1;
            });
            itemCounter = rows.length;
        }

        function actualizarProducto(select) {
            const row = select.closest('tr');
            const option = select.options[select.selectedIndex];
            
            if (select.value) {
                const precio = parseFloat(option.getAttribute('data-precio'));
                const stock = parseInt(option.getAttribute('data-stock'));
                
                row.querySelector('.precio-input').value = precio.toFixed(2);
                row.querySelector('.cantidad-input').max = stock;
                
                calcularSubtotal(row.querySelector('.cantidad-input'));
            } else {
                row.querySelector('.precio-input').value = '';
                row.querySelector('.cantidad-input').value = 1;
                row.querySelector('.igv-input').value = '';
                row.querySelector('.subtotal-input').value = '';
                calcularTotales();
            }
        }

        function calcularSubtotal(input) {
            const row = input.closest('tr');
            const precio = parseFloat(row.querySelector('.precio-input').value) || 0;
            const cantidad = parseInt(input.value) || 0;
            
            const subtotal = precio * cantidad;
            const igv = subtotal * 0.18;
            
            row.querySelector('.igv-input').value = igv.toFixed(2);
            row.querySelector('.subtotal-input').value = subtotal.toFixed(2);
            
            calcularTotales();
        }

        function calcularTotales() {
            let subtotalGeneral = 0;
            let igvGeneral = 0;
            
            document.querySelectorAll('#productosBody tr').forEach(row => {
                const subtotal = parseFloat(row.querySelector('.subtotal-input').value) || 0;
                const igv = parseFloat(row.querySelector('.igv-input').value) || 0;
                
                subtotalGeneral += subtotal;
                igvGeneral += igv;
            });
            
            const totalGeneral = subtotalGeneral + igvGeneral;
            
            document.getElementById('subtotalDisplay').value = '$' + subtotalGeneral.toFixed(2);
            document.getElementById('igvDisplay').value = '$' + igvGeneral.toFixed(2);
            document.getElementById('totalDisplay').value = '$' + totalGeneral.toFixed(2);
            
            document.getElementById('subtotal').value = subtotalGeneral.toFixed(2);
            document.getElementById('igv').value = igvGeneral.toFixed(2);
            document.getElementById('total').value = totalGeneral.toFixed(2);
        }

        // Preparar datos para enviar
document.getElementById('pedidoForm').addEventListener('submit', function(e) {
    const productos = document.querySelectorAll('#productosBody tr');
    
    if (productos.length === 0) {
        e.preventDefault();
        alert('Debe agregar al menos un producto al pedido');
        return false;
    }
    
    // Verificar que todos los productos tengan datos válidos
    let productosValidos = 0;
    productos.forEach(row => {
        const productoId = row.querySelector('.producto-select').value;
        const cantidad = row.querySelector('.cantidad-input').value;
        
        if (productoId && cantidad && cantidad > 0) {
            productosValidos++;
        }
    });
    
    if (productosValidos === 0) {
        e.preventDefault();
        alert('Debe agregar al menos un producto válido al pedido');
        return false;
    }
    
    console.log('Enviando pedido con ' + productosValidos + ' productos');
});

        // Agregar un producto por defecto al cargar
        window.onload = function() {
            agregarProducto();
        };
    </script>
</body>
</html>