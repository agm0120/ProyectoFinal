package com.example.proyectoeatq.ControlListaCompra;

import android.app.Application;

/*
Application va a hacer que esta sea la primera clase por la que pase la app cuando se inicie.
se usa para instanciar cosas que se van a usar en todo el proyecto.
 */
public class AppApplication extends Application {

    public static Preferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = new Preferences(getBaseContext());
    }
}
