package edu.co.udistrital.vista;

import edu.co.udistrital.controlador.VisualizadorMensajes;
import edu.co.udistrital.modelo.BoletaDTO;
import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame implements VisualizadorMensajes {
    
    private ControlesEntrada controlesEntrada;
    private VisualizadorArbolCompleto visualizadorArbol;
    private JScrollPane scrollArbol;

    public VentanaPrincipal() {
        setTitle("QueBoleta - Sistema de Indexación B+");
        // Hacemos la ventana un poco más ancha para que los árboles grandes quepan mejor
        setSize(1000, 800); 
        setMinimumSize(new Dimension(760, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // LA SOLUCIÓN: BorderLayout obliga a los elementos a pegarse a los bordes
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(18, 18, 22));

        // Inicializar los sub-paneles
        controlesEntrada = new ControlesEntrada();
        visualizadorArbol = new VisualizadorArbolCompleto();
        scrollArbol = crearScrollArbol(visualizadorArbol);

        // Construir el título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(18, 18, 22));
        // Agregamos un margen limpio: 15px arriba, 5px abajo
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        
        JLabel lblTitulo = new JLabel("QUEBOLETA - ÁRBOL B+");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(new Color(177, 0, 255)); // Púrpura Neón
        panelTitulo.add(lblTitulo);

        // AGRUPAR EL TÍTULO Y LOS CONTROLES
        // Creamos un "Panel Superior" que contenga ambas cosas
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(new Color(18, 18, 22));
        
        panelSuperior.add(panelTitulo);
        panelSuperior.add(controlesEntrada);

        // ENSAMBLAJE FINAL
        // Pegamos el panel superior estrictamente al NORTE (Arriba)
        add(panelSuperior, BorderLayout.NORTH);
        // Le damos todo el espacio sobrante del CENTRO al dibujo del árbol
        add(scrollArbol, BorderLayout.CENTER); 
    }

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

    public ControlesEntrada getControlesEntrada() { return controlesEntrada; }
    public VisualizadorArbolCompleto getVisualizadorArbol() { return visualizadorArbol; }
    
     /**
     * {@inheritDoc}
     *
     * El mensaje se renderiza en la pantalla mediante un cuadro de diálogo
     * emergente modal ({@link JOptionPane}).
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
