plugins {
    id("com.android.application")
    //Add the Google services Gradle plugin
    id("com.google.gms.google-services")
    // Plugin para uso privado de la clave de la API IA
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.example.proyectoeatq"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.proyectoeatq"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")

    // Firebase BoM. Necesario para incluir las dependenciasde los otros servicios de firebase.
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))

    // Firebase dependencies. Dependencia del auth y hueco para otras posibles
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")

    // SDK de Google AI para Android
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")

    // Necesario para manejar tareas asíncronas en Java
    implementation("com.google.guava:guava:31.1-android")

    //Test dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
}