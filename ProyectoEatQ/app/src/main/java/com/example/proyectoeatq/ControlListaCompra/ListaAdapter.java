package com.example.proyectoeatq.ControlListaCompra;
/*
El Adapter es como un organizador o coordinador:
- Crea los ViewHolders cuando hacen falta.
- Concecta cada ViewHolder con los datos que debe mostrar
- Dice cuántos items hay en total
 */

/* Este Adapter sirve para mostrar la lista de la compra, y para avisar al Fragment cuando alguien borra un item.
Un Adapter es una clase que se encarga de conectar los datos con las vistas que los van a mostrar.
En este caso, el Adapter se llama ListaAdapter, y se conecta con el ViewHolder (CompraViewHolder) y con el Fragment (ListaCompra).

 */

/*
Un Adapter es una clase que se encarga de conectar los datos con las vistas que los van a mostrar.
Sirve para mostrar listas de datos en un RecyclerView. El Adapter crea los ViewHolders, les asigna
los datos que deben mostrar, y le dice al RecyclerView cuántos items hay en total para que sepa
cuándo parar de hacer scroll.
En este caso, el Adapter se llama ListaAdapter, y se conecta con el ViewHolder (CompraViewHolder)
y con el Fragment (ListaCompra). El Adapter crea los ViewHolders, les asigna los datos de la lista
de la compra que deben mostrar, y le dice al RecyclerView cuántos items hay en total para que sepa
cuándo parar de hacer scroll. Además, el Adapter tiene una interfaz para avisar al Fragment cuando
alguien borra un item, para que el Fragment pueda actualizar la lista y el RecyclerView.

 */

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoeatq.R;

import java.util.List;

public class ListaAdapter extends RecyclerView.Adapter<CompraViewHolder>{

    //? TaskAdapter.java


    // todo: cambiar el nombre de la interfaz
    /*
    Esta interface sirve para que el ViewHolder pueda avisar al Fragment cuando alguien
    quiere borrar un item.
    El ViewHolder es el encargado de mostrar cada item de la lista, y cuando alguien pulsa
    el botón de borrar, el ViewHolder no tiene acceso directo al Fragment para decirle
    "oye, borra este item". Entonces, lo que hacemos es crear esta interfaz, que es como
     un canal de comunicación entre el ViewHolder y el Fragment. Cualquier clase que implemente
     esta interfaz tendrá un método itemBorrado, que recibe un int (la posición del item que se
     quiere borrar). El Fragment va a implementar esta interfaz, y cuando el ViewHolder llame a
     itemBorrado con la posición del item a borrar, el Fragment sabrá qué item debe eliminar
     de la lista.
     */
    public interface OnItemDeleteListener {
        /*Este void itemBorrado, que recibe un int position, es el método que se va a
        llamar desde el ViewHolder cuando alguien pulse el botón de borrar.
         */
        void borrarItem(int position);
    }
    // Variables
    private List<String> lista;
    private OnItemDeleteListener OnItemDeleteListener; //el canal de comunicación, como un listener

    /* El constructor debe recibir un ArrayList con los datos y el canal de
    comunicación, que avisará cuando alguien borre*/
    public ListaAdapter(List<String> lista, OnItemDeleteListener OnItemDeleteListener){
        this.lista = lista;
        this.OnItemDeleteListener = OnItemDeleteListener;
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
        holder.render(lista.get(position), OnItemDeleteListener);

    }


    /* getItemCount() le dice al RecyclerView cuántos items debe mostrar
    para que sepa cuando parar de hacer scroll*/
    @Override
    public int getItemCount() {
        return lista.size();
    }
}
