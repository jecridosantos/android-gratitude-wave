# Yuspa 🌟

¡Bienvenido a **Yuspa**!  
Una aplicación diseñada para ayudarte a cultivar la gratitud como un hábito diario, mientras sirve como una guía práctica para la comunidad de desarrolladores de Android.

---

## ✨ Propósito de la App

- **Para los usuarios:** Escribir y guardar notas de gratitud en un ambiente seguro e inspirador.
- **Para desarrolladores:** Un ejemplo funcional de una app construida con **Android Jetpack Compose**, **Kotlin**, y **Java 17**, utilizando Firebase como backend.

---

## ⚙️ Configuración del Proyecto

### Requisitos previos

1. **Instalar Android Studio** con soporte para Kotlin y Compose.
2. **Java 17** o superior instalado en tu sistema.
3. **Cuenta Firebase** configurada.

### Pasos para configurar Firebase

1. **Crear un proyecto en Firebase**:
   - Ve a [Firebase Console](https://console.firebase.google.com).
   - Crea un nuevo proyecto.

2. **Agregar una app Android**:
   - Configura tu aplicación Android en el proyecto de Firebase.
   - Descarga el archivo `google-services.json` y colócalo en la carpeta `app/` de tu proyecto.

   **Nota:** El archivo `google-services.json` incluido en este repositorio es un archivo de ejemplo con datos ficticios. **Debes generar tu propio archivo en la configuración de Firebase** para que la app funcione correctamente.

3. **Habilitar Autenticación**:
   - En la consola de Firebase, ve a la sección de **Authentication**.
   - Habilita los proveedores de inicio de sesión:
     - **Correo y contraseña**.
     - **Google**.

4. **Habilitar "Build with Gemini" (Opcional)**:
   - Si deseas usar la funcionalidad de generación de contenido proporcionada por Firebase, actualiza tu plan a **Blaze** (de pago).

---

## 🔐 Configuración de Llave Secreta

El proyecto incluye un mecanismo para cifrar las notas de los usuarios, asegurando su privacidad.  
Busca en el código la línea anotada con `//TODO: add custom secret key` y reemplázala con una clave secreta personalizada.

Ejemplo:

```kotlin
// Ejemplo:
val secretKey = "tu-clave-secreta-personalizada"
```
---

## 🛠️ Tecnologías Utilizadas

   - Android Jetpack Compose: Para un diseño de interfaz moderno y declarativo.
   - Kotlin: Lenguaje principal del proyecto.
   - Java 17: Para soporte avanzado de Java.
   - Firebase:
      - Autenticación de usuarios.
      - Almacenamiento de datos.
      - Build with Gemini.
---

## 🚀 Cómo ejecutar la app
1. Clona este repositorio:
   
```
git clone https://github.com/tuusuario/yuspa.git
```
2. Ábrelo en Android Studio.
3. Asegúrate de tener configurado Firebase correctamente (ver sección **Configuración del Proyecto**).
4. Ejecuta la app en un emulador o dispositivo físico.
