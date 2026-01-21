-- Insertar Candidatos
INSERT INTO candidatos (id, nombre, partido, fotoUrl, propuesta) VALUES (nextval('candidatos_SEQ'), 'Ana García', 'Partido Innovación', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Ana', 'Digitalizar la biblioteca');
INSERT INTO candidatos (id, nombre, partido, fotoUrl, propuesta) VALUES (nextval('candidatos_SEQ'), 'Carlos Ruiz', 'Unión Estudiantil', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Carlos', 'Mejorar las zonas deportivas');
INSERT INTO candidatos (id, nombre, partido, fotoUrl, propuesta) VALUES (nextval('candidatos_SEQ'), 'Elena Mora', 'Fuerza Universitaria', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Elena', 'Más becas de transporte');

-- Primero asegúrate de que la secuencia exista si usas PanacheEntity
-- INSERT para Estudiantes
INSERT INTO estudiantes (id, carnet, documento_identidad, nombre, ya_voto)
VALUES (nextval('estudiantes_SEQ'), '2024001', '12345678', 'Alfredo Pérez', false);

INSERT INTO estudiantes (id, carnet, documento_identidad, nombre, ya_voto)
VALUES (nextval('estudiantes_SEQ'), '2024002', '87654321', 'Maria Lopez', false);
