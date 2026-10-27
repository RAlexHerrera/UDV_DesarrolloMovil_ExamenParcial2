# UDV — Examen Parcial II — Desarrollo de Aplicaciones Móviles

Proyecto NotasApp: app Android de notas personales + su backend. Evaluación Parcial II,
Universidad Da Vinci de Guatemala.

## Estructura del repositorio

```
NotasApp/   App Android (Kotlin, MVVM, XML Layouts, Retrofit)
NotasApi/   Backend (Node.js + Express + SQLite)
```

Cada carpeta tiene su propio `README.md` con instrucciones específicas:

- [NotasApp/README.md](./NotasApp/README.md) — arquitectura, pantallas, cómo abrir y correr la app
- [NotasApi/README.md](./NotasApi/README.md) — cómo levantar el backend, endpoints, usuario de prueba

## Orden para correr el proyecto completo

1. `cd NotasApi && npm install && npm start` — deja el backend corriendo en `http://localhost:3000`.
2. Abrir `NotasApp` en Android Studio y ejecutar en un emulador — ya está configurado para apuntar a
   `http://10.0.2.2:3000/` (la URL con la que el emulador ve al `localhost` de la PC).
3. Iniciar sesión con el usuario de prueba que crea el backend automáticamente:
   `demo@udv.edu.gt` / `Demo1234`.

## Autor

Rodrigo Alexander Herrera Solares
