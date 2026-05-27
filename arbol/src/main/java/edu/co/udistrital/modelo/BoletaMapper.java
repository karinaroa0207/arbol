package edu.co.udistrital.modelo;

/**
 * Mapeador para convertir entre Boleta y BoletaDTO.
 * 
 * @author mauri
 * @version 1.0
 */
public class BoletaMapper {

    /**
     * Constructor privado para evitar instanciación (clase utilitaria).
     */
    private BoletaMapper() {
        // Constructor vacío y privado
    }

    /**
     * Convierte una entidad Boleta en su correspondiente DTO.
     * 
     * @param boleta Objeto Boleta a convertir (puede ser null)
     * @return DTO de la boleta, o null si el parámetro es null
     */
    public static BoletaDTO toDTO(Boleta boleta) {
        if (boleta == null) return null;
        return new BoletaDTO(boleta.getIdBoleta(), boleta.getEvento(), boleta.getZona());
    }
}