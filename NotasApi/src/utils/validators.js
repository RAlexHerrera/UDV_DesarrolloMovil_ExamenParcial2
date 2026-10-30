// Validaciones compartidas por las rutas. Centralizadas aquí para no repetir
// regex/longitudes en cada archivo de rutas.

const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

function esEmailValido(email) {
  return typeof email === "string" && EMAIL_REGEX.test(email.trim());
}

function esTextoNoVacio(valor, maxLength) {
  if (typeof valor !== "string") return false;
  const limpio = valor.trim();
  if (limpio.length === 0) return false;
  if (maxLength && limpio.length > maxLength) return false;
  return true;
}

module.exports = { esEmailValido, esTextoNoVacio };
