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

    View btn_introducirPlato, btn_verRecetas;


    public Resumen() {
        // Constructor vacío requerido
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos el layout
        View view = inflater.inflate(R.layout.fragment_resumen, container, false);

        // Cargo el botón y le asigno el listener para abrir el siguiente fragment
        btn_introducirPlato = view.findViewById(R.id.btn_introducirPlato);
        btn_verRecetas = view.findViewById(R.id.btn_verRecetas);

        btn_introducirPlato.setOnClickListener(v -> {
            navegar(new SubirPlato());
        });

        if(btn_verRecetas != null){
            btn_verRecetas.setOnClickListener(v -> {
                navegar(new Recetas());
            });
        }

        return view;
    }

    private void navegar(Fragment fragmento){
        if(getActivity() instanceof ActivityMain){
            ((ActivityMain) getActivity()).cambiarFragment(fragmento);
        }

    }

    /*
    private void abrirSiguienteFragment() {
        // Reemplaza 'OtroFragment' por el nombre real de tu clase destino
        Fragment nuevoFragment = new SubirPlato();

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, nuevoFragment) // R.id.container es el ID del FrameLayout en tu Activity principal
                .addToBackStack(null) // Permite volver atrás
                .commit();
    }
    */

}