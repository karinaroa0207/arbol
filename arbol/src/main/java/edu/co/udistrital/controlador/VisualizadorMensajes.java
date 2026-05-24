package edu.co.udistrital.controlador;

/**
 * Define el contrato para cualquier componente capaz de renderizar o notificar
 * mensajes al usuario en la interfaz gráfica. 
 *
 */
public interface VisualizadorMensajes {

    /**
     * Muestra un mensaje de error crítico o de validación al usuario final.
     *
     * La clase que implemente este método decidirá el canal visual para
     * renderizar el texto
     *
     * @param mensaje El texto descriptivo del error que se le presentará al
     * usuario. No debería ser {@code null} o estar vacío.
     * @param titulo Titulo del error
     */
    void mostrarMensajeError(String mensaje, String titulo);

    /**
     * Muestra un mensaje de error crítico o de validación al usuario final.
     *
     * La clase que implemente este método decidirá el canal visual para
     * renderizar el texto
     *
     * @param mensaje El texto descriptivo del error que se le presentará al
     * usuario. No debería ser {@code null} o estar vacío.     
     */
    void mostrarMensajeError(String mensaje);
    
    /**
     * Muestra un mensaje.
     *
     * La clase que implemente este método decidirá el canal visual para
     * renderizar el texto
     *
     * @param mensaje El texto descriptivo del error que se le presentará al
     * usuario. No debería ser {@code null} o estar vacío.     
     */
    void mostrarMensaje(String mensaje);
}
