# SmartLibrary - Registrar préstamo

API REST por capas (Controller -> Service -> Repository) con Spring Boot 3, Java 21, Spring Web, Spring Data JPA, Validation y H2.

## Ejecutar pruebas

    mvn test

## Ejecutar la API

    mvn spring-boot:run

Endpoints:

- POST http://localhost:8080/api/prestamos  body: {"estudianteId":1,"libroId":1}
- GET  http://localhost:8080/api/prestamos
- Consola H2: http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:smartlibrary)

Datos iniciales: estudiantes 1 y 2; libros 1 (disponible), 2 (prestado) y 3 (disponible).
