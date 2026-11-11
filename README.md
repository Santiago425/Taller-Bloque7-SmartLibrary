# Taller práctico bloque 7 - SmartLibrary (Registrar préstamo)

Diseño de Software - Universidad Cooperativa de Colombia

Integrantes: Felipe Alejandro Cerón Cifuentes (930013) - Santiago Campoverde

## 1_Documento

`Taller_practico_bloque_7_resuelto.docx`: actividades 1 a 7, tablas, diagramas, evidencias de pruebas y decisiones de diseño.

## 2_Proyecto_SpringBoot/smartlibrary

API REST por capas (Controller -> Service -> Repository) con Java 21, Maven, Spring Web, Spring Data JPA, Validation y H2.

    mvn test
    mvn spring-boot:run

- POST http://localhost:8080/api/prestamos  body: `{"estudianteId":1,"libroId":1}`
- GET  http://localhost:8080/api/prestamos

## 3_Diagramas

- `Visual_Paradigm/SmartLibrary - Bloque 7.vpp`: diagrama de clases por capas "Modelo por capas - Registrar prestamo" y diagrama de secuencia "Secuencia - POST /api/prestamos".
- `Imagenes/`: diagramas de clases y de secuencia en PNG y SVG.
