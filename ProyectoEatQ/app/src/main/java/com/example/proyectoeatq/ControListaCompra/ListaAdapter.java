package com.example.proyectoeatq.ControListaCompra;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoeatq.R;

import java.util.List;

public class ListaAdapter extends RecyclerView.Adapter<CompraViewHolder>{

    //? TaskAdapter.java

    // Crear na interfaz
    public interface interfazItemBorrado {
        void itemBorrado(int position);
    }
    // Variables
    private List<String> lista;
    private interfazItemBorrado interfazItemBorrado;

    //el constructor recibe la lista, y el listener
    public ListaAdapter(List<String> lista, interfazItemBorrado interfazItemBorrado){
        this.lista = lista;
        this.interfazItemBorrado = interfazItemBorrado;
    }

    //es la función que crea el viewholdre y le asigna el item de la lista.
    //necesitamos conectar el item_lista_compra.xml al CompraViewHolder.
    //Esto lo hace el onCreateViewHolder
    @NonNull
    @Override
    public CompraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* Creamos una instancia de LayoutInfalter.
        Esta clase (LayoutInfalter.) nos permite cargar xml para poder trabajar con ellos.
        Es parecido a la asociación que hacemos con el setContentView del onCreate.

        En el return devolvermos el ViewHolder, en cuyo constructor ya establecimos
        que recibiría una view (el item_lista_compra.xml). Así que le pasamos el item
        que vamos a inflar (el xml), el parent y el attach.
         */
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new CompraViewHolder(layoutInflater.inflate(R.layout.item_lista_compra,parent,false));

    }

    //Funcion encargada de que, al hacer scroll, guarde a la nueva posición.
    //contiene una instancia del viewholder.
    @Override
    public void onBindViewHolder(@NonNull CompraViewHolder holder, int position) {
        holder.render(lista.get(position), interfazItemBorrado);

    }

    // Esta función le dice al RecyclerView cuántos items debe mostrar
    @Override
    public int getItemCount() {
        return lista.size();
    }
}
