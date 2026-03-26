package com.example.proyectoeatq.DialogsEjercicios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.proyectoeatq.R;

public class DialogAndar extends DialogFragment {

    Button btn_guardarAndar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflamos el xml dialog_andar
        View view = inflater.inflate(R.layout.dialog_andar, container, false);

        btn_guardarAndar = view.findViewById(R.id.btn_guardarAndar);

        btn_guardarAndar.setOnClickListener(v ->{
            // TODO: ahora mismo solo cierra la ventana. Debe guardarlo en la base de datos
            dismiss();
        });

        return view;


    }

    @Override
    public void onStart() {
        super.onStart();
        // Establece el ancho del diálogo al 80% del ancho de la pantalla
        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
            getDialog().getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }


}
