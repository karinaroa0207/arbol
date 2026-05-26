package edu.co.udistrital.vista;

import edu.co.udistrital.controlador.VisualizadorMensajes;
import edu.co.udistrital.modelo.BoletaDTO;
import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de la aplicación QueBoleta.
 * Implementa la interfaz VisualizadorMensajes para mostrar diálogos al usuario.
 * Utiliza BorderLayout para organizar los componentes: título + controles arriba,
 * y el visualizador del árbol en el centro.
 * 
 * @author Karina Roa
 * @version 1.0
 * @see VisualizadorMensajes
 * @see ControlesEntrada
 * @see VisualizadorArbolCompleto
 */
public class VentanaPrincipal extends JFrame implements VisualizadorMensajes {
    
    /** Panel con los controles de entrada (botones, campos de texto, spinner). */
    private ControlesEntrada controlesEntrada;
    
    /** Panel que dibuja visualmente la estructura del árbol B+. */
    private VisualizadorArbolCompleto visualizadorArbol;
    
    /** Scroll pane que envuelve al visualizador para permitir desplazamiento. */
    private JScrollPane scrollArbol;

    /**
     * Constructor de la ventana principal.
     * Configura el título, tamaño, layout, inicializa los componentes
     * y los organiza en la ventana.
     */
    public VentanaPrincipal() {
        setTitle("QueBoleta - Sistema de Indexación B+");
        setSize(1000, 800); 
        setMinimumSize(new Dimension(760, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(18, 18, 22));

        controlesEntrada = new ControlesEntrada();
        visualizadorArbol = new VisualizadorArbolCompleto();
        scrollArbol = crearScrollArbol(visualizadorArbol);

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(18, 18, 22));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        
        JLabel lblTitulo = new JLabel("QUEBOLETA - ÁRBOL B+");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(new Color(177, 0, 255));
        panelTitulo.add(lblTitulo);

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(new Color(18, 18, 22));
        
        panelSuperior.add(panelTitulo);
        panelSuperior.add(controlesEntrada);

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollArbol, BorderLayout.CENTER); 
    }

    /**
     * Crea y configura el panel de desplazamiento (JScrollPane) para el visualizador del árbol.
     * 
     * @param visualizador Panel que dibuja el árbol
     * @return JScrollPane configurado con barras de desplazamiento automáticas
     */
    private JScrollPane crearScrollArbol(VisualizadorArbolCompleto visualizador) {
        JScrollPane scroll = new JScrollPane(
                visualizador,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(18, 18, 22));
        scroll.setBackground(new Color(18, 18, 22));
        scroll.getHorizontalScrollBar().setUnitIncrement(24);
        scroll.getVerticalScrollBar().setUnitIncrement(24);
        scroll.getHorizontalScrollBar().setBlockIncrement(180);
        scroll.getVerticalScrollBar().setBlockIncrement(100);
        return scroll;
    }

    /**
     * Obtiene el panel de controles de entrada.
     * @return Panel ControlesEntrada
     */
    public ControlesEntrada getControlesEntrada() { return controlesEntrada; }
    
    /**
     * Obtiene el panel visualizador del árbol.
     * @return Panel VisualizadorArbolCompleto
     */
    public VisualizadorArbolCompleto getVisualizadorArbol() { return visualizadorArbol; }
    
    /**
     * {@inheritDoc}
     * 
     * El mensaje se renderiza en la pantalla mediante un cuadro de diálogo
     * emergente modal ({@link JOptionPane}).
     */
    @Override
    public void mostrarMensajeError(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(null, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * {@inheritDoc}
     * 
     * El mensaje se renderiza en la pantalla mediante un cuadro de diálogo
     * emergente modal ({@link JOptionPane}).
     */
    @Override
    public void mostrarMensajeError(String mensaje) {
        mostrarMensajeError(mensaje, null);
    }

    /**
     * {@inheritDoc}
     * 
     * El mensaje se renderiza en la pantalla mediante un cuadro de diálogo
     * emergente modal ({@link JOptionPane}).
     */
    @Override
    public void mostrarMensaje(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(null, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * {@inheritDoc}
     * 
     * El mensaje se renderiza en la pantalla mediante un cuadro de diálogo
     * emergente modal ({@link JOptionPane}).
     */
    @Override
    public void mostrarMensaje(String mensaje) {
        mostrarMensaje(mensaje, null);
    }
            
    /**
     * {@inheritDoc}
     * 
     * El mensaje se renderiza en la pantalla mediante un cuadro de diálogo
     * emergente modal ({@link JOptionPane}).
     */
    @Override
    public void mostrarMensajeWarning(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(null, mensaje, titulo, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * {@inheritDoc}
     * 
     * El mensaje se renderiza en la pantalla mediante un cuadro de diálogo
     * emergente modal ({@link JOptionPane}).
     */
    @Override
    public void mostrarMensajeWarning(String mensaje) {
        mostrarMensajeWarning(mensaje, null);
    }

    /**
     * {@inheritDoc}
     * 
     * La boleta se muestra mediante un panel especializado para separar la
     * presentación del dato de negocio de los mensajes de texto simples.
     */
    @Override
    public void mostrarBoleta(BoletaDTO boleta) {
        JOptionPane.showMessageDialog(
                null,
                new PanelBoletaDetalle(boleta),
                "Boleta encontrada",
                JOptionPane.PLAIN_MESSAGE
        );
    }
}