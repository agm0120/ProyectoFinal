package com.example.proyectoeatq;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class HistorialComida extends Fragment {

    private long fechaSeleccionada;
    private PieChart pieChart;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private TextView comidas;

    // Constructor vacío requerido
    public HistorialComida() {}

    // Recibimos la fecha desde el fragment anterior
    public static HistorialComida newInstance(long timestamp) {
        HistorialComida fragment = new HistorialComida();

        Bundle args = new Bundle();
        args.putLong("fecha", timestamp);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            fechaSeleccionada = getArguments().getLong("fecha");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(
                R.layout.fragment_informacion_historial_comida,
                container,
                false
        );

        comidas = v.findViewById(R.id.tv_info_comida_detallada);
        pieChart = v.findViewById(R.id.pieChart_historial_comida);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // CONSULTAMOS FIREBASE
        obtenerResumenDelDia();

        return v;
    }

    private void obtenerResumenDelDia() {

        String uid = auth.getCurrentUser().getUid();

        // INICIO DEL DÍA
        Calendar inicioDia = Calendar.getInstance();
        inicioDia.setTimeInMillis(fechaSeleccionada);

        inicioDia.set(Calendar.HOUR_OF_DAY, 0);
        inicioDia.set(Calendar.MINUTE, 0);
        inicioDia.set(Calendar.SECOND, 0);
        inicioDia.set(Calendar.MILLISECOND, 0);

        // FIN DEL DÍA
        Calendar finDia = Calendar.getInstance();
        finDia.setTimeInMillis(fechaSeleccionada);

        finDia.set(Calendar.HOUR_OF_DAY, 23);
        finDia.set(Calendar.MINUTE, 59);
        finDia.set(Calendar.SECOND, 59);
        finDia.set(Calendar.MILLISECOND, 999);

        Timestamp inicioTimestamp = new Timestamp(inicioDia.getTime());
        Timestamp finTimestamp = new Timestamp(finDia.getTime());

        // CONSULTA FIRESTORE
        db.collection("Comida")
                .whereEqualTo("usuario", uid)
                .whereGreaterThanOrEqualTo("fecha", inicioTimestamp)
                .whereLessThanOrEqualTo("fecha", finTimestamp)
                .orderBy("fecha")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    float totalVegetales = 0;
                    float totalProteinas = 0;
                    float totalCarbohidratos = 0;

                    StringBuilder resumenComidas = new StringBuilder();

                    int numeroComida = 1;

                    int cantidadComidas = queryDocumentSnapshots.size();

                    resumenComidas.append("Comidas registradas: ")
                            .append(cantidadComidas)
                            .append("\n\n");
                    // RECORREMOS TODOS LOS DOCUMENTOS
                    for (com.google.firebase.firestore.DocumentSnapshot document :
                            queryDocumentSnapshots) {

                        Map<String, Object> porcentajes =
                                (Map<String, Object>) document.get("porcentajes");

                        if (porcentajes != null) {

                            Number vegetales =
                                    (Number) porcentajes.get("vegetales");

                            Number proteinas =
                                    (Number) porcentajes.get("proteinas");

                            Number carbohidratos =
                                    (Number) porcentajes.get("carbohidratos");

                            totalVegetales +=
                                    vegetales != null ? vegetales.floatValue() : 0f;

                            totalProteinas +=
                                    proteinas != null ? proteinas.floatValue() : 0f;

                            totalCarbohidratos +=
                                    carbohidratos != null ? carbohidratos.floatValue() : 0f;

                            resumenComidas.append("Comida ")
                                    .append(numeroComida)
                                    .append(":\n");

                            resumenComidas.append("Vegetales: ")
                                    .append(vegetales != null ? vegetales.intValue() : 0)
                                    .append("%\n");

                            resumenComidas.append("Proteínas: ")
                                    .append(proteinas != null ? proteinas.intValue() : 0)
                                    .append("%\n");

                            resumenComidas.append("Carbohidratos: ")
                                    .append(carbohidratos != null ? carbohidratos.intValue() : 0)
                                    .append("%\n\n");

                            numeroComida++;
                        }
                    }

                    // LOG PARA COMPROBAR
                    Log.d("FIREBASE",
                            "Veg: " + totalVegetales
                                    + " Prot: " + totalProteinas
                                    + " Carb: " + totalCarbohidratos);

                    // Mostramos la info de las comidas como texto
                    comidas.setText(resumenComidas.toString());
                    // PINTAMOS LA GRÁFICA
                    configurarGrafico(
                            totalVegetales,
                            totalProteinas,
                            totalCarbohidratos
                    );
                })
                .addOnFailureListener(e ->
                        Log.e("FIREBASE",
                                "Error obteniendo historial comida",
                                e)
                );
    }

    private void configurarGrafico(float veg, float prot, float carb) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        // SOLO AÑADIR SI > 0
        if (veg > 0)
            entries.add(new PieEntry(veg, "Vegetales"));

        if (prot > 0)
            entries.add(new PieEntry(prot, "Proteínas"));

        if (carb > 0)
            entries.add(new PieEntry(carb, "Carbohidratos"));

        PieDataSet dataSet = new PieDataSet(entries, "");

        // COLORES
        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(Color.parseColor("#4CAF50")); // Verde
        colors.add(Color.parseColor("#F44336")); // Rojo
        colors.add(Color.parseColor("#FFC107")); // Amarillo

        dataSet.setColors(colors);

        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(14f);

        dataSet.setValueFormatter(new PercentFormatter(pieChart));

        PieData data = new PieData(dataSet);

        // CONFIGURACIÓN DEL CHART
        pieChart.setData(data);

        pieChart.setDrawHoleEnabled(false);

        pieChart.setDrawEntryLabels(false);

        pieChart.setUsePercentValues(true);

        pieChart.getDescription().setEnabled(false);

        pieChart.animateY(1200);

        // LEYENDA
        Legend legend = pieChart.getLegend();

        legend.setEnabled(true);

        legend.setFormSize(10f);

        legend.setVerticalAlignment(
                Legend.LegendVerticalAlignment.BOTTOM
        );

        legend.setHorizontalAlignment(
                Legend.LegendHorizontalAlignment.CENTER
        );

        pieChart.invalidate();
    }
}