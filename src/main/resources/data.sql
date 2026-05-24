-- Usamos la cláusula INSERT IGNORE o sentencias directas controladas.
-- Para evitar que el servidor falle por llaves duplicadas (Primary Key Violation) cada vez que se reinicie el backend en el entorno local.

-- Inserción de Brawlers Iniciales
INSERT IGNORE INTO brawlers (id, name, avatar_asset, primary_color, required_trophies)
VALUES ('shelly', 'Shelly', 'assets/images/brawlers/shelly.png', '#9333EA', 0);

INSERT IGNORE INTO brawlers (id, name, avatar_asset, primary_color, required_trophies)
VALUES ('colt', 'Colt', 'assets/images/brawlers/colt.png', '#EF4444', 0);

INSERT IGNORE INTO brawlers (id, name, avatar_asset, primary_color, required_trophies)
VALUES ('spike', 'Spike', 'assets/images/brawlers/spike.png', '#22C55E', 500);

-- Inserción de Usuario de Prueba (ID: 1)
INSERT IGNORE INTO users (id, username, password, global_trophies, created_at)
VALUES (1, 'julio_once', 'password_encriptado_aqui', 0, NOW());