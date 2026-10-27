// Rutas de autenticación: login (la que usa la app móvil) y register
// (utilitaria, para crear usuarios de prueba sin editar la base de datos a mano).
const express = require("express");
const bcrypt = require("bcryptjs");
const db = require("../db");
const { generarToken } = require("../utils/jwt");

const router = express.Router();

// POST /api/auth/login — usada por NotasApp para autenticarse.
router.post("/login", (req, res) => {
  const { email, password } = req.body;

  if (!email || !password) {
    return res.status(400).json({ mensaje: "Email y contraseña son obligatorios" });
  }

  const usuario = db.prepare("SELECT * FROM usuarios WHERE email = ?").get(email);
  if (!usuario || !bcrypt.compareSync(password, usuario.password_hash)) {
    return res.status(401).json({ mensaje: "Credenciales inválidas" });
  }

  const token = generarToken(usuario);
  res.json({
    token,
    usuario: { id: usuario.id, nombre: usuario.nombre, email: usuario.email },
  });
});

// POST /api/auth/register — no la usa la app (el enunciado no pide pantalla de registro),
// solo sirve para crear usuarios de prueba vía curl/Postman.
router.post("/register", (req, res) => {
  const { nombre, email, password } = req.body;

  if (!nombre || !email || !password) {
    return res.status(400).json({ mensaje: "Nombre, email y contraseña son obligatorios" });
  }

  const existe = db.prepare("SELECT id FROM usuarios WHERE email = ?").get(email);
  if (existe) {
    return res.status(409).json({ mensaje: "Ya existe un usuario con ese email" });
  }

  const passwordHash = bcrypt.hashSync(password, 10);
  const resultado = db
    .prepare("INSERT INTO usuarios (nombre, email, password_hash) VALUES (?, ?, ?)")
    .run(nombre, email, passwordHash);

  res.status(201).json({ id: resultado.lastInsertRowid, nombre, email });
});

module.exports = router;
