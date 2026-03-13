package com.example.proyectoeatq.ControListaCompra;
/*
El Adapter es como un organizador o coordinador:
- Crea los ViewHolders cuando hacen falta.
- Concecta cada ViewHolder con los datos que debe mostrar
- Dice cuántos items hay en total
 */

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoeatq.R;

import java.util.List;

public class ListaAdapter extends RecyclerView.Adapter<CompraViewHolder>{

    //? TaskAdapter.java

    /* Interfaz que sirve para que el ViewHolder pueda avisar al Fragment cuando
     alguien quiere borrar un item. Es como un canal de comunicación entre ellos.
     "Cualquier clase que implemente esta interfaz tendrá un método itemBorrado,
     que recibe un int" */

    // todo: cambiar el nombre de la interfaz
    public interface interfazItemBorrado {
        void itemBorrado(int position);
    }
    // Variables
    private List<String> lista;
    private interfazItemBorrado interfazItemBorrado; //el canal de comunicación, como un listener

    /* El constructor debe recibir un ArrayList con los datos y el canal de
    comunicación, que avisará cuando alguien borre*/
    public ListaAdapter(List<String> lista, interfazItemBorrado interfazItemBorrado){
        this.lista = lista;
        this.interfazItemBorrado = interfazItemBorrado;
    }


    /* Función CompraViewHolder: crea nuevos ViewHolders cuando el RecyclerView
    los necesita, y asigna el item a la lista.*/
    @NonNull
    @Override
    public CompraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* 1. Crear una instancia de LayoutInflater. La clase LayoutInflater. permite
        cargar xml para poder trabajar cno ellos.
        2. Return, con el que devolvemos el ViewHolder.
        Le pasamos la view que necesita su constructor, el parent y un attach false).*/

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new CompraViewHolder(layoutInflater.inflate(R.layout.item_lista_compra,parent,false));

    }

    /* Función onBindViwHolder: encargada de que, al hacer scroll, guarde la nueva posicion.
    Toma un ViewHolder existente y le dice qué datos debe mostrar */
    @Override
    public void onBindViewHolder(@NonNull CompraViewHolder holder, int position) {
        /* lista.get(posicion) -> obtiene el string asociado a esa posicion
        El metodo render() del viewholder recibirá el texto que debe mostrar, y el 'listener'*/
        holder.render(lista.get(position), interfazItemBorrado);

    }


    /* getItemCount() le dice al RecyclerView cuántos items debe mostrar
    para que sepa cuando parar de hacer scroll*/
    @Override
    public int getItemCount() {
        return lista.size();
    }
}
