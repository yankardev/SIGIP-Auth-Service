# SIGIP Auth Service — migración piloto

Este repositorio es el destino independiente del módulo `backend/auth-service` del repositorio principal `yankardev/SIGIP`.

## Alcance de la migración

- Servicio Spring Boot: autenticación, usuarios, roles y JWT.
- Puerto por defecto: `8093`.
- Integraciones externas: Eureka y MySQL.
- Código compartido actualmente usado por el servicio: `sigip-common` (errores de negocio, respuestas de seguridad, filtro JWT y `JwtService`).

## Estado

Migración piloto en curso en la rama `migration/auth-service-pilot`. Antes de fusionarla a `main`, se debe comprobar que el servicio sea autocontenido en su repositorio, que Maven compile y que los endpoints de registro/login funcionen.

## Configuración local

Variables principales: `DB_URL`, `DB_USER`, `DB_PASSWORD`, `JWT_SECRET`, `EUREKA_URL`, `SIGIP_ADMIN_USERNAME`, `SIGIP_ADMIN_EMAIL` y `SIGIP_ADMIN_PASSWORD`.

No publiques secretos reales en este repositorio.
