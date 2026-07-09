# prueba-tecnica-Nicola-Di-Sabato

API CRUD de productos en Java 17 + Spring Boot con arquitectura hexagonal,
persistencia MySQL y migraciones con Flyway.

## Stack tecnico

- Java 17
- Spring Boot 3.5
- Spring Web / Validation / Data JPA
- Flyway
- MySQL 8
- JUnit 5 + Mockito + MockMvc + Testcontainers

## Como ejecutar local

1. Levantar base de datos:
	- docker compose up -d
2. Entrar al modulo:
	- cd prueba
3. Ejecutar pruebas:
	- mvnw.cmd test
4. Levantar aplicacion:
	- mvnw.cmd spring-boot:run

## Validacion de migraciones 

Se agrego una prueba de integracion que arranca un MySQL vacio con
Testcontainers y verifica que Flyway:

- crea flyway_schema_history
- aplica V1 sobre esquema vacio
- crea tabla product con las columnas esperadas

Archivo de prueba:
- prueba/src/test/java/com/prueba_tecnica_nicola/prueba/infrastructure/database/FlywayMigrationIntegrationTest.java

## Criterios 

- Migraciones versionadas y repetibles desde cero
- ddl-auto=validate para evitar drift entre entidad y esquema
- Contrato HTTP probado con casos de exito y error
- Manejo centralizado de errores con mensajes controlados
- Pruebas de servicio, controlador e integracion de infraestructura
