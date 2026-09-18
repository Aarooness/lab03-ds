# Laboratorio académico – Backend + Frontend + PostgreSQL



## Requisitos
- Docker Desktop
- Docker Compose
- Node.js 20+ para desarrollo local del frontend
- Java 17 para ejecución local del backend si se desea

## Estructura del proyecto

```text
lab_backend_spring/
├── backend/                  # Código del backend Spring Boot (este proyecto)
├── frontend/                 # App React + Vite
├── .env                      # Variables locales para Docker
├── .env.example              # Plantilla de variables (Parte 1)
├── docker-compose.yml        # Orquestación local completa
├── Dockerfile                # Imagen del backend
├── README.md                 # Instrucciones y checklist
└── src/                      # Código fuente del backend
```

## Variables de entorno
El archivo `.env.example` define las variables base del proyecto:

```env
POSTGRES_DB=labdb
POSTGRES_USER=labuser
POSTGRES_PASSWORD=labpass
POSTGRES_PORT=5433
APP_PORT=8080
FRONTEND_PORT=5173
```

Para el frontend, la app usa la variable de entorno `VITE_API_URL` con valor por defecto:

```env
VITE_API_URL=http://localhost:8080/api
```

## Ejecutar con Docker Compose
1. Ajusta `.env` si quieres personalizar los valores locales.
2. Desde la raíz del proyecto ejecuta:

```bash
docker compose up --build
```

3. Accede a:
- Frontend: http://localhost:5173
- Backend API: http://localhost:8080/api
- PostgreSQL: localhost:5433

## Credenciales por defecto
- Email: `admin@laboratorio.local`
- Password: `admin123`

## Endpoints del backend
- POST `/api/auth/login`
- POST `/api/empleados`
- GET `/api/empleados`
- GET `/api/empleados/{id}`
- PUT `/api/empleados/{id}`
- DELETE `/api/empleados/{id}`

## Checklist para el video demo (máx. 5 minutos)
1. Login fallido con credenciales incorrectas mostrando el error visible.
2. Login exitoso y redirección al panel.
3. Crear un empleado nuevo con validación de campos obligatorios.
4. Listar los empleados registrados.
5. Actualizar un empleado existente.
6. Eliminar un empleado con confirmación antes de borrar.

## Tecnologías
- Spring Boot 3.3.x
- Spring Data JPA
- PostgreSQL 16
- Spring Security (PasswordEncoder)
- BCrypt
- React + Vite
- Docker Compose

## Prompts
1
Actúa como un desarrollador Senior en Java Spring Boot y PostgreSQL. Necesito estructurar el backend y la base de datos para un laboratorio académico. Todo el entorno debe ser 100% local con Docker Compose (sin servicios en la nube ni AWS).

Nota sobre el modelo de datos: la entidad "Empleado" representa tanto al empleado como al usuario que inicia sesión (el enunciado del laboratorio mezcla ambos conceptos), así que debe tener 6 atributos e incluir credenciales de acceso.

Requisitos para esta Parte 1:

1. Estructura del Backend (Spring Boot):
   - Arquitectura en capas: Controller, Service, Repository, Entity, DTOs.
   - PostgreSQL conectado mediante Spring Data JPA.
   - Autenticación: endpoint POST /api/auth/login que reciba email y password, compare el password contra un hash BCrypt almacenado en la base de datos (usa spring-boot-starter-security solo para el PasswordEncoder, no es necesario implementar JWT completo si el laboratorio no lo exige; una sesión simple o un token básico es suficiente).
   - Configura CORS explícitamente para permitir peticiones desde http://localhost:5173.
   - Manejo global de errores con @ControllerAdvice: respuestas JSON consistentes (código, mensaje) tanto para credenciales inválidas como para errores de validación.

2. Entidad y Modelo de Datos:
   - Entidad Empleado con exactamente 6 atributos:
     1. id (Long, autoincremental)
     2. nombre (String, obligatorio)
     3. email (String, único, obligatorio, formato válido)
     4. rol (String: "admin" o "usuario")
     5. cargo (String)
     6. password (String, almacenado como hash BCrypt, nunca se devuelve en las respuestas JSON)
   - Usa Bean Validation (@NotBlank, @Email, etc.) en el DTO de entrada.
   - Script de inicialización (schema.sql y data.sql) que cree la tabla e inserte un usuario admin por defecto, con el password ya hasheado con BCrypt (indica el valor en texto plano en un comentario para poder loguearse: ej. admin / admin123).

3. Endpoints REST (CRUD completo):
   - POST /api/empleados — crear (rechaza campos obligatorios vacíos o email duplicado).
   - GET /api/empleados — listar todos.
   - GET /api/empleados/{id} — obtener uno.
   - PUT /api/empleados/{id} — actualizar.
   - DELETE /api/empleados/{id} — eliminar.
   - Ninguna respuesta debe incluir el campo password.

4. Infraestructura Docker (100% local):
   - Dockerfile multi-stage para la app Spring Boot (build con Maven/Gradle, imagen final liviana con JRE).
   - docker-compose.yml con:
     * servicio postgres (puerto 5432, volumen nombrado para persistencia, variables de entorno para usuario/clave/base de datos definidas en un archivo .env, NO hardcodeadas).
     * servicio backend (puerto 8080), con depends_on configurado con condition: service_healthy y un healthcheck en el servicio postgres.
   - Incluye un archivo .env.example con las variables necesarias (sin valores reales sensibles).

Genera el código limpio, comentado donde haga falta, y listo para ejecutar con "docker compose up --build".

2
Ahora desarrollaremos el Frontend y la integración completa con el backend de la Parte 1. Todo debe seguir siendo 100% local, sin servicios en la nube.

Requisitos para esta Parte 2:

1. Configuración del Frontend:
   - React + Vite, configurado para correr en el puerto 5173 (define esto explícitamente en vite.config.js con server.port = 5173; no usar el puerto 5172).
   - La URL del backend debe venir de una variable de entorno VITE_API_URL (por defecto http://localhost:8080/api), nunca hardcodeada en el código, para que funcione igual en desarrollo local y dentro de Docker.
   - Maneja el estado de sesión (usuario logueado) en el cliente, por ejemplo con Context API, y protege la ruta /dashboard con un componente de ruta privada que redirige a /login si no hay sesión activa.

2. Interfaz y Flujo de Usuario:
   - Pantalla de Login: formulario con email y password. Muestra un mensaje de error visible y claro cuando el login falla (401), y redirige a /dashboard cuando es exitoso.
   - Panel de Administración (CRUD de Empleados):
     * Tabla que liste los empleados con sus 6 atributos (excepto el password, que nunca se muestra).
     * Formulario o modal para "Crear Empleado" con validación de campos obligatorios y de formato de email antes de enviar la petición.
     * Modal o modo de edición para "Actualizar Empleado", precargado con los datos actuales.
     * Botón "Eliminar" que abre un modal de confirmación antes de ejecutar el DELETE.
     * Manejo visible de errores del backend (por ejemplo, email duplicado) en los formularios.
     * Botón "Cerrar sesión" que limpia el estado y redirige a /login.

3. Integración con Docker Compose:
   - Agrega el servicio frontend al docker-compose.yml existente: un Dockerfile multi-stage que construya la app de Vite y la sirva (puede usarse el propio servidor de Vite en modo preview o Nginx), expuesto en el puerto 5173, en la misma red que backend y postgres.

4. Documentación y entrega (README.md):
   - Instrucciones paso a paso para levantar todo con un solo comando: docker compose up --build.
   - Explicación de las variables de entorno necesarias (referenciando el .env.example de la Parte 1).
   - Credenciales de acceso por defecto (admin / admin123).
   - Estructura de carpetas del proyecto (backend/, frontend/, database/ o similar).
   - Un checklist claro de lo que debe mostrarse en el video demostrativo de máximo 5 minutos:
     1. Login fallido (credenciales incorrectas) mostrando el mensaje de error.
     2. Login exitoso, redirección al panel.
     3. Crear un empleado nuevo (mostrando la validación de campos obligatorios).
     4. Listar los empleados registrados.
     5. Actualizar un empleado existente.
     6. Eliminar un empleado con su confirmación.
