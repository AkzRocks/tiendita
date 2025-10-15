<%-- 
    Document   : nuevoProductos
    Created on : 14 oct 2025, 3:27:59
    Author     : Jean
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Nuevo Producto</title>
    </head>
    <body>
        <h1>Nuevo Producto</h1>
        <form action="GuardarProducto" method="POST">
            <table>
                <tr>
                    <td>Nombre:</td>
                    <td><input type="text" name="nombreProducto" required></td>
                </tr>
                <tr>
                    <td>Precio:</td>
                    <td><input type="number" step="0.01" name="precioProducto" required></td>
                </tr>
                <tr>
                    <td>Stock:</td>
                    <td><input type="number" name="stockProducto" required></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" value="Guardar"></td>
                </tr>
            </table>
        </form>
        <br>
        <a href="Productos">Volver a la lista</a>
    </body>
</html>