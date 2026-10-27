// Middleware que exige y valida el token en el header Authorization.
// Se usa en cualquier ruta que solo deba poder llamar un usuario autenticado
// (crear nota, listar notas).
const { verificarToken } = require("../utils/jwt");

function requiereAutenticacion(req, res, next) {
  const header = req.headers.authorization; // esperado: "Bearer <token>"

  if (!header || !header.startsWith("Bearer ")) {
    return res.status(401).json({ mensaje: "Falta el token de autenticación" });
  }

  const token = header.substring("Bearer ".length);

  try {
    const payload = verificarToken(token);
    req.usuarioId = payload.usuarioId; // disponible para las rutas siguientes
    next();
  } catch (error) {
    return res.status(401).json({ mensaje: "Token inválido o expirado" });
  }
}

module.exports = requiereAutenticacion;
