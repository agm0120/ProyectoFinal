package com.example.proyectoeatq;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class HistorialDeporte extends Fragment {

    private EditText et_partesEstiradas, et_tiempoAndado, et_pisosSubidos, et_sentadillasRealizadas;
    private GestorFirebase gestorDB;
    private long fechaSeleccionada;

    //Constructor vacío requerido para Fragment
     public HistorialDeporte() {}

    //Método estático para crear una nueva instancia del fragmento con la fecha seleccionada como argumento
    public static HistorialDeporte newInstance(long timestamp) {
        HistorialDeporte fragment = new HistorialDeporte();
        Bundle args = new Bundle();
        args.putLong("fecha", timestamp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestorDB = new GestorFirebase(getContext());

        //Obtenemos la fecha seleccionada de los argumentos
        if (getArguments() != null) {
            fechaSeleccionada = getArguments().getLong("fecha");
        }

    }

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos el layout para este fragmento
        View v = inflater.inflate(R.layout.fragment_informacion_historial_deporte, container, false);

        // Enlazamos los EditText con sus IDs del XML
        et_partesEstiradas = v.findViewById(R.id.et_partesEstiradas);
        et_tiempoAndado = v.findViewById(R.id.et_tiempoAndado);
        et_pisosSubidos = v.findViewById(R.id.et_pisosSubidos);
        et_sentadillasRealizadas = v.findViewById(R.id.et_sentadillasRealizadas);

        recuperarDatos();

        return v;
    }

    private void recuperarDatos() {
         // Obtenemos el rango de fechas para el día seleccionado (desde las 00:00 hasta las 23:59)
         Date [] rangoDia = obtenerRangoDia(fechaSeleccionada);

         gestorDB.consultarDatosPorFecha("Deporte", rangoDia[0], rangoDia[1], snapshots -> {
             // Aquí procesamos los datos recuperados
             int totalMinutosAndados = 0;
             int totalPisosSubidos = 0;
             int totalSentadillas = 0;
             Set<String> zonasEstiradas = new HashSet<>(); // Usamos un Set para evitar duplicados

             for(QueryDocumentSnapshot doc : snapshots){
                 String tipo = doc.getString("Ejercicio");
                 String contador = doc.getString("Contador");
                 if(tipo != null && contador != null){
                     switch (tipo){
                         case "Andar":
                             totalMinutosAndados += extraerMinutos(contador);
                             break;
                         case "Subir escaleras":
                             totalPisosSubidos += Integer.parseInt(contador);
                             break;
                         case "Sentadillas":
                             totalSentadillas += Integer.parseInt(contador);
                             break;
                         case "Estirar":
                             String[] partes = contador.split(", "); //Separamos las partes por comas
                             for(String parte : partes){
                                 zonasEstiradas.add(parte.trim()); //Agregamos cada parte al Set, eliminando espacios
                             }
                             break;

                     }
                 }
             }

             //convertimos el Set d elas zonas en una sola cadena de texto
             String estiramientosFinales = String.join(",", zonasEstiradas);

             colocarDatos(totalMinutosAndados, totalPisosSubidos, totalSentadillas, estiramientosFinales);

         });



    }

    private void colocarDatos(int minTotales, int pisos, int sentadillas, String estiramientos) {
        int horas = minTotales /60;
        int minutos = minTotales % 60;

        et_tiempoAndado.setText(horas + "h " + minutos + "m");
        et_pisosSubidos.setText(String.valueOf(pisos));
        et_sentadillasRealizadas.setText(String.valueOf(sentadillas));
        et_partesEstiradas.setText(estiramientos.isEmpty() ? "Ninguna" : estiramientos);

    }

    private int extraerMinutos(String tiempo) {
         try{
             String [] partes = tiempo.split(":");
             int horas = Integer.parseInt(partes[0].trim());
             int minutos = Integer.parseInt(partes[1].trim());
             return (horas* 60) + minutos; // Convertimos todo a minutos

         }catch(Exception e){
             return 0;
         }
    }

    private Date[] obtenerRangoDia(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date inicio = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date fin = cal.getTime();

        return new Date[]{inicio, fin};

    }


}
