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


public class EmergenteSentadillas extends DialogFragment {

    private EditText et_numSentadillas, et_notaUsuario;
    private Button btn_guardarSentadillas;
    private GestorFirebase gestorDB;

    public EmergenteSentadillas() {
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
        View v = inflater.inflate(R.layout.dialog_sentadillas, container, false);

        et_numSentadillas = v.findViewById(R.id.et_numSentadillas);
        et_notaUsuario = v.findViewById(R.id.et_notaUsuario);
        btn_guardarSentadillas = v.findViewById(R.id.btn_guardarSentadillas);

        btn_guardarSentadillas.setOnClickListener(view -> {
            String ejercicio = "Sentadillas";
            String numSentadillas = et_numSentadillas.getText().toString();
            String nota = et_notaUsuario.getText().toString();

            prepararEjercicio(ejercicio, numSentadillas, nota);
        });

        // Inflate the layout for this fragment
        return v;
    }

    private void prepararEjercicio(String ejercicio, String numSentadillas, String nota) {
        Map<String, Object> datosDeporte = new HashMap<>();
        datosDeporte.put("Ejercicio", ejercicio);
        datosDeporte.put("Contador", numSentadillas);
        datosDeporte.put("Anotación", nota);

        //Llamamos al método del gestor para guardar los datos en la base de datos
        gestorDB.guardarDatos("Deporte", datosDeporte);

        //ceramos el diálogo después de guardar los datos
        dismiss();
    }
}