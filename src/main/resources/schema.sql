CREATE TABLE IF NOT EXISTS empleado (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(255),
                          edad INT,
                          direccion VARCHAR(255),
                          fecha_nacimiento VARCHAR(255),
                          password VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS adoptante (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(255),
                           edad INT,
                           direccion VARCHAR(255),
                           fecha_nacimiento VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS mascota (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_mascota VARCHAR(31) NOT NULL,
    nombre VARCHAR(255),
    fecha_nacimiento VARCHAR(255),
    peso DOUBLE,
    recomendaciones_cuidado VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS adopcion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_adopcion VARCHAR(31) NOT NULL,
    empleado_id BIGINT,
    adoptante_id BIGINT,
    mascota_id BIGINT,
    fecha DATE,
    FOREIGN KEY (empleado_id) REFERENCES empleado(id),
    FOREIGN KEY (adoptante_id) REFERENCES adoptante(id),
    FOREIGN KEY (mascota_id) REFERENCES mascota(id)
);
