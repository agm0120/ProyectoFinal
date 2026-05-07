package com.example.proyectoeatq;

import org.junit.Test;

import static org.junit.Assert.*;


public class ExampleUnitTest {
    // TEST 1: Verificar que la conversión d etiempo de Firebase 801:30) a minutos es correcta
    @Test
    public void testExtraerMinutos(){
        String tiempoSimulado = "01 : 15"; //1h 15 minutos
        int esperado = 75; // (1*60) + 15 = 75 minutos

        //Lógica que usamos en el fragment
        String [] partes = tiempoSimulado.split(":");
        int horas = Integer.parseInt(partes[0].trim());
        int minutos = Integer.parseInt(partes[1].trim());
        int resultadoReal = (horas* 60) + minutos;

        assertEquals("La conversión de tiempo falló", esperado, resultadoReal);

    }

    // TEST 2: Verificar que el formato de visualización final es correcto
    @Test
    public void testFormatoTextoSalida() {
        int minutosTotales = 95;
        String esperado = "1h 35min";

        int horas = minutosTotales / 60;
        int minutos = minutosTotales % 60;
        String resultadoReal = horas + "h " + minutos + "min";

        assertEquals("El texto de salida no tiene el formato esperado", esperado, resultadoReal);
    }

    // TEST 3: Verificar que el acumulador de estiramientos no duplica zonas (Uso de Set)
    @Test
    public void testAcumuladorEstiramientosSinDuplicados() {
        java.util.Set<String> zonas = new java.util.HashSet<>();
        zonas.add("Piernas");
        zonas.add("Espalda");
        zonas.add("Piernas"); // Intentamos añadir duplicado

        assertEquals("El set debería haber ignorado el duplicado", 2, zonas.size());
    }

    // TEST 4: Verificar la validación de campos vacíos antes de enviar a Firebase
    @Test
    public void testValidacionDatoVacio() {
        String datoPrueba = "   "; // Solo espacios
        assertTrue("Debería considerarse vacío tras usar trim()", datoPrueba.trim().isEmpty());
    }



}