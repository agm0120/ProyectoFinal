package com.example.proyectoeatq;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;

public class Recetas extends Fragment {

    private RecyclerView rv_recetas;
    private Button btn_hidratos, btn_proteinas, btn_vegetales;

    // COMENTADO: He comentado el adaptador y la lista porque dependen de clases que aún dan error y no estan creadas dentro del proyecto
    // private RecetasAdapter adapter;
    // private List<Receta> listaRecetas = new ArrayList<>();

    public Recetas() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recetas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
    }

    private void initUI(View view) {
        initView(view);
        initRecyclerView();
        initListeners();
    }

    private void initView(View view) {
        rv_recetas = view.findViewById(R.id.rv_recetas);
        btn_hidratos = view.findViewById(R.id.btn_hidratos);
        btn_proteinas = view.findViewById(R.id.btn_proteinas);
        btn_vegetales = view.findViewById(R.id.btn_vegetales);
    }

    private void initRecyclerView() {
        /* COMENTADO POR AHORA PARA EVITAR ERRORES
        llenarRecetasDePrueba();

        rv_recetas.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RecetasAdapter(listaRecetas);
        rv_recetas.setAdapter(adapter);
        */
    }

    private void llenarRecetasDePrueba() {
        /* COMENTADO: La clase Receta aún no está lista
        listaRecetas.clear();
        listaRecetas.add(new Receta("Plato combinado de Pasta y Pollo a la Parrilla.", Color.parseColor("#F9F5C6"), android.R.drawable.ic_menu_gallery));
        listaRecetas.add(new Receta("Bowl de Verduras Asadas.", Color.parseColor("#D5F5CD"), android.R.drawable.ic_menu_gallery));
        listaRecetas.add(new Receta("Dúo de Proteínas a la Plancha con Quinoa.", Color.parseColor("#C8EBF7"), android.R.drawable.ic_menu_gallery));
        */
    }

    private void initListeners() {
        btn_hidratos.setOnClickListener(v -> { });
        btn_proteinas.setOnClickListener(v -> { });
        btn_vegetales.setOnClickListener(v -> { });
    }
}