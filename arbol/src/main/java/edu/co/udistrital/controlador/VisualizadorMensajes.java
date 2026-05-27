package edu.co.udistrital.controlador;


/**
 * Define el contrato para cualquier componente capaz de renderizar o notificar
 * mensajes al usuario en la interfaz gráfica.
 * 
 * @author Karina 
 * @version 1.0
 */
public interface VisualizadorMensajes {

    /**
     * Muestra un mensaje de error crítico o de validación al usuario final.
     * La clase que implemente este método decidirá el canal visual para
     * renderizar el texto.
     *
     * @param mensaje El texto descriptivo del error que se le presentará al
     *                usuario. No debería ser {@code null} o estar vacío.
     * @param titulo Título del error
     */
    void mostrarMensajeError(String mensaje, String titulo);

    /**
     * Muestra un mensaje de error crítico o de validación al usuario final.
     * La clase que implemente este método decidirá el canal visual para
     * renderizar el texto.
     *
     * @param mensaje El texto descriptivo del error que se le presentará al
     *                usuario. No debería ser {@code null} o estar vacío.
     */
    void mostrarMensajeError(String mensaje);
    
    /**
     * Muestra un mensaje informativo al usuario.
     * La clase que implemente este método decidirá el canal visual para
     * renderizar el texto.
     *
     * @param mensaje El texto descriptivo que se le presentará al usuario.
     *                No debería ser {@code null} o estar vacío.
     */
    void mostrarMensaje(String mensaje);
    
    /**
     * Muestra un mensaje informativo al usuario con un título específico.
     * La clase que implemente este método decidirá el canal visual para
     * renderizar el texto.
     *
     * @param mensaje El texto descriptivo que se le presentará al usuario.
     *                No debería ser {@code null} o estar vacío.
     * @param titulo Título del mensaje
     */
    void mostrarMensaje(String mensaje, String titulo);
    
    /**
     * Muestra un mensaje de advertencia (warning) al usuario.
     * La clase que implemente este método decidirá el canal visual para
     * renderizar el texto.
     *
     * @param mensaje El texto descriptivo de la advertencia que se le presentará
     *                al usuario. No debería ser {@code null} o estar vacío.
     */
    void mostrarMensajeWarning(String mensaje);
    
    /**
     * Muestra un mensaje de advertencia (warning) al usuario con un título específico.
     * La clase que implemente este método decidirá el canal visual para
     * renderizar el texto.
     *
     * @param mensaje El texto descriptivo de la advertencia que se le presentará
     *                al usuario. No debería ser {@code null} o estar vacío.
     * @param titulo Título de la advertencia
     */
    void mostrarMensajeWarning(String mensaje, String titulo);    
    
}