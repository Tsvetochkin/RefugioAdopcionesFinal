# Final - Refugio de Adopciones "Patitas Felices"

## Descripción del Proyecto

Este proyecto es un sistema de escritorio para la gestión de adopciones en la veterinaria "Patitas Felices". Fue desarrollado como parte de la asignatura "Programación Avanzada".

El sistema permite a los empleados registrados gestionar todo el ciclo de vida de una adopción, incluyendo:
- Registrar nuevas adopciones.
- Visualizar el historial completo de adopciones.
- Modificar los datos de una adopción existente.
- Eliminar registros de adopción del sistema.

El proyecto está construido sobre Java y Spring Boot, utilizando Java Swing para la interfaz gráfica y una base de datos H2 para la persistencia de datos.

## Funcionalidades Implementadas

- **Gestión de Sesión:** Login y registro de empleados.
- **CRUD de Adopciones:** Creación, Lectura, Modificación y Eliminación de registros de adopción a través de una interfaz gráfica unificada.
- **Persistencia de Datos:** Toda la información (empleados, adoptantes, mascotas, adopciones) se guarda en una base de datos H2.
- **Arquitectura MVC:** El código está estructurado siguiendo el patrón Modelo-Vista-Controlador, con una capa de servicio para la lógica de negocio y una capa DAO (implementada con Spring Data JPA) para el acceso a datos.

## Base de Datos Utilizada

- **Motor:** H2 Database Engine.
- **Configuración:** La base de datos es de tipo "in-memory" / basada в файле. Se crea automáticamente un archivo `refugio-db.mv.db` en la raíz del proyecto en el primer arranque. Los datos persisten entre ejecuciones.

## Instrucciones para Compilar y Ejecutar

**Requisitos:**
- Java Development Kit (JDK) 17 o superior.
- Apache Maven.

**Pasos para la ejecución:**

1.  Abra una terminal o línea de comandos.
2.  Navegue hasta el directorio raíz del proyecto (donde se encuentra el archivo `pom.xml`).
    ```bash
    cd ruta/a/su/proyecto/Final - Refugio
    ```
3.  Ejecute el siguiente comando de Maven para compilar y lanzar la aplicación:
    ```bash
    ./mvnw spring-boot:run
    ```
4.  Maven descargará las dependencias necesarias, compilará el código y lanzará la aplicación. La ventana de Login aparecerá en la pantalla.

**Credenciales de prueba:**
- **Usuario:** `Noe`
- **Contraseña:** `password`

*(Estas credenciales se crean automáticamente en la base de datos en el primer arranque del sistema).*
