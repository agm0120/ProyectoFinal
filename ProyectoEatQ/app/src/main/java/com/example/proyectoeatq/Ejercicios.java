package com.example.proyectoeatq;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.example.proyectoeatq.DialogsEjercicios.DialogAndar;
import com.example.proyectoeatq.DialogsEjercicios.DialogEscaleras;
import com.example.proyectoeatq.DialogsEjercicios.DialogEstirar;
import com.example.proyectoeatq.DialogsEjercicios.DialogSentadillas;

public class Ejercicios extends Fragment {

    Switch switch_andar, switch_escaleras, switch_estirar, switch_sentadillas;

    public Ejercicios() {
        // Required empty public constructor
    }


    public static Ejercicios newInstance(String param1, String param2) {
        Ejercicios fragment = new Ejercicios();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflamos el layout y lo guardamos en una variable para poder usarlo
        View view = inflater.inflate(R.layout.fragment_ejercicios, container, false);

        switch_andar = view.findViewById(R.id.switch_andar);
        switch_escaleras = view.findViewById(R.id.switch_escaleras);
        switch_estirar = view.findViewById(R.id.switch_estirar);
        switch_sentadillas = view.findViewById(R.id.switch_sentadillas);

        if(switch_andar != null){
            switch_andar.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked){
                    // Si el switch se activa, mostramos el diálogo
                    EmergenteAndar em_andar = new EmergenteAndar();
                    em_andar.show(getParentFragmentManager(), "EmergenteAndar");
                }
            });
        }

        if(switch_escaleras != null){
            switch_escaleras.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked){
                    // Si el switch se activa, mostramos el diálogo
                    EmergenteEscaleras em_escaleras = new EmergenteEscaleras();
                    em_escaleras.show(getParentFragmentManager(), "EmergenteEscaleras");
                }
            });
        }

        if(switch_sentadillas != null){
            switch_sentadillas.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked){
                    // Si el switch se activa, mostramos el diálogo
                    EmergenteSentadillas em_sentadillas = new EmergenteSentadillas();
                    em_sentadillas.show(getParentFragmentManager(), "EmergenteSentadillas");
                }
            });
        }

        if(switch_estirar != null){
            switch_estirar.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked){
                    // Si el switch se activa, mostramos el diálogo
                    EmergenteEstiramientos em_estiramientos = new EmergenteEstiramientos();
                    em_estiramientos.show(getParentFragmentManager(), "EmergenteEstiramientos");
                }
            });
        }

        return view;
    }
}