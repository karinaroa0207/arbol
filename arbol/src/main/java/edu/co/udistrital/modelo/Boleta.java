package edu.co.udistrital.modelo;

/**
 * Representa el valor (V) que almacenaremos en las hojas del Árbol B+.
 * Es la información real de negocio que nos interesa recuperar.
 * 
 * @author Karina 
 * @version 1.0
 */
public class Boleta {
    private int idBoleta; // Será nuestra clave (K) de búsqueda
    private String evento;
    private String zona;

    /**
     * Construye una nueva boleta con los datos especificados.
     * 
     * @param idBoleta Identificador único de la boleta
     * @param evento Nombre del evento
     * @param zona Zona del recinto
     */
    public Boleta(int idBoleta, String evento, String zona) {
        this.idBoleta = idBoleta;
        this.evento = evento;
        this.zona = zona;
    }

    /**
     * Obtiene el identificador de la boleta.
     * @return ID único que sirve como clave en el árbol
     */
    public int getIdBoleta() { return idBoleta; }
    
    /**
     * Obtiene el nombre del evento.
     * @return Nombre del evento
     */
    public String getEvento() { return evento; }
    
    /**
     * Obtiene la zona del recinto.
     * @return Zona del recinto (VIP, Platea, General, etc.)
     */
    public String getZona() { return zona; }

    @Override
    public String toString() {
        return "Boleta[ID=" + idBoleta + ", Evento=" + evento + ", Zona=" + zona + "]";
    }
}