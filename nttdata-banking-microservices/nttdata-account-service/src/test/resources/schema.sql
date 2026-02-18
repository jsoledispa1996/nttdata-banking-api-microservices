-- Test database schema for integration tests
CREATE TABLE IF NOT EXISTS ba_cuentas (
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

CREATE TABLE IF NOT EXISTS ba_movimientos (
    mo_id_movimiento INT AUTO_INCREMENT PRIMARY KEY,
    mo_fecha TIMESTAMP NOT NULL,
    mo_tipo_movimiento VARCHAR(20) NOT NULL,
    mo_valor DECIMAL(15,2) NOT NULL,
    mo_saldo DECIMAL(15,2) NOT NULL,
    mo_descripcion VARCHAR(255),
    mo_id_cuenta INT NOT NULL,
    FOREIGN KEY (mo_id_cuenta) REFERENCES ba_cuentas(cu_id_cuenta)
);
