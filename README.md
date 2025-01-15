# Backend de la Aplicación de Gestión Empresarial

## Contexto General
Este proyecto es un backend desarrollado en Java utilizando el framework Spring Boot. La aplicación permite gestionar empresas, productos, categorías, clientes y órdenes, ofreciendo funcionalidades CRUD y relaciones entre estas entidades. Además, se implementa seguridad con JWT y se utiliza PostgreSQL como base de datos.

El backend está desplegado en una instancia EC2 de AWS y configurado para ser accedido mediante un dominio público.

---

## Estructura del Proyecto

### Tecnologías y Herramientas
- **Spring Boot**: Para la construcción del backend.
- **PostgreSQL**: Base de datos relacional utilizada.
- **Hibernate/JPA**: Para la gestión de entidades y persistencia de datos.
- **JWT**: Implementado para seguridad y autenticación.
- **Swagger (Springdoc OpenAPI 3)**: Documentación interactiva de los endpoints.
- **AWS EC2**: Despliegue del backend.

### Dependencias Principales
- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-security`
- `springdoc-openapi-starter-webmvc-ui`
- `postgresql`

---

## Uso de Swagger

### Descripción
La aplicación incluye una integración con Swagger, lo que permite a los desarrolladores explorar y probar los endpoints de manera interactiva.

### Acceso a Swagger
1. Accede al endpoint `/swagger-ui.html` desde el navegador. Si el backend está desplegado en AWS, la URL sería:
   ```
   http://<PUBLIC_DOMAIN>:8080/swagger-ui.html
   ```
   Por ejemplo:
   ```
   http://ec2-52-204-25-137.compute-1.amazonaws.com:8080/swagger-ui.html
   ```
2. Se mostrará una interfaz gráfica con todos los endpoints organizados por controlador.

### Autorización con Bearer Token
Muchos endpoints requieren autorización con un token JWT.
1. Obtén el token utilizando el endpoint de autenticación (normalmente `/api/auth/login`).
2. En la parte superior derecha de la interfaz de Swagger, haz clic en "Authorize".
3. Ingresa el token con el formato:
   ```
   Bearer <TOKEN>
   ```
4. Una vez autorizado, todos los endpoints protegidos podrán ser probados directamente desde Swagger.

### Endpoints Públicos y Protegidos
- **Públicos**: Los endpoints relacionados con el registro de usuarios y autenticación no requieren un token JWT.
  - Ejemplo: `/api/auth/login`, `/api/auth/register`
- **Protegidos**: Los endpoints relacionados con empresas, productos, clientes, órdenes, etc., requieren autenticación.

---

## Opciones de Mejora

### 1. **Pruebas Unitarias y de Integración**
   - Implementar pruebas unitarias para todos los servicios y controladores utilizando JUnit y Mockito.
   - Configurar pruebas de integración con una base de datos en memoria como H2.

### 2. **Seguridad y Configuración de CORS**
   - Mejorar las reglas de seguridad configuradas en `SecurityConfig` para adaptarlas a un entorno de producción.
   - Configurar CORS para permitir solicitudes desde dominios específicos.

### 3. **Despliegue Automatizado**
   - Implementar pipelines CI/CD utilizando herramientas como GitHub Actions o Jenkins para automatizar el despliegue en AWS EC2.

### 4. **Optimización de Consultas**
   - Revisar y optimizar consultas JPA/Hibernate para mejorar el rendimiento, especialmente en endpoints que manejan grandes volúmenes de datos.

### 5. **Implementación de Caché**
   - Utilizar una solución como Redis para almacenar en caché las consultas más frecuentes y reducir la carga en la base de datos.

### 6. **Soporte Multilenguaje**
   - Añadir soporte para múltiples idiomas en los mensajes de error y las respuestas de los endpoints utilizando `MessageSource` en Spring.

---

## Guía para el Despliegue

### Requisitos Previos
1. **Instancia AWS EC2**:
   - Tipo: `t2.micro` (para pruebas) o superior.
   - Sistema Operativo: Amazon Linux 2.
2. **Base de Datos PostgreSQL**:
   - Configurada con las tablas necesarias para la aplicación.
3. **Java 17+**: Instalado en la instancia EC2.

### Pasos para Desplegar
1. Genera el archivo `.jar` del proyecto:
   ```
   mvn clean package
   ```
2. Sube el archivo `.jar` a la instancia EC2 utilizando `scp`:
   ```
   scp -i <RUTA_A_TU_CLAVE>.pem <RUTA_A_TU_JAR>.jar ec2-user@<DOMINIO_PUBLICO>:~/
   ```
3. Conéctate a la instancia EC2:
   ```
   ssh -i <RUTA_A_TU_CLAVE>.pem ec2-user@<DOMINIO_PUBLICO>
   ```
4. Ejecuta el archivo `.jar`:
   ```
   nohup java -jar <NOMBRE_DEL_JAR>.jar > app.log 2>&1 &
   ```
5. Verifica que el servicio esté en ejecución:
   ```
   curl http://localhost:8080/actuator/health
   ```

---

Con esta estructura y el soporte de Swagger, los desarrolladores pueden explorar y probar todos los endpoints fácilmente, mientras que las opciones de mejora ofrecen una hoja de ruta para futuras iteraciones del proyecto.

