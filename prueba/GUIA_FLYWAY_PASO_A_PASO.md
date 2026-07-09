# Guia paso a paso: probar Flyway desde cero (manual)

Esta guia es para que puedas repetir tu mismo la validacion de migraciones en local.

## Objetivo

Comprobar que, partiendo de una base MySQL vacia, Flyway aplica la migracion V1 correctamente.

## Requisitos

- Docker Desktop encendido
- Java 17
- Maven Wrapper del proyecto

## Paso 1: limpiar y levantar MySQL vacio

Desde la raiz del repo:

```powershell
docker compose down -v
docker compose up -d
```

Que hace esto:
- `down -v` elimina el contenedor y el volumen, o sea borra datos anteriores.
- `up -d` levanta MySQL limpio.

## Paso 2: verificar conexion de MySQL

```powershell
docker ps
```

Debes ver el contenedor `product_db` arriba.

## Paso 3: arrancar la app para disparar Flyway

```powershell
cd prueba
.\mvnw.cmd spring-boot:run
```

Que esperar en logs:
- mensaje de Flyway validando migraciones
- mensaje de Flyway aplicando version `1`
- mensaje de app arrancada

Cuando veas que arranco, puedes parar con `Ctrl + C`.

## Paso 4: validar en BD que la migracion corrio

Ejecuta este comando para entrar al contenedor:

```powershell
docker exec -it product_db mysql -uroot -ppassword product_db
```

Ya dentro de MySQL, corre:

```sql
SELECT version, description, success
FROM flyway_schema_history
ORDER BY installed_rank;
```

Resultado esperado:
- una fila con `version = 1`
- `description` similar a `create product table`
- `success = 1`

## Paso 5: validar que existe la tabla y columnas

En la misma consola MySQL:

```sql
DESCRIBE product;
```

Debes ver estas columnas:
- `id`
- `name`
- `description`
- `price`
- `stock`
- `created_at`
- `updated_at`

## Paso 6: validacion automatica equivalente (opcional)

Si quieres validar lo mismo por test de integracion:

```powershell
.\mvnw.cmd -Dtest=FlywayMigrationIntegrationTest test
```

## Checklist final

1. Base vacia creada desde cero
2. Flyway aplico V1
3. `flyway_schema_history` registra version 1 exitosa
4. Tabla `product` creada con columnas esperadas
5. Test de integracion pasa en verde
