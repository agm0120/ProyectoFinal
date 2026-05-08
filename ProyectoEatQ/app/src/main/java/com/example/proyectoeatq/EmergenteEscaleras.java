package com.example.proyectoeatq;

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


public class EmergenteEscaleras extends DialogFragment {
    private EditText et_numPisos, et_notaUsuario;
    private Button btn_guardarEscaleras;
    private GestorFirebase gestorDB;



    public EmergenteEscaleras() {
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

        View v = inflater.inflate(R.layout.dialog_escaleras, container, false);

        et_numPisos = v.findViewById(R.id.et_numPisos);
        et_notaUsuario = v.findViewById(R.id.et_notaUsuario);
        btn_guardarEscaleras = v.findViewById(R.id.btn_guardarEscaleras);

        btn_guardarEscaleras.setOnClickListener(view -> {
            String ejercicio = "Subir escaleras";
            String pisos = et_numPisos.getText().toString();
            String nota = et_notaUsuario.getText().toString();

            prepararEjercicio(ejercicio, pisos, nota);

        });

        // Inflate the layout for this fragment
        return v;
    }

    private void prepararEjercicio(String ejercicio, String pisos, String nota) {
        //Aquí puedes preparar el mapa de datos para guardar en Firestore
        Map<String, Object> datos = new HashMap<>();
        datos.put("Ejercicio", ejercicio);
        datos.put("Contador", pisos);
        datos.put("Anotación", nota);

        //Llamamos a la función de guardar datos en Firestore
        gestorDB.guardarDatos("Deporte", datos);

        //Cerramos el diálogo después de guardar
        dismiss();
    }
}