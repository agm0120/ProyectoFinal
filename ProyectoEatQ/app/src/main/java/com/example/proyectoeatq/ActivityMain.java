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