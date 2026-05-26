package edu.co.udistrital.vista;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de renderizado del nodo B+.
 * Utiliza Java 2D Graphics para crear un efecto Glassmorphism elegante
 * con gradientes oscuros y acentos de color.
 * 
 * @author Karina Roa
 * @version 1.0
 */
public class PanelVisualizador extends JPanel {
    
    /** Color neón azul para títulos de éxito. */
    private final Color NEON_AZUL = new Color(0, 229, 255);
    
    /** Color neón fucsia para bordes de la caja interior cuando se encuentra la boleta. */
    private final Color NEON_FUCSIA = new Color(255, 0, 127);
    
    /** Color gris para textos secundarios y mensajes de espera. */
    private final Color TEXTO_GRIS = new Color(170, 170, 180);

    /** ID de la boleta a visualizar. */
    private String idVisual = "";
    
    /** Nombre del evento a visualizar. */
    private String eventoVisual = "";
    
    /** Zona del recinto a visualizar. */
    private String zonaVisual = "";
    
    /** Indica si ya se realizó al menos una búsqueda. */
    private boolean huboBusqueda = false;
    
    /** Indica si la boleta fue encontrada exitosamente. */
    private boolean fueEncontrado = false;

    /**
     * Construye el panel visualizador.
     * Configura el panel como transparente para permitir el fondo del Frame.
     */
    public PanelVisualizador() {
        setOpaque(false);
    }

    /**
     * Actualiza los datos que se mostrarán en el panel y solicita el repintado.
     * 
     * @param id ID de la boleta
     * @param evento Nombre del evento
     * @param zona Zona del recinto
     * @param encontrado true si la boleta fue encontrada, false en caso contrario
     */
    public void actualizarDatos(String id, String evento, String zona, boolean encontrado) {
        this.idVisual = id;
        this.eventoVisual = evento;
        this.zonaVisual = zona;
        this.fueEncontrado = encontrado;
        this.huboBusqueda = true;
        repaint(); 
    }

    /**
     * Dibuja el contenido del panel utilizando Java 2D Graphics.
     * Implementa un efecto Glassmorphism con gradiente, borde brillante,
     * y muestra el resultado de la búsqueda (éxito o error).
     *
     * @param g Objeto Graphics utilizado para dibujar
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int padding = 50;
        int width = getWidth() - (padding * 2);
        int height = 280;

        GradientPaint gradienteCristal = new GradientPaint(
            padding, 20, new Color(45, 45, 55, 220), 
            padding, 20 + height, new Color(20, 20, 25, 220)
        );
        g2d.setPaint(gradienteCristal);
        g2d.fillRoundRect(padding, 20, width, height, 30, 30);
        
        g2d.setColor(new Color(255, 255, 255, 40));
        g2d.drawRoundRect(padding, 20, width, height, 30, 30);

        if (!huboBusqueda) {
            g2d.setColor(TEXTO_GRIS);
            g2d.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            FontMetrics fm = g2d.getFontMetrics();
            String msj = "Ingresa un ID para recorrer el árbol...";
            g2d.drawString(msj, (getWidth() - fm.stringWidth(msj)) / 2, 160);
            return;
        }

        if (fueEncontrado) {
            g2d.setColor(NEON_AZUL);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
            g2d.drawString("Nodo Hoja O(log n)", padding + 40, 65);

            int nodeY = 90;
            g2d.setColor(new Color(15, 15, 18, 200));
            g2d.fillRoundRect(padding + 30, nodeY, width - 60, 140, 20, 20);
            
            g2d.setColor(new Color(255, 0, 127, 150));
            g2d.drawRoundRect(padding + 30, nodeY, width - 60, 140, 20, 20);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Monospaced", Font.BOLD, 26));
            g2d.drawString("[ ID: " + idVisual + " ]", padding + 55, nodeY + 45);
            
            g2d.setColor(new Color(255, 255, 255, 30));
            g2d.drawLine(padding + 50, nodeY + 65, padding + width - 110, nodeY + 65);

            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawString("Evento :  " + eventoVisual, padding + 55, nodeY + 95);
            g2d.drawString("Zona   :  " + zonaVisual, padding + 55, nodeY + 125);

        } else {
            g2d.setColor(new Color(255, 80, 80));
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 24));
            g2d.drawString("ACCESO DENEGADO", padding + 50, 100);
            
            g2d.setColor(TEXTO_GRIS);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            g2d.drawString("El recorrido alcanzó el nivel de las hojas,", padding + 50, 140);
            g2d.drawString("pero la clave [" + idVisual + "] no existe.", padding + 50, 165);
        }
    }
}