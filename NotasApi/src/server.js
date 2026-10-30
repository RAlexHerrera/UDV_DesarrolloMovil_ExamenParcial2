require("dotenv").config();

const express = require("express");
const os = require("os");
const authRoutes = require("./routes/auth");
const notasRoutes = require("./routes/notas");

const app = express();
app.use(express.json());

app.use("/api/auth", authRoutes);
app.use("/api/notas", notasRoutes);

// Endpoint de salud: la app lo usa para verificar la conexión ANTES de mostrar el login.
app.get("/api/health", (req, res) => {
  res.json({ status: "ok", servicio: "NotasApi", timestamp: new Date().toISOString() });
});

// Chequeo rápido adicional en la raíz (útil para probar la URL desde el navegador).
app.get("/", (req, res) => {
  res.json({ status: "ok", servicio: "NotasApi" });
});

// 404 explícito para rutas no definidas, en vez del HTML por defecto de Express.
app.use((req, res) => {
  res.status(404).json({ mensaje: `Ruta no encontrada: ${req.method} ${req.path}` });
});

// Manejador de errores global: JSON malformado -> 400 claro; cualquier otro error
// no controlado -> 500 genérico (sin exponer el stack trace al cliente, pero sí
// registrado en consola del servidor para depurar).
app.use((err, req, res, next) => {
  if (err.type === "entity.parse.failed" || err instanceof SyntaxError) {
    return res.status(400).json({ mensaje: "El cuerpo de la petición no es un JSON válido" });
  }
  console.error("Error no controlado:", err);
  res.status(500).json({ mensaje: "Error interno del servidor" });
});

const PORT = process.env.PORT || 3000;

app.listen(PORT, () => {
  console.log(`NotasApi corriendo en el puerto ${PORT}`);
  console.log("");
  console.log("URLs para configurar BASE_URL en la app Android:");
  console.log(`  - Emulador de Android Studio:  http://10.0.2.2:${PORT}/`);

  const redes = os.networkInterfaces();
  for (const nombre of Object.keys(redes)) {
    for (const red of redes[nombre]) {
      if (red.family === "IPv4" && !red.internal) {
        console.log(`  - Dispositivo físico (misma red Wi-Fi):  http://${red.address}:${PORT}/`);
      }
    }
  }
});
