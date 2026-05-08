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

        btn_introducirPlato.setOnClickListener(v -> navegar(new SubirPlato()));

        btn_verRecetas.setOnClickListener(v -> navegar(new Recetas()));

        cargarResumenSemanal(); // 👈 IMPORTANTE

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

                    if (totalVeg < totalProt * 0.8 && totalVeg < totalCarb * 0.8) {
                        mensaje = "Tu consumo de vegetales es bajo";
                    } else if (totalProt < totalVeg * 0.8 && totalProt < totalCarb * 0.8) {
                        mensaje = "Tu consumo de proteínas es bajo";
                    } else if (totalCarb < totalVeg * 0.8 && totalCarb < totalProt * 0.8) {
                        mensaje = "Tu consumo de carbohidratos es bajo";
                    } else {
                        mensaje = "Tu dieta está bastante equilibrada";
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
        colors.add(Color.parseColor("#4CAF50"));
        colors.add(Color.parseColor("#F44336"));
        colors.add(Color.parseColor("#FFC107"));

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

    private void navegar(Fragment fragmento){
        if(getActivity() instanceof ActivityMain){
            ((ActivityMain) getActivity()).cambiarFragment(fragmento);
        }
    }
}