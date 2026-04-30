package com.example.proyectoeatq;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Map;

public class GestorFirebase {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private Context context;

    //Constructor para inicializar la conexión a Firestore
    public GestorFirebase(Context context) {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        this.context = context;
    }

    //Método para obtener la instancia de Firestore
    public String obtenerUidActual(){
        if(auth.getCurrentUser() != null){
            return auth.getCurrentUser().getUid();
        }
        return null; //TODO: Manejar el caso en que no haya un usuario autenticado
    }

    //Método para cerrar sesión del usuario actual
    public void cerrarSesion(){
        auth.signOut();
    }

    public void guardarDatos(String nombreColeccion, Map<String, Object> datos){
        String uid = obtenerUidActual();

        Toast.makeText(context, "Llamando a guardarDatos...", Toast.LENGTH_SHORT).show();

        if(uid != null){
            //añadismo el "sello" del usuario y la fecha automáticamente
            datos.put("Usuario", uid);
            datos.put("Fecha", new Date());

            db.collection(nombreColeccion).add(datos).addOnSuccessListener(documentReference -> {
                //Exito, los datos se guardaron correctamente
                Toast.makeText(context, "Actividad guardada correctamente", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                // Error al guardar
                Toast.makeText(context, "Error al guardar actividad: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

        //Método para recuperar datos de una colección específica
    public FirebaseFirestore getDb(){

        return db;
    }

    //Método para recuperar datos de una colección específica, filtrando por el usuario actual y un rango de fechas
    public void consultarDatosPorFecha(String coleccion,Date inicio, Date fin, OnDatosRecuperadosListener listener){
        String uid = obtenerUidActual();
        if(uid == null) return;

        db.collection(coleccion)
                .whereEqualTo("Usuario", uid)
                .whereGreaterThanOrEqualTo("Fecha", inicio)
                .whereLessThanOrEqualTo("Fecha", fin)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    //Datos recuperados exitosamente
                    listener.onSuccess(queryDocumentSnapshots);
                })
                .addOnFailureListener(e -> {
                    //Error al recuperar datos
                    Toast.makeText(context, "Error al recuperar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });


    }

    // Interfaz para manejar la respuesta de la consulta de datos, ya que es asíncrona
    public interface OnDatosRecuperadosListener {
        void onSuccess(com.google.firebase.firestore.QuerySnapshot snapshots);
    }



}
