package edu.co.udistrital.modelo;

/**
 * DTO para transferir datos de Boleta a la vista.
 * 
 * @author Karina Roa
 * @version 1.0
 */
public class BoletaDTO {
    private final int idBoleta;
    private final String evento;
    private final String zona;

    /**
     * Construye un DTO de boleta con los datos especificados.
     * 
     * @param idBoleta Identificador único de la boleta
     * @param evento Nombre del evento
     * @param zona Zona del recinto
     */
    public BoletaDTO(int idBoleta, String evento, String zona) {
        this.idBoleta = idBoleta;
        this.evento = evento;
        this.zona = zona;
    }

    /**
     * Obtiene el identificador de la boleta.
     * @return ID único de la boleta
     */
    public int getIdBoleta() { return idBoleta; }

    /**
     * Obtiene el nombre del evento.
     * @return Nombre del evento
     */
    public String getEvento() { return evento; }

    /**
     * Obtiene la zona del recinto.
     * @return Zona asignada (VIP, Platea, General, etc.)
     */
    public String getZona() { return zona; }

    @Override
    public String toString() {
        return "Boleta[ID=" + idBoleta + ", Evento=" + evento + ", Zona=" + zona + "]";
    }
}
