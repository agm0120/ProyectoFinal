package com.example.proyectoeatq;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectoeatq.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.example.proyectoeatq.Resumen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Resumen extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Resumen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters
    public static com.example.proyectoeatq.Resumen newInstance(String param1, String param2) {
        com.example.proyectoeatq.Resumen fragment = new com.example.proyectoeatq.Resumen();
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

        View view = inflater.inflate(R.layout.fragment_resumen, container, false);

        View boton = view.findViewById(R.id.btn_introducirPlato);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirSiguienteFragment();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void abrirSiguienteFragment() {
        // Reemplaza 'OtroFragment' por el nombre real de tu clase destino
        Fragment nuevoFragment = new SubirPlato();

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, nuevoFragment) // R.id.container es el ID del FrameLayout en tu Activity principal
                .addToBackStack(null) // Permite volver atrás
                .commit();
    }
}