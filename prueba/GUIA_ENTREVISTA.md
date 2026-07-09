# Guia 

## Checklist 

1. Verifico Docker Desktop encendido.
2. Corro el script de verificacion:
   - powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\verificacion-entrevista.ps1
3. Confirmo que la aplicacion arranca sin errores:
   - .\mvnw.cmd spring-boot:run
4. Dejo preparada una terminal con los curl del README.
5. Reviso que se vea claro el objetivo: CRUD, arquitectura hexagonal, validaciones y migraciones.

## Guion recomendado para presentar

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

## Preguntas tecnicas habituales y respuestas

1. ¿Por que arquitectura hexagonal en una prueba corta?
   - "Porque me permite demostrar separacion de responsabilidades y facilita cambios futuros. En una prueba tecnica suma valor porque hace visible la calidad del diseño, no solo que funciona."

2. ¿Por que usar Flyway en vez de dejar que JPA cree tablas?
   - "Porque necesito trazabilidad y control de cambios de esquema. Flyway deja historico versionado y hace reproducible el estado de la BD entre entornos."

3. ¿Por que usar ddl-auto=validate?
   - "Para evitar drift entre modelo y base de datos. Si hay una diferencia, falla al arrancar y lo detecto antes de romper en runtime."

4. ¿Como manejaste los errores?
   - "Con un handler global y respuestas consistentes. Distingo validaciones de negocio, errores de entrada y errores inesperados para mantener un contrato HTTP claro."

5. ¿Que priorizaste en las pruebas?
   - "Priorice lo que un entrevistador espera: flujo feliz del CRUD, validaciones, contratos de error y migraciones reales sobre base vacia."

6. Si tuvieras mas tiempo, ¿que mejorarias?
   - "Añadiria observabilidad basica (logs estructurados), OpenAPI, y una capa de pruebas E2E mas completa para escenarios de regresion."

## Simulacion rapida (entrevistador y yo)

1. Entrevistador: ¿La API funciona segun lo pedido?
   Yo: "Si. Tengo los cinco endpoints del CRUD funcionando y validados con pruebas unitarias y de controlador."

2. Entrevistador: ¿Como pruebas que no solo funciona el camino feliz?
   Yo: "Agregue escenarios de error para validacion de datos, id inexistente y errores de formato. La API responde 400, 404 y 500 con contrato uniforme."

3. Entrevistador: ¿Como aseguras migraciones limpias desde cero?
   Yo: "Use Flyway y ademas un test de integracion con Testcontainers que levanta MySQL vacio y verifica la aplicacion de V1."

4. Entrevistador: ¿Por que elegiste esta estructura de codigo?
   Yo: "Porque con hexagonal separo dominio, casos de uso e infraestructura. Eso me da codigo mas mantenible y mas facil de testear."

5. Entrevistador: ¿Que no implementaste y por que?
   Yo: "No implemente auth, frontend ni deploy porque estaban fuera de alcance del enunciado. Preferi asegurar calidad en backend, pruebas y migraciones."

6. Entrevistador: ¿Esta lista para correr localmente ahora?
   Yo: "Si. Con Docker para MySQL y el comando de Spring Boot levanta sin problemas. Antes de demo corro el script de verificacion para confirmar todo."

## Cierre recomendado

"Con esta implementacion busque equilibrio entre funcionalidad, mantenibilidad y buenas practicas. No solo hice que el CRUD funcione: tambien cuide el diseño, el contrato HTTP, la estrategia de errores y la reproducibilidad de base de datos."
