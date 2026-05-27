package edu.co.udistrital.controlador;

import edu.co.udistrital.modelo.ArbolBPlus;
import edu.co.udistrital.modelo.Boleta;
import edu.co.udistrital.vista.VentanaPrincipal;

/**
 * Controlador principal de la aplicación (Patrón MVC).
 * Actúa como intermediario entre la vista (VentanaPrincipal) y el modelo (Árbol B+).
 * 
 * <p>Responsabilidades:</p>
 * <ul>
 *   <li>Inicializar el árbol B+ con el orden seleccionado por el usuario</li>
 *   <li>Configurar los manejadores de eventos para las acciones de la UI</li>
 *   <li>Coordinar la comunicación entre vista y modelo</li>
 * </ul>
 * 
 * @author Karina Roa
 * @version 1.0
 * @see VentanaPrincipal
 * @see ArbolBPlus
 * @see ManejadorEventos
 */
public class ControladorPrincipal {
    
    private VentanaPrincipal vistaPrincipal;

    /**
     * Construye el controlador y establece la conexión vista-modelo.
     * 
     * @param vista Referencia a la ventana principal (interfaz gráfica)
     */
    public ControladorPrincipal(VentanaPrincipal vista) {
        this.vistaPrincipal = vista;        
        
        configurarEventos();
    }

    /**
     * Configura y enlaza los eventos de la interfaz con el manejador correspondiente.
     * El ManejadorEventos se encarga internamente de conectar los botones
     * (Insertar, Buscar, Eliminar, Rango, Reiniciar, Orden) con la lógica del árbol.
     */
    private void configurarEventos() {
        ManejadorEventos manejador = new ManejadorEventos(
            vistaPrincipal.getControlesEntrada(), 
            vistaPrincipal.getVisualizadorArbol(),
            vistaPrincipal,
            vistaPrincipal
        );
    }

    /**
     * Inicia la aplicación haciendo visible la ventana principal.
     * Este método debe llamarse después de la construcción del controlador.
     */
    public void arrancar() {
        vistaPrincipal.setVisible(true);
    }
}