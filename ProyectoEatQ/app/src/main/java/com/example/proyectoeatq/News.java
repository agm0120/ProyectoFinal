package com.example.proyectoeatq;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link News#newInstance} factory method to
 * create an instance of this fragment.
 */
public class News extends Fragment {

    // Parámetros de inicialización del fragmento
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // --- DECLARACIÓN DE COMPONENTES ---
    private Button btnInformate;
    private Button btnNoticiaActividad;
    private Button btnNoticiaBienestar;
    private ImageView imgPlato;
    private ImageView imgRunning;
    private ImageView imgYoga;

    public News() {
        // Required empty public constructor
    }

    public static News newInstance(String param1, String param2) {
        News fragment = new News();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Vincular componentes usando los IDs originales de tu XML
        btnInformate = view.findViewById(R.id.btn_informacion);
        btnNoticiaActividad = view.findViewById(R.id.btnVerNoticia1);
        btnNoticiaBienestar = view.findViewById(R.id.btnVerNoticia2);

        imgPlato = view.findViewById(R.id.imgMetodoPlato);
        imgRunning = view.findViewById(R.id.imgRunning);
        imgYoga = view.findViewById(R.id.imgYoga);

        // 2. Configurar la acción del botón "Infórmate"
        btnInformate.setOnClickListener(v -> {
            showPopupInformativo();
        });

        // 3. Configurar acciones para abrir las URLs de las noticias
        btnNoticiaActividad.setOnClickListener(v -> {
            abrirUrl("https://www.who.int/es/news-room/fact-sheets/detail/physical-activity");
        });

        btnNoticiaBienestar.setOnClickListener(v -> {
            abrirUrl("https://www.mayoclinic.org/es/healthy-lifestyle/stress-management/basics/stress-relief/hlv-20049495");
        });
    }

    // Método auxiliar para abrir URLs externas con control de errores UX (try-catch)
    private void abrirUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));

        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            // Si el móvil no tiene navegador instalado, capturamos el error y avisamos al usuario
            android.widget.Toast.makeText(getContext(), "No hay ninguna aplicación disponible para abrir este enlace", android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    // Método para mostrar el AlertDialog con la información detallada
    private void showPopupInformativo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("El Método del Plato");

        String mensajeInformativo = "El Método del Plato es una guía visual muy sencilla para preparar comidas sanas y equilibradas.\n\n" +
                "¿En qué consiste?\n\n" +
                "🥗 50% Vegetales y Frutas:\n" +
                "Ocupan la mitad del plato. Aportan vitaminas, minerales y fibra. ¡Cuanto más color y variedad, mejor! (Ojo: las patatas no cuentan en esta mitad).\n\n" +
                "🥩 25% Proteínas Saludables:\n" +
                "Un cuarto del plato. Elige pescados, aves, legumbres (como lentejas o garbanzos) y frutos secos. Es recomendable limitar las carnes rojas y evitar los embutidos.\n\n" +
                "🌾 25% Carbohidratos Complejos:\n" +
                "El último cuarto. Prioriza granos integrales como arroz integral, quinoa, avena o pasta de trigo integral, ya que mantienen estables los niveles de energía.\n\n" +
                "💧 Extra:\n" +
                "Acompaña tus comidas con agua y utiliza aceites saludables, como el aceite de oliva virgen extra.";

        builder.setMessage(mensajeInformativo);
        builder.setPositiveButton("Entendido", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}