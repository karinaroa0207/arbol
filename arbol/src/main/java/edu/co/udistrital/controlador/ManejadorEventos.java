package edu.co.udistrital.controlador;

import edu.co.udistrital.modelo.ArbolBPlus;
import edu.co.udistrital.modelo.Boleta;
import edu.co.udistrital.modelo.GeneradorDatos;
import edu.co.udistrital.vista.ControlesEntrada;
import edu.co.udistrital.vista.VisualizadorArbolCompleto;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ESTA ES LA CLASE PUENTE. 
 * Su responsabilidad es escuchar los clics de ambos botones ("VALIDAR" e "INSERTAR"),
 * leer los datos, ir a buscarlo o insertarlo en el Modelo (Árbol) y mandar a redibujar la Vista.
 */
public class ManejadorEventos implements ActionListener {
    
    private ArbolBPlus<Integer, Boleta> motorArbol;
    private ControlesEntrada inputVista;
    private VisualizadorArbolCompleto outputVista;

    // Inyección de dependencias a través del constructor
    public ManejadorEventos(ArbolBPlus<Integer, Boleta> motor, ControlesEntrada input, VisualizadorArbolCompleto output) {
        this.motorArbol = motor;
        this.inputVista = input;
        this.outputVista = output;
        
        // Cablear los botones a esta misma clase que implementa ActionListener
        inputVista.getBtnValidar().addActionListener(this);
        inputVista.getBtnInsertar().addActionListener(this);
        inputVista.getBtnReiniciar().addActionListener(this);
    }

    /**
     * Este método es disparado exclusivamente por el evento del clic en cualquier botón.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton botonClickeado = (JButton) e.getSource();
        
        if (botonClickeado == inputVista.getBtnInsertar()) {
            procesarInsercion();
        } else if (botonClickeado == inputVista.getBtnValidar()) {
            procesarBusqueda();
        } else if (botonClickeado == inputVista.getBtnReiniciar()) {
            procesarReiniciar();
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
                JOptionPane.showMessageDialog(null, "✓ BOLETA VÁLIDA: " + resultado.toString());
            } else {
                JOptionPane.showMessageDialog(null, "X BOLETA INEXISTENTE O FALSA.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El ID debe ser un número entero.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
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
            inputVista.getBtnInsertar().requestFocus(); // Quita el foco del texto
            
            // 5. Redibujar árbol
            outputVista.setArbol(motorArbol);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El ID de inserción debe ser un número entero.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarReiniciar() {
        // Vaciamos el árbol y redibujamos
        this.motorArbol = new ArbolBPlus<>(4); 
        outputVista.setArbol(motorArbol);
        JOptionPane.showMessageDialog(null, "Estructura del árbol reiniciada.");
    }
}