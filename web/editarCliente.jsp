<%-- 
    Document   : editarCliente
    Created on : 14 oct 2025, 18:28:42
    Author     : will
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Cliente"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar Cliente</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 600px;
            margin: 50px auto;
            padding: 20px;
            background-color: #f4f4f4;
        }
        .form-container {
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h2 {
            color: #333;
            text-align: center;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #555;
            font-weight: bold;
        }
        input[type="text"],
        input[type="email"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .btn-group {
            display: flex;
            gap: 10px;
            margin-top: 20px;
        }
        button {
            flex: 1;
            padding: 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        .btn-guardar {
            background-color: #4CAF50;
            color: white;
        }
        .btn-guardar:hover {
            background-color: #45a049;
        }
        .btn-cancelar {
            background-color: #f44336;
            color: white;
        }
        .btn-cancelar:hover {
            background-color: #da190b;
        }
    </style>
</head>
<body>
    <%
        Cliente cliente = (Cliente) request.getAttribute("cliente");
    %>
    
    <div class="form-container">
        <h2>Editar Cliente</h2>
        
        <form action="ModificarCliente" method="post">
            <input type="hidden" name="id" value="<%= cliente.getId() %>">
            
            <div class="form-group">
                <label>Nombre:</label>
                <input type="text" name="nombreCliente" value="<%= cliente.getNombreCliente() %>" required>
            </div>
            
            <div class="form-group">
                <label>Email:</label>
                <input type="email" name="emailCliente" value="<%= cliente.getEmailCliente() %>" required>
            </div>
            
            <div class="form-group">
                <label>Teléfono:</label>
                <input type="text" name="telefonoCliente" value="<%= cliente.getTelefonoCliente() %>" required>
            </div>

            <div class="form-group">
                <label>DNI/RUC:</label>
                <input type="text" name="dniRuc" value="<%= cliente.getDniRuc() %>" required>
            </div>

            <div class="form-group">
                <label>Dirección:</label>
                <input type="text" name="direccion" value="<%= cliente.getDireccion() %>" required>
            </div>
            
            <div class="btn-group">
                <button type="submit" class="btn-guardar">Guardar Cambios</button>
                <button type="button" class="btn-cancelar" onclick="window.location.href='Clientes'">Cancelar</button>
            </div>
        </form>
    </div>
</body>
</html>