# Battle.io

## Descripción del proyecto

Battle.io es una aplicación móvil desarrollada en Android Studio utilizando Jetpack Compose. La app simula un prototipo de videojuego con una interfaz moderna, funcional y organizada mediante navegación inferior.

El objetivo principal del proyecto es aplicar conceptos de Programación Orientada a Objetos, navegación entre pantallas, manejo de estados, persistencia de datos y diseño de interfaces modernas en Android.

La aplicación cuenta con varias pantallas funcionales. En la pantalla de inicio, el usuario puede visualizar su nombre, nivel, monedas y estadísticas principales. En la sección de partidas, puede simular una partida y actualizar automáticamente sus estadísticas. En la tienda, puede seleccionar y comprar paquetes de monedas de forma simulada. En el perfil, puede editar su nombre, correo, biografía y seleccionar una foto desde la galería del celular. Finalmente, en configuración, puede cambiar el idioma de la app, modificar la calidad gráfica y ajustar el volumen de música y efectos.

Los datos del usuario se guardan de forma local, por lo que los cambios se mantienen aunque la aplicación se cierre. Además, el idioma seleccionado se aplica a toda la interfaz de la aplicación.

## Funcionalidades principales

- Pantalla de inicio con resumen del jugador.
- Pantalla de partidas con simulación de juego.
- Pantalla de tienda con selección y compra simulada de paquetes.
- Pantalla de perfil con edición de datos personales.
- Selección de foto de perfil desde la galería del celular.
- La foto de perfil también se muestra en la pantalla de inicio.
- Pantalla de configuración.
- Cambio real de idioma entre español, inglés y francés.
- Guardado local de información mediante SharedPreferences.
- Navegación inferior completamente funcional.
- Interfaz moderna usando Material Design 3.
- Aplicación de Programación Orientada a Objetos mediante clases de datos.

## Tecnologías utilizadas

- Android Studio
- Kotlin
- Jetpack Compose
- Material Design 3
- SharedPreferences
- Activity Result API
- Android SDK

## Estructura general de la app

La aplicación está organizada en un solo archivo principal `MainActivity.kt`, donde se implementan las pantallas, la navegación, los estados y la lógica principal del sistema.

Las principales pantallas implementadas son:

- `HomeScreen`: muestra el inicio de la app y el resumen del jugador.
- `MatchesScreen`: permite simular partidas y actualizar estadísticas.
- `StoreScreen`: permite seleccionar paquetes y realizar compras simuladas.
- `ProfileScreen`: permite modificar datos del usuario y elegir foto de perfil.
- `SettingsScreen`: permite cambiar idioma, calidad gráfica y volumen.

También se utilizan clases de datos como:

- `GamePack`: representa los paquetes disponibles en la tienda.
- `MatchHistory`: representa el historial de partidas del jugador.

## Instrucciones para ejecutar la app

1. Clonar o descargar el repositorio del proyecto.

2. Abrir el proyecto en Android Studio.

3. Esperar a que Android Studio sincronice el proyecto con Gradle.

4. Verificar que el proyecto tenga habilitado Jetpack Compose.

5. Abrir el archivo principal:

```text
app > kotlin+java > com.example.proyectofinal > MainActivity.kt
