package com.example.proyectoeatq;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView; // Importante para el calendario
import android.widget.Toast;        // Para mostrar mensajes en pantalla
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton; // Para tus botones

public class InformacionHistorial extends Fragment {

    // 1. Definimos las variables privadas (los objetos que usaremos)
    private CalendarView cvCalendario;
    private MaterialButton btnDeporte;
    private MaterialButton btnComida;

    public InformacionHistorial() {
        // Constructor necesario
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 2. "Inflamos" la vista para poder buscar los ID del' XML
        View vista = inflater.inflate(R.layout.fragment_informacion_historial_deporte, container, false);

        // 3. Enlazamos las variables con los ID que pusiste en el XML
        cvCalendario = vista.findViewById(R.id.cv_calendario_historial);
        btnDeporte = vista.findViewById(R.id.btn_historial_deporte);
        btnComida = vista.findViewById(R.id.btn_historial_comida);

        // Lógica de los Botones
        btnDeporte.setOnClickListener(v -> {
            actualizarInterfaz(true); // Izquierda -> Rojo
        });

        btnComida.setOnClickListener(v -> {
            actualizarInterfaz(false); // Derecha -> Verde
        });

        // 4. Programamos qué pasa al tocar el calendario
        cvCalendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int anio, int mes, int diaDelMes) {
                // El mes viene de 0 a 11 (1 a 12)
                int mesReal = mes + 1;

                // Creamos un texto con la fecha
                String fecha = diaDelMes + "/" + mesReal + "/" + anio;

                // Mostramos un aviso rápido en pantalla con la fecha elegida
                Toast.makeText(getContext(), "Seleccionaste: " + fecha, Toast.LENGTH_SHORT).show();
            }
        });

        // 5. Programamos qué pasa al tocar los botones (solo para que hagan algo)
        btnDeporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Cambiando a Deporte", Toast.LENGTH_SHORT).show();
            }
        });

        btnComida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Cambiando a Comida", Toast.LENGTH_SHORT).show();
            }
        });

        return vista;

        
    }
        private void actualizarInterfaz(boolean esDeporteSeleccionado) {
            if (esDeporteSeleccionado) {
                btnDeporte.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
                btnComida.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
            } else {
                btnComida.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
                btnDeporte.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
            }
        }

}