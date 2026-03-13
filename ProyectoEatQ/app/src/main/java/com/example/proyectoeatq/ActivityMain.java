package com.example.proyectoeatq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ActivityMain extends AppCompatActivity {

    private FloatingActionButton bPlato;
    private ImageButton bCompra, bEjercicio, bRecetas, bNoticias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bPlato = findViewById(R.id.fabEatQ);
        bCompra = findViewById(R.id.btnMenuCarrito);
        bEjercicio = findViewById(R.id.btnMenuZapatillas);
        bRecetas = findViewById(R.id.btnMenuCocinero);
        bNoticias = findViewById(R.id.btnMenuNoticias);

        // Fragment que se cargara por defecto
        if (savedInstanceState == null){
            cambiarFragment(new Resumen());
        }

        bCompra.setOnClickListener(v -> {
            cambiarFragment(new ListaCompra());
        });

        bEjercicio.setOnClickListener(v -> {
            cambiarFragment(new Ejercicios());
        });

        bRecetas.setOnClickListener(v -> {
            cambiarFragment(new Recetas());
        });

        bNoticias.setOnClickListener(v -> {
            cambiarFragment(new News());
        });

        bPlato.setOnClickListener(v -> {
            cambiarFragment(new Resumen());
        });

    }

    private void cambiarFragment (Fragment fragmento){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Nuevo fragment a mostrar
        fragmentTransaction.replace(R.id.fragmentContainerView, fragmento);

        // Añade el cambio al historial (pila)
        fragmentTransaction.addToBackStack(null);

        // Confirmar el cambio
        fragmentTransaction.commit();

    }

}