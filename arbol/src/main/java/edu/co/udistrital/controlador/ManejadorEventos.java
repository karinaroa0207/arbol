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
    private VisualizadorBoleta vBoleta;

    /**
     * Inyecta las dependencias principales y registra esta clase como listener
     * de todos los botones disponibles en la barra de controles.
     * 
     * @param input Componente de la interfaz encargado de capturar las entradas del usuario y botones.
     * @param output Componente visual que se encarga de renderizar la estructura completa del arbol.
     * @param vMensajes Interfaz para mostrar notificaciones, alertas o mensajes informativos en la pantalla.
     * @param vBoleta Interfaz especifica encargada de renderizar graficamente los detalles de una boleta.
     */
    public ManejadorEventos(ControlesEntrada input, VisualizadorArbolCompleto output, VisualizadorMensajes  vMensajes, VisualizadorBoleta  vBoleta) {
        this.inputVista = input;
        this.motorArbol = new ArbolBPlus<>(inputVista.getOrdenSeleccionado());        
        this.outputVista = output;
        this.vMensajes = vMensajes; 
        this.vBoleta = vBoleta;
        
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
            case "Orden" -> procesarCambioOrden();
            default -> {
            }
        }
    }
    /**
     * Procesa la búsqueda de una boleta por su ID.
     * 
     * <p>Flujo:</p>
     * <ol>
     *   <li>Obtiene el ID ingresado por el usuario</li>
     *   <li>Valida que sea un número entero</li>
     *   <li>Busca en el árbol B+ usando ese ID como clave</li>
     *   <li>Muestra la boleta si existe, o un error si no se encuentra</li>
     * </ol>
     */
    private void procesarBusqueda() {
        try {
            String textoIngresado = inputVista.getIdValidar();
            int idBuscado = Integer.parseInt(textoIngresado.trim());

            Boleta resultado = motorArbol.buscar(idBuscado);
            if (resultado != null) {
                vBoleta.mostrarBoleta(BoletaMapper.toDTO(resultado));
            } else {
                vMensajes.mostrarMensajeError("X BOLETA INEXISTENTE O FALSA.", "Error de Validación");
            }

        } catch (NumberFormatException ex) {
             vMensajes.mostrarMensajeError("El ID debe ser un número entero.", "Error de Formato");
        }
    }
    /**
     * Procesa la inserción de una nueva boleta en el árbol.
     * 
     * <p>Flujo:</p>
     * <ol>
     *   <li>Obtiene el ID ingresado por el usuario</li>
     *   <li>Genera datos aleatorios (evento y zona) usando GeneradorDatos</li>
     *   <li>Inserta el par (ID, Boleta) en el árbol B+</li>
     *   <li>Si el ID ya existe, muestra advertencia y no inserta</li>
     *   <li>Actualiza la visualización gráfica del árbol</li>
     * </ol>
     */
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
            
            inputVista.focusBtn("Ingresar");
            outputVista.setArbol(ArbolBPlusMapper.toDTO(motorArbol, BoletaMapper::toDTO));

        } catch (NumberFormatException ex) {
            vMensajes.mostrarMensajeError("El ID de inserción debe ser un número entero.", "Error de Formato");
        }
    }
    /**
     * Procesa la eliminación de una boleta del árbol por su ID.
     * 
     * <p>Flujo:</p>
     * <ol>
     *   <li>Obtiene el ID a eliminar ingresado por el usuario</li>
     *   <li>Ejecuta la operación de eliminación en el árbol B+</li>
     *   <li>Si la clave existía, rebalancea automáticamente y actualiza la vista</li>
     *   <li>Si no existía, muestra advertencia informativa</li>
     * </ol>
     */
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
    /**
     * Procesa la búsqueda por rango de IDs (consulta por intervalo).
     * 
     * <p>Flujo:</p>
     * <ol>
     *   <li>Obtiene los límites inferior (inicio) y superior (fin)</li>
     *   <li>Valida que ambos sean números enteros</li>
     *   <li>Ejecuta la operación buscarRango en el árbol B+</li>
     *   <li>Muestra todas las boletas cuyos IDs están en el intervalo cerrado [inicio, fin]</li>
     * </ol>
     * 
     * <p>Esta operación aprovecha la lista enlazada de hojas del árbol B+,
     * por lo que es eficiente incluso para rangos grandes.</p>
     */
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
    
    /**
     * Vacia por completo el arbol B+ manteniendo el orden de nodos configurado actualmente.
     * 
     * Este metodo le ordena al arbol existente limpiar su estructura interna regresando a 
     * su estado inicial vacio, preservando la misma referencia en memoria para evitar desincronizaciones 
     * en el controlador. 
     * Al finalizar, actualiza los componentes visuales.
     * 
     * Todas las boletas previamente almacenadas se eliminan de forma permanente.
     */
    private void procesarReiniciar() {        
        motorArbol.reiniciarConNuevoOrden(inputVista.getOrdenSeleccionado());
        outputVista.setArbol(ArbolBPlusMapper.toDTO(motorArbol, BoletaMapper::toDTO));
        vMensajes.mostrarMensaje("Estructura del árbol reiniciada con orden " + motorArbol.getOrden() + ".");
    }
    
    /**
     * Reconfigura el arbol B+ adaptandolo a un nuevo grado u orden seleccionado por el usuario.
     * 
     * Debido a que cambiar el orden altera las reglas de division y capacidad de los nodos, 
     * el metodo formatea de forma segura el arbol existente inicializandolo con el nuevo orden 
     * y sin registros, para luego refrescar la representacion grafica de la interfaz.
     * 
     * Todas las boletas previamente almacenadas se eliminan de forma permanente.
     */
    private void procesarCambioOrden() {
        int ordenSeleccionado = inputVista.getOrdenSeleccionado();
        motorArbol.reiniciarConNuevoOrden(ordenSeleccionado);
        outputVista.setArbol(ArbolBPlusMapper.toDTO(motorArbol, BoletaMapper::toDTO));
        vMensajes.mostrarMensaje("Se creó un nuevo Árbol B+ de orden " + ordenSeleccionado + ".");
    }
}
