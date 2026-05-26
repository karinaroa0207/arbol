package edu.co.udistrital.controlador;

import edu.co.udistrital.modelo.ArbolBPlus;
import edu.co.udistrital.modelo.ArbolBPlusMapper;
import edu.co.udistrital.modelo.Boleta;
import edu.co.udistrital.modelo.BoletaMapper;
import edu.co.udistrital.modelo.GeneradorDatos;
import edu.co.udistrital.vista.ControlesEntrada;
import edu.co.udistrital.vista.VisualizadorArbolCompleto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Clase puente entre la vista y el modelo.
 * Escucha los botones de la interfaz, convierte los textos ingresados a claves
 * numéricas y delega las operaciones reales al Árbol B+.
 */
public class ManejadorEventos implements ActionListener {
    
    private ArbolBPlus<Integer, Boleta> motorArbol;
    private ControlesEntrada inputVista;
    private VisualizadorArbolCompleto outputVista;
    private VisualizadorMensajes vMensajes;

    /**
     * Inyecta las dependencias principales y registra esta clase como listener
     * de todos los botones disponibles en la barra de controles.
     */
    public ManejadorEventos(ArbolBPlus<Integer, Boleta> motor, ControlesEntrada input, VisualizadorArbolCompleto output, VisualizadorMensajes vMensajes) {
        this.motorArbol = motor;
        this.inputVista = input;
        this.outputVista = output;
        this.vMensajes = vMensajes; 
        
        inputVista.agregarListenerBotones(this);
    }

    /**
     * Despacha cada clic hacia la operación específica solicitada por el usuario.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String botonClickeado = e.getActionCommand();
        switch (botonClickeado) {
            case "Insertar" -> procesarInsercion();
            case "Validar" -> procesarBusqueda();
            case "Reiniciar" -> procesarReiniciar();
            case "Eliminar" -> procesarEliminacion();
            case "Rango" -> procesarBusquedaRango();
            default -> {
            }
        }
    }

    private void procesarBusqueda() {
        try {
            String textoIngresado = inputVista.getIdValidar();
            int idBuscado = Integer.parseInt(textoIngresado.trim());

            Boleta resultado = motorArbol.buscar(idBuscado);
            if (resultado != null) {
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
            String textoIngresado = inputVista.getIdInsertar();
            int idNuevo = Integer.parseInt(textoIngresado.trim());

            Boleta nuevaBoleta = GeneradorDatos.generarBoletaAleatoria(idNuevo);
            boolean insertado = motorArbol.insertar(idNuevo, nuevaBoleta);

            if (!insertado) {
                vMensajes.mostrarMensajeWarning("El ID " + idNuevo + " ya existe. En una base de datos de boletas, la clave debe ser única.", "Clave duplicada");
                return;
            }
            
            inputVista.getBtnInsertar().requestFocus();
            outputVista.setArbol(ArbolBPlusMapper.toDTO(motorArbol, BoletaMapper::toDTO));

        } catch (NumberFormatException ex) {
            vMensajes.mostrarMensajeError("El ID de inserción debe ser un número entero.", "Error de Formato");
        }
    }

    private void procesarEliminacion() {
        try {
            String textoIngresado = inputVista.getIdEliminar();
            int idEliminar = Integer.parseInt(textoIngresado.trim());

            boolean eliminado = motorArbol.eliminar(idEliminar);
            if (eliminado) {
                outputVista.setArbol(ArbolBPlusMapper.toDTO(motorArbol, BoletaMapper::toDTO));
                vMensajes.mostrarMensaje(null, "Boleta con ID " + idEliminar + " eliminada y árbol rebalanceado.");
            } else {
                vMensajes.mostrarMensajeWarning("No existe una boleta con ID " + idEliminar + ".", "Eliminación no realizada");
            }

        } catch (NumberFormatException ex) {
            vMensajes.mostrarMensajeError("El ID de eliminación debe ser un número entero.", "Error de Formato");
        }
    }

    private void procesarBusquedaRango() {
        try {
            int inicio = Integer.parseInt(inputVista.getRangoInicio().trim());
            int fin = Integer.parseInt(inputVista.getRangoFin().trim());

            List<Boleta> resultados = motorArbol.buscarRango(inicio, fin);
            if (resultados.isEmpty()) {
                vMensajes.mostrarMensaje("No hay boletas en el rango [" + inicio + ", " + fin + "].");
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

            vMensajes.mostrarMensaje(mensaje.toString(), "Resultado de rango");

        } catch (NumberFormatException ex) {
            vMensajes.mostrarMensajeError("Los límites del rango deben ser números enteros.", "Error de Formato");
        }
    }

    private void procesarReiniciar() {        
        motorArbol.reiniciar();
        outputVista.setArbol(ArbolBPlusMapper.toDTO(motorArbol, BoletaMapper::toDTO));
        vMensajes.mostrarMensaje("Estructura del árbol reiniciada.");
    }
}
