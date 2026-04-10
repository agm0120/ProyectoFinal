package com.example.proyectoeatq;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class Configuracion extends Fragment {


    private EditText etNombre, etFecha;
    private Button btn_guardar;

    public Configuracion() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configuracion, container, false);

        etNombre = view.findViewById(R.id.et_editNombre);
        etFecha = view.findViewById(R.id.et_editFecha);
        btn_guardar = view.findViewById(R.id.btn_guardarConfiguracion);

        btn_guardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString();
            String fecha = etFecha.getText().toString();

            //TODO: habría que guardar los nuevos datos en la base de datos

            // Mostrar un mensaje de confirmación
            Toast.makeText(getContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
