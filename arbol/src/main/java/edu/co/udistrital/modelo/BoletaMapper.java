package edu.co.udistrital.modelo;

/**
 *
 * @author mauri
 */
public class BoletaMapper {
    public static BoletaDTO toDTO(Boleta boleta) {
        if (boleta == null) return null;
        return new BoletaDTO(boleta.getIdBoleta(), boleta.getEvento(), boleta.getZona());
    }
}
