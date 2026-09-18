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
