package com.example.proyectoeatq;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/* Las preferences es donde vamos a tener localizada
toda la lógica de las ShadedPreferences
 */
public class Preferences {
    //creo el nombre de nuestra base de datos
    static final String PREF_NAME = "myDatabase";
    //clave con la que guardaremos los items
    static final String ITEMS = "valor_item"; // ? tasks_value
    static SharedPreferences prefs;
    static final String KEY_RECORDAR = "usuario_recordado";



    //Constructor que recibe el content
    public Preferences (Context context){
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Metodo para guardar información de la lista de la compra
    public void saveItem(List<LCItem> lista){
        /* Ya que el SharedPreferences solo permite guardar colecciones como Set
        (no acepta List), convertimos nuestra List a Set para poder guradarla.
        Usamos LinkedHashSet para manetner el orden de la lista origina (el HashSet
        los desordena). Además, no permite duplicados. */
        Set<String> set = new LinkedHashSet<>(lista);
        prefs.edit().putStringSet(ITEMS,set).apply();

    }

    //Metodo para recuperar informacion de la lista de la compra
    public List<LCItem> getList(){
        /* Al recuperar la información, hacemos el proceso inverso: obtenemos el Set
         y lo convertimos a List */
        Set <String> set = prefs.getStringSet(ITEMS, new LinkedHashSet<>());
        return new ArrayList<>(set);


    }

    //MÉTODOS PARA EL LOGIN ("RECUERDAME")
    public void setRecordar(boolean valor){
        prefs.edit().putBoolean(KEY_RECORDAR, valor).apply();
    }

    public boolean isRecordado(){
        return prefs.getBoolean(KEY_RECORDAR, false);
    }



}
