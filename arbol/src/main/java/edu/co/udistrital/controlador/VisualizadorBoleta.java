package edu.co.udistrital.controlador;

import edu.co.udistrital.modelo.BoletaDTO;

/**
 * Define el contrato para los componentes de la interfaz de usuario capaces de 
 * renderizar la información de una boleta de manera gráfica o estructurada.
 * 
 */
public interface VisualizadorBoleta {
    
    /**
     * Renderiza visualmente la información de una boleta validada utilizando 
     * una representación propia de la interfaz de usuario, evitando exponer 
     * el formato de texto crudo del modelo.
     *
     * @param boleta El Objeto de Transferencia de Datos (DTO) que contiene la 
     * información procesada y formateada de la boleta a mostrar.
     */
    void mostrarBoleta(BoletaDTO boleta);
}