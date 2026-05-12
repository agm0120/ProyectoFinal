package com.example.proyectoeatq.ControlRecetas;

public class Receta {
    private String Nombre, Descripción, Imagen, Tipo;


    // Constructor vacío obligatorio para Firebase
    public Receta() {}

    public Receta(String nombre, String descripción, String imagen, String tipo) {
        this.Nombre = nombre;
        this.Descripción = descripción;
        this.Imagen = imagen;
        this.Tipo = tipo;
    }

    // Getters (Importantes para el Adaptador)
    public String getNombre() { return Nombre; }
    public String getImagen() { return Imagen; }
    public String getDescripción() { return Descripción; }
    public String getTipo() { return Tipo; }
}