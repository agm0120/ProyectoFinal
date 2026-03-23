package com.example.proyectoeatq;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ResultadoPlato extends Fragment {

    private String infoIA;
    private TextView tvResultado;

    public ResultadoPlato() {
        // Constructor vacío obligatorio
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recuperamos el String que enviamos desde el Fragment anterior
        if (getArguments() != null) {
            // "info_ia" debe coincidir exactamente con la clave que pusiste en el Bundle anterior
            infoIA = getArguments().getString("info_ia");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos el layout
        View view = inflater.inflate(R.layout.fragment_resultado, container, false);

        // Inicializamos el TextView
        tvResultado = view.findViewById(R.id.textView5); // Reemplaza con tu ID real

        // Mostramos la respuesta de la IA
        if (infoIA != null) {
            tvResultado.setText(infoIA);
        } else {
            tvResultado.setText("No se recibió información de la IA.");
        }

        return view;
    }
}