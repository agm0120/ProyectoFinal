package com.example.proyectoeatq.ControlListaCompra;

/* Un Adapter es una clase que se encarga de conectar los datos con las  vistas que los van a mostrar,
para mostrar listas de datos en un RecyclerView. El Adapter crea los ViewHolders, les asigna los
datos de la lista de la compra que debe mostrar, y le dice al RV cuántos items hay en total para
que sepa cuando parar de hacer scroll. Además, el Adapter tiene uan interfaz para avisar al Frament
cuando alguien borrar un item, para que el Fragment pueda actualizar la lista y el RV.
El Adapter se llama ListaAdapter, se conecta con el VH (CompraViewHolder) y con el Fragment (ListaCompra).
*/


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoeatq.R;

import java.util.List;

public class LCAdapter extends RecyclerView.Adapter<LCViewHolder>{

    //? TaskAdapter.java

    /* Esta interface sirve para que el ViewHolder pueda avisar al Fragment cuando alguien
    quiere borrar un item ya que el ViewHolder no tiene acceso directo al Fragment para
    decirle "oye, borra este item".
    La interfaz será un canal de comunicación entre el ViewHolder y el Fragment. Cualquier clase
    que implemente esta interfaz tendrá el método itemBorrado(int posiciónItemABorrar).
    El Fragment implementará esta interfaz para que, cuando el ViewHolder llame al método
    itemBorrado(), el Fragment sepa qué item debe eliminar de la lista y actualizar el RecyclerView.
     */


    public interface OnItemDeleteListener {
        void borrarItem(int position);
    }

    public interface OnCheckChangedListener{
        void checkChanged(int position, boolean checked);
    }
    // Variables extraídas del Fragment para que el Adapter pueda usarlas.
    // El Adapter necesita la lista de la compra
    private List<LCItem> lista;
    private OnItemDeleteListener oidListener;
    private OnCheckChangedListener checkListener;

    /* El constructor debe recibir un ArrayList con los datos y el canal de
    comunicación, que avisará cuando un item se borre */
    /* El constructor debe recibir un ArrayList con los datos y el OnItemDeleteListener,
    que avisará cuando se le de al botón para borrar un item */
    public LCAdapter(List<LCItem> lista, OnItemDeleteListener oidListener, OnCheckChangedListener ccl){
        this.lista = lista;
        this.oidListener = oidListener;
        this.checkListener = ccl;
    }


    /* El método onCreateViewHOlder() sirve para crear un nuevo ViewHolder cuando el
    RecyclerView lo necesite. */
    @NonNull
    @Override
    public LCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        /* 1. Creamos el layoutInflater, que es el encargado de convertir el XML
        en una vista que podamos usar en el código.
        2. Devolvemos un nuevo CompraViewHolder, al que le pasamos la vista inflada del layout
        de cada item (item_lista_compra.xml), el parent y un attach false (porque no queremos
        que se añada automáticamente al parent, ya que el RecyclerView se encarga de eso).*/

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new LCViewHolder(layoutInflater.inflate(R.layout.item_lista_compra,parent,false));

    }


    /* La función onbindViewHolder() sirve para asignar los datos de la lista al ViewHolder.
    Se llama cada vez que el RecyclerView necesita mostrar un nuevo item, y le pasa el ViewHolder
    que debe mostrar ese item y la posición del item en la lista.
    El método render() del ViewHolder se encargará de mostrar el texto del item en las vistas
    correspondientes, y también de configurar el botón de borrar para que llame al método de
    la interfaz cuando se pulse.

     */
    @Override
    public void onBindViewHolder(@NonNull LCViewHolder holder, int position) {
        LCItem item = lista.get(position);

        /* lista.get(posicion) -> obtiene el string asociado a esa posicion
        El metodo render() del viewholder recibirá el texto que debe mostrar, y el 'listener'*/
        holder.render(item.texto, item.checked, oidListener, (pos, isChecked) -> {
            lista.get(pos).checked = isChecked;
            if (checkListener != null) {
                checkListener.checkChanged(pos, isChecked);
            }
        });

    }


    /* getItemCount() le dice al RecyclerView cuántos items debe mostrar
    para que sepa cuando parar de hacer scroll*/
    @Override
    public int getItemCount() {
        return lista.size();
    }
}
