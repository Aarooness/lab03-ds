-- Usuario por defecto: admin / admin123
INSERT INTO empleados (nombre, email, rol, cargo, password)
VALUES (
    'Administrador',
    'admin@laboratorio.local',
    'admin',
    'Administrador del sistema',
    '$2b$10$qSaE6oJmO20fr6DygWBS7.Ui.GGDjOww1JT6D1cNoS/pWJoyYk3PO'
)
ON CONFLICT (email) DO NOTHING;
