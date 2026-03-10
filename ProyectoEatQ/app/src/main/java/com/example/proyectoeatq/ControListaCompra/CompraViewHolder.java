package com.example.proyectoeatq.ControListaCompra;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoeatq.R;

public class CompraViewHolder extends RecyclerView.ViewHolder {

    //recojo el TextView de item_lista_compra.xml
    private TextView tvLista; //? tvTask
    ImageView btn_borrarItem; //? ivTaskDone

    // Constructor
    public CompraViewHolder(View itemView) {
        super(itemView);  // Llama al constructor de RecyclerView.ViewHolder
        tvLista = itemView.findViewById(R.id.tvLista);
        btn_borrarItem = itemView.findViewById(R.id.btn_borrarItem);

    }


    /* Función que recibe el item, en tipo String
    Esto se va a ir rellenando con cada item de la lista, de forma
    que primero va a tener un valor pero luego otro, e irá cambiando.
    El Adapter, cuando conecte el viewholder con el fragment, tendrá
    el listado entero de items, e irá recorriendolo.
     */
    public void render(String lista, ListaAdapter.interfazItemBorrado interfazItemBorrado){

        tvLista.setText(lista);

        btn_borrarItem.setOnClickListener(v -> {
            if (interfazItemBorrado != null){
                interfazItemBorrado.itemBorrado(getAbsoluteAdapterPosition());
            }
        });

    }
}