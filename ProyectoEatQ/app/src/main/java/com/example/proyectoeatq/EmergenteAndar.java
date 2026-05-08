package com.example.proyectoeatq;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;


public class EmergenteAndar extends DialogFragment {


    private EditText et_horas, et_minutos, et_notaUsuario;
    private Button btn_guardar;
    private GestorFirebase gestorDB;

    public EmergenteAndar() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestorDB = new GestorFirebase(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_andar, container, false);

        et_horas = v.findViewById(R.id.et_horas);
        et_minutos = v.findViewById(R.id.et_minutos);
        et_notaUsuario = v.findViewById(R.id.et_notaUsuario);
        btn_guardar = v.findViewById(R.id.btn_guardarAndar);

        //Configuramos el botón para guardar los datos cuando se haga clic
        btn_guardar.setOnClickListener(view -> {
            String ejercicio = "Andar";
            String h = et_horas.getText().toString();
            String m = et_minutos.getText().toString();
            String nota = et_notaUsuario.getText().toString();

            //Lamamos a la función de preparar el mapa
            prepararEjercicio(ejercicio,h,m,nota);
        });

        // Inflate the layout for this fragment
        return v;
    }

    public void prepararEjercicio(String ejercicio, String horas, String minutos, String notaUsuario){
        Map<String, Object> datosDeporte = new HashMap<>();

        datosDeporte.put("Ejercicio", ejercicio);
        datosDeporte.put("Contador", horas + " : " + minutos);
        datosDeporte.put("Anotación", notaUsuario);


        //Llamamos al método del gestor para guardar los datos en la base de datos
        gestorDB.guardarDatos("Deporte", datosDeporte);

        dismiss(); //Cerramos el diálogo después de guardar los datos

    }
}