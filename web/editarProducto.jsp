<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="modelo.Producto" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Producto</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header bg-success text-white">
                        <h3 class="mb-0">Editar Producto</h3>
                    </div>
                    <div class="card-body">
                        <%
                            Producto producto = (Producto) request.getAttribute("producto");
                            if (producto != null) {
                        %>
                        <form action="ModificarProducto" method="post">
                            <input type="hidden" name="id" value="<%= producto.getId() %>">
                            
                            <div class="mb-3">
                                <label for="nombreProducto" class="form-label">Nombre:</label>
                                <input type="text" class="form-control" id="nombreProducto" 
                                       name="nombreProducto" value="<%= producto.getNombreProducto() %>" required>
                            </div>
                            
                            <div class="mb-3">
                                <label for="precioProducto" class="form-label">Precio:</label>
                                <input type="number" step="0.01" class="form-control" id="precioProducto" 
                                       name="precioProducto" value="<%= producto.getPrecioProducto() %>" required>
                            </div>
                            
                            <div class="mb-3">
                                <label for="stockProducto" class="form-label">Stock:</label>
                                <input type="number" class="form-control" id="stockProducto" 
                                       name="stockProducto" value="<%= producto.getStockProducto() %>" required>
                            </div>
                            
                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="Productos" class="btn btn-secondary me-md-2">Cancelar</a>
                                <button type="submit" class="btn btn-success">Guardar Cambios</button>
                            </div>
                        </form>
                        <%
                            } else {
                        %>
                        <div class="alert alert-danger">
                            Producto no encontrado
                        </div>
                        <a href="Productos" class="btn btn-primary">Volver</a>
                        <%
                            }
                        %>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>