package edu.co.udistrital.controlador;

import edu.co.udistrital.modelo.ArbolBPlus;
import edu.co.udistrital.modelo.Boleta;
import edu.co.udistrital.modelo.GeneradorDatos;
import edu.co.udistrital.vista.ControlesEntrada;
import edu.co.udistrital.vista.VisualizadorArbolCompleto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ESTA ES LA CLASE PUENTE. Su responsabilidad es escuchar los clics de ambos
 * botones ("VALIDAR" e "INSERTAR"), leer los datos, ir a buscarlo o insertarlo
 * en el Modelo (Árbol) y mandar a redibujar la Vista.
 */
public class ManejadorEventos implements ActionListener {

    private ArbolBPlus<Integer, Boleta> motorArbol;
    private ControlesEntrada inputVista;
    private VisualizadorArbolCompleto outputVista;
    private VisualizadorMensajes vMensajes;

    // Inyección de dependencias a través del constructor
    public ManejadorEventos(ArbolBPlus<Integer, Boleta> motor, ControlesEntrada input, VisualizadorArbolCompleto output, VisualizadorMensajes vMensajes) {
        this.motorArbol = motor;
        this.inputVista = input;
        this.outputVista = output;
        this.vMensajes = vMensajes;

        // Cablear los botones a esta misma clase que implementa ActionListener
       inputVista.agregarListenerBotones(this);
    }

    /**
     * Este método es disparado exclusivamente por el evento del clic en
     * cualquier botón.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String botonClickeado = e.getActionCommand();
        switch (botonClickeado) {
            case "Insertar":
                procesarInsercion();
                break;
            case "Validar":
                procesarBusqueda();
                break;
            case "Reiniciar":
                procesarReiniciar();
                break;
            default:
                break;
        }        
    }

    private void procesarBusqueda() {
        try {
            // 1. Extraer dato de la vista
            String textoIngresado = inputVista.getIdValidar();
            int idBuscado = Integer.parseInt(textoIngresado.trim());

            // 2. Conectar con el modelo (Buscar O(log n))
            Boleta resultado = motorArbol.buscar(idBuscado);
            if (resultado != null) {
                // TODO: Implementar resaltado del nodo encontrado en el visualizador
                vMensajes.mostrarMensaje("✓ BOLETA VÁLIDA: " + resultado.toString());
            } else {
                vMensajes.mostrarMensajeError("X BOLETA INEXISTENTE O FALSA.", "Error de Validación");
            }

        } catch (NumberFormatException ex) {
            vMensajes.mostrarMensajeError("El ID debe ser un número entero.", "Error de Formato");            
        }
    }

    private void procesarInsercion() {
        try {
            // 1. Extraer ID de la vista
            String textoIngresado = inputVista.getIdInsertar();
            int idNuevo = Integer.parseInt(textoIngresado.trim());

            // 2. Usar nuestra nueva clase para generar los datos de forma automática y flexible
            Boleta nuevaBoleta = GeneradorDatos.generarBoletaAleatoria(idNuevo);

            // 3. Inserción matemática O(log n)
            motorArbol.insertar(idNuevo, nuevaBoleta);

            // 4. Limpiar el campo de texto automáticamente para facilitar la siguiente inserción
            inputVista.focusBtn("Insertar");// Quita el foco del texto

            // 5. Redibujar árbol
            outputVista.setArbol(motorArbol);

        } catch (NumberFormatException ex) {
            vMensajes.mostrarMensajeError("El ID de inserción debe ser un número entero.", "Error de Formato");
        }
    }

    private void procesarReiniciar() {
        // Vaciamos el árbol y redibujamos
        motorArbol.reiniciar();
        outputVista.setArbol(motorArbol);
        vMensajes.mostrarMensaje("Estructura del árbol reiniciada.");
    }
}
