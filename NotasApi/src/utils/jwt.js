// Firma y verificación de los tokens de sesión (JWT).
const jwt = require("jsonwebtoken");

const JWT_SECRET = process.env.JWT_SECRET || "clave-secreta-de-desarrollo-cambiar-en-produccion";
const EXPIRA_EN = "7d";

function generarToken(usuario) {
  return jwt.sign({ usuarioId: usuario.id }, JWT_SECRET, { expiresIn: EXPIRA_EN });
}

function verificarToken(token) {
  return jwt.verify(token, JWT_SECRET); // lanza error si es inválido o expiró
}

module.exports = { generarToken, verificarToken };
