package com.example.proyectoeatq;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

// Defino clase principal para manejar la lista y la hago heredar del adaptador de Android.
public class RecetasAdapter extends RecyclerView.Adapter<RecetasAdapter.RecetaViewHolder> {

    // variable privada donde guardardo la lista de recetas que reciba de la base de datos.
    private List<Receta> listaRecetas;

    // En mi constructor recibo esa lista de recetas y me la guardo en mi variable para poder usarla en el resto del código.
    public RecetasAdapter(List<Receta> listaRecetas) {
        this.listaRecetas = listaRecetas;
    }

    @NonNull
    @Override
    public RecetaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //diseño XML para crear la estructura visual de cada elemento de la lista.

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_receta, parent, false);
        return new RecetaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecetaViewHolder holder, int position) {
        // Saco la receta exacta que toca mostrar en esta posición.
        Receta receta = listaRecetas.get(position);

        //asigno el texto al título y el color al fondo de mi contenedor.
        holder.tvTitulo.setText(receta.getNombre());
        holder.container.setBackgroundColor(receta.getColorFondo());

        // Uso Glide para descargar la imagen directamente desde la URL de Firebase y colocarla en mi vista.
        Glide.with(holder.itemView.getContext())
                .load(receta.getImagenUrl())
                .into(holder.imgReceta);
    }

    @Override
    public int getItemCount() {
        // Le indico al adaptador exactamente cuántas recetas tengo en total.
        return listaRecetas.size();
    }

    // Creo esta clase interna para enlazar mis variables de Java con los IDs de mi diseño XML una sola vez y no sobrecargar la memoria.
    public static class RecetaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo;
        ImageView imgReceta;
        LinearLayout container;

        public RecetaViewHolder(@NonNull View itemView) {
            super(itemView);
            // Busco y guardo las referencias de la pantalla para usarlas más arriba.
            tvTitulo = itemView.findViewById(R.id.tv_titulo_receta);
            imgReceta = itemView.findViewById(R.id.img_receta);
            container = itemView.findViewById(R.id.container_receta);
        }
    }
}