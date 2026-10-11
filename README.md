# Sistema de Gestión de Biblioteca Escolar

EFT semana 9 - Asignatura Programación Orientada a Objetos II de Duoc UC.

## Descripción

BibliotecaEscolar es una aplicación de escritorio que permite administrar una biblioteca escolar mediante una interfaz gráfica desarrollada en Swing conectada a una base de datos MySQL.
El sistema contempla dos roles: bibliotecario y estudiante.

## Funcionalidades

- Inicio de sesión según rol.
- Administración de libros, categorías y estudiantes (CRUD).
- Registro de préstamos y devoluciones.
- Control de disponibilidad de libros.
- Consulta de préstamos para estudiantes.
- Reportes de inventario, préstamos pendientes e historial.

## Tecnologías utilizadas

- Java
- Swing
- MySQL
- Maven

## Estructura del proyecto

El sistema utiliza una arquitectura MVC, organizada en los siguientes paquetes:

- `modelo`: clases y entidades del sistema.
- `vista`: ventanas gráficas.
- `controlador`: comunicación entre las vistas y los DAO.
- `dao`: operaciones con la base de datos.
- `config`: configuración de conexión a MySQL.

Se aplicó conceptos de programación orientada a objetos como herencia, abstracción, interfaces, polimorfismo y concurrencia mediante hilos.

## Requisitos y ejecución

1. Tener instalado Java 26 y MySQL.
2. Crear la base de datos utilizando el script SQL incluido en la entrega.
3. Configurar la conexión en `DatabaseConnection.java`.
4. Abrir el proyecto en IntelliJ IDEA.
5. Cargar las dependencias de Maven.
6. Ejecutar la clase `Main.java`.

## Usuarios de prueba

| Rol | RUT | Contraseña |
|---|---|---|
| Bibliotecario | 12345678-9 | clave123 |
| Estudiante | 98765432-1 | clave123 |


## Autor

Luis Díaz C.

Duoc UC — Analista Programador Computacional