package edu.co.udistrital.controlador;

import edu.co.udistrital.modelo.ArbolBPlus;
import edu.co.udistrital.modelo.Boleta;
import edu.co.udistrital.modelo.GeneradorDatos;
import edu.co.udistrital.vista.ControlesEntrada;
import edu.co.udistrital.vista.VisualizadorArbolCompleto;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Clase puente entre la vista y el modelo.
 * Escucha los botones de la interfaz, convierte los textos ingresados a claves
 * numéricas y delega las operaciones reales al Árbol B+.
 */
public class ManejadorEventos implements ActionListener {
    
    private ArbolBPlus<Integer, Boleta> motorArbol;
    private ControlesEntrada inputVista;
    private VisualizadorArbolCompleto outputVista;

    /**
     * Inyecta las dependencias principales y registra esta clase como listener
     * de todos los botones disponibles en la barra de controles.
     */
    public ManejadorEventos(ArbolBPlus<Integer, Boleta> motor, ControlesEntrada input, VisualizadorArbolCompleto output) {
        this.motorArbol = motor;
        this.inputVista = input;
        this.outputVista = output;
        
        inputVista.getBtnValidar().addActionListener(this);
        inputVista.getBtnInsertar().addActionListener(this);
        inputVista.getBtnEliminar().addActionListener(this);
        inputVista.getBtnRango().addActionListener(this);
        inputVista.getBtnReiniciar().addActionListener(this);
    }

    /**
     * Despacha cada clic hacia la operación específica solicitada por el usuario.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton botonClickeado = (JButton) e.getSource();
        
        if (botonClickeado == inputVista.getBtnInsertar()) {
            procesarInsercion();
        } else if (botonClickeado == inputVista.getBtnValidar()) {
            procesarBusqueda();
        } else if (botonClickeado == inputVista.getBtnEliminar()) {
            procesarEliminacion();
        } else if (botonClickeado == inputVista.getBtnRango()) {
            procesarBusquedaRango();
        } else if (botonClickeado == inputVista.getBtnReiniciar()) {
            procesarReiniciar();
        }
    }

    private void procesarBusqueda() {
        try {
            String textoIngresado = inputVista.getIdValidar();
            int idBuscado = Integer.parseInt(textoIngresado.trim());

            Boleta resultado = motorArbol.buscar(idBuscado);
            if (resultado != null) {
                JOptionPane.showMessageDialog(null, "✓ BOLETA VÁLIDA: " + resultado);
            } else {
                JOptionPane.showMessageDialog(null, "X BOLETA INEXISTENTE O FALSA.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El ID debe ser un número entero.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarInsercion() {
        try {
            String textoIngresado = inputVista.getIdInsertar();
            int idNuevo = Integer.parseInt(textoIngresado.trim());

            Boleta nuevaBoleta = GeneradorDatos.generarBoletaAleatoria(idNuevo);
            boolean insertado = motorArbol.insertar(idNuevo, nuevaBoleta);

            if (!insertado) {
                JOptionPane.showMessageDialog(null, "El ID " + idNuevo + " ya existe. En una base de datos de boletas, la clave debe ser única.", "Clave duplicada", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            inputVista.getBtnInsertar().requestFocus();
            outputVista.setArbol(motorArbol);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El ID de inserción debe ser un número entero.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarEliminacion() {
        try {
            String textoIngresado = inputVista.getIdEliminar();
            int idEliminar = Integer.parseInt(textoIngresado.trim());

            boolean eliminado = motorArbol.eliminar(idEliminar);
            if (eliminado) {
                outputVista.setArbol(motorArbol);
                JOptionPane.showMessageDialog(null, "Boleta con ID " + idEliminar + " eliminada y árbol rebalanceado.");
            } else {
                JOptionPane.showMessageDialog(null, "No existe una boleta con ID " + idEliminar + ".", "Eliminación no realizada", JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El ID de eliminación debe ser un número entero.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarBusquedaRango() {
        try {
            int inicio = Integer.parseInt(inputVista.getRangoInicio().trim());
            int fin = Integer.parseInt(inputVista.getRangoFin().trim());

            ArrayList<Boleta> resultados = motorArbol.buscarRango(inicio, fin);
            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay boletas en el rango [" + inicio + ", " + fin + "].");
                return;
            }

            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Boletas encontradas en el rango [")
                   .append(inicio)
                   .append(", ")
                   .append(fin)
                   .append("]:\n\n");

            for (Boleta boleta : resultados) {
                mensaje.append(boleta).append("\n");
            }

            JOptionPane.showMessageDialog(null, mensaje.toString(), "Resultado de rango", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Los límites del rango deben ser números enteros.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarReiniciar() {
        this.motorArbol = new ArbolBPlus<>(4);
        outputVista.setArbol(motorArbol);
        JOptionPane.showMessageDialog(null, "Estructura del árbol reiniciada.");
    }
}
