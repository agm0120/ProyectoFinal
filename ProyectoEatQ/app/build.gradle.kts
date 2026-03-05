plugins {
    id("com.android.application")
    //Add the Google services Gradle plugin
    id("com.google.gms.google-services")

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    //He añadido el bom de firebase para poder incluir las dependencias de los otros servicios de firebase
    //La dependencia que habia del auth daba fallo y he tenido que modificarla
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    // TODO: Add the dependencies for Firebase products you want to use
    // When using de BoM, don't especify versions in Firebase dependencies
    //Aqui esta la dependencia del auth. Creo que más adelante tendremos que añadir otras dependencias de firebase
    implementation("com.google.firebase:firebase-auth")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
}