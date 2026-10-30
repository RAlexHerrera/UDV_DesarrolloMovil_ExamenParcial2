# NotasApi

Backend de **NotasApp** (Evaluación Parcial II — Desarrollo de Aplicaciones Móviles, Universidad Da Vinci
de Guatemala). Expone los endpoints de autenticación y de notas que consume la app Android.

## Tecnologías

- **Node.js + Express** — servidor HTTP
- **SQLite** (`better-sqlite3`) — base de datos en un solo archivo, sin necesidad de instalar un motor aparte
- **bcryptjs** — hash de contraseñas
- **jsonwebtoken** — tokens de sesión (JWT)

## Cómo ejecutarlo

```
npm install
npm start
```

Al arrancar, crea automáticamente la base de datos (`data/notas.db`) y un **usuario de prueba**:

```
email:    admin@notasapp.com
password: admin
```

La consola imprime las URLs a usar como `BASE_URL` en la app Android, tanto para el emulador
(`http://10.0.2.2:3000/`) como para un dispositivo físico en la misma red Wi-Fi.

## Endpoints

| Método | Ruta | Auth | Body | Descripción |
|---|---|---|---|---|
| POST | `/api/auth/login` | No | `{ email, password }` | Autentica y devuelve `{ token, usuario }` |
| POST | `/api/auth/register` | No | `{ nombre, email, password }` | Crea un usuario y responde igual que login (`{ token, usuario }`), así la app deja la sesión iniciada de una vez |
| POST | `/api/notas` | **Sí** | `{ titulo, contenido }` | Crea una nota del usuario autenticado |
| GET | `/api/notas` | **Sí** | — | Lista las notas del usuario autenticado |

Las rutas marcadas "Sí" requieren el header `Authorization: Bearer <token>`. Si falta o el token es
inválido/expiró, responden `401`.

## Base de datos

```
usuarios (id, nombre, email, password_hash, fecha_creacion)
notas    (id, usuario_id → usuarios.id, titulo, contenido, fecha_creacion)
```

Cada nota queda asociada al usuario que la creó; el listado solo devuelve las notas del usuario dueño
del token, nunca las de otro usuario.

## Variables de entorno

Copiar `.env.example` a `.env` y ajustar si se desea (ya trae valores por defecto funcionales):

```
PORT=3000
JWT_SECRET=clave-secreta
```

## Probar sin la app (curl)

```bash
# Login
curl -X POST http://localhost:3000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@notasapp.com","password":"admin"}'

# Crear nota (reemplazar <TOKEN>)
curl -X POST http://localhost:3000/api/notas \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{"titulo":"Mi nota","contenido":"Contenido de prueba"}'

# Listar notas
curl http://localhost:3000/api/notas -H "Authorization: Bearer <TOKEN>"
```

## App móvil relacionada

[../NotasApp](../NotasApp) — NotasApp (Android), en este mismo repositorio.

## Autor

Rodrigo Alexander Herrera Solares
