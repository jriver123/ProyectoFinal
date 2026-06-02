# ProyectoFinal - Battle.io

Aplicación Android en Jetpack Compose que ahora consume la API Java de usuarios ubicada en `/api/usuarios`.

## Qué consume

La app usa Retrofit para hacer CRUD sobre el recurso `Usuario`:

- `GET /api/usuarios`
- `GET /api/usuarios/{id}`
- `POST /api/usuarios`
- `PUT /api/usuarios/{id}`
- `DELETE /api/usuarios/{id}`

## Modelo esperado

La API debe manejar un usuario con estos campos:

- `id`
- `username`
- `password`
- `description`
- `email`

## Configuración de conexión

En `app/src/main/java/com/example/proyectofinal/data/remote/retrofitClient.kt` la app apunta por defecto a:

```text
http://10.0.2.2:8080/api/
```

### Importante

- Si usas un **emulador Android**, `10.0.2.2` apunta a tu PC.
- Si usas un **dispositivo físico**, reemplaza esa URL por la IP local de tu computadora, por ejemplo:

```text
http://192.168.1.50:8080/api/
```

## Cómo probar

1. Levanta tu backend Java/Spring Boot.
2. Verifica que exponga `/api/usuarios`.
3. Ejecuta la app Android.
4. Abre el perfil del jugador y usa:
   - **Actualizar desde API**
   - **Crear usuario en API** / **Guardar cambios**
   - **Eliminar usuario de la API**

## Build verificado

Se comprobó con:

```powershell
.\gradlew.bat :app:assembleDebug
```

