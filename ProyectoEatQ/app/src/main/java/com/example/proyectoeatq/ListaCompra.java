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


    /* En el Fragment no se usará onCreate porque el Fragment tiene un ciclo de vida diferente
    al de una Activity. En un Fragment, el método onCreate() se llama antes de que se cree la
    vista, por lo que no es el lugar adecuado para inicializar componentes de la interfaz de
    usuario o configurar el RecyclerView. En su lugar, se utiliza el método onViewCreated(),
    que se llama después de que la vista del Fragment ha sido creada, para realizar estas tareas.*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /* El onCreatedView se usa solo para inflar el layout del Fragment, es decir,
    para convertir el XML en una vista que se pueda usar en el código.*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      // Se usa solo para inflar el layout
        return inflater.inflate(R.layout.fragment_lista_compra, container, false);
    }

    /* El onViewCreated se usa para para todo lo que tenga que ver con la UI,
      como inicializar los componentes, configurar el RecyclerView, etc.*/
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

    /* Función para inicializar el RecyclerView, que es el componente que se encarga
    de mostrar la lista de la compra. Hay que añadirle el adapter, que es el encargado
    de mostrar cada item de la lista, y el layout manager, que es el encargado de
    mostrar cómo se van a mostrar las vistas
    En este caso hemos elegido un layout lineal, al que le pasamos el context del Fragment
    para que sepa dónde mostrar las vistas.*/
    private void initRecyclerView() {

        listaCompra = prefs.getList(); //carga la lista guardada en las preferencias
        rv_lista.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ListaAdapter(listaCompra, posicion -> borrarItem(posicion)); //forma abreviada con lambda
        rv_lista.setAdapter(adapter); //le pasamos el adapter que acabamos de crear
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