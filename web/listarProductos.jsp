<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Producto" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista de Productos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</head>
<body>
    <div class="container mt-5">
        <div class="card">
            <div class="card-header bg-success text-white d-flex justify-content-between align-items-center">
                <h2 class="mb-0">Lista de Productos</h2>
                <a href="nuevoProducto.jsp" class="btn btn-light">
                    <i class="bi bi-plus-circle"></i> Nuevo Producto
                </a>
                <a href="dashboard.jsp" class="btn-home">inicio</a>

<style>
.btn-home {
    position: fixed;
    bottom: 20px;
    left: 20px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    width: 60px;
    height: 60px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 1.5rem;
    text-decoration: none;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
    transition: all 0.3s ease;
    z-index: 1000;
}

.btn-home:hover {
    transform: scale(1.1);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.4);
}
</style>
            </div>
            <div class="card-body">
                <%
                    String mensaje = request.getParameter("mensaje");
                    String error = request.getParameter("error");
                    
                    if (mensaje != null) {
                        String textoMensaje = "";
                        if (mensaje.equals("eliminado")) textoMensaje = "Producto eliminado exitosamente";
                        else if (mensaje.equals("modificado")) textoMensaje = "Producto modificado exitosamente";
                %>
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <%= textoMensaje %>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <%
                    }
                    
                    if (error != null) {
                        String textoError = "";
                        if (error.equals("sql")) textoError = "Error en la base de datos";
                        else if (error.equals("no_encontrado")) textoError = "Producto no encontrado";
                        else if (error.equals("id_invalido")) textoError = "ID inválido";
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
                                <th>Nombre</th>
                                <th>Precio</th>
                                <th>Stock</th>
                                <th class="text-center">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<Producto> productos = (List<Producto>) request.getAttribute("productos");
                                if (productos != null && !productos.isEmpty()) {
                                    for (Producto producto : productos) {
                            %>
                            <tr>
                                <td><%= producto.getId() %></td>
                                <td><%= producto.getNombreProducto() %></td>
                                <td>$<%= String.format("%.2f", producto.getPrecioProducto()) %></td>
                                <td><%= producto.getStockProducto() %></td>
                                <td class="text-center">
                                    <a href="ModificarProducto?id=<%= producto.getId() %>" 
                                       class="btn btn-sm btn-warning" 
                                       title="Editar">
                                        <i class="bi bi-pencil-square"></i> Editar
                                    </a>
                                    <a href="EliminarProducto?id=<%= producto.getId() %>" 
                                       class="btn btn-sm btn-danger" 
                                       title="Eliminar"
                                       onclick="return confirm('¿Estás seguro de eliminar este producto?')">
                                        <i class="bi bi-trash"></i> Eliminar
                                    </a>
                                </td>
                            </tr>
                            <%
                                    }
                                } else {
                            %>
                            <tr>
                                <td colspan="5" class="text-center text-muted">
                                    No hay productos registrados
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