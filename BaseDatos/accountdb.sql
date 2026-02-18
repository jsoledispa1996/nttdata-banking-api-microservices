-- Script de instalación para la base de datos de cuentas (accountdb)
CREATE DATABASE IF NOT EXISTS accountdb;
USE accountdb;

CREATE TABLE ba_cuentas (
    cu_id_cuenta INT AUTO_INCREMENT PRIMARY KEY,
    cu_numero_cuenta VARCHAR(20) UNIQUE NOT NULL,
    cu_tipo_cuenta VARCHAR(20) NOT NULL,
    cu_saldo_inicial DECIMAL(15,2) NOT NULL,
    cu_saldo_actual DECIMAL(15,2) NOT NULL,
    cu_estado BOOLEAN NOT NULL,
    cu_id_cliente VARCHAR(20) NOT NULL,
    cu_fecha_creacion TIMESTAMP NOT NULL,
    cu_fecha_actualizacion TIMESTAMP
);

CREATE TABLE ba_movimientos (
    mo_id_movimiento INT AUTO_INCREMENT PRIMARY KEY,
    mo_fecha TIMESTAMP NOT NULL,
    mo_tipo_movimiento VARCHAR(20) NOT NULL,
    mo_valor DECIMAL(15,2) NOT NULL,
    mo_saldo DECIMAL(15,2) NOT NULL,
    mo_descripcion VARCHAR(255),
    mo_id_cuenta INT NOT NULL,
    FOREIGN KEY (mo_id_cuenta) REFERENCES ba_cuentas(cu_id_cuenta)
);

-- ========================================
-- Datos de prueba: Cuentas y Movimientos
-- ========================================

-- Cuentas de Jose Andres Soledispa Yagual (CLI001) 
INSERT INTO ba_cuentas (cu_numero_cuenta, cu_tipo_cuenta, cu_saldo_inicial, cu_saldo_actual, cu_estado, cu_id_cliente, cu_fecha_creacion) 
VALUES ('2400001', 'AHORRO', 5000.00, 25230.00, TRUE, 'CLI001', NOW());

INSERT INTO ba_cuentas (cu_numero_cuenta, cu_tipo_cuenta, cu_saldo_inicial, cu_saldo_actual, cu_estado, cu_id_cliente, cu_fecha_creacion) 
VALUES ('2400002', 'CORRIENTE', 10000.00, 34160.00, TRUE, 'CLI001', NOW());

-- Cuentas de Erick Geovanny Soledispa Yagual (CLI002)
INSERT INTO ba_cuentas (cu_numero_cuenta, cu_tipo_cuenta, cu_saldo_inicial, cu_saldo_actual, cu_estado, cu_id_cliente, cu_fecha_creacion) 
VALUES ('2400003', 'AHORRO', 3000.00, 4800.00, TRUE, 'CLI002', NOW());

INSERT INTO ba_cuentas (cu_numero_cuenta, cu_tipo_cuenta, cu_saldo_inicial, cu_saldo_actual, cu_estado, cu_id_cliente, cu_fecha_creacion) 
VALUES ('2400004', 'CORRIENTE', 7000.00, 6200.00, TRUE, 'CLI002', NOW());

-- Cuentas de Pedro Cornelio Guale Gonzalez (CLI003)
INSERT INTO ba_cuentas (cu_numero_cuenta, cu_tipo_cuenta, cu_saldo_inicial, cu_saldo_actual, cu_estado, cu_id_cliente, cu_fecha_creacion) 
VALUES ('2400005', 'AHORRO', 8000.00, 11500.00, TRUE, 'CLI003', NOW());

INSERT INTO ba_cuentas (cu_numero_cuenta, cu_tipo_cuenta, cu_saldo_inicial, cu_saldo_actual, cu_estado, cu_id_cliente, cu_fecha_creacion) 
VALUES ('2400006', 'CORRIENTE', 15000.00, 13750.00, TRUE, 'CLI003', NOW());

-- Cuentas de Judith Juliana Alfonzo Morales (CLI004)
INSERT INTO ba_cuentas (cu_numero_cuenta, cu_tipo_cuenta, cu_saldo_inicial, cu_saldo_actual, cu_estado, cu_id_cliente, cu_fecha_creacion) 
VALUES ('2400007', 'AHORRO', 6000.00, 7300.00, TRUE, 'CLI004', NOW());

INSERT INTO ba_cuentas (cu_numero_cuenta, cu_tipo_cuenta, cu_saldo_inicial, cu_saldo_actual, cu_estado, cu_id_cliente, cu_fecha_creacion) 
VALUES ('2400008', 'CORRIENTE', 12000.00, 10800.00, TRUE, 'CLI004', NOW());

-- Cuentas de Carmen Joanca Guale Morales (CLI005)
INSERT INTO ba_cuentas (cu_numero_cuenta, cu_tipo_cuenta, cu_saldo_inicial, cu_saldo_actual, cu_estado, cu_id_cliente, cu_fecha_creacion) 
VALUES ('2400009', 'AHORRO', 4500.00, 5950.00, TRUE, 'CLI005', NOW());

INSERT INTO ba_cuentas (cu_numero_cuenta, cu_tipo_cuenta, cu_saldo_inicial, cu_saldo_actual, cu_estado, cu_id_cliente, cu_fecha_creacion) 
VALUES ('2400010', 'CORRIENTE', 9000.00, 8350.00, TRUE, 'CLI005', NOW());

-- ========================================
-- Movimientos para Jose Andres (CLI001) - CUENTA AHORRO 2400001
-- ========================================
INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-01 09:30:00', 'DEPOSITO', 2000.00, 7000.00, 'Depósito inicial', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-05 14:20:00', 'DEPOSITO', 1500.00, 8500.00, 'Transferencia recibida', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-10 11:00:00', 'RETIRO', -250.00, 8250.00, 'Retiro cajero automático', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-11 08:15:00', 'DEPOSITO', 3500.00, 11750.00, 'Ingreso por freelance', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-12 10:30:00', 'RETIRO', -500.00, 11250.00, 'Pago servicios básicos', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-13 15:45:00', 'DEPOSITO', 850.00, 12100.00, 'Venta artículo usado', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-14 09:20:00', 'RETIRO', -1200.00, 10900.00, 'Compra supermercado', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-15 11:00:00', 'DEPOSITO', 2200.00, 13100.00, 'Bono laboral', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-16 14:30:00', 'RETIRO', -300.00, 12800.00, 'Pago internet', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-17 16:00:00', 'DEPOSITO', 1750.00, 14550.00, 'Reembolso empresa', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-18 10:15:00', 'RETIRO', -450.00, 14100.00, 'Pago celular', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-19 13:45:00', 'DEPOSITO', 980.00, 15080.00, 'Transferencia familiar', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-20 09:00:00', 'RETIRO', -1500.00, 13580.00, 'Compra electrodoméstico', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-21 11:30:00', 'DEPOSITO', 4200.00, 17780.00, 'Pago cliente proyecto', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-22 14:00:00', 'RETIRO', -650.00, 17130.00, 'Pago gasolina', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-23 08:30:00', 'DEPOSITO', 1100.00, 18230.00, 'Comisión por venta', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-24 16:45:00', 'RETIRO', -800.00, 17430.00, 'Restaurante familiar', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-25 10:00:00', 'DEPOSITO', 2500.00, 19930.00, 'Ingreso extra consultoría', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-26 12:30:00', 'RETIRO', -350.00, 19580.00, 'Pago farmacia', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-27 15:15:00', 'DEPOSITO', 1650.00, 21230.00, 'Dividendos inversión', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-28 09:45:00', 'RETIRO', -950.00, 20280.00, 'Pago tarjeta crédito', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-29 13:00:00', 'DEPOSITO', 3300.00, 23580.00, 'Pago mensual desarrollo', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-30 11:20:00', 'RETIRO', -1100.00, 22480.00, 'Compra ropa', 1);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-31 08:00:00', 'DEPOSITO', 2750.00, 25230.00, 'Pago proyecto finalizado', 1);

-- Movimientos para Jose Andres (CLI001) - CUENTA CORRIENTE 2400002
INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-02 10:15:00', 'DEPOSITO', 3000.00, 13000.00, 'Depósito nómina', 2);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-07 16:45:00', 'RETIRO', -500.00, 12500.00, 'Pago servicios', 2);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-03 09:00:00', 'DEPOSITO', 5500.00, 18000.00, 'Sueldo quincenal', 2);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-04 14:30:00', 'RETIRO', -2100.00, 15900.00, 'Pago alquiler', 2);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-06 11:15:00', 'DEPOSITO', 1800.00, 17700.00, 'Transferencia recibida', 2);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-08 10:00:00', 'RETIRO', -750.00, 16950.00, 'Compra electrónicos', 2);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-09 15:20:00', 'DEPOSITO', 2900.00, 19850.00, 'Ingreso por asesoría', 2);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-11 13:40:00', 'RETIRO', -1250.00, 18600.00, 'Pago seguro auto', 2);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-13 09:30:00', 'DEPOSITO', 4100.00, 22700.00, 'Pago cliente corporativo', 2);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-14 16:00:00', 'RETIRO', -580.00, 22120.00, 'Cena negocios', 2);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-16 10:45:00', 'DEPOSITO', 1950.00, 24070.00, 'Reintegro gastos', 2);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-17 14:15:00', 'RETIRO', -2300.00, 21770.00, 'Pago préstamo', 2);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-19 11:00:00', 'DEPOSITO', 3400.00, 25170.00, 'Honorarios profesionales', 2);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-21 15:30:00', 'RETIRO', -890.00, 24280.00, 'Compra oficina', 2);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-22 09:15:00', 'DEPOSITO', 5200.00, 29480.00, 'Pago proyecto completado', 2);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-23 13:50:00', 'RETIRO', -1600.00, 27880.00, 'Pago proveedores', 2);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-25 11:30:00', 'DEPOSITO', 2800.00, 30680.00, 'Ingreso capacitación', 2);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-26 16:20:00', 'RETIRO', -720.00, 29960.00, 'Mantenimiento vehículo', 2);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-28 10:00:00', 'DEPOSITO', 6500.00, 36460.00, 'Cierre contrato anual', 2);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-29 14:40:00', 'RETIRO', -3200.00, 33260.00, 'Inversión negocio', 2);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-30 09:25:00', 'DEPOSITO', 1850.00, 35110.00, 'Comisión mensual', 2);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-31 12:00:00', 'RETIRO', -950.00, 34160.00, 'Compras fin de mes', 2);

-- ========================================
-- Movimientos para Erick Geovanny (CLI002) - CUENTA AHORRO 2400003
-- ========================================
INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-03 08:00:00', 'DEPOSITO', 1200.00, 4200.00, 'Ahorro mensual', 3);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-12 13:30:00', 'DEPOSITO', 600.00, 4800.00, 'Bonificación', 3);

-- Movimientos para Erick Geovanny (CLI002) - CUENTA CORRIENTE 2400004
INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-04 09:20:00', 'RETIRO', -800.00, 6200.00, 'Pago tarjeta crédito', 4);

-- ========================================
-- Movimientos para Pedro Cornelio (CLI003) - CUENTA AHORRO 2400005
-- ========================================
INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-01 15:45:00', 'DEPOSITO', 2500.00, 10500.00, 'Inversión inicial', 5);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-15 10:30:00', 'DEPOSITO', 1000.00, 11500.00, 'Intereses ganados', 5);

-- Movimientos para Pedro Cornelio (CLI003) - CUENTA CORRIENTE 2400006
INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-06 12:00:00', 'RETIRO', -1250.00, 13750.00, 'Pago proveedores', 6);

-- ========================================
-- Movimientos para Judith Juliana (CLI004) - CUENTA AHORRO 2400007
-- ========================================
INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-08 11:15:00', 'DEPOSITO', 800.00, 6800.00, 'Ahorro quincenal', 7);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-20 14:00:00', 'DEPOSITO', 500.00, 7300.00, 'Venta productos', 7);

-- Movimientos para Judith Juliana (CLI004) - CUENTA CORRIENTE 2400008
INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-09 16:30:00', 'RETIRO', -1200.00, 10800.00, 'Compra mercadería', 8);

-- ========================================
-- Movimientos para Carmen Joanca (CLI005) - CUENTA AHORRO 2400009
-- ========================================
INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-11 09:45:00', 'DEPOSITO', 1000.00, 5500.00, 'Depósito inicial', 9);

INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-18 13:15:00', 'DEPOSITO', 450.00, 5950.00, 'Transferencia familiar', 9);

-- Movimientos para Carmen Joanca (CLI005) - CUENTA CORRIENTE 2400010
INSERT INTO ba_movimientos (mo_fecha, mo_tipo_movimiento, mo_valor, mo_saldo, mo_descripcion, mo_id_cuenta) 
VALUES ('2025-10-14 10:00:00', 'RETIRO', -650.00, 8350.00, 'Pago alquiler', 10);

-- Fin del script de instalación de accountdb.
