<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nuevo Cliente</title>
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
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            padding: 40px;
            max-width: 600px;
            width: 100%;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            animation: fadeInUp 0.6s ease;
        }

        h1 {
            color: #667eea;
            text-align: center;
            margin-bottom: 30px;
            font-size: 2.5rem;
        }

        form {
            width: 100%;
        }

        table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0 15px;
        }

        tr {
            transition: all 0.3s ease;
        }

        td {
            padding: 10px 5px;
        }

        td:first-child {
            font-weight: 600;
            color: #333;
            width: 120px;
            vertical-align: middle;
        }

        input[type="text"],
        input[type="email"],
        input[type="number"] {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e0e0e0;
            border-radius: 10px;
            font-size: 1rem;
            transition: all 0.3s ease;
            background: white;
            font-family: inherit;
        }

        input[type="text"]:focus,
        input[type="email"]:focus,
        input[type="number"]:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
            transform: translateY(-2px);
        }

        input[type="text"]:hover,
        input[type="email"]:hover,
        input[type="number"]:hover {
            border-color: #b8c5f0;
        }

        .button-row {
            text-align: center;
            padding-top: 20px;
        }

        input[type="submit"] {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 14px 40px;
            font-size: 1.1rem;
            font-weight: 600;
            border-radius: 10px;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
            margin-right: 10px;
        }

        input[type="submit"]:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 25px rgba(102, 126, 234, 0.6);
        }

        input[type="submit"]:active {
            transform: translateY(-1px);
        }

        .back-link {
            display: inline-block;
            margin-top: 20px;
            padding: 12px 30px;
            background: #f5f5f5;
            color: #666;
            text-decoration: none;
            border-radius: 10px;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .back-link:hover {
            background: #e0e0e0;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }

        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        @media (max-width: 600px) {
            .container {
                padding: 30px 20px;
            }

            h1 {
                font-size: 2rem;
            }

            table {
                border-spacing: 0 10px;
            }

            td:first-child {
                display: block;
                width: 100%;
                margin-bottom: 5px;
            }

            td {
                display: block;
                width: 100%;
                padding: 5px 0;
            }

            .button-row {
                display: flex;
                flex-direction: column;
                gap: 10px;
            }

            input[type="submit"],
            .back-link {
                width: 100%;
                margin: 0;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>✨ Nuevo Cliente</h1>
        <form action="GuardarCliente" method="POST">
            <table>
                <tr>
                    <td>Nombre:</td>
                    <td><input type="text" name="nombreCliente" placeholder="Ingrese el nombre completo" required></td>
                </tr>
                <tr>
                    <td>Email:</td>
                    <td><input type="email" name="emailCliente" placeholder="ejemplo@correo.com" required></td>
                </tr>
                <tr>
                    <td>Teléfono:</td>
                    <td><input type="number" name="telefonoCliente" placeholder="987654321" required></td>
                </tr>
                <tr>
                    <td>DNI/RUC:</td>
                    <td><input type="text" name="dniRuc" placeholder="DNI o RUC" required></td>
                </tr>
                <tr>
                    <td>Dirección:</td>
                    <td><input type="text" name="direccion" placeholder="Dirección completa" required></td>
                </tr>
                <tr>
                    <td colspan="2" class="button-row">
                        <input type="submit" value="Guardar">
                        <br>
                        <a href="Clientes" class="back-link">← Volver a la lista</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</body>
</html>