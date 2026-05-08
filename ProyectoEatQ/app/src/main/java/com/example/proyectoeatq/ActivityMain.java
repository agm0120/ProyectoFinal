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
                // Lógica de salida
                finish();
            }

            // Cierra el menú al pulsar una opción (usando END porque lo abres por la derecha)
            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });
        // ----------------------------------------------------------------

        // Fragment que se cargara por defecto
        if (savedInstanceState == null){
            cambiarFragment(new Resumen());
            cambiarColorActivo(null);
        }

        bCompra.setOnClickListener(v -> {
            cambiarFragment(new ListaCompra());
            cambiarColorActivo(bCompra);
        });

        bEjercicio.setOnClickListener(v -> {
            cambiarFragment(new Ejercicios());
            cambiarColorActivo(bEjercicio);
        });

        bRecetas.setOnClickListener(v -> {
            cambiarFragment(new Recetas());
            cambiarColorActivo(bRecetas);
        });

        bNoticias.setOnClickListener(v -> {
            cambiarFragment(new News());
            cambiarColorActivo(bNoticias);
        });

        bPlato.setOnClickListener(v -> {
            cambiarFragment(new Resumen());
            cambiarColorActivo(null);
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
    private void cambiarColorActivo(ImageButton botonActivo) {
        // 1. Color transparente para que se vea el verde original de tu XML en el fondo inactivo
        int colorFondoNormal = android.graphics.Color.TRANSPARENT;
        // 2. Verde más claro para el fondo activo
        int colorFondoActivo = android.graphics.Color.parseColor("#4CAF50");
        // 3. Negro (color de línea original/inactivo)
        int colorIconoInactivo = android.graphics.Color.BLACK;
        // 4. Blanco (efecto de borde/silueta blanco)
        int colorIconoActivo = android.graphics.Color.WHITE;

        // Paso A: Reiniciar los 4 botones a su aspecto original e inactivo.
        resetearAspectoBoton(bCompra, colorFondoNormal, colorIconoInactivo);
        resetearAspectoBoton(bEjercicio, colorFondoNormal, colorIconoInactivo);
        resetearAspectoBoton(bRecetas, colorFondoNormal, colorIconoInactivo);
        resetearAspectoBoton(bNoticias, colorFondoNormal, colorIconoInactivo);

        // Paso B: Aplicar el aspecto activo SOLO al botón pulsado.
        if (botonActivo != null) {
            // Aplicamos el color de fondo plano, llenando todo el rectángulo sin redondear.
            botonActivo.setBackgroundColor(colorFondoActivo);

            // Aplicar el tinte BLANCO al icono
            botonActivo.setColorFilter(colorIconoActivo);
        }
    }

    // Este método asegura que limpiamos cualquier fondo complejo previo antes de aplicar uno simple.
    private void resetearAspectoBoton(ImageButton boton, int colorFondo, int colorIcono) {
        boton.setBackground(null);
        boton.setBackgroundColor(colorFondo);
        boton.setColorFilter(colorIcono);
    }

}