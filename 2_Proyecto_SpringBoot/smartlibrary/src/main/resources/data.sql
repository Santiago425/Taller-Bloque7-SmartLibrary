INSERT INTO estudiante (id, nombre, correo) VALUES (1, 'Felipe Cerón', 'felipe.ceron@campusucc.edu.co');
INSERT INTO estudiante (id, nombre, correo) VALUES (2, 'Santiago Campoverde', 'santiago.campoverde@campusucc.edu.co');
INSERT INTO libro (id, titulo, disponible) VALUES (1, 'Clean Code', true);
INSERT INTO libro (id, titulo, disponible) VALUES (2, 'Patrones de Diseño', false);
INSERT INTO libro (id, titulo, disponible) VALUES (3, 'UML Distilled', true);
INSERT INTO prestamo (fecha_prestamo, fecha_devolucion, estudiante_id, libro_id) VALUES (CURRENT_DATE, NULL, 2, 2);
