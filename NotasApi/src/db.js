// Conexión a SQLite y creación de tablas. SQLite se eligió porque no requiere instalar
// ni levantar un servidor de base de datos aparte: todo vive en un solo archivo (data/notas.db),
// ideal para desarrollo y para la demo del proyecto.

const path = require("path");
const Database = require("better-sqlite3");
const bcrypt = require("bcryptjs");

const DB_PATH = path.join(__dirname, "..", "data", "notas.db");
const db = new Database(DB_PATH);

db.pragma("journal_mode = WAL");

// Tabla de usuarios: quien se autentica contra el backend.
db.exec(`
  CREATE TABLE IF NOT EXISTS usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    fecha_creacion TEXT NOT NULL DEFAULT (datetime('now'))
  )
`);

// Tabla de notas: cada nota pertenece a un usuario (usuario_id).
db.exec(`
  CREATE TABLE IF NOT EXISTS notas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    usuario_id INTEGER NOT NULL,
    titulo TEXT NOT NULL,
    contenido TEXT NOT NULL,
    fecha_creacion TEXT NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
  )
`);

// Usuario de prueba: se crea una sola vez, para poder probar el login sin tener que
// registrar uno manualmente. Esto NO es login hardcodeado: es un registro real en la
// base de datos, con contraseña hasheada igual que cualquier otro usuario.
function seedUsuarioDemo() {
  const existe = db.prepare("SELECT id FROM usuarios WHERE email = ?").get("admin@notasapp.com");
  if (existe) return;

  const passwordHash = bcrypt.hashSync("admin", 10);
  db.prepare(
    "INSERT INTO usuarios (nombre, email, password_hash) VALUES (?, ?, ?)"
  ).run("Admin", "admin@notasapp.com", passwordHash);

  console.log('Usuario de prueba creado -> email: "admin@notasapp.com", password: "admin"');
}

seedUsuarioDemo();

module.exports = db;
