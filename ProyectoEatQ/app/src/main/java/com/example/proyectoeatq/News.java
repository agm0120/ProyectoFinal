package com.example.proyectoeatq;

import android.app.AlertDialog;
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

    public News() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment News.
     */
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

        // 1. Vincular componentes usando los IDs exactos del XML
        Button btnInformate = view.findViewById(R.id.btn_informacion);
        ImageView imgPlato = view.findViewById(R.id.imgMetodoPlato);
        ImageView imgRunning = view.findViewById(R.id.imgRunning);
        ImageView imgYoga = view.findViewById(R.id.imgYoga);

        // 2. Asignar imágenes (Asegúrate de que los nombres coincidan con los de tu carpeta res/drawable)
        // Ejemplo: R.drawable.metodo_plato, R.drawable.running_news, R.drawable.yoga_relax
        // imgPlato.setImageResource(R.drawable.metodo_plato);
        // imgRunning.setImageResource(R.drawable.running_news);
        // imgYoga.setImageResource(R.drawable.yoga_relax);

        // 3. Configurar la acción del botón "Infórmate"
        btnInformate.setOnClickListener(v -> {
            showPopupInformativo();
        });
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