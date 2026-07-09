# Documento de entrega para evaluador

## Objetivo de la entrega

Este repositorio contiene una API CRUD de productos (solo backend) con Java 17, Spring Boot, MySQL y Flyway.
La solucion esta orientada a ejecucion local y validacion reproducible.

## Que comparto al evaluador

1. Link del repositorio (o zip del proyecto).
2. Instrucciones de ejecucion local en `README.md`.
3. Coleccion Postman lista para correr:
   - `prueba/postman/PruebaTecnica.postman_collection.json`
4. Environment Postman:
   - `prueba/postman/PruebaTecnica.local.postman_environment.json`
5. Guia manual de Flyway desde cero:
   - `prueba/GUIA_FLYWAY_PASO_A_PASO.md`
6. Guia de entrevista y resumen tecnico:
   - `prueba/GUIA_ENTREVISTA.md`

## Como ejecutar la solucion en local

1. Desde la raiz del repo, levantar base de datos:

```powershell
docker compose up -d
```

2. Entrar al backend:

```powershell
cd prueba
```

3. Ejecutar pruebas:

```powershell
.\mvnw.cmd test
```

4. Levantar aplicacion:

```powershell
.\mvnw.cmd spring-boot:run
```

## Como validar la API (Postman)

1. Importar coleccion `prueba/postman/PruebaTecnica.postman_collection.json`.
2. Importar environment `prueba/postman/PruebaTecnica.local.postman_environment.json`.
3. Seleccionar environment `Prueba Tecnica Local`.
4. Ejecutar el runner completo de la coleccion.

Resultado esperado:
- Requests: 10/10
- Tests: 19/19
- Los 400 y 404 forman parte de escenarios de validacion esperados.

## Como validar migraciones Flyway desde cero

### Validacion automatica

```powershell
cd prueba
.\mvnw.cmd -Dtest=FlywayMigrationIntegrationTest test
```

Resultado esperado:
- BUILD SUCCESS
- Tests de Flyway en verde

### Validacion manual en MySQL

1. Entrar a MySQL del contenedor:

```powershell
docker exec -it product_db mysql -uroot -ppassword product_db
```

2. Ver historial de Flyway:

```sql
SELECT version, description, success
FROM flyway_schema_history
ORDER BY installed_rank;
```

Resultado esperado:
- version = 1
- success = 1

3. Verificar tabla de negocio:

```sql
DESCRIBE product;
```

Resultado esperado:
- columnas: id, name, description, price, stock, created_at, updated_at

## Como se cubren los criterios de evaluacion

1. ¿La API funciona segun lo pedido?
- Se valida con runner Postman completo (CRUD + escenarios de error).

2. ¿El codigo esta organizado y es legible?
- Estructura por capas y separacion de responsabilidades (dominio, aplicacion, adaptadores).

3. ¿Las migraciones corren limpias desde cero contra MySQL vacia?
- Evidencia automatica (test de integracion Flyway) + evidencia manual (consultas SQL).

4. ¿Los tests unitarios son significativos?
- Hay pruebas de servicio (reglas de negocio), controlador (contrato HTTP) e integracion de migraciones.

5. ¿Manejo de errores razonable?
- Se validan respuestas controladas para 400, 404 y 500, no solo camino feliz.

## Alcance y fuera de alcance

Incluido:
- Backend CRUD
- Validaciones
- Manejo de errores
- Migraciones Flyway
- Tests

Fuera de alcance (segun enunciado):
- Autenticacion/autorizacion
- Frontend
- Deploy remoto
- Documentacion exhaustiva

## Cierre breve para evaluador

La solucion es ejecutable en local, validable de punta a punta y con evidencia reproducible para funcionalidad, calidad de codigo, migraciones y pruebas.
