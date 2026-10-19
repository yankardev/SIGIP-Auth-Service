# SIGIP Auth Service

Servicio de registro e inicio de sesión para SIGIP. Usa Java 25, Spring Boot, Spring Security, BCrypt, Spring Data JPA, MySQL y Eureka.

## Configuración
Editar `src/main/resources/application.properties` y configurar `spring.datasource.username` y `spring.datasource.password` según tu MySQL local.

Requisitos: JDK 25, MySQL y SIGIP Discovery Server activo en el puerto 8761.

Puerto: 8093.

## Endpoints
- `GET /api/auth/health`
- `POST /api/auth/registro`
- `POST /api/auth/login`

Registro de ejemplo:
```json
{
  "nombres": "Usuario Demo",
  "correo": "demo@sigip.com",
  "username": "cliente01",
  "password": "ClaveSegura123"
}
```

Login de ejemplo:
```json
{
  "username": "cliente01",
  "password": "ClaveSegura123"
}
```

El registro público asigna el rol CLIENTE. La contraseña se almacena con BCrypt, nunca en texto plano.
