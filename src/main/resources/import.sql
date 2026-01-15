-- Insertar Candidatos
INSERT INTO candidatos (id, nombre, partido, fotoUrl, propuesta) VALUES (nextval('candidatos_SEQ'), 'Ana García', 'Partido Innovación', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Ana', 'Digitalizar la biblioteca');
INSERT INTO candidatos (id, nombre, partido, fotoUrl, propuesta) VALUES (nextval('candidatos_SEQ'), 'Carlos Ruiz', 'Unión Estudiantil', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Carlos', 'Mejorar las zonas deportivas');
INSERT INTO candidatos (id, nombre, partido, fotoUrl, propuesta) VALUES (nextval('candidatos_SEQ'), 'Elena Mora', 'Fuerza Universitaria', 'https://api.dicebear.com/7.x/avataaars/svg?seed=Elena', 'Más becas de transporte');

-- Insertar Estudiantes de Prueba
-- El campo yaVoto es false (0) por defecto
INSERT INTO estudiantes (id, carnet, nombre, yaVoto) VALUES (nextval('estudiantes_SEQ'), '2024001', 'Alfredo Pérez', false);
INSERT INTO estudiantes (id, carnet, nombre, yaVoto) VALUES (nextval('estudiantes_SEQ'), '2024002', 'Maria Lopez', false);
INSERT INTO estudiantes (id, carnet, nombre, yaVoto) VALUES (nextval('estudiantes_SEQ'), '2024003', 'Juan Castro', false);