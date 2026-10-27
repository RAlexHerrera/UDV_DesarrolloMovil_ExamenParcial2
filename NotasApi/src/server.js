require("dotenv").config();

const express = require("express");
const os = require("os");
const authRoutes = require("./routes/auth");
const notasRoutes = require("./routes/notas");

const app = express();
app.use(express.json());

app.use("/api/auth", authRoutes);
app.use("/api/notas", notasRoutes);

// Chequeo rápido de que el servidor está vivo (útil para probar la URL desde el navegador).
app.get("/", (req, res) => {
  res.json({ status: "ok", servicio: "NotasApi" });
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
