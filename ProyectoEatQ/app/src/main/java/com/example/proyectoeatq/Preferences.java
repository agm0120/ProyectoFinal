package com.example.proyectoeatq;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.proyectoeatq.ControlListaCompra.LCItem;

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



    //Constructor que recibe el content
    public Preferences (Context context){
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Metodo para guardar información
    public void saveItem(List<LCItem> lista){
        /* Ya que el SharedPreferences solo permite guardar colecciones como Set
        (no acepta List), convertimos nuestra List a Set para poder guradarla.
        Usamos LinkedHashSet para manetner el orden de la lista origina (el HashSet
        los desordena). Además, no permite duplicados. */
        Set<String> set = new LinkedHashSet<>();
        for (LCItem item : lista){
                set.add(item.toString());
        }
        prefs.edit().putStringSet(ITEMS,set).apply();

    }

    //Metodo para recuperar informacion
    public List<LCItem> getList(){
        /* Al recuperar la información, hacemos el proceso inverso: obtenemos el Set
         y lo convertimos a List */
        Set <String> set = prefs.getStringSet(ITEMS, new LinkedHashSet<>());
        List<LCItem> lista = new ArrayList<>();
        for(String s : set){
            lista.add(LCItem.fromString(s));
        }
        return lista;


    }



}
