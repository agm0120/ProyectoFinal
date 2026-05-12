package com.example.proyectoeatq.ControlRecetas;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.proyectoeatq.R;

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
        //Saco exactamente qué receta estoy viendo ahora mientras recorro la lista, para mostrar sus datos en pantalla.
        Receta receta = listaRecetas.get(position);

        //Lógica de textos básica
        holder.tvTitulo.setText(receta.getNombre());


        configurarEtiquetaTipo(holder, receta.getTipo());
        cargarImagenReceta(holder, receta.getImagen());

        //Método para la lógica del click (detalles de la receta)
        holder.itemView.setOnClickListener(v -> mostrarDetalleReceta(v, receta));

    }

    //Método para la lógica del click (ver receta en detalle)
    private void mostrarDetalleReceta(View v, Receta receta) {
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_detalle_receta, null);

        TextView tvNombre = dialogView.findViewById(R.id.tv_detalleNombre);
        TextView tvDesc = dialogView.findViewById(R.id.tv_detalleDescripcion);
        ImageView imgDetalle = dialogView.findViewById(R.id.img_detalleFoto);

        tvNombre.setText(receta.getNombre());
        tvDesc.setText(receta.getDescripción() != null ? receta.getDescripción() : "No hay descripción disponible.");

        Glide.with(v.getContext()).load(receta.getImagen()).into(imgDetalle);

        // Creamos y mostramos el diálogo
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(v.getContext())
                .setView(dialogView)
                .setPositiveButton("Cerrar", (d, which) -> d.dismiss())
                .create();

        dialog.show();

        // Ajuste de dimensiones de la pantalla
        ajustarDimensionesDialogo(v, dialog);
    }

    private void ajustarDimensionesDialogo(View v, AlertDialog dialog) {

        if (dialog.getWindow() != null) {
            int width = (int)(v.getResources().getDisplayMetrics().widthPixels * 0.95);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    //Método para cargar la imagen
    private void cargarImagenReceta(RecetaViewHolder holder, String imagen) {
        Glide.with(holder.itemView.getContext())
                .load(imagen)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.imgReceta);

    }

    //Método para configurar el estilo visual del tipo de receta (color de fondo y texto)
    private void configurarEtiquetaTipo(RecetaViewHolder holder, String tipo) {
        if(tipo == null) return;

        holder.tvTipo.setText(tipo.toUpperCase());
        int colorFondo;
        Context context = holder.itemView.getContext();

        //determinamos el color de fondo según el tipo de receta
        if(tipo.equalsIgnoreCase("Carbohidratos")){
            colorFondo = ContextCompat.getColor(context, R.color.hidratosOn);
        } else if(tipo.equalsIgnoreCase("Proteínas")){
            colorFondo = ContextCompat.getColor(context, R.color.proteinasOn);
        } else if(tipo.equalsIgnoreCase("Vegetales")){
            colorFondo = ContextCompat.getColor(context, R.color.vegetalesOn);
        } else {
            colorFondo = Color.LTGRAY; //color neutro para tipos desconocidos
        }

        holder.tvTipo.setBackgroundTintList(ColorStateList.valueOf(colorFondo));
    }

    @Override
    public int getItemCount() {
        // Le indico al adaptador exactamente cuántas recetas tengo en total.
        return listaRecetas.size();
    }

    // Creo esta clase interna para enlazar mis variables de Java con los IDs de mi diseño XML una sola vez y no sobrecargar la memoria.
    public static class RecetaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvTipo;
        ImageView imgReceta;


        public RecetaViewHolder(@NonNull View itemView) {
            super(itemView);
            // Busco y guardo las referencias de la pantalla para usarlas más arriba.
            tvTitulo = itemView.findViewById(R.id.tv_titulo_receta);
            tvTipo = itemView.findViewById(R.id.tv_tipoReceta);
            imgReceta = itemView.findViewById(R.id.img_receta);

        }
    }
}