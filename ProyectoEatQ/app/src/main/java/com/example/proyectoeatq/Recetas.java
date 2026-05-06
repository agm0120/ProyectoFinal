package com.example.proyectoeatq;

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
import android.widget.Toast;

import com.example.proyectoeatq.ControlRecetas.Receta;
import com.example.proyectoeatq.ControlRecetas.RecetasAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Recetas extends Fragment {

    private RecyclerView rv_recetas;
    private Button btn_hidratos, btn_proteinas, btn_vegetales;
    private RecetasAdapter adapter;
    private List<Receta> listaRecetas = new ArrayList<>();

    // Mi referencia a la base de datos de Firebase
    private DatabaseReference mDatabase;

    public Recetas() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recetas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializo Firebase apuntando al nodo "recetas"
        mDatabase = FirebaseDatabase.getInstance().getReference("recetas");

        initUI(view);
    }

    private void initUI(View view) {
        initView(view);
        initRecyclerView();
        initListeners();

        // Escucho a Firebase
        cargarRecetasDesdeFirebase();
    }

    private void initView(View view) {

        rv_recetas = view.findViewById(R.id.rv_recetas);
        btn_hidratos = view.findViewById(R.id.btn_hidratos);
        btn_proteinas = view.findViewById(R.id.btn_proteinas);
        btn_vegetales = view.findViewById(R.id.btn_vegetales);
    }

    private void initRecyclerView() {
        rv_recetas.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecetasAdapter(listaRecetas);
        rv_recetas.setAdapter(adapter);
    }

    private void cargarRecetasDesdeFirebase() {
        // Este listener detecta cambios en tiempo real
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaRecetas.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Firebase mapea los datos directamente a mi clase Receta
                    Receta receta = dataSnapshot.getValue(Receta.class);
                    if (receta != null) {
                        listaRecetas.add(receta);
                    }
                }
                // Le aviso al adaptador para que pinte los nuevos datos
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initListeners() {
        // Preparado para filtrar en el futuro
        btn_hidratos.setOnClickListener(v -> { /* Filtrar por hidratos */ });
        btn_proteinas.setOnClickListener(v -> { /* Filtrar por proteinas */ });
        btn_vegetales.setOnClickListener(v -> { /* Filtrar por vegetales */ });
    }
}