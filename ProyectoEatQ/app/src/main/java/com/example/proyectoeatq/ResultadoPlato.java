package com.example.proyectoeatq;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.json.JSONObject;

import java.util.ArrayList;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResultadoPlato extends Fragment {

    private String infoIA;
    private TextView tvResultado;
    private PieChart pieChart;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private Button buttonGuardar;

    public ResultadoPlato() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            infoIA = getArguments().getString("info_ia");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resultado, container, false);

        tvResultado = view.findViewById(R.id.textView5);
        pieChart = view.findViewById(R.id.pieChart_comida);
        buttonGuardar = view.findViewById(R.id.btn_guardarPlato);

        auth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        String uid = auth.getCurrentUser().getUid();

        if (infoIA != null) {
            procesarYMostrarDatos(infoIA, uid);
        } else {
            tvResultado.setText("No se recibió información de la IA.");
        }

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                // 1. Limpieza de Markdown
                String jsonLimpio = infoIA.replaceAll("```json", "")
                        .replaceAll("```", "")
                        .trim();

                JSONObject jsonObject = new JSONObject(jsonLimpio);

                // 2. EXTRAER VALORES (Corregido para tu JSON anidado)
                if (!jsonObject.has("porcentajes")) {
                    throw new Exception("El JSON no contiene la llave 'porcentajes'");
                }

                JSONObject porcentajes = jsonObject.getJSONObject("porcentajes");

                // Usamos optDouble para evitar cierres si falta una llave específica
                // Revisa que estas llaves ("vegetales", "proteinas", etc.) coincidan exactamente con tu JSON
                float vegetales = (float) porcentajes.optDouble("vegetales", 0);
                float proteinas = (float) porcentajes.optDouble("proteinas", 0);
                float carbohidratos = (float) porcentajes.optDouble("carbohidratos", 0); // Ajusté esta llave

                String analisis = jsonObject.getString("analisis_detallado");

                guardarPlato(uid, analisis, vegetales, proteinas, carbohidratos);

                } catch (Exception e) {
                    Log.e("JSON_ERROR", "Error al parsear: " + e.getMessage());
                    tvResultado.setText("Ocurrió un error al procesar los resultados.");
                    Toast.makeText(getContext(), "Error en el formato de datos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void abrirSiguienteFragment() {
        Fragment nuevoFragment = new Resumen();

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, nuevoFragment)
                .addToBackStack(null)
                .commit();
    }

    private void procesarYMostrarDatos(String rawJson, String uid) {
        try {
            Log.d("JSON_IA_RAW", rawJson);
            // 1. Limpieza de Markdown
            String jsonLimpio = rawJson.replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            JSONObject jsonObject = new JSONObject(jsonLimpio);

            // 2. EXTRAER VALORES (Corregido para tu JSON anidado)
            if (!jsonObject.has("porcentajes")) {
                throw new Exception("El JSON no contiene la llave 'porcentajes'");
            }

            JSONObject porcentajes = jsonObject.getJSONObject("porcentajes");

            // Usamos optDouble para evitar cierres si falta una llave específica
            // Revisa que estas llaves ("vegetales", "proteinas", etc.) coincidan exactamente con tu JSON
            float vegetales = (float) porcentajes.optDouble("vegetales", 0);
            float proteinas = (float) porcentajes.optDouble("proteinas", 0);
            float carbohidratos = (float) porcentajes.optDouble("carbohidratos", 0); // Ajusté esta llave


            // --- Mostrar el análisis de texto formateado (No el JSON crudo) ---
            if (jsonObject.has("analisis_detallado")) {
                tvResultado.setText(jsonObject.getString("analisis_detallado"));
            } else {
                tvResultado.setText(jsonObject.toString(4));
            }

            // 3. Configurar el gráfico con los datos extraídos
            configurarGrafico(vegetales, proteinas, carbohidratos);

        } catch (Exception e) {
            Log.e("JSON_ERROR", "Error al parsear: " + e.getMessage());
            tvResultado.setText("Ocurrió un error al procesar los resultados.");
            Toast.makeText(getContext(), "Error en el formato de datos", Toast.LENGTH_SHORT).show();
        }
    }

    private void configurarGrafico(float veg, float prot, float carb) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // Solo añadir si el porcentaje es mayor a cero
        if (veg > 0) entries.add(new PieEntry(veg, "Vegetales"));
        if (prot > 0) entries.add(new PieEntry(prot, "Proteínas"));
        if (carb > 0) entries.add(new PieEntry(carb, "Carbohidratos"));

        PieDataSet dataSet = new PieDataSet(entries, "");

        // COLORES PERSONALIZADOS (Más estéticos que los por defecto)
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.verdeGrafico));
        colors.add(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.rojoGrafico));
        colors.add(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.amarilloGrafico));
        dataSet.setColors(colors);

        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(14f);
        // Formatear valores para que muestren el símbolo de porcentaje (%)
        dataSet.setValueFormatter(new PercentFormatter(pieChart));

        PieData data = new PieData(dataSet);

        // Configuración estética general del gráfico
        pieChart.setData(data);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setUsePercentValues(true); // Usar valores relativos (%)
        pieChart.getDescription().setEnabled(false); //
        pieChart.animateY(1200); // Animación suave

        // Configurar Leyenda
        pieChart.getLegend().setEnabled(true);
        pieChart.getLegend().setFormSize(10f);
        pieChart.getLegend().setVerticalAlignment(com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.BOTTOM);
        pieChart.getLegend().setHorizontalAlignment(com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER);

        pieChart.invalidate(); // Refrescar gráfico
    }

    private void guardarPlato(String uid, String analisisTexto, float veg, float prot, float carb) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> datosComida = new HashMap<>();
        datosComida.put("analisis", analisisTexto);
        datosComida.put("fecha", new Timestamp(new Date()));

        Map<String, Object> porcentajes = new HashMap<>();
        porcentajes.put("vegetales", veg);
        porcentajes.put("proteinas", prot);
        porcentajes.put("carbohidratos", carb);

        datosComida.put("porcentajes", porcentajes);
        datosComida.put("usuario", uid);

        db.collection("Comida").add(datosComida)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Plato registrado",
                            Toast.LENGTH_SHORT).show();
                    abrirSiguienteFragment();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al guardar plato",
                            Toast.LENGTH_SHORT).show();
                });
    }
}