package edu.co.udistrital.modelo;

/**
 * Representa el valor (V) que almacenaremos en las hojas del Árbol B+.
 * Es la información real de negocio que nos interesa recuperar.
 */
public class Boleta {
    private int idBoleta; // Será nuestra clave (K) de búsqueda
    private String evento;
    private String zona;

    public Boleta(int idBoleta, String evento, String zona) {
        this.idBoleta = idBoleta;
        this.evento = evento;
        this.zona = zona;
    }

    public int getIdBoleta() { return idBoleta; }
    public String getEvento() { return evento; }
    public String getZona() { return zona; }

    @Override
    public String toString() {
        return "Boleta[ID=" + idBoleta + ", Evento=" + evento + ", Zona=" + zona + "]";
    }
}