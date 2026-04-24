package com.example.proyectoeatq;

public class Receta {
    private String nombre;
    private int colorFondo;
    private String imagenUrl; // Aquí Firebase meterá el enlace (ej: "https://firebasestorage...")

    // Constructor vacío obligatorio para Firebase
    public Receta() {}

    public Receta(String nombre, int colorFondo, String imagenUrl) {
        this.nombre = nombre;
        this.colorFondo = colorFondo;
        this.imagenUrl = imagenUrl;
    }

    public String getNombre() { return nombre; }
    public int getColorFondo() { return colorFondo; }
    public String getImagenUrl() { return imagenUrl; }
}