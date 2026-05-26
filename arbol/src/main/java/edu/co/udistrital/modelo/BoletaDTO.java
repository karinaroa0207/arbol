
package edu.co.udistrital.modelo;

public class BoletaDTO {
    private final int idBoleta;
    private final String evento;
    private final String zona;

    public BoletaDTO(int idBoleta, String evento, String zona) {
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
