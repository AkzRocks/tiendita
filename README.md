# 🛍️ Tiendita — Sistema de Administración de Tienda

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
![Apache Tomcat](https://img.shields.io/badge/Tomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=black)
![Status](https://img.shields.io/badge/Estado-En%20desarrollo-yellow?style=for-the-badge)
![License](https://img.shields.io/badge/Licencia-MIT-green?style=for-the-badge)

---

## 🧾 Descripción

**Tiendita** es una aplicación web desarrollada en **Java con Servlets y MySQL**, diseñada para la **gestión administrativa de una tienda**.  
Permite realizar operaciones **CRUD** (Crear, Leer, Actualizar y Eliminar) sobre **clientes, productos y pedidos**, con una estructura modular y escalable.  

Este proyecto forma parte de un **trabajo universitario**, enfocado en aplicar conceptos de **programación web, bases de datos y arquitectura MVC**.

---

## 🚀 Tecnologías utilizadas

| Componente | Descripción |
|--|--|
| **Java** | Lenguaje principal para la lógica de negocio. |
| **Servlets & JSP** | Controladores y vistas del sistema. |
| **MySQL** | Base de datos relacional para persistencia de información. |
| **Apache Tomcat** | Servidor web para el despliegue de la aplicación. |
| **HTML / CSS / JS** | Interfaz de usuario del sistema web. |

---

## ⚙️ Funcionalidades principales

✅ **Módulo de Clientes**  
- Registro de nuevos clientes  
- Edición y eliminación  
- Visualización en lista/tablas  

✅ **Módulo de Productos**  
- CRUD completo de productos  
- Control de stock y precios  

✅ **Módulo de Pedidos**  
- Registro de pedidos asociados a clientes  
- Visualización de historial  
- Actualización del estado del pedido  

---

## 🧱 Estructura del proyecto
```
MITienda/
│
├── Web Pages/ → Archivos JSP y HTML (interfaz web)
│
├── Source Packages/ → Código fuente en Java
│ ├── controlador/ → Servlets (lógica de negocio)
│ └── modelo/ → Clases del modelo y conexión a la base de datos
│
├── Libraries/ → Dependencias (Jakarta EE, MySQL Connector, etc.)
│
└── Configuration Files/ → Archivos de configuración (web.xml, context.xml)
```
---

## 🧠 Arquitectura MVC

- **Modelo (`modelo/`)** → Representa las entidades y conexión con la base de datos (Cliente, Producto, Pedido, Conexion).  
- **Vista (`Web Pages/`)** → Archivos JSP y HTML que muestran la información al usuario.  
- **Controlador (`controlador/`)** → Servlets encargados de recibir las solicitudes y coordinar las operaciones CRUD.

---

## 🛠️ Requisitos previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- ☕ **JDK 21**
- 🐱‍👤 **Apache Tomcat 10 o superior**
- 🐬 **MySQL Server / Workbench**
- 🧰 **NetBeans / IntelliJ / Eclipse** (opcional, recomendado)

---

## 🧠 Configuración del entorno

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/AkzRocks/tiendita.git
   cd tiendita
   
2. **Configurar la base de datos**

Crea una base de datos en MySQL:

Importa el script SQL incluido en el proyecto

3. **Configura la conexión MySQL**
En la clase modelo/Conexion.java, actualiza tus credenciales:
```java
private static final String URL = "jdbc:mysql://localhost:3306/tiendita_db";
private static final String USER = "root";
private static final String PASSWORD = "tu_contraseña";
```
o tambien puede configurar tu varible de entorno con la contraseña de tu base de datos, la varible de entorno esta con el nombre de **MYSQL_PASSWORD**

4. **Despliega en Tomcat**

Abre el proyecto en NetBeans o tu IDE preferido.

Asigna el servidor Apache Tomcat o TomEE.

Ejecuta el proyecto (Run).

---
## 💻 Funcionalidades

| Módulo        | Operaciones                     | Descripción                               |
| ------------- | ------------------------------- | ----------------------------------------- |
| **Clientes**  | Crear, listar, editar, eliminar | Administración de información de clientes |
| **Productos** | Crear, listar, editar, eliminar | Control de inventario y precios           |
| **Pedidos**   | Crear, listar, editar, eliminar | Registro y gestión de pedidos de clientes |

---

## 🧩 Librerías utilizadas

mysql-connector-java-8.0.30.jar → Conexión con MySQL

Tomcat / TomEE → Contenedor web para ejecutar los servlets

---

## 🧑‍💻 Autores

| 👨‍🎓 Nombre | 💼 Rol / Usuario |
|-------------|------------------|
| Chumioque Tello, Jesús | Estudiante - UTP |
| Zevallos López, Jean | Estudiante - UTP |
| Navarro Silva, Wilder Alejandro ([AkzRocks](https://github.com/AkzRocks)) | Desarrollador principal |
| Atahua Huauya, Everson Samuel | Estudiante - UTP |
| Prada Martos, Renzo Felipe | Estudiante - UTP |

📘 **Proyecto académico — Universidad Tecnológica del Perú**

---

## 📜 Licencia

Este proyecto está bajo la Licencia MIT.

---

## 🌟 Agradecimientos

- Docentes y tutores del curso.

- Comunidad Java y Jakarta EE.

- Compañeros de clase que aportaron ideas y pruebas.

