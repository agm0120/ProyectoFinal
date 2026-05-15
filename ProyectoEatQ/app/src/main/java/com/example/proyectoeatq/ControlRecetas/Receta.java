package com.example.proyectoeatq.ControlRecetas;

public class Receta {
    private String Nombre, Descripcion, Imagen, Tipo;


    // Constructor vacío obligatorio para Firebase
    public Receta() {}

    public Receta(String nombre, String descripcion, String imagen, String tipo) {
        this.Nombre = nombre;
        this.Descripcion = descripcion;
        this.Imagen = imagen;
        this.Tipo = tipo;
    }

    // Getters (Importantes para el Adaptador)
    public String getNombre() { return Nombre; }
    public String getImagen() { return Imagen; }
    public String getDescripcion() { return Descripcion; }
    public String getTipo() { return Tipo; }
}