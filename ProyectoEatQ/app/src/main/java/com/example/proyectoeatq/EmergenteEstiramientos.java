package com.example.proyectoeatq;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


public class EmergenteEstiramientos extends DialogFragment {

   private CheckBox check_brazos, check_piernas, check_torso;
   private EditText et_notaUsuario;
   private Button btn_guardarEstirar;
   private GestorFirebase gestorDB;

    public EmergenteEstiramientos() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            gestorDB = new GestorFirebase(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.dialog_estiramientos, container, false);

            check_brazos = v.findViewById(R.id.check_brazos);
            check_piernas = v.findViewById(R.id.check_piernas);
            check_torso = v.findViewById(R.id.check_torso);
            et_notaUsuario = v.findViewById(R.id.et_notaUsuario);
            btn_guardarEstirar = v.findViewById(R.id.btn_guardarEstirar);

            btn_guardarEstirar.setOnClickListener(view ->{
                String ejercicio = "Estirar";

                //Contruimos una cadena de partes del cuerpo que se han estirado, separadas por espacios
                StringBuilder partesCuerpo = new StringBuilder();
                if (check_brazos.isChecked()) partesCuerpo.append("Brazos, ");
                if (check_piernas.isChecked()) partesCuerpo.append("Piernas, ");
                if (check_torso.isChecked()) partesCuerpo.append("Torso y espalda, ");

                String resultadoPartes = partesCuerpo.toString();

                //Quitamos la última coma y espacio si es que hay
                if(resultadoPartes.endsWith(", ")){
                    resultadoPartes = resultadoPartes.substring(0, resultadoPartes.length() - 2);
                }

                //Filtro para evitar que guarde si no se ha seleccionado ninguna parte del cuerpo
                if(resultadoPartes.isEmpty()){
                    Toast.makeText(getContext(), "Por favor, selecciona al menos una parte del cuerpo.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String nota = et_notaUsuario.getText().toString();
                prepararEjercicio(ejercicio, resultadoPartes, nota);

            });

        // Inflate the layout for this fragment
        return v;
    }

    private void prepararEjercicio(String ejercicio, String resultadoPartes, String nota) {

        //Preparamos un mapa con los datos del ejercicio
        Map<String, Object> datosDeporte = new HashMap<>();
        datosDeporte.put("Ejercicio", ejercicio);
        datosDeporte.put("Contador", resultadoPartes);
        datosDeporte.put("Anotación", nota);

        //Guardamos el mapa en la base de datos
        gestorDB.guardarDatos("Deporte", datosDeporte);

        //Cerramos el diálogo después de guardar
        dismiss();
    }
}