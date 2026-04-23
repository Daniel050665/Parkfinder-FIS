-- Tipos de vehículo
INSERT INTO TIPO_VEHICULO (nombre) VALUES ('Automóvil');
INSERT INTO TIPO_VEHICULO (nombre) VALUES ('Motocicleta');

-- Usuario de demo (contraseña: demo1234)
INSERT INTO USUARIOS (nombre, correo, contrasena, estado_cuenta, fecha_registro, usos_acumulados)
VALUES ('Juan Pérez', 'demo@parkfinder.com', 'demo1234', true, '2026-01-01', 2);

-- Vehículo del usuario demo (id_usuario=1, id_tipo_vehiculo=1 → Automóvil)
INSERT INTO VEHICULOS (placa, id_usuario, id_tipo_vehiculo)
VALUES ('ABC-123', 1, 1);

-- Parqueaderos
INSERT INTO PARQUEADEROS (nombre, direccion, zona, cupos_totales, cupos_disponibles, precio_por_hora, precio_suscripcion, horario_apertura, horario_cierre, estado)
VALUES ('Parqueadero Central', 'Cra 13 # 52-40', 'Chapinero', 20, 12, 3500.0, 80000.0, '06:00', '22:00', 'ACTIVO');

INSERT INTO PARQUEADEROS (nombre, direccion, zona, cupos_totales, cupos_disponibles, precio_por_hora, precio_suscripcion, horario_apertura, horario_cierre, estado)
VALUES ('ParkExpress Norte', 'Av. Chile # 49-20', 'Chicó', 30, 18, 4000.0, 80000.0, '00:00', '23:59', 'ACTIVO');

INSERT INTO PARQUEADEROS (nombre, direccion, zona, cupos_totales, cupos_disponibles, precio_por_hora, precio_suscripcion, horario_apertura, horario_cierre, estado)
VALUES ('SuperPark Chapinero', 'Cra 7 # 58-10', 'Chapinero', 15, 9, 5000.0, 80000.0, '00:00', '23:59', 'ACTIVO');

INSERT INTO PARQUEADEROS (nombre, direccion, zona, cupos_totales, cupos_disponibles, precio_por_hora, precio_suscripcion, horario_apertura, horario_cierre, estado)
VALUES ('Estacionamiento El Lago', 'Cll 63 # 9-42', 'Teusaquillo', 10, 5, 2800.0, 80000.0, '07:00', '21:00', 'ACTIVO');

INSERT INTO PARQUEADEROS (nombre, direccion, zona, cupos_totales, cupos_disponibles, precio_por_hora, precio_suscripcion, horario_apertura, horario_cierre, estado)
VALUES ('ParkCenter Usaquén', 'Cll 119 # 6-30', 'Usaquén', 25, 20, 4500.0, 80000.0, '06:00', '22:00', 'ACTIVO');

-- Cupos Parqueadero Central (id=1)
INSERT INTO CUPOS (numero_cupo, disponible, id_parqueadero) VALUES (1, true, 1);
INSERT INTO CUPOS (numero_cupo, disponible, id_parqueadero) VALUES (2, true, 1);
INSERT INTO CUPOS (numero_cupo, disponible, id_parqueadero) VALUES (3, true, 1);
INSERT INTO CUPOS (numero_cupo, disponible, id_parqueadero) VALUES (4, false, 1);
INSERT INTO CUPOS (numero_cupo, disponible, id_parqueadero) VALUES (5, true, 1);

-- Cupos ParkExpress Norte (id=2)
INSERT INTO CUPOS (numero_cupo, disponible, id_parqueadero) VALUES (1, true, 2);
INSERT INTO CUPOS (numero_cupo, disponible, id_parqueadero) VALUES (2, true, 2);
INSERT INTO CUPOS (numero_cupo, disponible, id_parqueadero) VALUES (3, false, 2);

-- Cupos SuperPark Chapinero (id=3)
INSERT INTO CUPOS (numero_cupo, disponible, id_parqueadero) VALUES (1, true, 3);
INSERT INTO CUPOS (numero_cupo, disponible, id_parqueadero) VALUES (2, true, 3);
INSERT INTO CUPOS (numero_cupo, disponible, id_parqueadero) VALUES (3, true, 3);

-- Cupos Estacionamiento El Lago (id=4)
INSERT INTO CUPOS (numero_cupo, disponible, id_parqueadero) VALUES (1, true, 4);
INSERT INTO CUPOS (numero_cupo, disponible, id_parqueadero) VALUES (2, false, 4);

-- Cupos ParkCenter Usaquén (id=5)
INSERT INTO CUPOS (numero_cupo, disponible, id_parqueadero) VALUES (1, true, 5);
INSERT INTO CUPOS (numero_cupo, disponible, id_parqueadero) VALUES (2, true, 5);
INSERT INTO CUPOS (numero_cupo, disponible, id_parqueadero) VALUES (3, true, 5);