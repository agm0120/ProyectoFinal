package com.example.proyectoeatq;

import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Recetas extends Fragment {

    private String filtroActual = "Todos";
    private RecyclerView rv_recetas;
    private Button btn_hidratos, btn_proteinas, btn_vegetales;
    private RecetasAdapter adapter;
    private List<Receta> listaRecetas = new ArrayList<>();
    private FirebaseFirestore db;

    public Recetas() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recetas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        initUI(view);

    }

    private void initUI(View view) {
        initView(view);
        initRecyclerView();
        initListeners();

        // Carga inicial de datos al entrar (mostramos todas)
        cargarRecetasDesdeFirestore("Todos");
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

    private void initListeners(){
        btn_hidratos.setOnClickListener(v -> gestionarFiltro("Carbohidratos"));
        btn_proteinas.setOnClickListener(v -> gestionarFiltro("Proteínas"));
        btn_vegetales.setOnClickListener(v -> gestionarFiltro("Vegetales"));
    }

    private void gestionarFiltro(String filtroSeleccionado) {
        if(filtroActual.equals(filtroSeleccionado)){
            // Si el filtro seleccionado es el mismo que el actual, lo desactivo y muestro todos.
            filtroActual = "Todos";
        } else {
            // Si es diferente, activo el nuevo filtro.
            filtroActual = filtroSeleccionado;
        }

        actualizarEstiloBotones();
        cargarRecetasDesdeFirestore(filtroActual);

    }

    private void actualizarEstiloBotones() {
        // Ponemos todos los botones en su estado "Off" por defecto
        btn_hidratos.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.hidratosOff)));
        btn_proteinas.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.proteinasOff)));
        btn_vegetales.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.vegetalesOff)));

        // Si el filtro actual es uno de los tres, encendemos su versión "On"
        switch(filtroActual){
            case "Carbohidratos":
                btn_hidratos.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.hidratosOn)));
                break;
            case "Proteínas":
                btn_proteinas.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.proteinasOn)));
                break;
            case "Vegetales":
                btn_vegetales.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.vegetalesOn)));
                break;
        }
    }

    private void cargarRecetasDesdeFirestore(String filtro) {
        db.collection("Recetas").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                listaRecetas.clear();
                for (QueryDocumentSnapshot document : task.getResult()){
                    Receta receta = document.toObject(Receta.class);
                    if(filtro.equals("Todos") || receta.getTipo().equalsIgnoreCase(filtro)){
                        listaRecetas.add(receta);
                    }
                }
                adapter.notifyDataSetChanged();
            }else {
                Toast.makeText(getContext(), "Error al cargar recetas: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}