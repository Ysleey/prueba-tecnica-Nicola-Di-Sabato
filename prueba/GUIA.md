# Guia 

## Checklist 

1. Verifico Docker Desktop encendido.
2. Corro el script de verificacion:
   - powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\verificacion-entrevista.ps1
3. Confirmo que la aplicacion arranca sin errores:
   - .\mvnw.cmd spring-boot:run
4. Dejo preparada una terminal con los curl del README.
5. Reviso que se vea claro el objetivo: CRUD, arquitectura hexagonal, validaciones y migraciones.

## Guion 

1. Contexto 
   - "La prueba consiste en un CRUD de productos con Java 17 y Spring Boot, priorizando codigo limpio y buenas practicas."

2. Arquitectura 
   - "Use arquitectura hexagonal para separar dominio, casos de uso y adaptadores."
   - "El dominio no depende de infraestructura."
   - "La capa web y la persistencia son adaptadores conectados por puertos."

3. Calidad funcional 
   - "Implemente CRUD completo y paginacion."
   - "Añadi validaciones de negocio y de entrada para que los errores sean predecibles."
   - "Centralice el manejo de errores para responder 400, 404 y 500 con formato uniforme."

4. Persistencia y migraciones (30-40s)
   - "Use Flyway para versionar esquema y asegurar arranque desde base vacia."
   - "Configure ddl-auto=validate para detectar desalineaciones entre entidades y BD."

5. Pruebas 
   - "Cubro servicio, controlador y migracion con pruebas de integracion."
   - "Con esto valido tanto logica como contrato HTTP e infraestructura."

