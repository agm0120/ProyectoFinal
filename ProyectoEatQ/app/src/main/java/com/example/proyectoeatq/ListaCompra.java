package com.example.proyectoeatq;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListaCompra extends Fragment {

    private Button btn_añadir;
    private EditText textoArticulo;
    private RecyclerView rv_lista;

    List<String> listaCompra = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* El onCreate se podrá utiilzar para:
        - Recibir argumentos con getArguments()
        - Inicializar ViewModels
        - Configurar cosas que no dependan de la vista
         */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      // Se usa solo para inflar el layout
        return inflater.inflate(R.layout.fragment_lista_compra, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedIntanceState){
        // Se usará para todo lo que tenga que ver con la UI
        super.onViewCreated(view,savedIntanceState);

        initUI(view);


    }

    private void initUI(View view) {
        initView(view);
        initListeners();
    }

    private void initListeners() {
        btn_añadir.setOnClickListener(view -> añadirItem()); //forma abreviada
    }

    private void añadirItem() {
        String item = textoArticulo.toString();
        listaCompra.add(item);
    }

    //Método para inicializar los componentes
    private void initView(View view) {
        btn_añadir = view.findViewById(R.id.btn_añadir);
        textoArticulo = view.findViewById(R.id.textoArticulo);
        rv_lista = view.findViewById(R.id.rv_lista);
    }






}