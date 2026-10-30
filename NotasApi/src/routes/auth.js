// Rutas de autenticación: login y register. Ambas usadas por la app móvil
// (la pantalla "Crear cuenta" llama a /register).
const express = require("express");
const bcrypt = require("bcryptjs");
const db = require("../db");
const { generarToken } = require("../utils/jwt");
const { esEmailValido, esTextoNoVacio } = require("../utils/validators");

const router = express.Router();

// POST /api/auth/login — usada por NotasApp para autenticarse.
router.post("/login", (req, res) => {
  const { email, password } = req.body;

  if (!esTextoNoVacio(email) || !esTextoNoVacio(password)) {
    return res.status(400).json({ mensaje: "Email y contraseña son obligatorios" });
  }
  if (!esEmailValido(email)) {
    return res.status(400).json({ mensaje: "Formato de correo inválido" });
  }

  const emailLimpio = email.trim().toLowerCase();
  const usuario = db.prepare("SELECT * FROM usuarios WHERE email = ?").get(emailLimpio);
  if (!usuario || !bcrypt.compareSync(password, usuario.password_hash)) {
    return res.status(401).json({ mensaje: "Credenciales inválidas" });
  }

  const token = generarToken(usuario);
  res.json({
    token,
    usuario: { id: usuario.id, nombre: usuario.nombre, email: usuario.email },
  });
});

// POST /api/auth/register — crea la cuenta y de una vez inicia sesión (devuelve token),
// para que el usuario no tenga que loguearse por separado justo después de registrarse.
router.post("/register", (req, res) => {
  const { nombre, email, password } = req.body;

  if (!esTextoNoVacio(nombre, 100) || !esTextoNoVacio(email) || !esTextoNoVacio(password)) {
    return res.status(400).json({ mensaje: "Nombre, email y contraseña son obligatorios" });
  }
  if (!esEmailValido(email)) {
    return res.status(400).json({ mensaje: "Formato de correo inválido" });
  }
  if (password.length < 4) {
    return res.status(400).json({ mensaje: "La contraseña debe tener al menos 4 caracteres" });
  }

  const emailLimpio = email.trim().toLowerCase();
  const existe = db.prepare("SELECT id FROM usuarios WHERE email = ?").get(emailLimpio);
  if (existe) {
    return res.status(409).json({ mensaje: "Ya existe un usuario con ese email" });
  }

  const passwordHash = bcrypt.hashSync(password, 10);
  const resultado = db
    .prepare("INSERT INTO usuarios (nombre, email, password_hash) VALUES (?, ?, ?)")
    .run(nombre.trim(), emailLimpio, passwordHash);

  const usuario = { id: resultado.lastInsertRowid, nombre: nombre.trim(), email: emailLimpio };
  const token = generarToken(usuario);
  res.status(201).json({ token, usuario });
});

module.exports = router;
