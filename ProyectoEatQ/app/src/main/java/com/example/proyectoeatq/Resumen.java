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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class Resumen extends Fragment {

    private View btn_introducirPlato, btn_verRecetas;
    private TextView infoResumen;
    private PieChart pieChart;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private String filtroRecetas = "Todos";

    public Resumen() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_resumen, container, false);

        pieChart = view.findViewById(R.id.pieChart_resumen);
        infoResumen = view.findViewById(R.id.tv_infoResumen);

        btn_introducirPlato = view.findViewById(R.id.btn_introducirPlato);
        btn_verRecetas = view.findViewById(R.id.btn_verRecetas);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btn_introducirPlato.setOnClickListener(v -> navegar(new SubirPlato(), "Plato"));

        btn_verRecetas.setOnClickListener(v -> {
            Recetas fragmentRecetas = new Recetas();

            // Creamos la "mochila" (Bundle) y metemos el filtro
            Bundle args = new Bundle();
            args.putString("categoria_filtro", filtroRecetas);
            fragmentRecetas.setArguments(args);

            // Navegamos pasando el fragmento ya preparado con sus datos
            navegar(fragmentRecetas, "Recetas");
        });

        cargarResumenSemanal();

        return view;
    }

    private void cargarResumenSemanal() {

        String uid = auth.getCurrentUser().getUid();

        // FECHA HACE 7 DÍAS
        Calendar inicio = Calendar.getInstance();
        inicio.add(Calendar.DAY_OF_YEAR, -7);

        Calendar fin = Calendar.getInstance();

        db.collection("Comida")
                .whereEqualTo("usuario", uid)
                .whereGreaterThanOrEqualTo("fecha", inicio.getTime())
                .whereLessThanOrEqualTo("fecha", fin.getTime())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    float totalVeg = 0;
                    float totalProt = 0;
                    float totalCarb = 0;

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                        Map<String, Object> porcentajes =
                                (Map<String, Object>) document.get("porcentajes");

                        if (porcentajes != null) {

                            Number veg = (Number) porcentajes.get("vegetales");
                            Number prot = (Number) porcentajes.get("proteinas");
                            Number carb = (Number) porcentajes.get("carbohidratos");

                            totalVeg += veg != null ? veg.floatValue() : 0f;
                            totalProt += prot != null ? prot.floatValue() : 0f;
                            totalCarb += carb != null ? carb.floatValue() : 0f;
                        }
                    }

                    Log.d("FIREBASE",
                            "Veg: " + totalVeg
                                    + " Prot: " + totalProt
                                    + " Carb: " + totalCarb);

                    String mensaje;

                    // 1. Calculamos el total de nutrientes
                    float totalAlimentos = totalVeg + totalProt + totalCarb;

                    // Evitamos dividir por cero si el resumen está vacío
                    if (totalAlimentos == 0) {
                        mensaje = "Aún no has registrado ningún plato";
                    } else {
                        // 2. Calculamos el porcentaje real de cada grupo en el plato
                        float pctVeg = totalVeg / totalAlimentos;
                        float pctProt = totalProt / totalAlimentos;
                        float pctCarb = totalCarb / totalAlimentos;

                        // 3. Evaluamos según el Método del Plato (Ideales: Veg 0.50, Prot 0.25, Carb 0.25)
                        // Usamos un margen de tolerancia para que no tenga que ser exacto al milímetro
                        if (pctVeg < 0.40) {
                            // Si los vegetales bajan del 40% (el ideal es 50%)
                            mensaje = "Tu consumo de vegetales es bajo. Procura consumir más vegetales.";
                            filtroRecetas="Vegetales";
                        } else if (pctProt < 0.20) {
                            // Si la proteína baja del 20% (el ideal es 25%)
                            mensaje = "Tu consumo de proteínas es bajo. Procura consumir más proteinas.";
                            filtroRecetas="Proteínas";
                        } else if (pctCarb < 0.20) {
                            // Si los carbohidratos bajan del 20% (el ideal es 25%)
                            mensaje = "Tu consumo de carbohidratos es bajo. Procura consumir más carbohidratos.";
                            filtroRecetas="Carbohidratos";
                        } else {
                            mensaje = "¡Excelente! Estas siguiendo las proporciones del Método del Plato.";
                            filtroRecetas="Todos";
                        }
                    }

                    configurarGrafico(totalVeg, totalProt, totalCarb);

                    infoResumen.setText(mensaje);

                });
    }

    private void configurarGrafico(float veg, float prot, float carb) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        if (veg > 0) entries.add(new PieEntry(veg, "Vegetales"));
        if (prot > 0) entries.add(new PieEntry(prot, "Proteínas"));
        if (carb > 0) entries.add(new PieEntry(carb, "Carbohidratos"));

        PieDataSet dataSet = new PieDataSet(entries, "");

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.verdeGrafico));
        colors.add(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.rojoGrafico));
        colors.add(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.amarilloGrafico));

        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(14f);
        dataSet.setValueFormatter(new PercentFormatter(pieChart));

        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1200);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        pieChart.invalidate();
    }

    private void navegar(Fragment fragmento, String titulo){
        if(getActivity() instanceof ActivityMain){
            ((ActivityMain) getActivity()).cambiarFragment(fragmento, titulo);
        }
    }
}