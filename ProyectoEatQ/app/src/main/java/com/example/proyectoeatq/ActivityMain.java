package com.example.proyectoeatq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

// --- AÑADIDO PARA EL MENÚ LATERAL: Importaciones necesarias ---
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
// --------------------------------------------------------------

public class ActivityMain extends AppCompatActivity {

    private FloatingActionButton bPlato;
    private ImageButton bCompra, bEjercicio, bRecetas, bNoticias;

    // --- AÑADIDO PARA EL MENÚ LATERAL: Variables ---
    private ImageButton bPerfil;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    // -----------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bPlato = findViewById(R.id.btn_plato);
        bCompra = findViewById(R.id.btn_listaCompra);
        bEjercicio = findViewById(R.id.btn_ejercicio);
        bRecetas = findViewById(R.id.btn_recetas);
        bNoticias = findViewById(R.id.btn_noticias);

        // --- AÑADIDO PARA EL MENÚ LATERAL: Inicializar vistas y clics ---
        bPerfil = findViewById(R.id.btn_perfilUsuario);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Al pulsar tu foto de perfil, se abre el menú por la derecha
        bPerfil.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.END);
        });

        // Detectar clics dentro de las opciones del menú lateral
        navigationView.setNavigationItemSelectedListener(item -> {
            // Cierra el menú al pulsar una opción
            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
        // ----------------------------------------------------------------

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
        fragmentTransaction.replace(R.id.fcv_contenedor_principal, fragmento);

        // Añade el cambio al historial (pila)
        fragmentTransaction.addToBackStack(null);

        // Confirmar el cambio
        fragmentTransaction.commit();

    }

    // --- AÑADIDO PARA EL MENÚ LATERAL: Botón "Atrás" del móvil ---
    // Si el menú está abierto y el usuario le da a atrás, se cierra el menú en vez de salir de la app
    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }
    // -------------------------------------------------------------
}