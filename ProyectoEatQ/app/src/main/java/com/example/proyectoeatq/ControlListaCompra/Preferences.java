package com.example.proyectoeatq.ControlListaCompra;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/*
Las preferences es donde vamos a tner localizados toda la lógica de las shadedpreferences
 */
public class Preferences {
    //creo el nombre de nuestra base de datos
    static final String PREF_NAME = "myDatabase";
    //clave con la que guardaremos los items
    static final String ITEMS = "valor_item"; // ? tasks_value
    static SharedPreferences prefs;



    //Constructor que recibe el content
    public Preferences (Context context){
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveItem(List<String> lista){
        /* Ya que el SharedPreferences solo permite guardar colecciones
        como Set (no acepta List), convertimos nuestra List a Set para poder guradarla.
        Usamos LinkedHashSet para manetner el orden de la lista origina
        (el HashSet los desordena). Además, no permite duplicados.
         */
        Set<String> set = new LinkedHashSet<>(lista);
        prefs.edit().putStringSet(ITEMS,set).apply();

    }


}
