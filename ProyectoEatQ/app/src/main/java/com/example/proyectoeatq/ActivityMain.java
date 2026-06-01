package com.example.proyectoeatq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

// --- AÑADIDO PARA EL MENÚ LATERAL: Importaciones necesarias ---
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
// --------------------------------------------------------------

public class ActivityMain extends AppCompatActivity {

    private FloatingActionButton bPlato;
    private ImageButton bCompra, bEjercicio, bRecetas, bNoticias;

    private TextView cabecera;

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

        cabecera = findViewById(R.id.tv_tituloCabecera);

        // --- AÑADIDO PARA EL MENÚ LATERAL: Inicializar vistas y clics ---
        bPerfil = findViewById(R.id.btn_perfilUsuario);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        actualizarHeaderNavigation();

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
                cambiarFragment(new InformacionUsuario(), "Mi perfil");

            } else if (id == R.id.nav_historial) { // Ojo al nombre en tu XML: nav_hitorial
                // TODO: Crear el fragmento de historial y cambiarlo aquí
                cambiarFragment(new Historial(), "Historial");

            } else if (id == R.id.nav_contactos) {
                cambiarFragment(new Contacto(), "Contacto");

            } else if (id == R.id.nav_config) {
                //TODO: Crear el fragmento de configuración y cambiarlo aquí
                cambiarFragment(new Configuracion(), "Configuración");

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
            cambiarFragment(new Resumen(), "EatQ");
            cambiarColorActivo(null);
        }

        bCompra.setOnClickListener(v -> {
            cambiarFragment(new ListaCompra(), "Lista Compra");
            cambiarColorActivo(bCompra);
        });

        bEjercicio.setOnClickListener(v -> {
            cambiarFragment(new Ejercicios(), "Ejercicios");
            cambiarColorActivo(bEjercicio);
        });

        bRecetas.setOnClickListener(v -> {
            cambiarFragment(new Recetas(), "Recetas");
            cambiarColorActivo(bRecetas);
        });

        bNoticias.setOnClickListener(v -> {
            cambiarFragment(new News(), "Noticias");
            cambiarColorActivo(bNoticias);
        });

        bPlato.setOnClickListener(v -> {
            cambiarFragment(new Resumen(), "EatQ");
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

            Fragment fragmentoActual = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
            if (fragmentoActual != null && cabecera != null) {

                if (fragmentoActual instanceof Resumen) {
                    cabecera.setText("EatQ"); // O "Inicio" / "Resumen"
                } else if (fragmentoActual instanceof ListaCompra) {
                    cabecera.setText("Lista de la Compra");
                } else if (fragmentoActual instanceof Ejercicios) {
                    cabecera.setText("Ejercicios");
                } else if (fragmentoActual instanceof Recetas) {
                    cabecera.setText("Recetas");
                } else if (fragmentoActual instanceof News) {
                    cabecera.setText("Noticias");
                } else if (fragmentoActual instanceof InformacionUsuario) {
                    cabecera.setText("Mi Perfil");
                } else if (fragmentoActual instanceof Historial) {
                    cabecera.setText("Historial");
                } else if (fragmentoActual instanceof Contacto) {
                    cabecera.setText("Contacto");
                } else if (fragmentoActual instanceof Configuracion) {
                    cabecera.setText("Configuración");
                } else if (fragmentoActual instanceof SubirPlato) {
                    cabecera.setText("Plato");
                }
            }
        });

    }

    public void cambiarFragment (Fragment fragmento, String titulo){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Nuevo fragment a mostrar
        fragmentTransaction.replace(R.id.fragmentContainerView, fragmento);

        // Añade el cambio al historial (pila)
        fragmentTransaction.addToBackStack(null);

        // Confirmar el cambio
        fragmentTransaction.commit();

        // Cambiar cabecera de forma dinamica
        if (cabecera != null && titulo != null) {
            cabecera.setText(titulo);
        }

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

        // 2. Verde oscuro específico solicitado para el fondo activo
        int colorFondoActivo = android.graphics.Color.parseColor("#036607"); // <================ COLOR VERDE DEL XML ONCHECK
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

    // Metodo para mostrar el correo y nombre del usuario en el sidecar
    public void actualizarHeaderNavigation() {
        if (navigationView != null) {
            View headerView = navigationView.getHeaderView(0);
            TextView tvNombre = headerView.findViewById(R.id.tv_nombre_sidebar);
            TextView tvEmail = headerView.findViewById(R.id.tv_email_sidebar);

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Seguimos necesitando el UID de Auth solo para saber QUÉ documento buscar
            if (mAuth.getCurrentUser() != null) {
                String uid = mAuth.getCurrentUser().getUid();

                db.collection("Usuario").document(uid).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                // Extraemos ambos campos directamente de Firestore
                                String nombre = documentSnapshot.getString("nombreUsuario");
                                String email = documentSnapshot.getString("correo");

                                tvNombre.setText(nombre != null ? nombre : "Usuario");
                                tvEmail.setText(email != null ? email : "Sin correo");

                                android.util.Log.d("SIDEBAR", "Datos cargados desde Firestore");
                            }
                        })
                        .addOnFailureListener(e -> android.util.Log.e("SIDEBAR", "Error: " + e.getMessage()));
            }
        }
    }

}