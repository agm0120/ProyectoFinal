package com.example.proyectoeatq;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.proyectoeatq.ControListaCompra.ListaAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListaCompra extends Fragment {

    private Button btn_añadir; // ? btnAddTask
    private EditText textoArticulo; //? etTask
    private RecyclerView rv_lista; //? rvTasks
    //creamos la instancia del ListaAdapter
    //vamos a ir llamándolo cada vez que modifiquemos la lista de la compra
    private ListaAdapter adapter;  //? adapter

    List<String> listaCompra = new ArrayList<>(); //? tasks

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
        initRecyclerView();
    }

    /* Función que va a configurar el RecyclerView para añadirle
    el adapter y que funcione todo el fujo
     */
    private void initRecyclerView() {
        //el layoutmanager es el encargado de mostrar cómo se van a mostrar las vistas
        //va aser un linearlayoutmanager, y le pasamos el contexto de fragment
        rv_lista.setLayoutManager(new LinearLayoutManager(getContext()));
        //va a recibir el listado de la compra (arraylist)
        adapter = new ListaAdapter(listaCompra);
        //le decimos que el adapter que va a usar es el que acabamos de crear.
        rv_lista.setAdapter(adapter);
    }

    private void initListeners() {
        btn_añadir.setOnClickListener(view -> añadirItem()); //forma abreviada
    }

    private void añadirItem() { //? addTask()
        String item = textoArticulo.getText().toString().trim();
        if(!item.isEmpty()){ listaCompra.add(item);}
        //le dice al adapter que se han añadido nuevos valores par que pinte de nuevo la lista
        adapter.notifyDataSetChanged();
        textoArticulo.setText("");
    }

    //Método para inicializar los componentes
    private void initView(View view) {
        btn_añadir = view.findViewById(R.id.btn_añadir);
        textoArticulo = view.findViewById(R.id.textoArticulo);
        rv_lista = view.findViewById(R.id.rv_lista);
    }






}