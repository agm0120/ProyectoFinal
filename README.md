# EatQ - Tu Asistente Nutricional Inteligente 🥗



**EatQ** es una aplicación Android diseñada para ayudar a los usuarios a seguir el **"Método del Plato"**. Utiliza Inteligencia Artificial para analizar fotografías de comidas en tiempo real, proporcionando un desglose nutricional automático y permitiendo un seguimiento exhaustivo de la actividad física y hábitos alimenticios.



---

## 📋 Requisitos del Sistema

Para asegurar el correcto funcionamiento de la aplicación, el entorno debe cumplir con los siguientes requisitos técnicos:

* **Versión de Android:** Mínima API 24 (Android 7.0 Nougat) o superior.

* **Versión de Gradle:** 8.0.

* **Entorno de Desarrollo:** Android Studio Ladybug (2024.2.1) o superior recomendado.

* **Conexión a Internet:** Requerida para la autenticación de usuarios, sincronización de base de datos y procesamiento de imágenes con IA.

* **Espacio en disco:** Se recomienda disponer de al menos **500 MB** para la instalación y almacenamiento de caché.

---

## 🛠️ Instrucciones de Compilación e Instalación

Sigue estos pasos exactos para configurar y ejecutar el proyecto en tu entorno local:

### 1. Clonar el Repositorio

Abre tu terminal y clona el proyecto desde el repositorio oficial:

```bash
git clone [https://github.com/tu-usuario/proyectoeatq.git](https://github.com/tu-usuario/proyectoeatq.git)
```

### 2. Configuración de Firebase
La aplicación integra servicios de **Firebase Auth, Firestore y Realtime Database**.
1. Descarga el archivo `google-services.json` desde tu consola de Firebase.

2. Mueve el archivo a la carpeta del módulo de la aplicación: `ProyectoEatQ/app/`.

### 3. Configuración de la API de Gemini (IA)
La detección de alimentos se realiza mediante el modelo `gemini-2.5-flash`.
1. Obtén una API Key válida en [Google AI Studio](https://aistudio.google.com/).
   
2. En la raíz del proyecto, abre o crea el archivo `local.properties`.
   
3. Añade la siguiente clave:
   
```properties
GEMINI_API_KEY=tu_clave_aqui
```