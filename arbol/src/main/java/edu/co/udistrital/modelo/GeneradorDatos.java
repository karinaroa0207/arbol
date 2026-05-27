package edu.co.udistrital.modelo;

import java.util.Random;

/**
 * Clase utilitaria (Factory) para generar datos de prueba (Mock Data).
 * Permite que la inserción en el árbol sea más ágil sin necesidad de 
 * escribir a mano el evento y la zona cada vez.
 * 
 * @author Karina Roa
 * @version 1.0
 */
public class GeneradorDatos {
    
    /**
     * Constructor privado para evitar instanciación (clase utilitaria).
     */
    private GeneradorDatos() {
        // Constructor vacío y privado
    }
    
    // Arreglos de datos prefabricados
    private static final String[] EVENTOS = {
        "Martin Garrix", "Dua Lipa", "Morat", 
        "Coldplay", "Feid", "Festival Estéreo Picnic"
    };
    
    private static final String[] ZONAS = {
        "VIP", "General", "Platino", "Palcos", "Gradería Norte"
    };

    /**
     * Construye un objeto Boleta asignándole un evento y una zona al azar.
     * 
     * @param id El identificador único ingresado por el usuario
     * @return Objeto Boleta instanciado y listo para guardar en el árbol
     */
    public static Boleta generarBoletaAleatoria(int id) {
        Random rnd = new Random();
        
        String eventoAleatorio = EVENTOS[rnd.nextInt(EVENTOS.length)];
        String zonaAleatoria = ZONAS[rnd.nextInt(ZONAS.length)];
        
        return new Boleta(id, eventoAleatorio, zonaAleatoria);
    }
}