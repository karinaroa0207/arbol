package edu.co.udistrital.controlador;

import edu.co.udistrital.controlador.ControladorPrincipal;
import edu.co.udistrital.vista.VentanaPrincipal;
import javax.swing.SwingUtilities;

/**
 * Clase principal (punto de entrada) de la aplicación del Árbol B+.
 * Se encarga de inicializar la interfaz gráfica en el hilo de eventos de Swing.
 * 
 * @author Karina Roa
 * @version 1.0
 */
public class Main {

    /**
     * Constructor privado para evitar instanciación (clase utilitaria).
     */
    private Main() {
        // Constructor vacío y privado
    }

    /**
     * Punto de entrada principal de la aplicación.
     * Lanza la ventana principal del sistema en el hilo de eventos de Swing
     * para garantizar la seguridad en la interfaz gráfica.
     * 
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal vista = new VentanaPrincipal();
            ControladorPrincipal controlador = new ControladorPrincipal(vista);
            controlador.arrancar();
        });
    }
}