package com.example.proyectoeatq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

// --- AÑADIDO PARA EL MENÚ LATERAL: Importaciones necesarias ---
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import android.content.Intent;
// --------------------------------------------------------------

public class ActivityMain extends AppCompatActivity {

    private FloatingActionButton bPlato;
    private ImageButton bCompra, bEjercicio, bRecetas, bNoticias;

    // --- AÑADIDO PARA EL MENÚ LATERAL: Variables ---
    private ImageButton bPerfil, btn_atrasMain;
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
        btn_atrasMain = findViewById(R.id.btn_atrasMain);

        // --- AÑADIDO PARA EL MENÚ LATERAL: Inicializar vistas y clics ---
        bPerfil = findViewById(R.id.btn_perfilUsuario);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Al pulsar tu foto de perfil, se abre el menú por la derecha
        bPerfil.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.END);
        });

        //Botón back
        btn_atrasMain.setOnClickListener(v -> {
            onBackPressed();
        });

        // Detectar clics dentro de las opciones del menú lateral
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_perfil) {
                // Asumiendo que tienes un fragmento llamado PerfilUsuario
                cambiarFragment(new InformacionUsuario());

            } else if (id == R.id.nav_historial) { // Ojo al nombre en tu XML: nav_hitorial
               // TODO: Crear el fragmento de historial y cambiarlo aquí
                cambiarFragment(new InformacionHistorial());

            } else if (id == R.id.nav_contactos) {
                cambiarFragment(new Contacto());

            } else if (id == R.id.nav_config) {
                //TODO: Crear el fragmento de configuración y cambiarlo aquí
                cambiarFragment(new Configuracion());

            } else if (id == R.id.nav_logout) {
                // 1. Cerramos sesión en Firebase
                FirebaseAuth.getInstance().signOut();

                // 2. Resetamos el preference de "Recuerdame"
                Preferences misPreferencias = new Preferences(ActivityMain.this);
                misPreferencias.setRecordar(false);

                //3. Volvemos al login
                Intent intent = new Intent(ActivityMain.this, ActivityMainLogin.class);

                // Limpiamos el historial de actividades para que el usuario
                //no pueda volver al Main pulsando Atrás
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                finish(); //cerramos la actividad
            }

            // Cierra el menú al pulsar una opción (usando END porque lo abres por la derecha)
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

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            int stackCount = getSupportFragmentManager().getBackStackEntryCount();
            //Si hay más de 1 fragmento, se muestra la flecha. si no, se oculta.
            if(stackCount > 1){
                btn_atrasMain.setVisibility(View.VISIBLE);
            }else {
                btn_atrasMain.setVisibility(View.GONE);
            }
        });

    }

    public void cambiarFragment (Fragment fragmento){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Nuevo fragment a mostrar
        fragmentTransaction.replace(R.id.fragmentContainerView, fragmento);

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