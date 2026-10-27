# NotasApp

Aplicación Android de gestión de notas personales, desarrollada para la **Evaluación Parcial II** del curso
Desarrollo de Aplicaciones Móviles — Universidad Da Vinci de Guatemala.

Permite a un usuario autenticarse contra un backend REST, crear notas (título + contenido) y consultar el
listado de sus notas guardadas.

## Tecnologías

- **Lenguaje:** Kotlin
- **UI:** XML Layouts (`ConstraintLayout` y `LinearLayout` según pantalla)
- **Arquitectura:** MVVM (Model – View – ViewModel)
- **Estado:** LiveData (`Resource<T>`: Loading / Success / Error)
- **Red:** Retrofit + OkHttp, con interceptor de autenticación
- **Sesión persistente:** `EncryptedSharedPreferences` ("recordar login")

## Pantallas

| Pantalla | Layout raíz | Descripción |
|---|---|---|
| Login | ConstraintLayout | Autenticación contra el backend. Si ya hay una sesión guardada, la salta automáticamente. |
| Dashboard | LinearLayout | Bienvenida con el nombre del usuario + accesos a Crear nota / Ver notas + Cerrar sesión. |
| Crear nota | ConstraintLayout | Formulario de título y contenido, con validaciones e indicador de carga. |
| Listado de notas | LinearLayout | `RecyclerView` con `CardView` (ConstraintLayout interno), obtenido del backend. |

## Arquitectura

```
Activity (View) → ViewModel → Repository → ApiService (Retrofit) → Backend
```

La Activity solo observa un `LiveData<Resource<T>>` del ViewModel; el Repository es quien decide si hubo
éxito, error del servidor, sesión expirada (401) o falta de conexión. El token de sesión se agrega
automáticamente a cada petición mediante un `Interceptor` de OkHttp.

La documentación técnica completa (estructura de paquetes, responsabilidad de cada clase, flujo entre
pantallas) está en [`Documentacion_NotasApp.docx`](./Documentacion_NotasApp.docx).

## Backend / API

El backend es **[NotasApi](../NotasApi)**, en este mismo repositorio (Node.js + Express + SQLite):

```
POST api/auth/login     { email, password }        → { token, usuario: { id, nombre, email } }
POST api/notas          { titulo, contenido }        → { id, titulo, contenido, fechaCreacion }
GET  api/notas                                       → [ { id, titulo, contenido, fechaCreacion }, ... ]
```

`POST` y `GET /api/notas` requieren el header `Authorization: Bearer <token>`.

## Cómo ejecutar

1. Levantar el backend primero: en [`../NotasApi`](../NotasApi), `npm install && npm start`.
2. Abrir esta carpeta (`NotasApp`) en Android Studio (`File > Open`) y esperar el sync de Gradle.
3. Ejecutar en el emulador (▶ Run) — `RetrofitClient.kt` ya apunta a `http://10.0.2.2:3000/`, que es como
   el emulador ve al `localhost` de la PC. Para un dispositivo físico, cambiar esa URL por la IP de la PC
   en la red Wi-Fi (NotasApi la imprime al arrancar).
4. Iniciar sesión con el usuario de prueba que el backend crea automáticamente: `demo@udv.edu.gt` / `Demo1234`.

## Estructura del proyecto

```
app/src/main/java/com/udv/notasapp/
├── NotasApp.kt                  Punto único de creación de dependencias
├── data/
│   ├── local/SessionManager.kt  Sesión cifrada (token + "recordar login")
│   ├── remote/                  ApiService, RetrofitClient, AuthInterceptor
│   │   └── dto/                 Modelos de petición/respuesta
│   └── repository/               AuthRepository, NotasRepository
├── ui/
│   ├── login/
│   ├── dashboard/
│   ├── createnote/
│   └── noteslist/                Activity + ViewModel por pantalla
└── util/                         Resource (estado async), ViewModelFactory
```

## Autor

Rodrigo Alexander Herrera Solares
