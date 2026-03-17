package com.example.proyectoeatq.ControlListaCompra;

import android.graphics.Paint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoeatq.R;


/* Un viewholder es una clase que se encarga de mostrar cada item de la lista.
Es como un molde o plantilla para cada item.
Lo que hace es recoger las vistas del layout de cada item (en este caso,
el layout item_lista_compra.xml), y tiene una función render() que recibe
los datos de cada item y los muestra en esas vistas.*/
public class CompraViewHolder extends RecyclerView.ViewHolder {

    //Referencias de las vistas de item_lista_compra.xml
    private TextView tvLista;
    private ImageView btn_borrarItem;
    private CheckBox check_item; // ? checkcomprado


    // Constructor (se ejecuta una vez por cada ViewHolder creado)
    public CompraViewHolder(View itemView) { //itemView = el layout inflado
        super(itemView);  // Llama al constructor de RecyclerView.ViewHolder
        tvLista = itemView.findViewById(R.id.tvLista);
        btn_borrarItem = itemView.findViewById(R.id.btn_borrarItem);
        check_item = itemView.findViewById(R.id.check_item);

    }

    /* El método render() recibe los datos y los coloca en las vistas guardadas
    (las vistas del layout). Esta función va a ir iterando, de forma que la variable
    item tendrá un valor diferente cada vez. */
    public void render(String item, ListaAdapter.OnItemDeleteListener oidl){

        tvLista.setText(item); //pone el texto (el item) en el TextView


        resetCheckbox();

        configuracionCheckbox();





        /* Función para el boton de borrar item, que al pulsarlo, llama al método
        borrarItem de la interfaz, pasando la posición del item a borrar. El método itemBorrado
        se va a ejecutar en el Fragment, que es el que implementa la interfaz, y ahí se va a
        borrar el item de la lista y se va a actualizar el RecyclerView.
         */
        btn_borrarItem.setOnClickListener(v -> {
            if (oidl != null){
                oidl.borrarItem(getAdapterPosition());
            }
        });

    }

    /* Función para el checkbox, que al marcarlo, pone el texto del item tachado y de color gris,
     y al desmarcarlo vuelve al estado por defecto */
    private void configuracionCheckbox() {

        check_item.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tvLista.setPaintFlags(tvLista.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                tvLista.setTextColor(itemView.getContext().getResources().getColor(R.color.textoChecked));
            } else {
                tvLista.setPaintFlags(tvLista.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                tvLista.setTextColor(itemView.getContext().getResources().getColor(R.color.negroTexto));
            }
        });

    }

    //método para resetear el estado del checkbox a su estado por defecto, para evitar problemas de reciclaje de vistas
    private void resetCheckbox() {

        check_item.setOnCheckedChangeListener(null); //reseteo del listener para evitar problemas de reciclaje de vistas
        check_item.setChecked(false); //reseteo del estado del checkbox a su estado por defecto
        // me aseguro de que el texto está en su estado normal
        tvLista.setPaintFlags(tvLista.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        tvLista.setTextColor(itemView.getContext().getResources().getColor(R.color.negroTexto));


    }
}