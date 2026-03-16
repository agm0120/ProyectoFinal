package com.example.proyectoeatq;

import static com.example.proyectoeatq.ControlListaCompra.AppApplication.prefs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.proyectoeatq.ControlListaCompra.ListaAdapter;

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


    /* Función que va a configurar el RecyclerView para añadirle el adapter y
    que funcione todo el fujo */
    private void initRecyclerView() {
        /*
        El layout manager es el encargado de mostrar cómo se van a mostrar las vistas.
        En este caso, va a ser un LinearLayoutManager, que muestra los items en una lista vertical.
        A este LinearLayoutManager le vamos a pasar el contexto del fragment, para que sepa
        dónde mostrar las vistas.
        El ListaAdapter.interfazItemBorrado es como un listener, en este caso para borrar */

        listaCompra = prefs.getList(); //carga la lista guardada en las preferencias
        rv_lista.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ListaAdapter(listaCompra, new ListaAdapter.OnItemDeleteListener(){
            @Override
            public void borrarItem(int posicion){
                borrarItem(posicion);
            }
        });
        rv_lista.setAdapter(adapter); //el adapter que va a usar es el que acabamos de crear

    }

    private void borrarItem(int posicion){ // ?deleteTask
        listaCompra.remove(posicion);
        adapter.notifyDataSetChanged();
        // ? opción 2 adapter.notifyItemRemoved(posicion);
        prefs.saveItem(listaCompra); //guarda la lista actualizada en las preferencias

    }
    
    private void initListeners() {
        btn_añadir.setOnClickListener(view -> añadirItem()); //forma abreviada
    }

    private void añadirItem() { //? addTask()
        String item = textoArticulo.getText().toString().trim();
        //añade el item a la lista, pero solo si no está vacío
        if(!item.isEmpty()){ listaCompra.add(item);}
        //le dice al adapter que se han añadido nuevos valores par que pinte de nuevo la lista
        adapter.notifyDataSetChanged();
        textoArticulo.setText("");
        prefs.saveItem(listaCompra); //guarda la lista actualizada en las preferencias
    }

    //Método para inicializar los componentes
    private void initView(View view) {
        btn_añadir = view.findViewById(R.id.btn_añadir);
        textoArticulo = view.findViewById(R.id.textoArticulo);
        rv_lista = view.findViewById(R.id.rv_lista);
    }






}