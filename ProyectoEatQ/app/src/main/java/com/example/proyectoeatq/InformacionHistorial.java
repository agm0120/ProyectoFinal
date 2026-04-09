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
    private CalendarView cv_calendarioHistorial;
    private MaterialButton btn_cargarDeporte;
    private MaterialButton btn_cargarComida;

    private boolean deporteActivo = false; //de primeras el deporte no está seleccionado

    public InformacionHistorial() {
          // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 2. "Inflamos" la vista para poder buscar los ID del' XML
        View vista = inflater.inflate(R.layout.fragment_historial, container, false);

        // 3. Enlazamos las variables con los ID que pusiste en el XML
        cv_calendarioHistorial = vista.findViewById(R.id.cv_historial);
        btn_cargarDeporte = vista.findViewById(R.id.btn_historialDeporte);
        btn_cargarComida = vista.findViewById(R.id.btn_historialComida);

        //Llamamos a cargar Comida para que al abrir el fragmento, ya esté seleccionado y se vea la interfaz de comida
        configurarModoComida();




        // 4. Programamos qué pasa al tocar el calendario
        cv_calendarioHistorial.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int anio, int mes, int diaDelMes) {
                // El mes viene de 0 a 11 (1 a 12). Luego creamos un texto con la fecha seleccionada
                int mesReal = mes + 1;
                String fecha = diaDelMes + "/" + mesReal + "/" + anio;

                // Si deporteActivo es true, muestra "Deporte", si es false, muestra "Comida"
                String tipo = deporteActivo ? "Deporte" : "Comida";
                Toast.makeText(getContext(), "Viendo: " + tipo + " del: " + fecha, Toast.LENGTH_SHORT).show();
            }
        });


        //Para los botones, llamamos a las funciones que configuran cada modo (deporte o comida)
        // y dentro de esas funciones, actualizamos la interfaz
        btn_cargarDeporte.setOnClickListener(v -> configurarModoDeporte());
        btn_cargarComida.setOnClickListener(v -> configurarModoComida());

        return vista;
    }

    private void configurarModoComida() {
        deporteActivo = false; // Indicamos que el modo deporte no está activo
        actualizarInterfaz(false); //boton de comida en verde
        Toast.makeText(getContext(), "Historial de Comida", Toast.LENGTH_SHORT).show();
    }

    private void configurarModoDeporte() {
        deporteActivo = true; // Indicamos que el modo deporte está activo
        actualizarInterfaz(true); //boton de deporte en rojo
        Toast.makeText(getContext(), "Historial de Deporte", Toast.LENGTH_SHORT).show();
    }

    private void actualizarInterfaz(boolean deporteSeleccionado) {

        int verdeActivo = getContext().getColor(R.color.verdeLogo);
        int grisInactivo = getContext().getColor(R.color.grisBoton);

            if (deporteSeleccionado) {
                btn_cargarDeporte.setBackgroundTintList(ColorStateList.valueOf(verdeActivo));
                btn_cargarComida.setBackgroundTintList(ColorStateList.valueOf(grisInactivo));
            } else {
                btn_cargarDeporte.setBackgroundTintList(ColorStateList.valueOf(grisInactivo));
                btn_cargarComida.setBackgroundTintList(ColorStateList.valueOf(verdeActivo));
            }
        }

}