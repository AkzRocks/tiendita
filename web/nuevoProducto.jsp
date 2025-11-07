<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Nuevo Producto</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                min-height: 100vh;
                display: flex;
                justify-content: center;
                align-items: center;
                padding: 20px;
            }

            .container {
                background: white;
                border-radius: 20px;
                box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
                padding: 40px;
                max-width: 500px;
                width: 100%;
                animation: slideIn 0.5s ease-out;
            }

            @keyframes slideIn {
                from {
                    opacity: 0;
                    transform: translateY(-30px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            .header {
                text-align: center;
                margin-bottom: 35px;
            }

            .header i {
                font-size: 50px;
                color: #667eea;
                margin-bottom: 15px;
            }

            h1 {
                color: #333;
                font-size: 28px;
                font-weight: 600;
                margin-bottom: 10px;
            }

            .subtitle {
                color: #666;
                font-size: 14px;
            }

            .form-group {
                margin-bottom: 25px;
            }

            label {
                display: block;
                color: #444;
                font-weight: 500;
                margin-bottom: 8px;
                font-size: 14px;
            }

            label i {
                margin-right: 8px;
                color: #667eea;
                width: 20px;
            }

            input[type="text"],
            input[type="number"] {
                width: 100%;
                padding: 14px 16px;
                border: 2px solid #e0e0e0;
                border-radius: 10px;
                font-size: 15px;
                transition: all 0.3s ease;
                outline: none;
                background: #f8f9fa;
            }

            input[type="text"]:focus,
            input[type="number"]:focus {
                border-color: #667eea;
                background: white;
                box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
            }

            input[type="text"]:hover,
            input[type="number"]:hover {
                border-color: #b8c5f2;
            }

            .button-group {
                margin-top: 35px;
                display: flex;
                gap: 15px;
            }

            input[type="submit"],
            .btn-back {
                flex: 1;
                padding: 14px 24px;
                border: none;
                border-radius: 10px;
                font-size: 16px;
                font-weight: 600;
                cursor: pointer;
                transition: all 0.3s ease;
                text-decoration: none;
                display: inline-flex;
                align-items: center;
                justify-content: center;
                gap: 8px;
            }

            input[type="submit"] {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
            }

            input[type="submit"]:hover {
                transform: translateY(-2px);
                box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
            }

            input[type="submit"]:active {
                transform: translateY(0);
            }

            .btn-back {
                background: white;
                color: #667eea;
                border: 2px solid #667eea;
            }

            .btn-back:hover {
                background: #667eea;
                color: white;
                transform: translateY(-2px);
                box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
            }

            .input-hint {
                font-size: 12px;
                color: #888;
                margin-top: 5px;
            }

            @media (max-width: 600px) {
                .container {
                    padding: 30px 20px;
                }

                h1 {
                    font-size: 24px;
                }

                .button-group {
                    flex-direction: column;
                }
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <i class="fas fa-box-open"></i>
                <h1>Nuevo Producto</h1>
                <p class="subtitle">Completa la información del producto</p>
            </div>

            <form action="GuardarProducto" method="POST">
                <div class="form-group">
                    <label for="nombreProducto">
                        <i class="fas fa-tag"></i>Nombre del Producto
                    </label>
                    <input type="text" 
                           id="nombreProducto" 
                           name="nombreProducto" 
                           placeholder="Ej: Laptop HP Pavilion"
                           required>
                </div>

                <div class="form-group">
                    <label for="precioProducto">
                        <i class="fas fa-dollar-sign"></i>Precio
                    </label>
                    <input type="number" 
                           id="precioProducto" 
                           step="0.01" 
                           name="precioProducto" 
                           placeholder="0.00"
                           required>
                    <div class="input-hint">Ingrese el precio con dos decimales</div>
                </div>

                <div class="form-group">
                    <label for="stockProducto">
                        <i class="fas fa-boxes"></i>Stock Disponible
                    </label>
                    <input type="number" 
                           id="stockProducto" 
                           name="stockProducto" 
                           placeholder="0"
                           min="0"
                           required>
                    <div class="input-hint">Cantidad de unidades disponibles</div>
                </div>

                <div class="button-group">
                    <a href="Productos" class="btn-back">
                        <i class="fas fa-arrow-left"></i>Volver
                    </a>
                    <input type="submit" value="Guardar Producto">
                </div>
            </form>
        </div>
    </body>
</html>