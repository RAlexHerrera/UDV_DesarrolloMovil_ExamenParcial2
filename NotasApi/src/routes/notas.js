// Rutas de notas: ambas requieren autenticación (ver middleware/auth.js) y trabajan
// únicamente con las notas del usuario dueño del token, nunca con las de otro usuario.
const express = require("express");
const db = require("../db");
const requiereAutenticacion = require("../middleware/auth");
const { esTextoNoVacio } = require("../utils/validators");

const router = express.Router();
router.use(requiereAutenticacion);

const TITULO_MAX = 100;
const CONTENIDO_MAX = 5000;

// Convierte una fila de la tabla notas al formato que espera el DTO Nota de la app
// (fecha_creacion -> fechaCreacion), para que el JSON calce exactamente con Kotlin/Gson.
function aDto(fila) {
  return {
    id: fila.id,
    titulo: fila.titulo,
    contenido: fila.contenido,
    fechaCreacion: fila.fecha_creacion,
  };
}

// POST /api/notas — crea una nota asociada al usuario autenticado.
router.post("/", (req, res) => {
  const { titulo, contenido } = req.body;

  if (!esTextoNoVacio(titulo, TITULO_MAX)) {
    return res.status(400).json({
      mensaje: `El título es obligatorio y no puede superar los ${TITULO_MAX} caracteres`,
    });
  }
  if (!esTextoNoVacio(contenido, CONTENIDO_MAX)) {
    return res.status(400).json({
      mensaje: `El contenido es obligatorio y no puede superar los ${CONTENIDO_MAX} caracteres`,
    });
  }

  const resultado = db
    .prepare("INSERT INTO notas (usuario_id, titulo, contenido) VALUES (?, ?, ?)")
    .run(req.usuarioId, titulo.trim(), contenido.trim());

  const fila = db.prepare("SELECT * FROM notas WHERE id = ?").get(resultado.lastInsertRowid);
  res.status(201).json(aDto(fila));
});

// GET /api/notas — devuelve únicamente las notas del usuario autenticado.
router.get("/", (req, res) => {
  const filas = db
    .prepare("SELECT * FROM notas WHERE usuario_id = ? ORDER BY fecha_creacion DESC")
    .all(req.usuarioId);

  res.json(filas.map(aDto));
});

module.exports = router;
